
import operations.*;
import student.dt100261_CityOperations;
import student.dt100261_CourierOperations;
import student.dt100261_CourierRequestOperation;
import student.dt100261_DistrictOperations;
import student.dt100261_GeneralOperations;
import student.dt100261_PackageOperations;
import student.dt100261_UserOperations;
import student.dt100261_VehicleOperations;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new dt100261_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new dt100261_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new dt100261_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new dt100261_CourierRequestOperation();
        GeneralOperations generalOperations = new dt100261_GeneralOperations();
        UserOperations userOperations = new dt100261_UserOperations();
        VehicleOperations vehicleOperations = new dt100261_VehicleOperations();
        PackageOperations packageOperations = new dt100261_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
