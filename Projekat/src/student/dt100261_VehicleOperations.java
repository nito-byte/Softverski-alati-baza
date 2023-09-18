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
import operations.VehicleOperations;


public class dt100261_VehicleOperations implements VehicleOperations {

    private int brObrisanih;

    @Override
    public boolean insertVehicle(String regBroj, int tipGoriva, BigDecimal potrosnja) {
        try {
            //boolean insertVehicle(@NotNull java.lang.String licencePlateNumber,int fuelType,
            //                               java.math.BigDecimal fuelConsumtion)
            //    Inserts new vehicle in the system.
            //    Parameters:
            //        licencePlateNumber - - "registration number". Unique.
            //        fuelType - - see project specification.
            //        fuelConsumtion - - see project specification.
            //    Returns:
            //        success of operation.

            
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" INSERT INTO Vozilo values(?,?,?) ");
            ps.setString(1, regBroj);
            ps.setInt(2, tipGoriva);
            ps.setBigDecimal(3, potrosnja.setScale(3, RoundingMode.HALF_UP));
            int rez = ps.executeUpdate();
            
            if(rez == 0){
                return false;
            }
            
            
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }        
     

    @Override
    public int deleteVehicles(String... names) {
        //int deleteVehicles(@NotNull java.lang.String... licencePlateNumbers)
        //Parameters:
        //    licencePlateNumbers - -
        //Returns:
        //    number of deleted vehicles
    

        brObrisanih = 0;
        for (String i : names) {
            try {

                Connection connection = DB.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE Vozilo where RegistracioniBroj = ?");
                ps.setString(1, i);
                int pom = ps.executeUpdate();
                if (pom != 0) {
                    brObrisanih++;
                } 

            } catch (SQLException ex) {
                Logger.getLogger(dt100261_CityOperations.class.getName()).log(Level.SEVERE, null, ex);

            }
            
        }

       return brObrisanih;
    }
    
    
    @Override
    public List<String> getAllVehichles() {
    //    java.util.List<java.lang.String> getAllVehichles()
    //    Returns:
    //        {@link List } of licence plate numbers of all vehicles.
    
    try {
            Connection connection = DB.getInstance().getConnection();
            List<String> list = new ArrayList<String>();
            String query = "select RegistracioniBroj from Vozilo";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                String i = rs.getString(1);
               // System.out.println("Lista: " + i);
                i = i.replaceAll("\\s+", "");  //izbacim sve blanko znakove
                list.add(i);
            }

            return list;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);
                return null;

        }
        
    }



    @Override
    public boolean changeFuelType(String regBroj, int tipGoriva) {
       // boolean changeFuelType(@NotNull java.lang.String licensePlateNumber, int fuelType)
       // Parameters:
       //      licensePlateNumber - -
       //      fuelType - -
       // Returns:
       //     success of the operation.
       try{
         Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" UPDATE Vozilo "
                                    + " SET TipGoriva = ? " 
                                    + " WHERE RegistracioniBroj = ? ");
            ps.setInt(1, tipGoriva);
            ps.setString(2, regBroj);
            int rez = ps.executeUpdate();
            
            if(rez == 0){
                return false;
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

    @Override
    public boolean changeConsumption(String regBroj, BigDecimal potrosnja) {
     //   boolean changeConsumption(@NotNull java.lang.String licensePlateNumber, java.math.BigDecimal fuelConsumption)
     //   Parameters:
     //       licensePlateNumber - -
     //       fuelConsumption - -
     //   Returns:
     //       success of the operation.
       try{
         Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" UPDATE Vozilo "
                                    + " SET Potrosnja = ? " 
                                    + " WHERE RegistracioniBroj = ? ");
            
            ps.setBigDecimal(1, potrosnja.setScale(3, RoundingMode.HALF_UP));
            ps.setString(2, regBroj);
            int rez = ps.executeUpdate();
            
            if(rez == 0){
                return false;
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
}
