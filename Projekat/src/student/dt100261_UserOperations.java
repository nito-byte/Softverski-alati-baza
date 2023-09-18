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
import operations.UserOperations;


public class dt100261_UserOperations implements UserOperations {

    private int param;
    private int brObrisanih;
    private int brPaketa;

    @Override
    public boolean insertUser(String username, String firstName, String lastName, String password) {
        
    //         boolean insertUser(@NotNulljava.lang.String userName,
    //                   @NotNulljava.lang.String firstName,
    //                   @NotNulljava.lang.String lastName,
    //                   @NotNulljava.lang.String password)
    //         Insets new user to the system.
    //Parameters:
    //      userName - - has to be unique.
    //      firstName - - has to start with capital letter.
    //      lastName - - has to start with capital letter.
    //      password - - has to be longer than 8 characters. 
    //      It should contain at least one number and one letter.
    //Returns:
    //      success of the operation.
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Korisnik values(?,?,?,?,?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setInt(5, 0);
            ps.executeUpdate();

            return true;
        } catch (SQLException ex) {
           // System.out.println("greska");
            return false;
        }

    }

    @Override
    public int declareAdmin(String userName) {
    //        int declareAdmin(@NotNull java.lang.String userName)
    //        Declares the given user as an admin.
    //Parameters:
    //      userName - - of the future admin.
    //Returns:
    //      0 - success. 1 - already admin. 2 - failed due to lack of given user.
        int vr = 0;
        try {
            vr = pomocnaMetodaDeclareAdmin(userName);
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_UserOperations.class.getName()).log(Level.SEVERE, null, ex);

        }
        return vr;

    }

    public int pomocnaMetodaDeclareAdmin(String userName) throws SQLException {

        // int declareAdmin(@NotNull java.lang.String userName)
        //Declares the given user as an admin.
        //Parameters:
        //    userName - -of the future admin.
        //Returns:
        //    0 - success. 1 - already admin. 2 - failed due to lack of given user.
        //treba da proverim da li vec postoji u tabeli usera
        Connection connection = DB.getInstance().getConnection();
        PreparedStatement ps1 = connection.prepareStatement(" SELECT IDKorisnik "
                + " from Korisnik "
                + " where Username = ? ");
        ps1.setString(1, userName);
        ResultSet rs = ps1.executeQuery();

        int rez = -1;
        while (rs.next()) {
            rez = rs.getInt(1);
            System.out.println("id " + rez);
        }
        param = rez;

        if (rez != -1) {
            //treba da proverim da li se vec nalazi u tabeli admin
            PreparedStatement ps2 = connection.prepareStatement(" SELECT * "
                    + " from Admin "
                    + " where IDKorisnik = ? ");
            ps2.setInt(1, param);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = -1;
            while (rs2.next()) {
                rez2 = rs2.getInt(1);
                System.out.println("id " + rez2);
            }

            if (rez2 != -1) {
                return 1;
            } else {
                PreparedStatement ps3 = connection.prepareStatement("INSERT INTO Admin values(?)");
                ps3.setInt(1, param);
                ps3.executeUpdate();

                //treba da vratim 0 ako sam insertovala u tabelu admin
                return 0;
            }

        } else {
            return 2;
        }
    }

    @Override
    public Integer getSentPackages(String... names) {
//        Returns a number of sent packages for the all  users.
//                Parameters: userNames - -of the users whose packages is to be counted
//                Returns:Number of sent packages.If there are no such user, it should be null.

        brPaketa = 0;
        for (String i : names) {
            try {
                Connection connection = DB.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT BrojPoslatihPaketa"
                        + " FROM Korisnik where Username = ?");
                ps.setString(1, i);
                ResultSet rs = ps.executeQuery();
                
                if (!rs.isBeforeFirst()) {
                    return null;
                }

                int rez = 0;
                while (rs.next()) {
                    rez = rs.getInt(1);
                    brPaketa += rez;
                }

                

                // return brPaketa;
            } catch (SQLException ex) {
                Logger.getLogger(dt100261_CityOperations.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

        return brPaketa;
    }

    @Override
    public int deleteUsers(String... names) {
    //        int deleteUsers(@NotNull
    //                java.lang.String... userNames)
    //Delete all users with given names.
    //Parameters:
    //userNames - - username
    //Returns:
    //number of deleted users.
        brObrisanih = 0;
        for (String i : names) {
            try {
                Connection connection = DB.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement("delete Korisnik where Username = ?");
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
    public List<String> getAllUsers() {
    //        java.util.List<java.lang.String> getAllUsers()
    //Returns:
    //List of Integer, user names of all users.
        
        
        try {
            Connection connection = DB.getInstance().getConnection();
            List<String> list = new ArrayList<String>();

            String query = "select Username from Korisnik";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                String i = rs.getString(1);
                i = i.replaceAll("\\s+", "");  //izbacim sve blanko znakove 
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
