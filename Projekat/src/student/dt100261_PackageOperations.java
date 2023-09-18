/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.PackageOperations;


public class dt100261_PackageOperations implements PackageOperations {

    public BigDecimal getPackagePrice1(int type, BigDecimal weight, double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
            case 0:
                return new BigDecimal(10.0D * distance).multiply(percentage.add(new BigDecimal(1)));
            case 1:
                return new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance).multiply(percentage.add(new BigDecimal(1)));
            case 2:
                return new BigDecimal((75.0D + weight.doubleValue() * 600.0D) * distance).multiply(percentage.add(new BigDecimal(1)));
        }
        return null;
    }

    public double euclidean1(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        try {
            //        int insertPackage(int districtFrom,
            //                          int districtTo,
            //                          @NotNull java.lang.String userName,
            //                          int packageType,
            //                          java.math.BigDecimal weight)
            //        Parameters:
            //            districtFrom - departing district.
            //            districtTo - arrival district.
            //            userName - of sender.
            //            packageType - - see project documentation.
            //            weight - - see project documentation.
            //        Returns:
            //            primary key of newly inserted row. In case of failure, return -1.

            //treba prvo da dohvatim IDOpstinePreuzima, ako ne postoji return -1
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT * "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps.setInt(1, districtFrom);
            ResultSet rs = ps.executeQuery();  //mislim da je ovo provera da li uopste postoji opstina

            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt(1);
            }

            if (rez == 0) {
                return -1;
            }

            //treba da dohvatim IDOpstinaDostavlja, ako ne postoji return -1
            PreparedStatement ps1 = connection.prepareStatement(" SELECT * "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps1.setInt(1, districtTo);
            ResultSet rs1 = ps1.executeQuery();

            int rez1 = 0;
            while (rs1.next()) {
                rez1 = rs1.getInt(1);
            }

            if (rez1 == 0) {
                return -1;
            }

            //treba da dohvatim IDKorisnika na osnovu userName-a, ako ne postoji return -1
            PreparedStatement ps2 = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Korisnik "
                    + " WHERE Username = ? ");
            ps2.setString(1, userName);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = 0;
            while (rs2.next()) {
                rez2 = rs2.getInt(1);
            }

            if (rez2 == 0) {
                return -1;
            }
            int IDKorisnik = rez2;

            //postavim inicijalni Status, Cenu, VremePrihvatanja i ove ID-eve sa INSERT-om, ako ne return -1,
            PreparedStatement ps3 = connection.prepareStatement("INSERT INTO Paket "
                    + " VALUES(?,?,?,?,?,?,?,?)");
            ps3.setInt(1, packageType);
            ps3.setBigDecimal(2, weight.setScale(3, RoundingMode.HALF_UP));
            ps3.setInt(3, 0);
            ps3.setBigDecimal(4, null);
            ps3.setDate(5, null);
            ps3.setInt(6, districtFrom);
            ps3.setInt(7, districtTo);
            ps3.setInt(8, IDKorisnik);
            int r = ps3.executeUpdate();

            if (r == 0) {
                return -1;
            }
            //vracam primarni kljuc dodatog reda
            //return r;  

            PreparedStatement p = connection.prepareStatement(" SELECT IDPaket "
                    + " FROM Paket "
                    + " WHERE TipPaketa = ? "
                    + " AND TezinaPaketa = ?"
                    + " AND OpstinaPreuzima = ?"
                    + " AND OpstinaDostavlja = ?"
                    + " AND IDKorisnik = ? ");
            p.setInt(1, packageType);
            p.setBigDecimal(2, weight.setScale(3, RoundingMode.HALF_UP));
            p.setInt(3, districtFrom);
            p.setInt(4, districtTo);
            p.setInt(5, IDKorisnik);
            ResultSet rr = p.executeQuery();

            int re = 0;
            while (rr.next()) {
                re = rr.getInt(1);
            }

            if (re == 0) {
                return -1;
            }

            return re;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {
        try {
            //        int insertTransportOffer(@NotNull java.lang.String couriersUserName,
            //                         int packageId,
            //                         java.math.BigDecimal pricePercentage)
            //        Used for creating new transport offer. If the price percentage is null,
            //        then that should be randomly generated (in bounds of +/-10%).
            //
            //        Parameters:
            //                couriersUserName - who is submitting an offer.
            //                packageId - -
            //                pricePercentage - - explanation given above.
            //        Returns:
            //                primary key of newly inserted offer. In case of failure, return -1.

            // na osnovu userName dohvatim koji je IDKurira (tabela Korisnik)
            
            
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ppp = connection.prepareStatement(" SELECT Ku.Status,Ku.IDKorisnik "
                    + " FROM Kurir Ku ,Korisnik Ko  "
                    + " WHERE Ku.IDKorisnik = Ko.IDKorisnik "
                    + " AND  Ko.Username = ? ");
            ppp.setString(1, couriersUserName);
            ResultSet rss = ppp.executeQuery();

            
            int IDKorisnik = -1;
            int Status = -1;
            while (rss.next()) {
                Status = rss.getInt(1);  // ovo je status Kurira
                IDKorisnik = rss.getInt(2);
            }

            if (Status == -1) {
                return -2;
            }

            //ako kurir trenutno vozi, on ne daje ponude
            if (Status == 1) {
                return -1;
            }
            
            
            
            
            PreparedStatement ps = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Korisnik "
                    + " WHERE Username = ? ");
            ps.setString(1, couriersUserName);
            ResultSet rs = ps.executeQuery();

            int rez = 0;
            while (rs.next()) {
                rez = rs.getInt(1);
            }

            if (rez == 0) {
                return -1;
            }
            int IDKurir = rez;

            // sad odem i proverim da li je taj korisnik kurir
            PreparedStatement ps1 = connection.prepareStatement(" SELECT * "
                    + " FROM Kurir "
                    + " WHERE IDKorisnik = ? ");
            ps1.setInt(1, IDKurir);
            ResultSet rs1 = ps1.executeQuery();

            int rez1 = 0;
            while (rs1.next()) {
                rez1 = rs1.getInt(1);
            }

            if (rez1 == 0) {
                return -1;
            }

            // dohvatim i IDVozila za tog kurira
            PreparedStatement ps2 = connection.prepareStatement(" SELECT IDVozilo "
                    + " FROM Kurir "
                    + " WHERE IDKorisnik = ? ");
            ps2.setInt(1, IDKurir);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = 0;
            while (rs2.next()) {
                rez2 = rs2.getInt(1);
            }

            if (rez2 == 0) {
                return -1;
            }
            int IDVozilo = rez2;

            // ako je pricePercentage null, onda treba da generisem neki procenat izmedju 90 i 110
            if (pricePercentage == null) {
                Random r = new Random();
                int lower = 10;
                int upper = 100;
                int pom = (int) (Math.random() * (upper - lower)) + lower;
                pricePercentage = BigDecimal.valueOf(pom).movePointLeft(3);
            }

            System.out.println("PRICE PERCENTAGE " + pricePercentage);

            // uradim INSERT u tabelu
            PreparedStatement ps3 = connection.prepareStatement("INSERT INTO Ponuda "
                    + " VALUES(?,?,?,?)");
            ps3.setBigDecimal(1, pricePercentage.setScale(3, RoundingMode.HALF_UP));
            ps3.setInt(2, packageId);
            ps3.setInt(3, IDKurir);
            ps3.setInt(4, IDVozilo);
            int r = ps3.executeUpdate();

            if (r == 0) {
                return -1;
            }
            //vracam primarni kljuc dodatog reda
            //return r;  

            PreparedStatement p = connection.prepareStatement(" SELECT IDPonuda "
                    + " FROM Ponuda "
                    + " WHERE ProcenatCeneIsporuke = ? "
                    + " AND IDPaket = ?"
                    + " AND IDKorisnik = ?"
                    + " AND IDVozilo = ? ");
            p.setBigDecimal(1, pricePercentage.setScale(3, RoundingMode.HALF_UP));
            p.setInt(2, packageId);
            p.setInt(3, IDKurir);
            p.setInt(4, IDVozilo);
            ResultSet rrr = p.executeQuery();

            int ree = 0;
            while (rrr.next()) {
                ree = rrr.getInt(1);
            }

            if (ree == 0) {
                return -1;
            }

            return ree;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public boolean acceptAnOffer(int offerId) {
        try {
            //        boolean acceptAnOffer(int offerId)
            //        Parameters:
            //            offerId - for the accepted offer.
            //        Returns:
            //            success of the operation.

            // treba da postavim cenu u paketu
            Connection connection = DB.getInstance().getConnection();

            PreparedStatement ps = connection.prepareStatement(" SELECT IDPaket, IDKorisnik, IDVozilo,ProcenatCeneIsporuke"
                    + " FROM Ponuda "
                    + " WHERE IDPonuda = ?");
            ps.setInt(1, offerId);
            ResultSet rss = ps.executeQuery();

            int IDPaket = -1, IDKorisnik = -1, IDVozilo = -1;

            BigDecimal procenat = null;
            while (rss.next()) {
                IDPaket = rss.getInt(1);
                IDKorisnik = rss.getInt(2);
                IDVozilo = rss.getInt(3);
                procenat = rss.getBigDecimal(4);

            }

            if (IDPaket == -1) {
                return false;
            }

            PreparedStatement ps1 = connection.prepareStatement(" SELECT OpstinaPreuzima, OpstinaDostavlja "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            ps1.setInt(1, IDPaket);
            ResultSet rs = ps1.executeQuery();

            int rez = -1;
            int OpstinaPreuzima = -1;
            int OpstinaDostavlja = -1;
            while (rs.next()) {
                OpstinaPreuzima = rs.getInt(1);
                OpstinaDostavlja = rs.getInt(2);

                rez = 0;
            }

            if (rez == -1) {
                return false;
            }

            PreparedStatement ps2 = connection.prepareStatement(" SELECT Opstina.X, Opstina.Y "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps2.setInt(1, OpstinaPreuzima);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = -1;
            int X1 = -1;
            int Y1 = -1;
            while (rs2.next()) {
                X1 = rs2.getInt(1);
                Y1 = rs2.getInt(2);

                rez2 = 0;
            }

            if (rez2 == -1) {
                return false;
            }

            PreparedStatement ps3 = connection.prepareStatement(" SELECT Opstina.X, Opstina.Y "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps3.setInt(1, OpstinaDostavlja);
            ResultSet rs3 = ps3.executeQuery();

            int rez3 = -1;
            int X2 = -1;
            int Y2 = -1;
            while (rs3.next()) {
                X2 = rs3.getInt(1);
                Y2 = rs3.getInt(2);

                rez3 = 0;
            }

            if (rez3 == -1) {
                return false;
            }

            // treba da dohvatim iz tabele ponuda tu ponudu sa odgovarajucim ID-em
            PreparedStatement pp = connection.prepareStatement(" SELECT TipPaketa, TezinaPaketa "
                    + " FROM Paket "
                    + " WHERE IDPaket = ?");
            pp.setInt(1, IDPaket);
            ResultSet rssp = pp.executeQuery();

            int k = -1;
            int TipPaketa = -1;
            BigDecimal TezinaPaketa = null;
            while (rssp.next()) {
                TipPaketa = rssp.getInt(1);
                TezinaPaketa = rssp.getBigDecimal(2);

                k = 0;
            }

            if (k == -1) {
                return false;
            }

            BigDecimal cena = getPackagePrice1(TipPaketa, TezinaPaketa, euclidean1(X1, Y1, X2, Y2), procenat);

            PreparedStatement p5 = connection.prepareStatement(" UPDATE Paket "
                    + " SET Cena = ? "
                    + " WHERE IDPaket = ? ");
            p5.setBigDecimal(1, cena.setScale(3, RoundingMode.HALF_UP));
            p5.setInt(2, IDPaket);
            int rez5 = p5.executeUpdate();

            if (rez5 == 0) {
                return false;
            }

            // treba da napunim tabelu voznja sa tom ponudom, i tada ce da mi se
            // pozove Trigger
            PreparedStatement ps4 = connection.prepareStatement("INSERT INTO Voznja "
                    + " VALUES(?,?,?)");
            ps4.setInt(1, IDPaket);
            ps4.setInt(2, IDKorisnik);
            ps4.setInt(3, IDVozilo);
            int r11 = ps4.executeUpdate();

            if (r11 == 0) {
                return false;
            }
        //*******************************************************************************************    
            // jos da postavim za mene kao korisnika da sam poslao jos jedan
            PreparedStatement aps1 = connection.prepareStatement(" SELECT IDKorisnik "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            aps1.setInt(1, IDPaket);
            ResultSet ars = aps1.executeQuery();

            int IDPosiljalac = -1;
            while (ars.next()) {
                IDPosiljalac = ars.getInt(1);
            }

            if (IDPosiljalac == -1) {
                return false;
            }
            
            
            PreparedStatement aps2 = connection.prepareStatement(" SELECT BrojPoslatihPaketa "
                    + " FROM Korisnik "
                    + " WHERE IDKorisnik = ? ");
            aps2.setInt(1, IDPosiljalac);
            ResultSet ars2 = aps2.executeQuery();

            int Broj = -1;
            while (ars2.next()) {
                Broj = ars2.getInt(1);
            }

            if (Broj == -1) {
                return false;
            }
            
            Broj+=1;
            System.out.println(Broj + "lalallalalal");
            
            PreparedStatement p55 = connection.prepareStatement(" UPDATE Korisnik "
                    + " SET BrojPoslatihPaketa = ? "
                    + " WHERE IDKorisnik = ? ");
            p55.setInt(1, Broj);
            p55.setInt(2, IDPosiljalac);
            int rez55 = p55.executeUpdate();

            if (rez55 == 0) {
                return false;
            }
            
            
            
            
            

            return true;

            // treba da izbrisem sve ponude iz tabele ponuda koje su sa tim ID-em
            //treba da postavim status paketa na zahtev prihvacen tj 1
            //treba da postavim vreme prihvatanja zahteva
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Integer> getAllOffers() {
        //        java.util.List<java.lang.Integer> getAllOffers()
        //        Returns:
        //            List of primary keys of all offers.

        try {
            Connection connection = DB.getInstance().getConnection();
            List<Integer> list = new ArrayList<Integer>();
            String query = " select IDPonuda from Ponuda ";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int i = rs.getInt(1);
                list.add(i);
            }

            return list;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        //        java.util.List<PackageOperations.Pair<java.lang.Integer,java.math.BigDecimal>> 
        //                getAllOffersForPackage(int packageId)
        //        Get all offers for specific package.
        //        Parameters:
        //            packageId - - id of a package.
        //        Returns:
        //            List of Pair where first param is id of offer and second 
        //            one is percentage that courier has given.

        // treba da napravim jedan select koji ce da pokupi iz tabele Ponuda
        // iz tog selecta dohvatim dve vrednosti i pakujem ih u listu
        try {
            Connection connection = DB.getInstance().getConnection();
            List<Pair<Integer, BigDecimal>> list = new ArrayList<Pair<Integer, BigDecimal>>();
            String query = " select IDPonuda, ProcenatCeneIsporuke"
                    + " from Ponuda "
                    + " where IDPaket = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery(query);

            while (rs.next()) {
                int IDPonuda = rs.getInt(1);
                BigDecimal procenat = rs.getBigDecimal(2);
                dt100261d_Pair p = new dt100261d_Pair(IDPonuda, procenat);
                list.add(p);
            }

            return list;

        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public boolean deletePackage(int packageId) {
        //        boolean deletePackage(int packageId)
        //        Parameters:
        //            packageId - of a package to be deleted.
        //        Returns:
        //            success of operation.
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("delete Paket where IDPaket = ?");
            ps.setInt(1, packageId);
            int pom = ps.executeUpdate();
            if (pom != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {
        //        boolean changeWeight(int packageId,
        //                             @NotNull java.math.BigDecimal newWeight)
        //        Parameters:
        //            packageId - -
        //            newWeight - -
        //        Returns:
        //            success of operation
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" UPDATE Paket "
                    + " SET TezinaPaketa = ? "
                    + " WHERE IDPaket = ? ");
            ps.setBigDecimal(1, newWeight.setScale(3, RoundingMode.HALF_UP));
            ps.setInt(2, packageId);
            int rez = ps.executeUpdate();

            if (rez == 0) {
                return false;
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean changeType(int packageId, int newType) {
        //        boolean changeType(int packageId,int newType)
        //        Parameters:
        //            packageId - -
        //            newType - -
        //        Returns:
        //            success of operation.
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" UPDATE Paket "
                    + " SET TipPaketa = ? "
                    + " WHERE IDPaket = ? ");
            ps.setInt(1, newType);
            ps.setInt(2, packageId);
            int rez = ps.executeUpdate();

            if (rez == 0) {
                return false;
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public Integer getDeliveryStatus(int packageId) {
        //        java.lang.Integer getDeliveryStatus(int packageId)
        //        Parameters:
        //            packageId - -
        //        Returns:
        //            - see project documentation for information about status types. 
        //            - null for no such package.
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT Status "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            int rez = -1;
            while (rs.next()) {
                rez = rs.getInt(1);
            }

            if (rez == -1) {
                return null;
            }

            return rez;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        //        java.math.BigDecimal getPriceOfDelivery(int packageId)
        //        Parameters:
        //            packageId - -
        //        Returns:
        //            price of the delivery. If it's not yet calculated, return null.
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT Cena "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            BigDecimal rez = null;
            while (rs.next()) {
                rez = rs.getBigDecimal(1);
            }

            if (rez == null) {
                return null;
            }

            return rez;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public Date getAcceptanceTime(int packageId) {
        //        java.sql.Date getAcceptanceTime(int packageId)
        //        Acceptance time for a delivery (package), is time when an 
        //        offer for that delivery has been accepted.
        //        Parameters:
        //                packageId - -
        //        Returns:
        //                Date - time of acceptance. If it is not yet accepted, return null.
        //        
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT VremePrihvatanjaZahteva "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            Date rez = null;
            while (rs.next()) {
                rez = rs.getDate(1);
            }

            if (rez == null) {
                return null;
            }

            return rez;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        try {
            //        java.util.List<java.lang.Integer> getAllPackagesWithSpecificType(int type)
            //        Parameters:
            //                type - of package to be returned.
            //        Returns:
            //                List of packages primary keys.

            Connection connection = DB.getInstance().getConnection();
            List<Integer> list = new ArrayList<Integer>();
            PreparedStatement stm = connection.prepareStatement(" SELECT IDPaket "
                    + " FROM Paket "
                    + " WHERE TipPaketa = ? ");
            stm.setInt(1, type);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int i = rs.getInt(1);
                System.out.println("Lista: " + i);
                list.add(i);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackages() {
        try {
            //        java.util.List<java.lang.Integer> getAllPackages()
            //        Returns:
            //            List of all packages within the system.
            Connection connection = DB.getInstance().getConnection();
            List<Integer> list = new ArrayList<Integer>();
            PreparedStatement stm = connection.prepareStatement(" SELECT IDPaket "
                    + " FROM Paket ");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int i = rs.getInt(1);
                list.add(i);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getDrive(String courierUsername) {
        try {
            //            java.util.List<java.lang.Integer> getDrive(java.lang.String courierUsername)
   //            This method should return all package ids that are bundled together as a
    //            single drive. Return only packages that are not delivered yet.
    //            Parameters:
    //                    courierUsername - -
    //            Returns:
    //                    List of all package ids for current drive of courier. If there is 
    //                    no current drive, then return null.

    // treba da dohvatim sve pakete kod kojih je status jednak 2 a odnose se na datog
    //kurira sa tim userName-om
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT Ku.IDKorisnik "
                    + " FROM Kurir Ku ,Korisnik Ko  "
                    + " WHERE Ku.IDKorisnik = Ko.IDKorisnik "
                    + " AND  Ko.Username = ? "
                    + " AND Ku.Status = ?");
            ps.setString(1, courierUsername);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();

            int IDKorisnik = -1;
            while (rs.next()) {
                IDKorisnik = rs.getInt(1);
            }
            
            if(IDKorisnik==-1){
                return null;
            }

            List<Integer> list = new ArrayList<Integer>();
            PreparedStatement stm = connection.prepareStatement(" SELECT IDPaket "
                                                                + " FROM Paket "
                                                                + " WHERE Status = ? "
                                                                + " AND IDKorisnik = ?");
            ps.setInt(1, 2);
            ps.setInt(1, IDKorisnik);
            ResultSet r = stm.executeQuery();

            while (r.next()) {
                int i = r.getInt(1);
                list.add(i);
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    
   }

    @Override
    public int driveNextPackage(String courierUserName) {
        //            int driveNextPackage(@NotNull java.lang.String courierUserName)
        //            This method invocation starts a drive of deliveries. If drive has already started, 
        //            then drive next delivery according to the given algorithm (see project documentation).
        //            \n So each time this method is called, if there are pending packages, one of them 
        //            is delivered.\n
        //            Parameters:
        //                courierUserName - - username of courier delivering packages.
        //            Returns:
        //                packageId of a delivered package,\n -1 if there is nothing to 
        //                drive for the given courier,\n -2 any other case.

        //proverim Status za kurira cije je dato courierUserName
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(" SELECT Ku.Status,Ku.IDKorisnik "
                    + " FROM Kurir Ku ,Korisnik Ko  "
                    + " WHERE Ku.IDKorisnik = Ko.IDKorisnik "
                    + " AND  Ko.Username = ? ");
            ps.setString(1, courierUserName);
            ResultSet rs = ps.executeQuery();

            int rez = -1;
            int IDKorisnik = -1;
            while (rs.next()) {
                rez = rs.getInt(1);  // ovo je status Kurira
                IDKorisnik = rs.getInt(2);
            }

            if (rez == -1) {
                return -2;
            }

            //ako do sada nije vozio, postavim ga da vozi i postavim ostale na dva
            if (rez == 0) { //to znaci da do tada nije vozio, postavim ga na vozi
                PreparedStatement ps1 = connection.prepareStatement(" UPDATE Kurir "
                        + " SET Status = ? "
                        + " WHERE IDKorisnik = ? ");
                ps1.setInt(1, 1);
                ps1.setInt(2, IDKorisnik);
                int rez1 = ps1.executeUpdate();

                if (rez1 == 0) {
                    return -2;
                }
            

            //postavim svima status na 2
            PreparedStatement ps4 = connection.prepareStatement(" SELECT P.IDPaket "
                    + " FROM Voznja V , Paket P  "
                    + " WHERE V.IDPaket = P.IDPaket "
                    + " AND  V.IDKorisnik = ? "
                    + " AND P.Status = ? "
                    + " ORDER BY P.VremePrihvatanjaZahteva ASC, P.IDPaket ASC ");
            ps4.setInt(1, IDKorisnik);
            ps4.setInt(2, 1);
            ResultSet rs4 = ps4.executeQuery();

            int rez4 = -1;
            while (rs4.next()) { //posto je ovo petlja svakome cu postaviti statusPaketa na 2
                rez4 = rs4.getInt(1);
                PreparedStatement ps33 = connection.prepareStatement(" UPDATE Paket "
                        + " SET Status = ? "
                        + " WHERE IDKorisnik = ? ");
                ps33.setInt(1, 2);
                ps33.setInt(2, IDKorisnik);
                int rez33 = ps33.executeUpdate();

                if (rez33 == 0) {
                    return -2;
                }
            }
            }

            //ako je status=vozi onda dohvatim sledeci paket po pravilu order by za VremePrihvatanja 
            //koji ima status=2
            PreparedStatement ps2 = connection.prepareStatement(" SELECT TOP 1 P.IDPaket, V.IDVozilo "
                    + " FROM Voznja V , Paket P  "
                    + " WHERE V.IDPaket = P.IDPaket "
                    + " AND  V.IDKorisnik = ? "
                    + " AND P.Status = ? "
                    + " ORDER BY P.VremePrihvatanjaZahteva ASC, P.IDPaket ASC ");
            ps2.setInt(1, IDKorisnik);
            ps2.setInt(2, 2);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = -1;
            int IDPaket = -1;
            int IDVozilo = -1;
            while (rs2.next()) {
                rez2 = rs2.getInt(1);
                IDVozilo = rs2.getInt(2);
                IDPaket = rez2;
            }
            //??????????????????????????
            if (rez2 == -1) {    //to onda znaci da nema paketa za mene i ne racunam profit
                PreparedStatement ps3 = connection.prepareStatement(" UPDATE Kurir "
                        + " SET Status = ? "
                        + " WHERE IDKorisnik = ? ");
                ps3.setInt(1, 0);
                ps3.setInt(2, IDKorisnik);
                int rez3 = ps3.executeUpdate();

                if (rez3 == 0) {
                    return -2;
                }

                return -1; ////????????????????????
            }

            //kad ga razvezem onda mu promenim status na isporucen
            //i treba da ga ubacim u tabelu Profit
            PreparedStatement ps3 = connection.prepareStatement(" UPDATE Paket"
                    + " SET Status = ? "
                    + " WHERE IDPaket = ? ");
            ps3.setInt(1, 3);
            ps3.setInt(2, IDPaket);
            int rez3 = ps3.executeUpdate();

            if (rez3 == 0) {
                return -2;
            }

            //izracunavam gubitak
            List<Integer> pom = pomocnaProfit(IDPaket);
            int X1 = pom.get(0);
            int Y1 = pom.get(1);
            int X2 = pom.get(2);
            int Y2 = pom.get(3);

            //dohvatam tip goriva:
            PreparedStatement ps21 = connection.prepareStatement(" SELECT TipGoriva, Potrosnja "
                    + " FROM Vozilo  "
                    + " WHERE IDVozilo = ? ");
            ps21.setInt(1, IDVozilo);
            ResultSet rs21 = ps21.executeQuery();

            int TipGoriva = -1;
            BigDecimal Potrosnja = null;
            while (rs21.next()) {
                TipGoriva = rs21.getInt(1);
                Potrosnja = rs21.getBigDecimal(2);
            }

            if (TipGoriva == -2) {
                return -2;
            }

            int tip = 0;
            if (TipGoriva == 0) {
                tip = 15;
            }
            if (TipGoriva == 1) {
                tip = 36;
            }
            if (TipGoriva == 2) {
                tip = 32;
            }

            BigDecimal loss = new BigDecimal(euclidean1(X1, Y1, X2, Y2) * tip).multiply(Potrosnja);

            //dohvatam tip goriva:
            PreparedStatement ps212 = connection.prepareStatement(" SELECT Cena "
                    + " FROM Paket  "
                    + " WHERE IDPaket = ? ");
            ps212.setInt(1, IDPaket);
            ResultSet rs212 = ps212.executeQuery();

            BigDecimal Cena = null;
            while (rs212.next()) {
                Cena = rs212.getBigDecimal(1);
            }

            if (TipGoriva == -2) {
                return -2;
            }

            //zatim u sada u tabelu Pomocna da sacuvam Cenu, Gubitak, X i Y
            PreparedStatement ps33 = connection.prepareStatement("INSERT INTO Pomocna "
                    + " VALUES(?,?,?,?,?,?)");
            ps33.setBigDecimal(1, Cena.setScale(3, RoundingMode.HALF_UP));
            ps33.setBigDecimal(2, loss.setScale(3, RoundingMode.HALF_UP));
            ps33.setInt(3, X1);
            ps33.setInt(4, Y1);
            ps33.setInt(5, X2);
            ps33.setInt(6, Y2);
            int r = ps33.executeUpdate();

            if (r == 0) {
                return -2;
            }

            //ako sam isporucila sve zahteve (if there is nothing to drive 
            //for the given courier) promenim status na ne_vozi
            PreparedStatement ps44 = connection.prepareStatement(" SELECT COUNT(P.IDPaket) "
                    + " FROM Voznja V , Paket P  "
                    + " WHERE V.IDPaket = P.IDPaket "
                    + " AND  V.IDKorisnik = ? "
                    + " AND P.Status = ? ");
            ps44.setInt(1, IDKorisnik);
            ps44.setInt(2, 2);
            ResultSet rs44 = ps44.executeQuery();

            int rez44 = -1;
            while (rs44.next()) {
                rez44 = rs44.getInt(1);
            }
            if (rez44 == 0) {
                PreparedStatement ps5 = connection.prepareStatement(" UPDATE Kurir "
                        + " SET Status = ? "
                        + " WHERE IDKorisnik = ? ");
                ps5.setInt(1, 0);
                ps5.setInt(2, IDKorisnik);
                int rez5 = ps5.executeUpdate();

                if (rez5 == 0) {
                    return -2;
                }
            }

            /**
             * **************************************************
             */
            //ako sam isporucila sve zahteve, racunam sada profit
            
            if (rez44 == 0) {
            PreparedStatement ps45 = connection.prepareStatement(" SELECT SUM(Cena), SUM (Loss) "
                    + " FROM Pomocna");
            ResultSet rs45 = ps45.executeQuery();

            BigDecimal SumaCena = null;
            BigDecimal SumaLoss = null;
            while (rs45.next()) {
                SumaCena = rs45.getBigDecimal(1);
                SumaLoss = rs45.getBigDecimal(2);
            }

            //sada jos za ostatak puta da izracunam
            PreparedStatement ps46 = connection.prepareStatement(" SELECT Pomocna.X1, Pomocna.Y1, Pomocna.X2, Pomocna.Y2 "
                    + " FROM Pomocna");
            ResultSet rs46 = ps46.executeQuery();

            int X11 = -1;
            int Y11 = -1;
            int X22 = -1;
            int Y22 = -1;
            List<Koordinate> lista = new ArrayList<Koordinate>();
            while (rs46.next()) {
                X11 = rs46.getInt(1);
                Y11 = rs46.getInt(2);
                X22 = rs46.getInt(2);
                Y22 = rs46.getInt(2);

                Koordinate k = new Koordinate(X11, Y11, X22, Y22);
                lista.add(k);
            }
            int BrojIsporucenihPaketa = lista.size();

            if (lista.size() != 0 && lista.size() >= 2) {
                while (lista.size() >= 2) {
                    int x1 = lista.get(0).getX2();
                    int y1 = lista.get(0).getY2();

                    int x2 = lista.get(1).getX1();
                    int y2 = lista.get(1).getY1();

                    lista.remove(0);
                    BigDecimal ll = new BigDecimal(euclidean1(x1, y1, x2, y2) * tip).multiply(Potrosnja);
                    if (ll.compareTo(BigDecimal.ZERO) > 0) {
                        SumaLoss.add(ll);
                    }
                }
            }

            BigDecimal profit = SumaCena.subtract(SumaLoss);

            //sad jos da postavim u profit kurira
            PreparedStatement ps55 = connection.prepareStatement(" UPDATE Kurir "
                    + " SET OstvarenProfit = ? "
                    + " WHERE IDKorisnik = ? ");
            ps55.setBigDecimal(1, profit.setScale(3, RoundingMode.HALF_UP));
            ps55.setInt(2, IDKorisnik);
            int rez55 = ps55.executeUpdate();

            if (rez55 == 0) {
                return -2;
            }
            
            
            //i jos samo da postavim broj isporucenih paketa
            PreparedStatement ps56 = connection.prepareStatement(" UPDATE Kurir "
                    + " SET BrojIsporucenihPaketa = ? "
                    + " WHERE IDKorisnik = ? ");
            ps56.setInt(1, BrojIsporucenihPaketa);
            ps56.setInt(2, IDKorisnik);
            int rez56 = ps56.executeUpdate();

            if (rez56 == 0) {
                return -2;
            }
            

            }
            return IDPaket;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }

    }

    public List<Integer> pomocnaProfit(int IDPaket) {
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement ps1 = connection.prepareStatement(" SELECT OpstinaPreuzima, OpstinaDostavlja "
                    + " FROM Paket "
                    + " WHERE IDPaket = ? ");
            ps1.setInt(1, IDPaket);
            ResultSet rs = ps1.executeQuery();

            int rez = -1;
            int OpstinaPreuzima = -1;
            int OpstinaDostavlja = -1;
            while (rs.next()) {
                OpstinaPreuzima = rs.getInt(1);
                OpstinaDostavlja = rs.getInt(2);

                rez = 0;
            }

            if (rez == -1) {
                return null;
            }

            PreparedStatement ps2 = connection.prepareStatement(" SELECT Opstina.X, Opstina.Y "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps2.setInt(1, OpstinaPreuzima);
            ResultSet rs2 = ps2.executeQuery();

            int rez2 = -1;
            int X1 = -1;
            int Y1 = -1;
            while (rs2.next()) {
                X1 = rs2.getInt(1);
                Y1 = rs2.getInt(2);

                rez2 = 0;
            }

            if (rez2 == -1) {
                return null;
            }

            PreparedStatement ps3 = connection.prepareStatement(" SELECT Opstina.X, Opstina.Y "
                    + " FROM Opstina "
                    + " WHERE IDOpstina = ? ");
            ps3.setInt(1, OpstinaDostavlja);
            ResultSet rs3 = ps3.executeQuery();

            int rez3 = -1;
            int X2 = -1;
            int Y2 = -1;
            while (rs3.next()) {
                X2 = rs3.getInt(1);
                Y2 = rs3.getInt(2);

                rez3 = 0;
            }

            if (rez3 == -1) {
                return null;
            }

            List<Integer> novi = new ArrayList<Integer>();
            novi.add(X1);
            novi.add(Y1);
            novi.add(X2);
            novi.add(Y2);
            return novi;
        } catch (SQLException ex) {
            Logger.getLogger(dt100261_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

//    @Override
//    public List<Integer> getDrive(String paramString) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}

//3> CREATE TRIGGER myTriggerINSERT
//4> ON Employee
//5> FOR INSERT
//6> AS
//7> DECLARE @ID int, @Name nvarchar(30)
//8>
//9> SET @ID = (SELECT ID FROM inserted)
//10> SET @Name = (SELECT Name FROM inserted)
//11>
//12> INSERT myArchive (type, ID, newName) VALUES('INSERT', @ID, @Name)
//13> GO
//1>
//2>
//3> CREATE TRIGGER myTriggerDELETE
//4> ON Employee
//5> FOR DELETE
//6> AS
//7> DECLARE @ID int, @Name nvarchar(30)
//8>
//9> SET @ID = (SELECT ID FROM deleted)
//10> SET @Name = (SELECT Name FROM deleted)
//11>
//12> INSERT myArchive (type, ID, oldName ) VALUES('DELETE', @ID, @Name)
//13> GO
//1>
//2> CREATE TRIGGER myTriggerUPDATE
//3> ON Employee
//4> INSTEAD OF UPDATE
//5> AS
//6>
//7> DECLARE @ID int, @newName nvarchar(30), @oldName nvarchar(30)
//8>
//9> IF (SELECT ID FROM inserted) <> (SELECT ID FROM deleted)
//10>     RAISERROR ('You are not allowed to change ID.', 10,1)
//11> ELSE
//12> BEGIN
//13>
//14> --set local variables
//15> SET @ID = (SELECT ID FROM inserted)
//16> SET @newName = (SELECT Name FROM inserted)
//17> SET @oldName = (SELECT Name FROM deleted)
//18>
//19> --write to table
//20> UPDATE Employee SET Name = @newName WHERE ID = @ID
//21> -- write to archive
//22> INSERT myArchive (type, ID, newName, oldName) VALUES('UPDATE', @ID, @newName, @oldName)
//23> END
//24> GO
//1>
//2> INSERT Employee (id, name) VALUES (13, 'Rickie')
//3> GO
