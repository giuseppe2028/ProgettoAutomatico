package Automatic.Control;

import Automatic.RichiestaAstensione;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlPianificaTurni {
    //LocalDate dataCorrente = LocalDate.now();
    //for testing
    LocalDate dateCorrente = LocalDate.of(2023,19,2);
    LocalDate dataPianificaTurni = LocalDate.of(2023,1,1);
    public ControlPianificaTurni(){
        pianifica();
    }
    private void pianifica(){
        Map<Integer,List<RichiestaAstensione>> datiAssenza = new HashMap<>();
        if(dateCorrente.compareTo(dataPianificaTurni) == 0){
            List<Integer> matricole = Daemon.getMatricole();
            for(int i = 0; i<matricole.size(); i++){
                //richiedo le richieste di astensione
                //salvo il tutto in una mappa, in modo tale da avere ad ogni matricola una richiesta di astensione
                datiAssenza.put(matricole.get(i),Daemon.getRichiesteAstensione(matricole.get(i)));
            }
            //adesso per ogni giorno
            //setto la data di fine
            LocalDate dataFineTurnazione = LocalDate.of(dateCorrente.getYear(),dateCorrente.getMonthValue()+3,dateCorrente.getDayOfMonth());
            for(LocalDate j = dateCorrente; j.isBefore(dataFineTurnazione); j.plusDays(1)){
                int servizio = 1;
                for(int i = 1; i<4; i++){

                }
                //ora faccio per ogni servizio, ovvero per un servizio io assegner
            }
        }
    }
}

