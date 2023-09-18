package tests;

import java.util.List;
import operations.CityOperations;
import operations.DistrictOperations;
import operations.GeneralOperations;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;





public class DistrictOperationsTest
{
  private GeneralOperations generalOperations;
  private DistrictOperations districtOperations;
  private CityOperations cityOperations;
  private TestHandler testHandler;
  
  public DistrictOperationsTest() {}
  
  @Before
  public void setUp()
    throws Exception
  {
    testHandler = TestHandler.getInstance();
    Assert.assertNotNull(testHandler);
    
    cityOperations = testHandler.getCityOperations();
    Assert.assertNotNull(cityOperations);
    
    districtOperations = testHandler.getDistrictOperations();
    Assert.assertNotNull(districtOperations);
    
    generalOperations = testHandler.getGeneralOperations();
    Assert.assertNotNull(generalOperations);
    
    generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {
    generalOperations.eraseAll();
  }
  
  @Test
  public void insertDistrict_ExistingCity() {
    int idCity = cityOperations.insertCity("Belgrade", "11000");
    
    Assert.assertNotEquals(-1L, idCity);
    
    int idDistrict = districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    
    Assert.assertNotEquals(-1L, idDistrict);
    
    Assert.assertTrue(districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict)));
  }
  
  @Test
  public void deleteDistricts_multiple_existing() {
    int idCity = cityOperations.insertCity("Belgrade", "11000");
    
    Assert.assertNotEquals(-1L, idCity);
    
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    
    int idDistrict1 = districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    
    Assert.assertEquals(2L, districtOperations.deleteDistricts(new String[] { districtOneName, districtTwoName }));
  }
  
  @Test
  public void deleteDistrict() {
    int idCity = cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int idDistrict1 = districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    Assert.assertTrue(districtOperations.deleteDistrict(idDistrict1));
  }
  
  @Test
  public void deleteAllDistrictsFromCity() {
    String cityName = "Belgrade";
    int idCity = cityOperations.insertCity(cityName, "11000");
    
    Assert.assertNotEquals(-1L, idCity);
    
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    
    int idDistrict1 = districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    
    Assert.assertEquals(2L, districtOperations.deleteAllDistrictsFromCity(cityName));
  }
  
  @Test
  public void getAllDistrictsFromCity() {
    String cityName = "Belgrade";
    int idCity = cityOperations.insertCity(cityName, "11000");
    
    Assert.assertNotEquals(-1L, idCity);
    
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    
    int idDistrict1 = districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    
    Assert.assertTrue(districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict1)));
    Assert.assertTrue(districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict2)));
  }
}
