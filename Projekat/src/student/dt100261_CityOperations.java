/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CityOperations;

public class dt100261_CityOperations implements CityOperations {

    private int brObrisanih;

    public dt100261_CityOperations() {
    }

    @Override
    public int insertCity(String name, String postalCode) {
        
    //   int insertCity(java.lang.String name, java.lang.String postalCode)
    //   Insert new city.
    //Parameters:
    //  name - - the name of the city.
    //  postalCode - - postal code for the city.
    //Returns:
    //  new row's primary key on success, or -1 on failure.

    try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Grad values(?,?)");
            ps.setString(1, name);
            ps.setString(2, postalCode);
            ps.executeUpdate();


            PreparedStatement ps1 = connection.prepareStatement("select IDGra from Grad where Naziv = ? and PostanskiBroj = ?");
            ps1.setString(1, name);
            ps1.setString(2, postalCode);
            ResultSet rs = ps1.executeQuery();

            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt(1);
                System.out.println("id " + rez);
            }
            
            if(rez==0){
                return -1;
            }
            
            return rez;

        } catch (SQLException ex) {
            System.out.println("greska");
            return -1;
        }

    }

    @Override
    public int deleteCity(String... names) {
    //  int deleteCity(@NotNull java.lang.String... names)
    //  Delete cities by name.
    //Parameters:
    //  names - - names of the cities to be deleted.
    //Returns:
    //  number of deleted records.

        System.out.println("Number of arguments: " + names.length);
        brObrisanih = 0;
        for (String i : names) {
            try {
              //  System.out.println(" Ime iz niza: " + i);

                Connection connection = DB.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement("delete Grad where Naziv = ?");
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
    public boolean deleteCity(int idCity) {
    //   boolean deleteCity(int idCity)
    //   Delete city by primary key.
    //Parameters:
    //   idCity - - primary key of the city to be deleted.
    //Returns:
    //   success of operation.
        
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("delete Grad where IDGra = ?");
            ps.setInt(1, idCity);
            int pom = ps.executeUpdate();
            if (pom != 0) {
                return true;
            } else {
                return false;

            }
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;

        }

       // return false;
    }

    @Override
    public List<Integer> getAllCities() {
    //java.util.List<java.lang.Integer> getAllCities()
    //Get primary keys of all cities in the system.
    //Returns:
    //  List of Integer, primary keys of all cities.
        try {
            Connection connection = DB.getInstance().getConnection();
            List<Integer> list = new ArrayList<Integer>();
            String query = "select IDGra from Grad";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int i = rs.getInt(1);
                System.out.println("Lista: " + i);
                list.add(i);
            }

            return list;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_CityOperations.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
