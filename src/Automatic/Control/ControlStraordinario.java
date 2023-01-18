package Automatic.Control;

import Automatic.JavaMail;
import Automatic.Util.Daemon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class ControlStraordinario {
LocalTime oraCorrente = LocalTime.now();
LocalDate dataCorrente = LocalDate.now();

public ControlStraordinario(){
    run();
}
    private void run(){
    JavaMail mail = new JavaMail();
    int matricola;
        for(int i = 0; i< Daemon.getTurni(dataCorrente,oraCorrente).size(); i++ ){
            //richiedo le richieste di astensione
            //devo fare il for per ogni richiesta
            Random random = new Random();
            matricola = Daemon.matricoleReperibile().get(random.nextInt(Daemon.getTurni(dataCorrente,oraCorrente).size()-1));
            Daemon.updateTurni(matricola,Daemon.getTurni(dataCorrente,oraCorrente).get(i));


           mail.setOggetto("Sei stato chiamato a sostituire un turno");
           mail.setTesto("Sei stato chiamato a sostituire un turno");
            try {
                JavaMail.sendMail(Daemon.getMail(matricola));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

}
