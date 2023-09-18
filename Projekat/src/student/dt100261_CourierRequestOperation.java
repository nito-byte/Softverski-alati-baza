/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierRequestOperation;


public class dt100261_CourierRequestOperation implements CourierRequestOperation {

    private int IDKorisnik;
    private int IDVozilo;

    @Override
    public boolean insertCourierRequest(String userName, String licencePlateNumber) {
        try {
            //  boolean insertCourierRequest(@NotNull java.lang.String userName,
            //                               @NotNull java.lang.String licencePlateNumber)
            //  Parameters:
            //      userName - of user who intends to become courier.
            //      licencePlateNumber - of the vehicle they intend to drive.
            //  Returns:
            //      success of operation.

            //  dohvatim Korisnika iz tabele korisnik, return false ako ne moze
            //  dohvatim Vozilo iz tabele Vozilo, return false ako ne moze
            //  insertujem u tabelu KurirZahtev
            ////////////////////////////////////////
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Korisnik "
                    + " WHERE Username = ? ");
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();

            int rez = -1;
            while (rs.next()) {
                rez = rs.getInt(1);
            }

            if (rez == -1) {
                return false;
            }
            IDKorisnik = rez;

            ///////////////////////////////////////////////
            PreparedStatement ps1 = connection.prepareStatement(" SELECT IDVozilo "
                    + " FROM Vozilo "
                    + " WHERE RegistracioniBroj = ? ");
            ps1.setString(1, licencePlateNumber);
            ResultSet rs1 = ps1.executeQuery();

            int rez1 = -1;
            while (rs1.next()) {
                rez1 = rs1.getInt(1);
            }

            if (rez1 == -1) {
                return false;
            }
            IDVozilo = rez1;

            ///////////////////////////////////////////////
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO KurirZahtev values(?,?)");
            ps2.setInt(1, IDKorisnik);
            ps2.setInt(2, IDVozilo);
            int r = ps2.executeUpdate();

            if (r == 0) {
                return false;
            }

            return true;
        } catch (SQLException ex) {
           // System.out.println("greska");
            return false;
        }

    }

    @Override
    public boolean deleteCourierRequest(String userName) {
        int IDKor;
        //  boolean deleteCourierRequest(@NotNull java.lang.String userName)
        //  Parameters:
        //      userName - - username
        //  Returns:
        //      success of operation.

        // treba da obrisem korisnikov zahtev za vozilom iz tabele KurirZahtev
        try {

            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Korisnik "
                    + " WHERE Username = ? ");
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();

            int rez = -1;
            while (rs.next()) {
                int i = rs.getInt(1);
            }

            if (rez == -1) {
                return false;
            }
            IDKor = rez;

            PreparedStatement ps1 = connection.prepareStatement(" DELETE KorisnikZahtev "
                    + " WHERE IDKorisnik = ?");
            ps1.setInt(1, IDKor);
            int pom = ps1.executeUpdate();
            if (pom != 0) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean changeVehicleInCourierRequest(String userName, String licencePlateNumber) {
        int IDKor, IDVoz;
        try {
            //  boolean changeVehicleInCourierRequest(@NotNull java.lang.String userName,
            //                                        @NotNull java.lang.String licencePlateNumber)
            //  Parameters:
            //      userName - - username
            //      licencePlateNumber - new license plate number.
            //  Returns:
            //      success of operation.

            // treba da dohvatim IDKorisnika i IDVozila i da onda:
            // proverim da li postoji zahtev za taj IDKorisnika, ako ne return false
            // ako postoji onda update na novi IDVozila
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Korisnik "
                    + " WHERE Username = ? ");
            ps.setString(1, licencePlateNumber);
            ResultSet rs = ps.executeQuery();

            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt(1);
            }

            if (rez == 0) {
                return false;
            }
            IDKor = rez;

            ///////////////////////////////////////////////
            PreparedStatement ps1 = connection.prepareStatement(" SELECT IDVozilo "
                    + " FROM Vozilo "
                    + " WHERE RegistracioniBroj = ? ");
            ps1.setString(1, licencePlateNumber);
            ResultSet rs1 = ps1.executeQuery();

            int rez1 = 0;
            while (rs1.next()) {
                rez = rs1.getInt(1);
            }

            if (rez1 == 0) {
                return false;
            }
            IDVoz = rez1;

            ///////////////////////////////////////////////
            PreparedStatement ps2 = connection.prepareStatement(" UPDATE KurirZahtev "
                    + " SET IDVozilo = ? "
                    + " WHERE IDKorisnik = ? ");
            ps2.setInt(1, IDVoz);
            ps2.setInt(2, IDKor);
            int rez2 = ps2.executeUpdate();

            if (rez2 == 0) {
                return false;
            }

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<String> getAllCourierRequests() {
        //  java.util.List<java.lang.String> getAllCourierRequests()
        //  Returns:
        //      List of all courier requests.

        try {
            Connection connection = DB.getInstance().getConnection();
            List<String> list = new ArrayList<String>();
            String query = " SELECT IDKorisnik from KurirZahtev ";
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int i = rs.getInt(1);
                Connection connection1 = DB.getInstance().getConnection();
                PreparedStatement ps1 = connection1.prepareStatement(" SELECT Username "
                        + " from Korisnik "
                        + " where IDKorisnik = ? ");
                ps1.setInt(1, i);
                ResultSet rs1 = ps1.executeQuery();

                String rez = null;
                while (rs.next()) {
                    rez = rs.getString(1);
                    rez = rez.replaceAll("\\s+", "");
                    list.add(rez);
                }

            }

            return list;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public boolean grantRequest(String username) {
        //  boolean grantRequest(@NotNull java.lang.String username)
        //  Grants a request for user to become courier.
        //  Parameters:
        //         username - - username
        //  Returns:
        //         success of operation.

       
            try {
                Connection connection = DB.getInstance().getConnection();
                CallableStatement cstmt = connection.prepareCall("{? = call dbo.spGrantRequest(?)}");
                cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
                cstmt.setString(2, username);
                cstmt.execute();
                System.out.println("RETURN STATUS: " + cstmt.getInt(1));
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //mislim da treba da mi bude IDKorisnik uneque, IDVozilo uneque
            //i da mi je kombinacija IDKorisnik, IDVozilo u tabeli kurir jedinstvena
            return true;
       
    }

}

//public static void executeSprocInParams(Connection con) {  
//   try {  
//      PreparedStatement pstmt = con.prepareStatement("{call dbo.uspGetEmployeeManagers(?)}");  
//      pstmt.setInt(1, 50);  
//      ResultSet rs = pstmt.executeQuery();  
//
//      while (rs.next()) {  
//         System.out.println("EMPLOYEE:");  
//         System.out.println(rs.getString("LastName") + ", " + rs.getString("FirstName"));  
//         System.out.println("MANAGER:");  
//         System.out.println(rs.getString("ManagerLastName") + ", " + rs.getString("ManagerFirstName"));  
//         System.out.println();  
//      }  
//      rs.close();  
//      pstmt.close();  
//   }  
//
//   catch (Exception e) {  
//      e.printStackTrace();  
//    }  
//}  
