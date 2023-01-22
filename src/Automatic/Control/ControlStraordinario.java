package Automatic.Control;

import Automatic.JavaMail;
import Automatic.RichiestaAstensione;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControlStraordinario implements Runnable{
    public ControlStraordinario(){

    }
//LocalTime oraCorrente = LocalTime.now();
//LocalDate dataCorrente = LocalDate.now();
    LocalTime oraCorrente = LocalTime.of(23,9,10);
    LocalDate dataCorrente = LocalDate.of(2023,1,11);

@Override
public void run(){
    System.out.println("thread straordinario avviato");
    JavaMail mail = new JavaMail();
    int matricola;
        for(int i = 0; i< Daemon.getTurni(dataCorrente,oraCorrente).size(); i++ ){
           //richiedo le richieste di astensione
            List<RichiestaAstensione> richiestaAstensione = Daemon.getRichiesteAstensione(dataCorrente);
            for(int j=0; j<richiestaAstensione.size(); j++){
                Random random = new Random(System.currentTimeMillis());
                matricola = Daemon.matricoleReperibile().get(random.nextInt(Daemon.getTurni(dataCorrente,oraCorrente).size()+1));
                //salvo la matricola in una lista di matricole già scelta
                Daemon.updateTurni(matricola,Daemon.getTurni(dataCorrente,oraCorrente).get(i));
                System.out.println("eseguo");
                System.out.println("la matricola"+ matricola + "è stata scelta");
            }

/*
           mail.setOggetto("Sei stato chiamato a sostituire un turno");
           mail.setTesto("Sei stato chiamato a sostituire un turno");
            try {
                JavaMail.sendMail(Daemon.getMail(matricola));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/

        }
    }

}
