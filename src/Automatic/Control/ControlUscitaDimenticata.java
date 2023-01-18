package Automatic.Control;



import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ControlUscitaDimenticata {

    //private LocalTime orario = LocalTime.now();
    //private LocalDate data = LocalDate.now();
    private LocalTime orario = LocalTime.of(8,00,00);
    private LocalDate data = LocalDate.of(2023,01,11);
    private List<Integer> matricole;
    private LocalTime orariFineTurno;

    public ControlUscitaDimenticata(){

        calcolo();
    }

    private void calcolo(){
        matricole = Daemon.getMatricoleImpiegati(data,orario);
        for(int i = 0; i<matricole.size();i++){
             orariFineTurno = Daemon.getFineTurno(matricole.get(i),data);
            System.out.println(orario);
           /* if(orario.isAfter(orariFineTurno.plus(10, ChronoUnit.MINUTES))){
                if(!Daemon.verifyUscita(matricole.get(i),data)){
                    System.out.println("ora invio la matricola al datore");
                }
        }*/

        }
    }

}
