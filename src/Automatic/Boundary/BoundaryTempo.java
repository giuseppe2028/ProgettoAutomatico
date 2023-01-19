package Automatic.Boundary;


import Automatic.Control.ControlCalcoloStipendi;
import Automatic.Control.ControlPianificaTurni;
import Automatic.Control.ControlStraordinario;
import Automatic.Control.ControlUscitaDimenticata;

import java.util.ArrayList;
import java.util.List;

public class BoundaryTempo {




    /*public void clickOrarioData(){
        new ControlUscitaDimenticata();
    }

     */

   /* public void getData(){
        new ControlCalcoloStipendi();
    }*/

    //testo le richieste di straordinario:
    public void getDataStraordinario(){
        ControlStraordinario controlStraordinario= new ControlStraordinario();
    }

public void getDataTurni(){
    ControlPianificaTurni controlPianificaTurni = new ControlPianificaTurni();
}

}
