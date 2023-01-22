/*package Automatic.Control;

import Automatic.RichiestaAstensione;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


//todo algoritmo da sistemare
public class ControlPianificaTurni implements Runnable{
    private int numMinDipendenti = 4;
    //LocalDate dataCorrente = LocalDate.now();
    //for testing
    LocalDate dateCorrente = LocalDate.of(2023,1,1);
    LocalDate dataPianificaTurni = LocalDate.of(2023,1,1);
public ControlPianificaTurni(){

}
    @Override
    public void run(){
        System.out.println("thread pianifica turni avviato");
         int id = 0;
        Map<Integer,List<RichiestaAstensione>> datiAssenza = new HashMap<>();
        if(dateCorrente.compareTo(dataPianificaTurni) == 0){
            List<Integer> matricole = Daemon.getMatricole();
            for(int i = 0; i<matricole.size(); i++){
                //richiedo le richieste di astensione
                //salvo il tutto in una mappa, in modo tale da avere ad ogni matricola una richiesta di astensione
                datiAssenza.put(matricole.get(i),Daemon.getRichiesteAstensione(matricole.get(i)));
            }
            //adesso per ogni giorno
            //setto la data di fine della turniazione
            LocalDate dataFineTurnazione = LocalDate.of(dateCorrente.getYear(),dateCorrente.getMonthValue()+3,dateCorrente.getDayOfMonth());
            //conservo il numero di dipendenti per ogni servizio

            LocalDate j = dateCorrente;
            while (j.isBefore(dataFineTurnazione)) {
                System.out.println("DATA: "+j.toString());
                System.out.println("ciaoooo"+j);
                System.out.println("entro");
                //prendo il turno di mattina
                //riempio il primo servizio
                //sto facendo per mattina e pomeriggio, quidni il cosice lo ripeto due volte
                List<Integer> dipendentiRimanenti = Daemon.getMatricole();
                System.out.println("ciollagrossa "+dipendentiRimanenti.size());
                for(int orario = 0; orario <2; orario++){
                    //todo chiedere a gab perchè non si aggionra la data
                    id++;
                    System.out.println("Stampo l'id"+id);
                    int [] servizi = {0,0,0,0};
                    while (servizi[0]<numMinDipendenti){
                        System.out.println(numMinDipendenti);
                        //aggiungo un dipendente casuale
                        //per semplicità faccio direttamente la insert al db
                        //prendo una matricola casuale dai dipendenti rimanenti
                        Random random = new Random(System.currentTimeMillis());
                        int matricola = dipendentiRimanenti.get(random.nextInt(1,dipendentiRimanenti.size()));
                        System.out.println(matricola);
                        //la aggiungo al turno
                        //controllo se non ha in giorno di astensione oggi
                        boolean controllo = isNotAstensione(datiAssenza,j,matricola);
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
                        servizi[0]++;
                    }
                    //SECONDA ITERAZIONE
                    while (servizi[1]<numMinDipendenti()){
                        System.out.println(numMinDipendenti());
                        //aggiungo un dipendente casuale
                        //per semplicità faccio direttamente la insert al db
                        //prendo una matricola casuale dai dipendenti rimanenti
                        int matricola = dipendentiRimanenti.get((int)(Math.random()*dipendentiRimanenti.size()));
                        //la aggiungo al turno
                        //controllo se non ha in giorno di astensione oggi
                        boolean controllo = isNotAstensione(datiAssenza,j,matricola);
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
                        servizi[1]++;
                    }
                    //TERZA ITERAZIONE
                    while (servizi[2]<numMinDipendenti()){
                        System.out.println(numMinDipendenti());
                        //aggiungo un dipendente casuale
                        //per semplicità faccio direttamente la insert al db
                        //prendo una matricola casuale dai dipendenti rimanenti
                        int matricola = dipendentiRimanenti.get((int)(Math.random()*dipendentiRimanenti.size()));
                        //la aggiungo al turno
                        //controllo se non ha in giorno di astensione oggi
                        boolean controllo = isNotAstensione(datiAssenza,j,matricola);
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
                        servizi[2]++;
                    }
                    //QUARTA ITERAZIONE
                    while (servizi[3]<numMinDipendenti){
                        System.out.println(numMinDipendenti());
                        //aggiungo un dipendente casuale
                        //per semplicità faccio direttamente la insert al db
                        //prendo una matricola casuale dai dipendenti rimanenti
                        int matricola = dipendentiRimanenti.get((int)(Math.random()*dipendentiRimanenti.size()));
                        //la aggiungo al turno
                        //controllo se non ha in giorno di astensione oggi
                        boolean controllo = isNotAstensione(datiAssenza,j,matricola);
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
                        servizi[3]++;
                    }
                    //LO FACCIO PER GLI AMMINISTRATIVI


                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                j = j.plusDays(1);
            }
        }
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
    private int numMaxDipendenti(){
        int dipendenti = Daemon.getNumImpiegati();
        //faccio diviso due perchè ho due dipendenti e poi faccio diviso quattro per i servizi
        return (dipendenti/2)/4;

    }
    private int numMinAmministrativi(){
        int amministrativi = Daemon.getNumAmministrativi();
        return (amministrativi/2)/4;
    }
}
*/
