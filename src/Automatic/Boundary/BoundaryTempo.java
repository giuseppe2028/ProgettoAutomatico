package Automatic.Boundary;


import Automatic.Control.ControlCalcoloStipendi;
import Automatic.Control.ControlUscitaDimenticata;

import java.util.ArrayList;
import java.util.List;

public class BoundaryTempo {




    public void clickOrarioData(){
        new ControlUscitaDimenticata();
    }

    public void getData(){
        new ControlCalcoloStipendi();
    }
}
