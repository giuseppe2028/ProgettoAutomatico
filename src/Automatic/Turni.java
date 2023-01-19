package Automatic;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turni {
    private int id;
    private  int refImpiegfato;
    private String tipoturno;
    private LocalTime inizioTurno;
    private LocalTime fineTurno;
    private LocalDate dataTurno;
private boolean straordinario;
    public Turni(int id, String tipoturno, LocalTime inizioTurno, LocalTime fineTurno, LocalDate dataTurno,boolean straordinario){
        this.id = id;
        this.tipoturno = tipoturno;
        this.inizioTurno = inizioTurno;
        this.fineTurno = fineTurno;
        this.dataTurno = dataTurno;
        this.straordinario = straordinario;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefImpiegfato() {
        return refImpiegfato;
    }

    public void setRefImpiegfato(int refImpiegfato) {
        this.refImpiegfato = refImpiegfato;
    }

    public String getTipoturno() {
        return tipoturno;
    }

    public void setTipoturno(String tipoturno) {
        this.tipoturno = tipoturno;
    }

    public LocalTime getInizioTurno() {
        return inizioTurno;
    }

    public void setInizioTurno(LocalTime inizioTurno) {
        this.inizioTurno = inizioTurno;
    }

    public LocalTime getFineTurno() {
        return fineTurno;
    }

    public void setFineTurno(LocalTime fineTurno) {
        this.fineTurno = fineTurno;
    }

    public LocalDate getDataTurno() {
        return dataTurno;
    }

    public void setDataTurno(LocalDate dataTurno) {
        this.dataTurno = dataTurno;
    }
}
