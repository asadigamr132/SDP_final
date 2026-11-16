import factory.BikeFactory;
import factory.CarFactory;
import factory.VanFactory;
import factory.VehicleFactory;
import vehicle.Bike;
import vehicle.Car;
import vehicle.Van;
import vehicle.Vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleFactoryTest {
    @Test
    void carFactoryCreatesCarWithRequestedDetails() {
        Vehicle vehicle = new CarFactory().createVehicle(1, "Toyota Camry");

        assertInstanceOf(Car.class, vehicle);
        assertEquals(1, vehicle.getId());
        assertEquals("Toyota Camry", vehicle.getModel());
        assertEquals("CAR", vehicle.getType());
    }

    @Test
    void bikeFactoryCreatesBike() {
        Vehicle vehicle = new BikeFactory().createVehicle(2, "Trek FX");

        assertInstanceOf(Bike.class, vehicle);
        assertEquals(2, vehicle.getId());
        assertEquals("Trek FX", vehicle.getModel());
        assertEquals("BIKE", vehicle.getType());
    }

    @Test
    void vanFactoryCreatesVan() {
        Vehicle vehicle = new VanFactory().createVehicle(3, "Ford Transit");

        assertInstanceOf(Van.class, vehicle);
        assertEquals(3, vehicle.getId());
        assertEquals("Ford Transit", vehicle.getModel());
        assertEquals("VAN", vehicle.getType());
    }

    @Test
    void factoryCreatesIndependentVehicles() {
        VehicleFactory factory = new CarFactory();
        Vehicle first = factory.createVehicle(1, "First car");
        Vehicle second = factory.createVehicle(2, "Second car");

        first.rent();

        assertNotSame(first, second);
        assertFalse(first.isAvailable());
        assertTrue(second.isAvailable());
    }
}
