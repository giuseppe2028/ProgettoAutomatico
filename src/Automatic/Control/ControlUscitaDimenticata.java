package Automatic.Control;



import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class ControlUscitaDimenticata implements Runnable{

    //private LocalTime orario = LocalTime.now();
    //private LocalDate data = LocalDate.now();
public ControlUscitaDimenticata(){

}
    //FORSE FUNZIONA
    private LocalTime orario = LocalTime.of(20,10,00);
    private LocalDate data = LocalDate.of(2023,01,11);
    private List<Integer> matricole;
    private LocalTime orariFineTurno;

    @Override
    public void run(){
        System.out.println("thread uscita dimenticata avviato");
        matricole = Daemon.getMatricoleImpiegati(data,orario);
        for(int i = 0; i<matricole.size();i++){
            System.out.println("matricola: " + matricole.get(i));
             orariFineTurno = Daemon.getFineTurno(matricole.get(i),data);
             System.out.println("FINE TURNO "+orariFineTurno);
           if(orario.isAfter(orariFineTurno.plus(10, ChronoUnit.MINUTES))){
                if(!Daemon.verifyUscita(matricole.get(i),data)){
                    System.out.println("ora invio la matricola al datore");
                    //timbro l'uscita
                    Daemon.insertTimbraturaUscitaDimenticata((Integer) Daemon.getDatiTurni(matricole.get(i),data).get(0),(Integer) Daemon.getDatiTurni(matricole.get(i),data).get(1), (String)Daemon.getDatiTurni(matricole.get(i),data).get(2),"Uscita","Uscita Dimenticata",(LocalDate) Daemon.getDatiTurni(matricole.get(i),data).get(3),(LocalTime) Daemon.getDatiTurni(matricole.get(i),data).get(4));
                }
        }

        }
    }

}
