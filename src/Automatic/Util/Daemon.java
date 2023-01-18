package Automatic.Util;

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

            String sql = "select * from Timbratura T, Turno TR where T.ref_turno = TR.id and tipo_timbratura = 'ENTRATA' and data_turno = '2023-01-11'";
            preparedStatement  = conn.prepareStatement(sql);
            //preparedStatement.setDate(1, Date.valueOf(data));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                matricole.add(resultSet.getInt(1));

            }
            resultSet.close();
            preparedStatement.close();
            return matricole;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static LocalTime getFineTurno(int matricola,LocalDate date){
        //LocalTime tempo;
        LocalTime tempo;
        try {
            String sql = "select fine_turno from Turno,Timbratura where ref_impiegato = ? and ref_turno = id and data_turno = '2023-01-11'";
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean verifyUscita(int matricola, LocalDate data){

        try {
            String sql = "select * from Timbratura T, Turno TR where T.ref_impiegato =? and T.ref_turno = TR.id and data_turno = ? and T.tipo_timbratura = 'USCITA'";
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
            String sql = "select Timbratura.ref_impiegato, round(sum(fine_turno-Turno.inizio_turno)/10000,0) from Turno, Timbratura where Timbratura.ref_turno = Turno.id and Timbratura.ref_impiegato = ? and Timbratura.tipo_timbratura = 'Uscita' group by Timbratura.ref_impiegato";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,matricola);
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
        String sql = "select Timbratura.ref_impiegato, round(sum(fine_turno-Turno.inizio_turno)/10000,0) from Turno, Timbratura where Timbratura.ref_turno = Turno.id and Timbratura.ref_impiegato = ? and Timbratura.tipo_timbratura = 'Uscita' and Turno.straordinario = true group by Timbratura.ref_impiegato";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,matricola);
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
        String sql = "select T.* from Turno T, Timbratura TR where TR.ref_turno = T.id and T.data_turno = ? and TR.tipo_timbratura = 'ENTRATA'";
        preparedStatement = conn.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
           listaDaRitornare.add(new Turni(resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3),resultSet.getTime(4).toLocalTime(),resultSet.getTime(5).toLocalTime(),resultSet.getDate(6).toLocalDate()));
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
            preparedStatement.setInt(1,turno.getId());
            preparedStatement.setInt(2,matricola);
            preparedStatement.setString(3,turno.getTipoturno());
            preparedStatement.setTime(4,Time.valueOf(turno.getInizioTurno()));
            preparedStatement.setTime(5,Time.valueOf(turno.getFineTurno()));
            preparedStatement.setDate(6,Date.valueOf(turno.getDataTurno()));

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

public List<Object> getDatiTurni(int matricola, LocalDate data){
    List<Object> listaRitorno = new ArrayList<>();
    try {
        String sql =  "select id,tipo_turno,fine_turno from Turno,Timbratura where ref_impiegato = ? and ref_turno = id and data_turno = '2023-01-11'";
        preparedStatement = conn.prepareStatement(sql);
       preparedStatement.setInt(1,matricola);
       if(resultSet.next()){
           listaRitorno.add(matricola);
           listaRitorno.add(resultSet.getInt(1));
          listaRitorno.add( resultSet.getString(2));
          listaRitorno.add(resultSet.getTime(1));
          return  listaRitorno;

       }else {
           return null;
       }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}
}

