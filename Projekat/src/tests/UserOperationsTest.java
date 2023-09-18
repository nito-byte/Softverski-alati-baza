package tests;

import java.util.List;
import operations.GeneralOperations;
import operations.UserOperations;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserOperationsTest
{
  private GeneralOperations generalOperations;
  private UserOperations userOperations;
  private TestHandler testHandler;
  
  public UserOperationsTest() {}
  
  @Before
  public void setUp() throws Exception
  {
    testHandler = TestHandler.getInstance();
    Assert.assertNotNull(testHandler);
    
    userOperations = testHandler.getUserOperations();
    Assert.assertNotNull(userOperations);
    
    generalOperations = testHandler.getGeneralOperations();
    Assert.assertNotNull(generalOperations);
    
    generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception
  {}
  
  @Test
  public void insertUser() {
    String username = "crno.dete";
    String firstName = "Svetislav";
    String lastName = "Kisprdilov";
    String password = "sisatovac123";
    
    Assert.assertTrue(userOperations.insertUser(username, firstName, lastName, password));
  }
  

  @Test
  public void declareAdmin()
  {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    
    userOperations.insertUser(username, firstName, lastName, password);
    Assert.assertEquals(0L, userOperations.declareAdmin(username));
  }
  
  @Test
  public void declareAdmin_NoSuchUser() {
    Assert.assertEquals(2L, userOperations.declareAdmin("Nana"));
  }
  
  @Test
  public void declareAdmin_AlreadyAdmin() {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    
    userOperations.insertUser(username, firstName, lastName, password);
    userOperations.declareAdmin(username);
    Assert.assertEquals(1L, userOperations.declareAdmin(username));
  }
  
  
  
  /* ovi testovi su jos ostali da se rese*/
  
  
  
  @Test
  public void getSentPackages_userExisting() {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    
    userOperations.insertUser(username, firstName, lastName, password);
    Assert.assertEquals(new Integer(0), userOperations.getSentPackages(new String[] { username }));
  }
  
  @Test
  public void getSentPackages_userNotExisting() {
    String username = "rope";
    Assert.assertNull(userOperations.getSentPackages(new String[] { username }));
  }
 
  
    /* ovi testovi su jos ostali da se rese*/
  
  
  
  @Test
  public void deleteUsers() {
    String username1 = "rope";
    String firstName1 = "Pero";
    String lastName1 = "Simic";
    String password1 = "tralalalala123";
    
    userOperations.insertUser(username1, firstName1, lastName1, password1);
    
    String username2 = "rope_2";
    String firstName2 = "Pero";
    String lastName2 = "Simic";
    String password2 = "tralalalala321";
    
    userOperations.insertUser(username2, firstName2, lastName2, password2);
    
    Assert.assertEquals(2L, userOperations.deleteUsers(new String[] { username1, username2 }));
  }
  
  @Test
  public void getAllUsers() {
    String username1 = "rope";
    String firstName1 = "Pero";
    String lastName1 = "Simic";
    String password1 = "tralalalala221";
    
    userOperations.insertUser(username1, firstName1, lastName1, password1);
    
    String username2 = "rope_2";
    String firstName2 = "Pero";
    String lastName2 = "Simic";
    String password2 = "tralalalala222";
    
    userOperations.insertUser(username2, firstName2, lastName2, password2);
    
    Assert.assertEquals(2L, userOperations.getAllUsers().size());
    
    Assert.assertTrue(userOperations.getAllUsers().contains(username1));
    Assert.assertTrue(userOperations.getAllUsers().contains(username2));
  }
}
