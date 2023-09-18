/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.GeneralOperations;


public class dt100261_GeneralOperations implements GeneralOperations {

    Connection connection = DB.getInstance().getConnection();

    @Override
    public void eraseAll() {
        try {
            String query = "delete from Grad";
            Statement stm = connection.createStatement();
            stm.executeUpdate(query);
            query = "delete  from Opstina";
            stm.executeUpdate(query);
            query = "delete from Korisnik";
            stm.executeUpdate(query);
            query = "delete from Admin";
            stm.executeUpdate(query);
            query = "delete  from Vozilo";
            stm.executeUpdate(query);

            query = "delete  from Kurir";
            stm.executeUpdate(query);
            query = "delete  from Ponuda"; 
            stm.executeUpdate(query);
            
            query = "delete  from Paket"; 
            stm.executeUpdate(query);
            query = "delete  from Pomocna"; 
            stm.executeUpdate(query);
            query = "delete  from Voznja"; 
            stm.executeUpdate(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
