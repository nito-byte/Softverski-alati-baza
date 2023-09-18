/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.DistrictOperations;


public class dt100261_DistrictOperations implements DistrictOperations {

    private int brObrisanih;

    

    @Override
    public int insertDistrict(String name, int cityId, int x, int y) {
    //  int insertDistrict(java.lang.String name, int cityId,int xCord,int yCord)
    //      Insert new district with given name, the city which that 
    //      district is a part of, and coordinates of the district.
    //Parameters:
    //      name - - district name.
    //      cityId - - primary key of the city.
    //      xCord - - x cord ("longitude") given in km.
    //      yCord - - y cord ("latitude") given in km.
    //Returns:
    //      new row's primary key on success, or -1 on failure.
        
        
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" INSERT into Opstina VALUES (?,?,?,?) ");
            ps.setString(1, name);
            ps.setInt(2, cityId);
            ps.setInt(3, x);
            ps.setInt(4, y);
            ps.executeUpdate();
          //  System.out.println("insertovana opstina");

            PreparedStatement ps1 = connection.prepareStatement("select IDOpstina from Opstina "
                                                                + " where Naziv = ? "
                                                                + " and IDGra = ? "
                                                                + " and X = ? "
                                                                + " and Y = ? ");
            ps1.setString(1, name);
            ps1.setInt(2, cityId);
            ps1.setInt(3, x);
            ps1.setInt(4, y);
            ResultSet rs = ps1.executeQuery();

            int rez = -1;
            while (rs.next()) {
                rez = rs.getInt(1);
                System.out.println("id " + rez);
            }
            
            if(rez==-1){
                return -1;
            }
            
            return rez;

        } catch (SQLException ex) {
            System.out.println("greska");
            return -1;
        }

       
    }

    @Override
    public int deleteDistricts(String... names) {
    //        int deleteDistricts(@NotNull java.lang.String... names)
    //  Delete districts by name.
    //Parameters:
    //  names - - names of the districts to be deleted.
    //Returns:
    //  number of deleted records.
        
  
        
        System.out.println("Number of arguments: " + names.length);
        brObrisanih = 0;
        for (String i : names) {
            try {
              //  System.out.println(" Ime iz niza: " + i);

                Connection connection = DB.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement("delete from Opstina where Naziv = ?");
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
    public boolean deleteDistrict(int idDistrict) {
    //  boolean deleteDistrict(int idDistrict)
    //  Delete district by primary key.
    //Parameters:
    //  idDistrict - - primary key of the district to be deleted.
    //Returns:
    //  success of operation.
           try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("delete from Opstina where IDOpstina = ?");
            ps.setInt(1, idDistrict);
            int pom = ps.executeUpdate();
            if (pom != 0) {
                return true;
            } else {
                return false;

            }
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);

        }

        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String nazivGrada) {
    //        int deleteAllDistrictsFromCity(@NotNull java.lang.String nameOfTheCity)
    //        Delete all district from given city.
    //Parameters:
    //      nameOfTheCity - - the name of the city, containing all district that should be deleted.
    //Returns:
    //      number of deleted records.
        
        
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps1 = connection.prepareStatement("select IDGra from Grad "
                                                                + " where Naziv = ? ");
            ps1.setString(1, nazivGrada);
            ResultSet rs = ps1.executeQuery();
           
            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt("IDGra");
                System.out.println("id " + rez);
            }
            
            
            PreparedStatement ps = connection.prepareStatement("delete from Opstina where IDGra = ?");
            ps.setInt(1, rez);
            int pom = ps.executeUpdate();
            return pom;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

        
        
        
        
        
        
    
     //   Connection connection = DB.getInstance().getConnection();
       /* try {
            PreparedStatement p = connection.prepareStatement("select IDGra from Grad where Naziv = ?");
            p.setString(1, nameOdTheCity);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                int IDGra = rs.getInt(1);
                PreparedStatement ps = connection.prepareStatement("delete Opstina where IDGra = ?");
                ps.setInt(1, IDGra);
                return ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }*/
     //   return -1;
   // }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idCity) {
//        java.util.List<java.lang.Integer> getAllDistrictsFromCity(int idCity)
//      Get primary keys of all districts from the given.
//Parameters:
//      idCity - - primary key of the desired city.
//Returns:
//      List of Integer, primary keys of all districts. 
//      Should be null if there is no city with the given primary key.
        Connection connection = DB.getInstance().getConnection();
        try {
            List<Integer> list = new ArrayList<Integer>();
            PreparedStatement ps = connection.prepareStatement("select IDOpstina from Opstina where IDGra = ?");
            ps.setInt(1, idCity);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int i = rs.getInt(1);
                list.add(i);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public List<Integer> getAllDistricts() {
        
    //        java.util.List<java.lang.Integer> getAllDistricts()
    //      Get primary keys of all districts in the system.
    //Returns:
    //      List of Integer, primary keys of all districts.
        Connection connection = DB.getInstance().getConnection();
        try {
            List<Integer> list = new ArrayList<Integer>();
            String query = "select IDOpstina from Opstina";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int i = rs.getInt(1);
                list.add(i);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
