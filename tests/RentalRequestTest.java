import observer.Customer;
import offer.Extra;
import request.RentalRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RentalRequestTest {
    private final Customer customer = new Customer("Olzhas");

    @Test
    void requestKeepsRequiredValuesAndDefaults() {
        RentalRequest request = new RentalRequest.Builder(customer, 1, 2).build();

        assertSame(customer, request.getCustomer());
        assertEquals(1, request.getVehicleId());
        assertEquals(2, request.getUnits());
        assertTrue(request.getExtras().isEmpty());
        assertEquals("Rental office", request.getPickupLocation());
        assertEquals("", request.getNotes());
    }

    @Test
    void requestKeepsOptionalDetails() {
        RentalRequest request = new RentalRequest.Builder(customer, 1, 2)
                .addExtra(Extra.INSURANCE)
                .pickupLocation("Airport")
                .notes("Morning pickup")
                .build();

        assertEquals(1, request.getExtras().size());
        assertEquals(Extra.INSURANCE, request.getExtras().get(0));
        assertEquals("Airport", request.getPickupLocation());
        assertEquals("Morning pickup", request.getNotes());
    }

    @Test
    void duplicateExtraIsStoredOnce() {
        RentalRequest request = new RentalRequest.Builder(customer, 1, 2)
                .addExtra(Extra.INSURANCE)
                .addExtra(Extra.INSURANCE)
                .build();

        assertEquals(1, request.getExtras().size());
    }

    @Test
    void reusingBuilderDoesNotChangeEarlierRequest() {
        RentalRequest.Builder builder = new RentalRequest.Builder(customer, 1, 2);
        RentalRequest first = builder.addExtra(Extra.INSURANCE).build();
        RentalRequest second = builder.addExtra(Extra.ROADSIDE_ASSISTANCE)
                .pickupLocation("Airport")
                .notes("Morning pickup")
                .build();

        assertEquals(1, first.getExtras().size());
        assertEquals("Rental office", first.getPickupLocation());
        assertEquals("", first.getNotes());
        assertEquals(2, second.getExtras().size());
        assertEquals("Airport", second.getPickupLocation());
    }

    @Test
    void callerCannotChangeRequestExtras() {
        RentalRequest request = new RentalRequest.Builder(customer, 1, 2)
                .addExtra(Extra.INSURANCE).build();

        assertThrows(UnsupportedOperationException.class,
                () -> request.getExtras().clear());
        assertEquals(1, request.getExtras().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void builderRejectsNonPositiveDuration(int duration) {
        assertThrows(IllegalArgumentException.class,
                () -> new RentalRequest.Builder(customer, 1, duration));
    }

    @Test
    void builderRejectsNullCustomer() {
        assertThrows(NullPointerException.class,
                () -> new RentalRequest.Builder(null, 1, 2));
    }

    @Test
    void builderRejectsNullExtraAndNotes() {
        RentalRequest.Builder builder = new RentalRequest.Builder(customer, 1, 2);

        assertThrows(NullPointerException.class, () -> builder.addExtra(null));
        assertThrows(NullPointerException.class, () -> builder.notes(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void builderRejectsBlankPickupLocation(String location) {
        RentalRequest.Builder builder = new RentalRequest.Builder(customer, 1, 2);

        assertThrows(IllegalArgumentException.class,
                () -> builder.pickupLocation(location));
    }

    @Test
    void builderRejectsNullPickupLocation() {
        RentalRequest.Builder builder = new RentalRequest.Builder(customer, 1, 2);

        assertThrows(IllegalArgumentException.class, () -> builder.pickupLocation(null));
    }
}
