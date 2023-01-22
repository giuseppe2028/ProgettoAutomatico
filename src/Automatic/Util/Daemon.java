package Automatic.Util;

import Automatic.RichiestaAstensione;
import Automatic.Turni;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Daemon {
    private static String URL = "jdbc:mysql://localhost:3306/Azienda";
    private static String username = "root";
    private static String passwordDBMS = "root1234";
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection conn;
    private static List<Integer> matricole;
    private static List<LocalTime> oraFineTurno;

    public Daemon(){
        try{
        conn  = DriverManager.getConnection(URL,username,passwordDBMS);
        System.out.println("Connessione Stabilita");
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    //mi prendo gli impiegati che hanno effettuato un'entrata
    public static List<Integer> getMatricoleImpiegati(LocalDate data, LocalTime tempo){
        try {
            matricole = new ArrayList<>();
            System.out.println(Date.valueOf(data).toString());
            //query originaleselect * from Timbratura T, Turno TR where T.ref_turno = TR.id and tipo_timbratura = 'ENTRATA' and data_turno = '2023-01-11'

            String sql = "select * from Timbratura T, Turno TR where T.ref_data = TR.data_turno and T.ref_impiegato =TR.ref_impiegato and tipo_timbratura = 'ENTRATA' and data_turno = '2023-01-11'";
            preparedStatement  = conn.prepareStatement(sql);
            //preparedStatement.setDate(1, Date.valueOf(data));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                matricole.add(resultSet.getInt(1));

            }
            resultSet.close();
            return matricole;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //da rivedere
    public static LocalTime getFineTurno(int matricola,LocalDate date){
        //LocalTime tempo;
        LocalTime tempo;
        try {
            String sql = "select fine_turno from Turno,Timbratura where ref_impiegato = ? and Turno.ref_impiegato = Timbratura.ref_impiegato and Turno.data_turno = Timbratura.ref_data and data_turno = '2023-01-11'";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
            //preparedStatement.setDate(2,Date.valueOf(data.toString()));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                tempo= resultSet.getTime(1).toLocalTime();
                resultSet.close();
                return tempo;
            }else{
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public static void insertTimbraturaUscitaDimenticata(int matricola, int idTurno, String tipoTurno, String tipoTimbratura,String motivazione, LocalDate data,LocalTime ora){

        try {
            String sql = "insert into Timbratura values(?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
            preparedStatement.setInt(2,idTurno);
            preparedStatement.setString(3,tipoTurno);
            preparedStatement.setString(4,tipoTimbratura);
            preparedStatement.setString(5,motivazione);
            preparedStatement.setDate(6, Date.valueOf(data));
            preparedStatement.setTime(7,Time.valueOf(ora));
            preparedStatement.execute();
            System.out.println("finito");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean verifyUscita(int matricola, LocalDate data){

        try {
            String sql = "select * from Timbratura T, Turno TR where T.ref_impiegato =? and T.ref_data = TR.data_turno and T.ref_impiegato = TR.ref_impiegato and data_turno = ? and T.tipo_timbratura = 'USCITA'";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
            preparedStatement.setDate(2,Date.valueOf(data));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    private static int oreTotaliDipendente(int matricola){

        try {
            String sql = "select Timbratura.ref_impiegato, round(sum(fine_turno-Turno.inizio_turno)/10000,0) from Turno, Timbratura where Turno.ref_impiegato = Timbratura.ref_impiegato and Turno.data_turno = Timbratura.ref_data and Timbratura.ref_impiegato = ? and Timbratura.tipo_timbratura = 'Uscita' and MONTH(data_turno) = ?  group by Timbratura.ref_impiegato";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
            preparedStatement.setInt(2,LocalDate.now().getMonthValue());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {

                return resultSet.getInt(2);
            }
            else {
                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
public static List<Integer> getMatricole(){
        List<Integer> matricole = new ArrayList<>();
    try {
        String sql = "Select  matricola from Impiegato";
        preparedStatement = conn.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            matricole.add(resultSet.getInt(1));
        }
        return matricole;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

public static List<Object> getDatiStipendio(int matricola){
        List<Object> datiStipendio = new ArrayList<>();
        //prendo le ore lavorate e le aggiungo
        datiStipendio.add(oreTotaliDipendente(matricola));
        datiStipendio.add(costoOrario(matricola));
        //aggiungo le trattenute fiscali
        datiStipendio.add(300);
        datiStipendio.add(oreStraordinario(matricola));
    System.out.println("STRAORDINARIO" + oreStraordinario(matricola));
        datiStipendio.add(isReperibile());

return datiStipendio;
}

private static int costoOrario(int matricola){

    try {
        String sql = "select costo_orario from Servizio S, Impiegato I where matricola = ? and ref_servizio = S.id";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,matricola);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }


}
public static int getServizio(int matricola){
    try {
        String sql = "select ref_servizio from Impiegato where matricola = ?" ;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,matricola);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

private static int oreStraordinario(int matricola){
//todo: aggiungere lo straodinario
    try {
        String sql = "select Timbratura.ref_impiegato, round(sum(fine_turno-Turno.inizio_turno)/10000,0) from Turno, Timbratura where Turno.ref_impiegato = Timbratura.ref_impiegato and Turno.data_turno = Timbratura.ref_data and Timbratura.ref_impiegato = ? and Timbratura.tipo_timbratura = 'Uscita' and Turno.straordinario = true group by Timbratura.ref_impiegato and MONTH(data_turno) = ?";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,matricola);
        preparedStatement.setInt(2,LocalDate.now().getMonthValue());
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getInt(2);
        }
        else{
            return 0;
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

private static boolean isReperibile(){
    try {
        String sql  = "select * from Impiegato where reperibile = true";
        preparedStatement = conn.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}
public static List<Turni> getTurni(LocalDate dataCorrente, LocalTime oraCorrente){
List<Turni> listaDaRitornare = new ArrayList<>();
    try {
        String sql = "select T.* from Turno T, Timbratura TR where T.ref_impiegato = TR.ref_impiegato and T.data_turno = TR.ref_data and T.data_turno = ? and TR.tipo_timbratura = 'ENTRATA'";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setDate(1,Date.valueOf(dataCorrente));
        resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
           listaDaRitornare.add(new Turni(resultSet.getInt(1),resultSet.getString(2),resultSet.getTime(3).toLocalTime(),resultSet.getTime(4).toLocalTime(),resultSet.getDate(5).toLocalDate(),resultSet.getBoolean(6)));
        }

        return  listaDaRitornare;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

public static List<Integer> matricoleReperibile(){
       List<Integer> listaDiRitorno = new ArrayList<>();
    try {
        String sql = "select matricola from Impiegato where reperibile = true";
        preparedStatement = conn.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            listaDiRitorno.add(resultSet.getInt(1));
        }
        return listaDiRitorno;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }



}
    public static void updateTurni(int matricola, Turni turno){

        try {
            String sql ="insert into Turno values (?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,turno.getTipoturno());
            preparedStatement.setTime(2,Time.valueOf(turno.getInizioTurno()));
            preparedStatement.setTime(3,Time.valueOf(turno.getFineTurno()));
            preparedStatement.setDate(4,Date.valueOf(turno.getDataTurno()));
            preparedStatement.setBoolean(5,false);
            preparedStatement.setInt(6,matricola);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static String getMail(int matricola){

        try {
            String sql = "select mail_personale from Impiegato where matricola = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, matricola);
           resultSet = preparedStatement.executeQuery();
           resultSet.next();
           return resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
public static List<Integer> getMatricoleImpiegati(Turni turno, int servizio){
     return null;
        //todo da implementare
}

public static List<Object> getDatiTurni(int matricola, LocalDate data){
    List<Object> listaRitorno = new ArrayList<>();
    try {
        System.out.println(matricola);
        String sql =  "select tipo_turno,data_turno,fine_turno from Turno,Timbratura where ref_impiegato = ? and Turno.ref_impiegato = Timbratura.ref_impiegato and Turno.data_turno = Timbratura.ref_data and data_turno = '2023-01-11'";
        preparedStatement = conn.prepareStatement(sql);
       preparedStatement.setInt(1,matricola);
       resultSet = preparedStatement.executeQuery();
       if(resultSet.next()){
           listaRitorno.add(matricola);
           listaRitorno.add(resultSet.getInt(1));
          listaRitorno.add( resultSet.getString(2));
          listaRitorno.add(resultSet.getDate(3).toLocalDate());
          listaRitorno.add(resultSet.getTime(4).toLocalTime());
          return  listaRitorno;

       }else {
           return null;
       }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}
public static List<RichiestaAstensione> getRichiesteAstensione(LocalDate dataCorrente){
        List<RichiestaAstensione> richiestaAstensione = new ArrayList<>();
    try {
        String sql = "select * from richiesta where ? > Richiesta.data_inizio and ?<Richiesta.data_fine";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setDate(1,Date.valueOf(dataCorrente));
        preparedStatement.setDate(2,Date.valueOf(dataCorrente));

        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            richiestaAstensione.add(new RichiestaAstensione(resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3),resultSet.getString(4),resultSet.getDate(5).toLocalDate(),resultSet.getDate(6).toLocalDate(),resultSet.getTime(7).toLocalTime(),resultSet.getTime(8).toLocalTime(),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),resultSet.getBlob(12)));
        }
        return richiestaAstensione;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }


}
    public static List<RichiestaAstensione> getRichiesteAstensione(int matricola){
        List<RichiestaAstensione> richiestaAstensione = new ArrayList<>();
        try {
            String sql = "select * from richiesta where ref_impiegato = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                richiestaAstensione.add(new RichiestaAstensione(resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3),resultSet.getString(4),resultSet.getDate(5).toLocalDate(),resultSet.getDate(6).toLocalDate(),resultSet.getTime(7).toLocalTime(),resultSet.getTime(8).toLocalTime(),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),resultSet.getBlob(12)));
            }
            return richiestaAstensione;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static int getNumImpiegati(){

        try {
            String sql = "select count(matricola) from Impiegato where ruolo = 'Impiegato'";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static int getNumAmministrativi(){

        try {
            String sql = "select count(matricola) from Impiegato where ruolo = 'Amministrativo'";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void insertPropostaTurni(String tipoTurno,LocalTime inizioTurno,LocalTime fineTurno,LocalDate dataTurno,int matricola){

        try {
            String sql = "insert into PropostaTurno values (?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
            preparedStatement.setString(2,tipoTurno);
            preparedStatement.setTime(3,Time.valueOf(inizioTurno));
            preparedStatement.setTime(4,Time.valueOf(fineTurno));
            preparedStatement.setDate(5,Date.valueOf(dataTurno));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static void copia(){
        try {
            String sql ="Select * from propostaturno";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            copia1(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private static void copia1(ResultSet resultSet){

            try {
                while (resultSet.next()){
                    System.out.println("entro");
                    String sql1 = "insert into Turno values(?,?,?,?,?,?)";
                    preparedStatement = conn.prepareStatement(sql1);
                    preparedStatement.setString(1,resultSet.getString(2));
                    preparedStatement.setTime(2,resultSet.getTime(3));
                    preparedStatement.setTime(3,resultSet.getTime(4));
                    preparedStatement.setDate(4,resultSet.getDate(5));
                    preparedStatement.setBoolean(5,false);
                    preparedStatement.setInt(6,resultSet.getInt(1));
                    preparedStatement.execute();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
        public static void timbraImpiegato(int impiegato){
        LocalTime inizio = LocalTime.of(8,0);
            LocalTime fine = LocalTime.of(14,0);
            try {
                String sql = "select * from turno where ref_impiegato = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, impiegato);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    String sql1 = "insert into timbratura values (?,?,?,?,?,?)";
                    preparedStatement = conn.prepareStatement(sql1);
                    preparedStatement.setDate(1,resultSet.getDate(4));
                    preparedStatement.setInt(2,resultSet.getInt(6));
                    preparedStatement.setString(3,"ENTRATA");
                    preparedStatement.setString(4,"null");
                    preparedStatement.setDate(5,resultSet.getDate(4));
                    preparedStatement.setTime(6,Time.valueOf(inizio));
                    preparedStatement.execute();
                    String sql2 = "insert into timbratura values (?,?,?,?,?,?)";
                    preparedStatement = conn.prepareStatement(sql1);
                    preparedStatement.setDate(1,resultSet.getDate(4));
                    preparedStatement.setInt(2,resultSet.getInt(6));
                    preparedStatement.setString(3,"USCITA");
                    preparedStatement.setString(4,"null");
                    preparedStatement.setDate(5,resultSet.getDate(4));
                    preparedStatement.setTime(6,Time.valueOf(inizio));
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        public static void insertStipendio(List<Object> listaDaInserire){
            try {
                String sql = "insert into Stipendio values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,(int)listaDaInserire.get(0));
                preparedStatement.setString(2,(String) listaDaInserire.get(1));
                preparedStatement.setInt(3,(int)listaDaInserire.get(2));
                preparedStatement.setInt(4,(int) listaDaInserire.get(3));
                preparedStatement.setInt(5,(int) listaDaInserire.get(4));
                preparedStatement.setDouble(6,(double) listaDaInserire.get(5));
                preparedStatement.setDouble(7,(double) listaDaInserire.get(6));
                preparedStatement.setInt(8,(int) listaDaInserire.get(7));
                preparedStatement.setInt(9,(int) listaDaInserire.get(8));
                preparedStatement.setBoolean(10,(boolean) listaDaInserire.get(9));
                preparedStatement.setDouble(11,(double) listaDaInserire.get(10));
                preparedStatement.setInt(12,(int) listaDaInserire.get(11));
                preparedStatement.setDouble(13,(double) listaDaInserire.get(12));
                preparedStatement.setInt(14,(int) listaDaInserire.get(13));
                preparedStatement.setDouble(15,(double) listaDaInserire.get(14));
                preparedStatement.execute();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }



