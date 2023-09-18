package tests;

import java.math.BigDecimal;
import operations.DistrictOperations;
import operations.GeneralOperations;
import operations.PackageOperations;
import operations.UserOperations;
import org.junit.Assert;

public class PublicModuleTest
{
  private TestHandler testHandler;
  
  public PublicModuleTest() {}
  
  @org.junit.Before
  public void setUp()
  {
    testHandler = TestHandler.getInstance();
    Assert.assertNotNull(testHandler);
    Assert.assertNotNull(testHandler.getGeneralOperations());
    testHandler.getGeneralOperations().eraseAll();
  }
  
  @org.junit.After
  public void tearUp() {
    testHandler.getGeneralOperations().eraseAll();
  }
  
  private double euclidean(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
  
  private BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
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
  
  @org.junit.Test
  public void publicOne()
  {
    String courierLastName = "Ckalja";
    String courierFirstName = "Pero";
    String courierUsername = "perkan";
    String password = "sabi2018";
    
    testHandler.getUserOperations()
      .insertUser(courierUsername, courierFirstName, courierLastName, password);
    
    String licencePlate = "BG323WE";
    int fuelType = 0;
    BigDecimal fuelConsumption = new BigDecimal(8.3D);
    
    testHandler.getVehicleOperations().insertVehicle(licencePlate, fuelType, fuelConsumption);
    
    testHandler.getCourierRequestOperation().insertCourierRequest(courierUsername, licencePlate);
    
    testHandler.getCourierRequestOperation().grantRequest(courierUsername);
    
    Assert.assertTrue(testHandler.getCourierOperations().getAllCouriers().contains(courierUsername));
    
    String senderUsername = "masa";
    String senderFirstName = "Masana";
    String senderLastName = "Leposava";
    password = "lepasampasta1";
    
    testHandler.getUserOperations()
      .insertUser(senderUsername, senderFirstName, senderLastName, password);
    
    int cityId = testHandler.getCityOperations().insertCity("Novo Milosevo", "21234");
    
    int cordXd1 = 10;
    int cordYd1 = 2;
    


    int districtIdOne = testHandler.getDistrictOperations().insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
    
    int cordXd2 = 2;
    int cordYd2 = 10;
    


    int districtIdTwo = testHandler.getDistrictOperations().insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
    
    int type1 = 0;
    BigDecimal weight1 = new BigDecimal(123);
    


    int packageId1 = testHandler.getPackageOperations().insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
    
    BigDecimal packageOnePrice = getPackagePrice(type1, weight1, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    

    int offerId = testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
    testHandler.getPackageOperations().acceptAnOffer(offerId);
    

    int type2 = 1;
    BigDecimal weight2 = new BigDecimal(321);
    


    int packageId2 = testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
    
    BigDecimal packageTwoPrice = getPackagePrice(type2, weight2, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    


    offerId = testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
    testHandler.getPackageOperations().acceptAnOffer(offerId);
    
    int type3 = 1;
    BigDecimal weight3 = new BigDecimal(222);
    


    int packageId3 = testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
    
    BigDecimal packageThreePrice = getPackagePrice(type3, weight3, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    

    offerId = testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
    testHandler.getPackageOperations().acceptAnOffer(offerId);
    
    Assert.assertEquals(1L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId1).intValue());  //ovde je greska jer nije 
                                            //postavljen status na 1 (zahtev prihvacen)
    
    Assert.assertEquals(packageId1, testHandler
      .getPackageOperations().driveNextPackage(courierUsername));
    
    Assert.assertEquals(3L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId1).intValue());
    

    Assert.assertEquals(2L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId2).intValue());
    
    Assert.assertEquals(packageId2, testHandler
      .getPackageOperations().driveNextPackage(courierUsername));
    
    Assert.assertEquals(3L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId2).intValue());
    

    Assert.assertEquals(2L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId3).intValue());
    
    Assert.assertEquals(packageId3, testHandler
      .getPackageOperations().driveNextPackage(courierUsername));
    
    Assert.assertEquals(3L, testHandler
      .getPackageOperations().getDeliveryStatus(packageId3).intValue());
    

    BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
    BigDecimal loss = new BigDecimal(euclidean(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0D * 15.0D).multiply(fuelConsumption);
    
    BigDecimal actual = testHandler.getCourierOperations().getAverageCourierProfit(0); 
    //ovde mi je jos uvek greska zato sto nisam promenila profit u kuriru
    //kad promenim profit u kuriru, onda cu moci da uporedim tu vrednost
    //koju sam ja dobila, sa njihovom i tolerise gresku zbog zaokruzivanja
    Assert.assertTrue(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.05D))) < 0);
    Assert.assertTrue(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.95D))) > 0);
  }
}
