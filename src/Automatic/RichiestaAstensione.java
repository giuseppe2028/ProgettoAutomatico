package Automatic;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalTime;

public class RichiestaAstensione {
    private int id,ref_impiegato,matricola_destinazione;
    private String categoria,stato,svolgimento,motivazione,tipologia;
    private LocalDate data_inizio,data_fine;
    private LocalTime ora_inizio,ora_fine;
    private Blob allegato;

    public RichiestaAstensione(int id, int ref_impiegato, String categoria, String stato, LocalDate data_inizio, LocalDate data_fine, LocalTime ora_inizio, LocalTime ora_fine, String svolgimento, String motivazione, String tipologia,Blob allegato){
        this.id=id;
        this.ref_impiegato=ref_impiegato;
        this.categoria=categoria;
        this.stato=stato;
        this.data_inizio=data_inizio;
        this.data_fine=data_fine;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
        this.svolgimento=svolgimento;
        this.motivazione=motivazione;
        this.tipologia=tipologia;
        this.allegato=allegato;
    }
    @Override
    public String toString(){
        return Integer.toString(this.id);
    }

    public int getMatricola_destinazione() {
        return matricola_destinazione;
    }

    public void setMatricola_destinazione(int matricola_destinazione) {
        this.matricola_destinazione = matricola_destinazione;
    }

    public int getRef_impiegato() {
        return ref_impiegato;
    }

    public void setRef_impiegato(int ref_impiegato) {
        this.ref_impiegato = ref_impiegato;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getSvolgimento() {
        return svolgimento;
    }

    public void setSvolgimento(String svolgimento) {
        this.svolgimento = svolgimento;
    }

    public String getMotivazione() {
        return motivazione;
    }

    public void setMotivazione(String motivazione) {
        this.motivazione = motivazione;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }



    public LocalDate getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(LocalDate data_inizio) {
        this.data_inizio = data_inizio;
    }

    public LocalDate getData_fine() {
        return data_fine;
    }

    public void setData_fine(LocalDate data_fine) {
        this.data_fine = data_fine;
    }



    public LocalTime getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(LocalTime ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public LocalTime getOra_fine() {
        return ora_fine;
    }

    public void setOra_fine(LocalTime ora_fine) {
        this.ora_fine = ora_fine;
    }

    public Blob getAllegato() {
        return allegato;
    }

    public void setAllegato(Blob allegato) {
        this.allegato = allegato;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }
}
