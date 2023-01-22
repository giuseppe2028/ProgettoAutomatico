package Automatic.Control;

import Automatic.Turni;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ControlChiudiServizio implements Runnable{
    LocalDate dataCorrente;
    LocalTime oraCorrente;
    public ControlChiudiServizio(){
        dataCorrente= LocalDate.now();
        oraCorrente = LocalTime.now();
    }
    public void run(){
        System.out.println("thread chiudi servizio avviato");
        List<Turni> listaTurni;
        List<Integer> matricole;
        listaTurni = Daemon.getTurni(dataCorrente,oraCorrente);
        for(int i = 0; i<listaTurni.size(); i++){
            //todo mettere il fatto che è riferito ad ogni servizio (all'interno scrivere di turno probabilemnte vi sarà un ref servizio)
            //matricole = Daemon.
        }

    }

    private void ridistribuisciImpiegati(){
        //asegno agli impiegati a di quel servizio un numero casuale da 1 a 3 (saltando quel servizio)
    }
}
