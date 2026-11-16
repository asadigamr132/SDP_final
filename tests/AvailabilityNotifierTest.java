import facade.RentalFacade;
import observer.VehicleObserver;
import vehicle.Vehicle;
import vehicle.VehicleType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityNotifierTest {
    private RentalFacade facade;
    private RecordingObserver observer;

    @BeforeEach
    void setUp() {
        facade = new RentalFacade();
        facade.addVehicle(VehicleType.CAR, 1, "Car");
        facade.addVehicle(VehicleType.BIKE, 2, "Bike");
        observer = new RecordingObserver();
    }

    @Test
    void successfulReturnNotifiesAllSubscribersAfterStateChanges() {
        RecordingObserver second = new RecordingObserver();
        facade.subscribe(1, observer);
        facade.subscribe(1, second);
        facade.rentVehicle(1, 1);

        assertTrue(observer.vehicleIds.isEmpty());

        facade.returnVehicle(1);

        assertEquals(List.of(1), observer.vehicleIds);
        assertEquals(List.of(1), second.vehicleIds);
        assertEquals(List.of("Available"), observer.states);
    }

    @Test
    void duplicateSubscriptionDoesNotDuplicateNotification() {
        facade.subscribe(1, observer);
        facade.subscribe(1, observer);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);

        assertEquals(List.of(1), observer.vehicleIds);
    }

    @Test
    void unsubscribedObserverReceivesNoNotification() {
        facade.subscribe(1, observer);
        facade.unsubscribe(1, observer);
        facade.unsubscribe(1, observer);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);

        assertTrue(observer.vehicleIds.isEmpty());
    }

    @Test
    void returningAnotherVehicleDoesNotNotifyObserver() {
        facade.subscribe(1, observer);
        facade.rentVehicle(2, 1);
        facade.returnVehicle(2);

        assertTrue(observer.vehicleIds.isEmpty());
    }

    @Test
    void failedReturnDoesNotNotifyObserver() {
        facade.subscribe(1, observer);

        assertThrows(IllegalStateException.class, () -> facade.returnVehicle(1));
        assertTrue(observer.vehicleIds.isEmpty());

        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);
        assertThrows(IllegalStateException.class, () -> facade.returnVehicle(1));
        assertEquals(List.of(1), observer.vehicleIds);
    }

    @Test
    void subscriptionRemainsActiveForLaterReturns() {
        facade.subscribe(1, observer);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);

        assertEquals(List.of(1, 1), observer.vehicleIds);
    }

    @Test
    void oneObserverCanSubscribeToDifferentVehicles() {
        facade.subscribe(1, observer);
        facade.subscribe(2, observer);
        facade.unsubscribe(1, observer);
        facade.rentVehicle(1, 1);
        facade.returnVehicle(1);
        facade.rentVehicle(2, 1);
        facade.returnVehicle(2);

        assertEquals(List.of(2), observer.vehicleIds);
    }

    @Test
    void subscriptionRejectsNullObserverAndUnknownVehicle() {
        assertThrows(NullPointerException.class, () -> facade.subscribe(1, null));
        assertThrows(IllegalArgumentException.class, () -> facade.subscribe(99, observer));
        assertThrows(IllegalArgumentException.class, () -> facade.unsubscribe(99, observer));
    }

    private static class RecordingObserver implements VehicleObserver {
        private final List<Integer> vehicleIds = new ArrayList<>();
        private final List<String> states = new ArrayList<>();

        @Override
        public void update(Vehicle vehicle) {
            vehicleIds.add(vehicle.getId());
            states.add(vehicle.getStateName());
        }
    }
}
