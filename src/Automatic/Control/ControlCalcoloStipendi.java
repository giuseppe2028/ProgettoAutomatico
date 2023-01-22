package Automatic.Control;

import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ControlCalcoloStipendi implements Runnable{
    List<Object> valoriDbms;
    private List<Integer> matricole;
    List<Object> datiStipendio;

    public ControlCalcoloStipendi(){
        //per test
        //Daemon.copia();
        //per test
        //Daemon.timbraImpiegato(1030);
    //todo devo fare il checkData
    run();
    //inserisco tutto nel db
}
public void run(){
    System.out.println("thread calcolo stipendi avviato");
    matricole = Daemon.getMatricole();
    //serebbe per ogni matricola
    for (int i = 0; i<matricole.size(); i++){
        valoriDbms = new ArrayList<>();
        //inserisco il primo elemento
        valoriDbms.add(matricole.get(i));
        valoriDbms.add(convertMese(LocalDate.now().getMonthValue()));
        valoriDbms.add(LocalDate.now().getYear());
        double stipendio = calcoloStipendio(matricole.get(i));

        valoriDbms.add(stipendio);
        //calcolo lo stipendio
        System.out.println(stipendio);

    }
}
private double calcoloStipendio(int matricola) {
    System.out.println("----------------------------");
    System.out.println(matricola);
    LocalDate date = LocalDate.now();
    datiStipendio = Daemon.getDatiStipendio(matricola);

        //aggiungo le ore lavorate
        valoriDbms.add(datiStipendio.get(0));
        System.out.println("Dati Stipendio: " + datiStipendio);
        //ottendo le ore totali e li moltiplico per il costo orario
        //stipendio += (Integer) datiStipendio.get(0) * (Integer) datiStipendio.get(1);
        int cost = (int) datiStipendio.get(0) * (int) datiStipendio.get(1);
        //aggiungo il saldo ore lavorate
        valoriDbms.add(cost);
        //todo fare che lo stipendio base dipende dal ruolo
        System.out.println("stipendio base : " + cost);
        double stipendio = cost;

        //sottraggo le trattenute fiscali
        //calcolo la gratifica
        stipendio += (stipendio * getGratifica(matricola));
        valoriDbms.add(getGratifica(matricola));
        //con il saldo: 7
        valoriDbms.add(stipendio*getGratifica(matricola));
        System.out.println("stipendio + gratifica" + stipendio);

        stipendio += (int) datiStipendio.get(3) * 30;

    //aggiungo le ore di straordinario
        valoriDbms.add(datiStipendio.get(3));
    //aggiungo il saldo ore straordianrio
        valoriDbms.add((int)datiStipendio.get(3) * 30);
        //aggiundo la reperibilità
        valoriDbms.add(datiStipendio.get(4));
        //aggiungo la maggiorazione per reperibilita
        if ((Boolean) datiStipendio.get(4)) {
            //aggiungo il saldo per la reperibilità
            valoriDbms.add(stipendio * 0.1);
            stipendio += (stipendio * 0.1);
        }
        if(!(Boolean) datiStipendio.get(4)){
            valoriDbms.add(0);
        }

        if (date.getMonthValue() == 12) {
            stipendio += (stipendio * 2);
            valoriDbms.add(stipendio);
        }
        if(date.getMonthValue() != 12){
            valoriDbms.add(0);
        }
        //levo le trattenute
        valoriDbms.add(stipendio);
        stipendio = stipendio - (int) datiStipendio.get(2);
        //aggiungo le trattenute
        valoriDbms.add(datiStipendio.get(2));
        System.out.println("stipendio - trattenute : " + stipendio);
        if (stipendio < 0) {
            stipendio = 0;
        }
        valoriDbms.add(stipendio);
    System.out.println(valoriDbms.size());
    System.out.println(valoriDbms.toString());
        Daemon.insertStipendio(valoriDbms);
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
    private String convertMese(int mese){
        switch (mese){
            case 1:
                return "Gennaio";
            case 2:
                return "Febbraio";
            case 3:
                return "Marzo";
            case 4:
                return "Aprile";
            case 5:
                return "Maggio";
            case 6:
                return "Giugno";
            case 7:
                return "Luglio";
            case 8:
                return "Agosto";
            case 9:
                return "Settembre";
            case 10:
                return "Ottobre";
            case 11:
                return "Novembre";
            case 12:
                return "Dicembre";

        }
        return null;
    }
}
