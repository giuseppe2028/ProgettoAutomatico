package Automatic.Control;

import Automatic.RichiestaAstensione;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PianificaTurni {
    private int numMinImpiegati = 4;
    //private LocalDate dataCorrente = LocalDate.now();
    //for testing
    private LocalDate dateCorrente = LocalDate.of(2023,1,1);
    private LocalDate dataPianificaTurni = LocalDate.of(2023,1,1);
    //setto la data di fine della turniazione
    LocalDate dataFineTurnazione = LocalDate.of(dateCorrente.getYear(),dateCorrente.getMonthValue()+3,dateCorrente.getDayOfMonth());
    public PianificaTurni(){
        run();
    }
    public void run() {
        //prelevo i dati riguardo i giorni di astensione di un singolo dipendente
        //salvo tutto in una coppia chiave valore
        Map<Integer, List<RichiestaAstensione>> datiAssenza = new HashMap<>();
        //controllo la data corrente
        if(dateCorrente.compareTo(dataPianificaTurni)==0){
            datiAssenza = calcoloRichiesteAstensione();
            calcolo();
        }
    }
private Map<Integer, List<RichiestaAstensione>> calcoloRichiesteAstensione(){
    Map<Integer, List<RichiestaAstensione>> datiAssenza = new HashMap<>();
    List<Integer> matricole = Daemon.getMatricole();
    for(int i = 0; i<matricole.size(); i++){
        //salvo il tutto in una mappa, in modo tale da avere ad ogni matricola una richiesta di astensione
        datiAssenza.put(matricole.get(i),Daemon.getRichiesteAstensione(matricole.get(i)));
    }
    return datiAssenza;
}
//todo mettere il massimo numero di dipendenti
private void calcolo(){
    LocalDate j = dateCorrente;
    while (j.isBefore(dataFineTurnazione)){
        //prendo tutti i dipendenti
        List<Integer> dipendentiRimanenti = Daemon.getMatricole();

        //adesso calcolo i turni di mattina e di pomeriggio
        for(int orario =0; orario<2;orario++ ){
            int [] servizi = {0,0,0,0};
            for(int servizio = 0; servizio<servizi.length; servizio++){
                //faccio icalcoli per ogni servizio
                while (servizi[servizio]<numMaxImpiegati()){
                    System.out.println(servizi[servizio]);
                    //aggiungo un dipendente casuale
                    //per semplicità faccio direttamente la insert al db
                    //prendo una matricola casuale dai dipendenti rimanenti
                    Random random = new Random(System.currentTimeMillis());
                    int matricola = dipendentiRimanenti.get(random.nextInt(1,dipendentiRimanenti.size()));
                    //controllo se non ha in giorno di astensione oggi
                    boolean controllo = isNotAstensione(calcoloRichiesteAstensione(),j,matricola);
                    if(controllo){
                        if(orario == 0){
                            LocalTime inizio = LocalTime.of(8,0,0);
                            LocalTime fine = LocalTime.of(14,0,0);
                            //aggiungo il turno
                            Daemon.insertPropostaTurni("mattina",inizio,fine,j,matricola);
                        }
                        else {
                            LocalTime inizio = LocalTime.of(14,0,0);
                            LocalTime fine = LocalTime.of(20,0,0);
                            //aggiungo il turno
                            Daemon.insertPropostaTurni("pomeriggio",inizio,fine,j,matricola);
                        }
                        //rimuovo dalla lista il dipendente
                        dipendentiRimanenti.remove((Integer) matricola);
                        System.out.println(dipendentiRimanenti);
                    }
                    servizi[servizio]++;

                }
                }

            }
            j = j.plusDays(1);
        }

    }
    private int numMaxImpiegati(){
        int dipendenti = Daemon.getNumImpiegati();
        //faccio diviso due perchè ho due dipendenti e poi faccio diviso quattro per i servizi
        return (dipendenti/2)/4;

    }
    private boolean isNotAstensione(Map<Integer,List<RichiestaAstensione>> datiAssenza,LocalDate j,int matricola){
        List<RichiestaAstensione> richiestaAstensioneUtilizzo = datiAssenza.get(matricola);
        for (int i = 0; i<richiestaAstensioneUtilizzo.size();i++){
            if(j.isAfter(richiestaAstensioneUtilizzo.get(i).getData_inizio()) && j.isBefore(richiestaAstensioneUtilizzo.get(i).getData_fine())){
                return false;
            }
            else{
                return true;
            }
        }
        return true;
    }

}