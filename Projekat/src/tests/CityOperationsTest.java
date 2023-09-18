package tests;

import java.util.List;
import java.util.Random;
import operations.CityOperations;
import operations.GeneralOperations;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CityOperationsTest
{
  private TestHandler testHandler;
  private GeneralOperations generalOperations;
  private CityOperations cityOperations;
  
  public CityOperationsTest() {}
  
  @Before
  public void setUp() throws Exception
  {
    testHandler = TestHandler.getInstance();
    Assert.assertNotNull(testHandler);
    cityOperations = testHandler.getCityOperations();
    Assert.assertNotNull(cityOperations);
    generalOperations = testHandler.getGeneralOperations();
    Assert.assertNotNull(generalOperations);
    generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {
    generalOperations.eraseAll();
  }
  
  @Test
  public void insertCity_OnlyOne() throws Exception {
    String name = "Tokyo";
    String postalCode = "100";
    
    int rowId = cityOperations.insertCity(name, postalCode);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(1L, list.size());
    Assert.assertTrue(list.contains(Integer.valueOf(rowId)));
  }
  
  @Test
  public void insertCity_TwoCities_SameBothNameAndPostalCode() throws Exception {
    String name = "Tokyo3";
    String postalCode = "1003";
    
    int rowIdValid = cityOperations.insertCity(name, postalCode);
    int rowIdInvalid = cityOperations.insertCity(name, postalCode);
    
    Assert.assertEquals(-1L, rowIdInvalid);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(1L, list.size());
    Assert.assertTrue(list.contains(Integer.valueOf(rowIdValid)));
  }
  
  @Test
  public void insertCity_TwoCities_SameName() throws Exception {
    String name = "Tokyo";
    String postalCode1 = "100";
    String postalCode2 = "1020";
    
    int rowIdValid = cityOperations.insertCity(name, postalCode1);
    int rowIdInvalid = cityOperations.insertCity(name, postalCode2);
    
    Assert.assertEquals(-1L, rowIdInvalid);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(1L, list.size());
    Assert.assertTrue(list.contains(Integer.valueOf(rowIdValid)));
  }
  
  @Test
  public void insertCity_TwoCities_SamePostalCode() throws Exception {
    String name1 = "Tokyo";
    String name2 = "Beijing";
    String postalCode = "100";
    
    int rowIdValid = cityOperations.insertCity(name1, postalCode);
    int rowIdInvalid = cityOperations.insertCity(name2, postalCode);
    
    Assert.assertEquals(-1L, rowIdInvalid);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(1L, list.size());
    Assert.assertTrue(list.contains(Integer.valueOf(rowIdValid)));
  }
  
  @Test
  public void insertCity_MultipleCities() throws Exception
  {
    String name1 = "Tokyo";
    String name2 = "Beijing";
    String postalCode1 = "100";
    String postalCode2 = "065001";
    
    int rowId1 = cityOperations.insertCity(name1, postalCode1);
    int rowId2 = cityOperations.insertCity(name2, postalCode2);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(2L, list.size());
    Assert.assertTrue(list.contains(Integer.valueOf(rowId1)));
    Assert.assertTrue(list.contains(Integer.valueOf(rowId2)));
  }
  
  @Test
  public void deleteCity_WithId_OnlyOne() {
    String name = "Beijing";
    String postalCode = "065001";
    
    int rowId = cityOperations.insertCity(name, postalCode);
    
    Assert.assertNotEquals(-1L, rowId);
    
    Assert.assertTrue(cityOperations.deleteCity(rowId));
    
    Assert.assertEquals(0L, cityOperations.getAllCities().size());
  }
  
  @Test
  public void deleteCity_WithId_OnlyOne_NotExisting() {
    Random random = new Random();
    
    int rowId = random.nextInt();
    System.out.println("rowId ima vrednost: "+ rowId);
    boolean b = cityOperations.deleteCity(rowId);
    System.out.println("boolean vrednost je: " + b);
    Assert.assertFalse(cityOperations.deleteCity(rowId));
    
    Assert.assertEquals(0L, cityOperations.getAllCities().size());
  }
  
  @Test
  public void deleteCity_WithName_One() {
    String name = "Beijing";
    String postalCode = "065001";
    
    int rowId = cityOperations.insertCity(name, postalCode);
    
    Assert.assertNotEquals(-1L, rowId);
    
    Assert.assertEquals(1L, cityOperations.deleteCity(new String[] { name }));
    
    Assert.assertEquals(0L, cityOperations.getAllCities().size());
  }
  
  @Test
  public void deleteCity_WithName_MultipleCities() throws Exception {
    String name1 = "Tokyo";
    String name2 = "Beijing";
    String postalCode1 = "100";
    String postalCode2 = "065001";
    
    int rowId1 = cityOperations.insertCity(name1, postalCode1);
    int rowId2 = cityOperations.insertCity(name2, postalCode2);
    
    List<Integer> list = cityOperations.getAllCities();
    
    Assert.assertEquals(2L, list.size());
    
    Assert.assertEquals(2L, cityOperations.deleteCity(new String[] { name1, name2 }));
  }
  
  @Test
  public void deleteCity_WithName_OnlyOne_NotExisting() {
    String name = "Tokyo";
    
    Assert.assertEquals(0L, cityOperations.deleteCity(new String[] { name }));
    
    Assert.assertEquals(0L, cityOperations.getAllCities().size());
  }
}
