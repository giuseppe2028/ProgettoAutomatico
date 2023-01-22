package Automatic.Boundary;


import Automatic.Control.*;

import java.util.ArrayList;
import java.util.List;

public class BoundaryTempo {




    public void clickOrarioData(){
        ControlUscitaDimenticata controlUscitaDimenticata= new ControlUscitaDimenticata();
        Thread thread = new Thread(controlUscitaDimenticata);
        thread.start();
    }

    public void getDataStipendio(){
        ControlCalcoloStipendi controlCalcoloStipendi = new ControlCalcoloStipendi();
        Thread thread = new Thread(controlCalcoloStipendi);
        thread.start();
    }

    //testo le richieste di straordinario:
    public void getDataStraordinario(){

        ControlStraordinario controlStraordinario= new ControlStraordinario();
        Thread thread = new Thread(controlStraordinario);
        thread.start();
    }

public void getDataTurni(){
    PianificaTurni pianificaTurni = new PianificaTurni();
    //ControlPianificaTurni controlPianificaTurni = new ControlPianificaTurni();
    //Thread thread = new Thread(controlPianificaTurni);
    //thread.start();
}

}
