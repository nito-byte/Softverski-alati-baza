package tests;

import com.sun.istack.internal.NotNull;
import operations.CityOperations;
import operations.CourierOperations;
import operations.CourierRequestOperation;
import operations.DistrictOperations;
import operations.GeneralOperations;
import operations.PackageOperations;
import operations.UserOperations;
import operations.VehicleOperations;

public class TestHandler
{
  private static TestHandler testHandler = null;
  private CityOperations cityOperations;
  private CourierOperations courierOperations;
  private CourierRequestOperation courierRequestOperation;
  private DistrictOperations districtOperations;
  private GeneralOperations generalOperations;
  private UserOperations userOperations;
  private VehicleOperations vehicleOperations;
  private PackageOperations packageOperations;
  
  private TestHandler(@NotNull CityOperations cityOperations, @NotNull CourierOperations courierOperations, @NotNull CourierRequestOperation courierRequestOperation, @NotNull DistrictOperations districtOperations, @NotNull GeneralOperations generalOperations, @NotNull UserOperations userOperations, @NotNull VehicleOperations vehicleOperations, @NotNull PackageOperations packageOperations)
  {
    this.cityOperations = cityOperations;
    this.courierOperations = courierOperations;
    this.courierRequestOperation = courierRequestOperation;
    this.districtOperations = districtOperations;
    this.generalOperations = generalOperations;
    this.userOperations = userOperations;
    this.vehicleOperations = vehicleOperations;
    this.packageOperations = packageOperations;
  }
  
  public static void createInstance(@NotNull CityOperations cityOperations, @NotNull CourierOperations courierOperations, @NotNull CourierRequestOperation courierRequestOperation, @NotNull DistrictOperations districtOperations, @NotNull GeneralOperations generalOperations, @NotNull UserOperations userOperations, @NotNull VehicleOperations vehicleOperations, @NotNull PackageOperations packageOperations)
  {
    testHandler = new TestHandler(cityOperations, courierOperations, courierRequestOperation, districtOperations, generalOperations, userOperations, vehicleOperations, packageOperations);
  }
  
  static TestHandler getInstance()
  {
    return testHandler;
  }
  
  CityOperations getCityOperations()
  {
    return cityOperations;
  }
  
  CourierOperations getCourierOperations()
  {
    return courierOperations;
  }
  
  CourierRequestOperation getCourierRequestOperation()
  {
    return courierRequestOperation;
  }
  
  DistrictOperations getDistrictOperations()
  {
    return districtOperations;
  }
  
  GeneralOperations getGeneralOperations()
  {
    return generalOperations;
  }
  
  UserOperations getUserOperations()
  {
    return userOperations;
  }
  
  VehicleOperations getVehicleOperations()
  {
    return vehicleOperations;
  }
  
  PackageOperations getPackageOperations()
  {
    return packageOperations;
  }
}
