package Automatic.Control;

import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ControlCalcoloStipendi {
    private List<Integer> matricole;
    List<Object> datiStipendio;

    public ControlCalcoloStipendi(){
    //todo devo fare il checkData
    calcolo();
}
private void calcolo(){
    matricole = Daemon.getMatricole();
    //serebbe per ogni matricola
    for (int i = 0; i<matricole.size(); i++){
        double stipendio = calcoloStipendio(matricole.get(i));
        //calcolo lo stipendio
        System.out.println(stipendio);
    }
}
private double calcoloStipendio(int matricola){
    System.out.println("----------------------------");
    System.out.println(matricola);
        LocalDate date = LocalDate.now();
        datiStipendio = Daemon.getDatiStipendio(matricola);
        //ottendo le ore totali e li moltiplico per il costo orario
        //stipendio += (Integer) datiStipendio.get(0) * (Integer) datiStipendio.get(1);
        int cost = (int)datiStipendio.get(0)*(int)datiStipendio.get(1);
        double stipendio = cost;

        //sottraggo le trattenute fiscali
        stipendio = stipendio - (int)datiStipendio.get(2);
        //calcolo la gratifica
        stipendio += (stipendio * getGratifica(matricola));
        //aggiungo le ore di straordinario
        stipendio += (int) datiStipendio.get(3)*30;


        if((Boolean) datiStipendio.get(4)){
            stipendio += (stipendio * 0.1 );
        }
        if(date.getMonthValue() ==12){
            stipendio +=(stipendio*2);
        }
        if(stipendio<0){
            stipendio = 0;
        }
return stipendio;
}
private double getGratifica(int matricola){
        int servizio = Daemon.getServizio(matricola);
        switch (servizio){
            case 1:
                return 0.4;
            case 2:
                return 0.3;
            case 3:
                return 0.2;
            case 4:
                return 0.1;

        }
        return 0;
}
}
