/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierOperations;


public class dt100261_CourierOperations implements CourierOperations {

    private int param;
    private int IDVozila;
    private int IDKorisnik;

    @Override
    public boolean insertCourier(String username, String regBroj) {
        try {
            //  boolean insertCourier(@NotNull java.lang.String courierUserName,
            //                      @NotNull java.lang.String licencePlateNumber)
            //  Creates a new courier.
            //  Parameters:
            //      courierUserName - theirs user name.
            //      licencePlateNumber - of the vehicle that they are driving.
            //  Returns:
            //      success of operation.

            //[IDKorisnik],[IDVozilo],[BrojIsporucenihPaketa],[OstvarenProfit],[Status]
            //na osnovu username-a dohvatim IDKorisnika, tj. IDKurira
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps1 = connection.prepareStatement(" SELECT IDKorisnik "
                    + " from Korisnik "
                    + " where Username = ? ");
            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();

            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt(1);
                System.out.println("id " + rez);
            }
            param = rez;

            if (rez != 0) {
                IDKorisnik = rez;
                //treba da proverim da li se nalazi u tabeli Vozilo za dati regBroj
                PreparedStatement ps2 = connection.prepareStatement(" SELECT IDVozilo "
                        + " from Vozilo "
                        + " where RegistarskiBroj = ? ");
                ps2.setString(1, regBroj);
                ResultSet rs2 = ps2.executeQuery();

                int rez2 = 0;
                while (rs2.next()) {
                    rez2 = rs2.getInt(1);
                    System.out.println("id " + rez2);
                }

                if (rez2 != 0) {
                    IDVozila = rez2;

                    connection = DB.getInstance().getConnection();
                    PreparedStatement ps = connection.prepareStatement(" INSERT INTO Kurir values(?,?,?,?,?) ");
                    ps.setInt(1, IDKorisnik);
                    ps.setInt(2, IDVozila);
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, 0);
                    int rez1 = ps.executeUpdate();

                    if (rez1 == 0) {
                        return false;
                    }

                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean deleteCourier(String username) {
        try {
            //  boolean deleteCourier(@NotNull java.lang.String courierUserName)
            //  Deletes a courier by user name.
            //  Parameters:
            //      courierUserName - -
            //  Returns:
            //      success of the operation.

            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("delete Korisnik where Username = ?");
            ps.setString(1, username);
            int pom = ps.executeUpdate();
            if (pom != 0) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public List<String> getCouriersWithStatus(int paramInt) {
        //  java.util.List<java.lang.String> getCouriersWithStatus(int statusOfCourier)
        //  Parameters:
        //      statusOfCourier - - see project documentation.
        //  Returns:
        //      List of all couriers with specific status.

        try {
            Connection connection = DB.getInstance().getConnection();
            List<String> list = new ArrayList<String>();
            String query = "select IDKorisnik from Kurir where Status = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int i = rs.getInt(1);
                System.out.println("Lista: " + i);
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
    public List<String> getAllCouriers() {
        //  java.util.List<java.lang.String> getAllCouriers()
        //  Returns:
        //      List of all couriers in the system. Should be sorted by profit descending.

        // treba da dohvatim sve ID-eve kurira koje sam sortirala desc za profit,
        //a onda za njih da dohvatim njihov username
        try {
            Connection connection = DB.getInstance().getConnection();
            List<String> list = new ArrayList<String>();
            String query = " SELECT IDKorisnik from Kurir "
                         + " ORDER BY OstvarenProfit DESC";
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int i = rs.getInt(1);
                Connection connection1 = DB.getInstance().getConnection();
                PreparedStatement ps1 = connection1.prepareStatement(" SELECT Username "
                        + " from Korisnik "
                        + " where IDKorisnik = ? ");
                ps1.setInt(1, i);
                ResultSet rs1 = ps1.executeQuery();

                String rez = null;
                while (rs1.next()) {
                    rez = rs1.getString(1);
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
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        //java.math.BigDecimal getAverageCourierProfit(int numberOfDeliveries)
        //Parameters:
        //    numberOfDeliveries - that a courier has completed.
        //Returns:
        //    average profit for couriers. Only count couriers whose number of deliveries 
        //    is equal or greater than numberOfDeliveries.

        
        // TREBA DA PRODJEM KROZ TABELU Kurir i da izracunam AVG za OstvarenProfit
        // ali treba da stavim u where klauzulu ovaj uslov
        
        
        try{
        Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT AVG(OstvarenProfit)"
                                            + " FROM Kurir"
                                            + " WHERE BrojIsporucenihPaketa >= ?");
            ps.setInt(1, numberOfDeliveries);
            ResultSet rs = ps.executeQuery();
            
            BigDecimal rez = null;
            while (rs.next()) {
                    rez = rs.getBigDecimal(1);
            }
            
            return rez;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

}
