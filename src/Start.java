import Automatic.Boundary.BoundaryTempo;
import Automatic.Util.Daemon;

public class Start {
    public static void main(String[] args) {
        new Daemon();
        //faccio runnare 5 thread



        BoundaryTempo boundaryTempo = new BoundaryTempo();
        //boundaryTempo.clickOrarioData();
        //boundaryTempo.getData();
        boundaryTempo.getDataStraordinario();


    }

}
