package tests;

import java.io.PrintStream;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;



public final class TestRunner
{
  private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
  private static Class[] unitTestClasses = { CityOperationsTest.class, DistrictOperationsTest.class, UserOperationsTest.class, VehicleOperationsTest.class };
  


  private static Class[] moduleTestClasses = { PublicModuleTest.class };
  
  public TestRunner() {}
  
  private static double runUnit() { double numberOfSuccessfulCases = 0.0D;
    double numberOfAllCases = 0.0D;
    double points = 0.0D;
    JUnitCore jUnitCore = new JUnitCore();
    
    for (Class testClass : unitTestClasses) {
      System.out.println("\n" + testClass.getName());
      
      Request request = Request.aClass(testClass);
      Result result = jUnitCore.run(request);
      
      System.out.println("Successful:" + (result.getRunCount() - result.getFailureCount()));
      System.out.println("All:" + result.getRunCount());
      
      numberOfAllCases = result.getRunCount();
      numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
      
      points += numberOfSuccessfulCases / numberOfAllCases;
    }
    

    return points;
  }
  
  private static double runModule() {
    double numberOfSuccessfulCases = 0.0D;
    double numberOfAllCases = 0.0D;
    double points = 0.0D;
    JUnitCore jUnitCore = new JUnitCore();
    
    for (Class testClass : moduleTestClasses) {
      System.out.println("\n" + testClass.getName());
      
      Request request = Request.aClass(testClass);
      Result result = jUnitCore.run(request);
      
      System.out.println("Successful:" + (result.getRunCount() - result.getFailureCount()));
      System.out.println("All:" + result.getRunCount());
      
      numberOfAllCases = result.getRunCount();
      numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
      points += numberOfSuccessfulCases / numberOfAllCases;
    }
    

    return points;
  }
  
  private static double runPublic() {
    double res = 0.0D;
    res += runUnit() * 2.0D;
    res += runModule() * 2.0D;
    return res;
  }
  
  public static void runTests() {
    double resultsPublic = runPublic();
    System.out.println("Points won on public tests is: " + resultsPublic);
  }
}
