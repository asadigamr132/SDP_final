import facade.RentalFacade;
import vehicle.VehicleType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class VehicleStateTest {
    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void newVehicleIsAvailable(VehicleType type) {
        RentalFacade facade = createFacade(type);

        assertEquals("Available", facade.getVehicleState(1));
    }

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void rentedVehicleCannotBeRentedAgain(VehicleType type) {
        RentalFacade facade = createFacade(type);
        facade.rentVehicle(1, 1);

        assertThrows(IllegalStateException.class, () -> facade.rentVehicle(1, 1));
        assertEquals("Rented", facade.getVehicleState(1));
    }

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void returnedVehicleCanBeRentedAgain(VehicleType type) {
        RentalFacade facade = createFacade(type);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);

        assertEquals("Available", facade.getVehicleState(1));

        facade.rentVehicle(1, 1);

        assertEquals("Rented", facade.getVehicleState(1));
    }

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void availableVehicleCannotBeReturned(VehicleType type) {
        RentalFacade facade = createFacade(type);

        assertThrows(IllegalStateException.class, () -> facade.returnVehicle(1));
        assertEquals("Available", facade.getVehicleState(1));
    }

    private RentalFacade createFacade(VehicleType type) {
        RentalFacade facade = new RentalFacade();
        facade.addVehicle(type, 1, "Test vehicle");
        return facade;
    }
}
