import facade.RentalFacade;
import observer.Customer;
import offer.Extra;
import offer.RentalOffer;
import pricing.HourlyPricing;
import request.RentalRequest;
import vehicle.VehicleType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalFacadeTest {
    private RentalFacade facade;

    @BeforeEach
    void setUp() {
        facade = new RentalFacade();
        facade.addVehicle(VehicleType.CAR, 1, "Toyota Camry");
        facade.addVehicle(VehicleType.BIKE, 2, "Trek FX");
        facade.addVehicle(VehicleType.VAN, 3, "Ford Transit");
    }

    @Test
    void facadeSelectsCorrectFactoryForEachVehicleType() {
        assertTrue(facade.getQuote(1, 1).getDescription().contains("Toyota Camry (CAR)"));
        assertTrue(facade.getQuote(2, 1).getDescription().contains("Trek FX (BIKE)"));
        assertTrue(facade.getQuote(3, 1).getDescription().contains("Ford Transit (VAN)"));
    }

    @Test
    void duplicateIdDoesNotReplaceExistingVehicle() {
        assertThrows(IllegalArgumentException.class,
                () -> facade.addVehicle(VehicleType.VAN, 1, "Another vehicle"));
        assertTrue(facade.getQuote(1, 1).getDescription().contains("Toyota Camry (CAR)"));
    }

    @Test
    void unknownVehicleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> facade.getQuote(99, 1));
        assertThrows(IllegalArgumentException.class, () -> facade.rentVehicle(99, 1));
        assertThrows(IllegalArgumentException.class, () -> facade.returnVehicle(99));
        assertThrows(IllegalArgumentException.class, () -> facade.getVehicleState(99));
    }

    @Test
    void failedQuoteLeavesVehicleAvailable() {
        assertThrows(IllegalArgumentException.class, () -> facade.rentVehicle(1, 0));
        assertEquals("Available", facade.getVehicleState(1));
    }

    @Test
    void gettingQuoteDoesNotRentVehicle() {
        RentalOffer quote = facade.getQuote(1, 2, Extra.INSURANCE);

        assertEquals(22000.0, quote.getPrice(), 0.001);
        assertEquals("Available", facade.getVehicleState(1));
    }

    @Test
    void changingStrategyChangesNewQuotes() {
        RentalRequest request = new RentalRequest.Builder(new Customer("Olzhas"), 1, 2)
                .build();

        assertEquals(20000.0, facade.getQuote(request).getPrice(), 0.001);

        facade.setPricingStrategy(new HourlyPricing());

        assertEquals(3000.0, facade.getQuote(request).getPrice(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "CAR, false, 20000, 22000, 21500, 23500",
            "BIKE, false, 4000, 6000, 5500, 7500",
            "VAN, false, 30000, 32000, 31500, 33500",
            "CAR, true, 3000, 5000, 4500, 6500",
            "BIKE, true, 600, 2600, 2100, 4100",
            "VAN, true, 5000, 7000, 6500, 8500"
    })
    void quotesCombineVehiclePricingAndExtras(VehicleType type, boolean hourly,
            double base, double insurance, double roadside, double both) {
        RentalFacade rental = new RentalFacade();
        rental.addVehicle(type, 10, "Test vehicle");
        if (hourly) {
            rental.setPricingStrategy(new HourlyPricing());
        }

        assertEquals(base, rental.getQuote(10, 2).getPrice(), 0.001);
        assertEquals(insurance, rental.getQuote(10, 2, Extra.INSURANCE).getPrice(), 0.001);
        assertEquals(roadside,
                rental.getQuote(10, 2, Extra.ROADSIDE_ASSISTANCE).getPrice(), 0.001);
        assertEquals(both,
                rental.getQuote(10, 2, Extra.INSURANCE, Extra.ROADSIDE_ASSISTANCE).getPrice(), 0.001);
    }

    @Test
    void rentingOneVehicleDoesNotChangeAnotherVehicle() {
        facade.rentVehicle(1, 1);

        assertEquals("Rented", facade.getVehicleState(1));
        assertEquals("Available", facade.getVehicleState(2));
        assertEquals("Available", facade.getVehicleState(3));
    }

    @Test
    void requestWorksThroughCompleteRentalAndNotificationFlow() {
        List<Integer> notifications = new ArrayList<>();
        facade.subscribe(1, vehicle -> notifications.add(vehicle.getId()));
        RentalRequest request = new RentalRequest.Builder(new Customer("Altair"), 1, 2)
                .addExtra(Extra.INSURANCE)
                .addExtra(Extra.ROADSIDE_ASSISTANCE)
                .pickupLocation("Airport")
                .notes("Pickup at 10:00")
                .build();

        RentalOffer rental = facade.rentVehicle(request);

        assertEquals(23500.0, rental.getPrice(), 0.001);
        assertTrue(rental.getDescription().contains("Altair"));
        assertTrue(rental.getDescription().contains("Airport"));
        assertTrue(rental.getDescription().contains("Pickup at 10:00"));
        assertEquals("Rented", facade.getVehicleState(1));
        assertTrue(notifications.isEmpty());
        assertThrows(IllegalStateException.class, () -> facade.rentVehicle(request));

        facade.returnVehicle(1);

        assertEquals("Available", facade.getVehicleState(1));
        assertEquals(List.of(1), notifications);
    }
}
