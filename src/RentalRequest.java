import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentalRequest {
    private final Customer customer;
    private final int vehicleId;
    private final int units;
    private final List<Extra> extras;
    private final String pickupLocation;
    private final String notes;

    private RentalRequest(Builder builder) {
        customer = builder.customer;
        vehicleId = builder.vehicleId;
        units = builder.units;
        extras = List.copyOf(builder.extras);
        pickupLocation = builder.pickupLocation;
        notes = builder.notes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getUnits() {
        return units;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getNotes() {
        return notes;
    }

    public static class Builder {
        private final Customer customer;
        private final int vehicleId;
        private final int units;
        private final List<Extra> extras = new ArrayList<>();
        private String pickupLocation = "Rental office";
        private String notes = "";

        public Builder(Customer customer, int vehicleId, int units) {
            this.customer = Objects.requireNonNull(customer, "Customer cannot be null");
            if (units <= 0) {
                throw new IllegalArgumentException("Rental units must be positive");
            }
            this.vehicleId = vehicleId;
            this.units = units;
        }

        public Builder addExtra(Extra extra) {
            Objects.requireNonNull(extra, "Extra cannot be null");
            if (!extras.contains(extra)) {
                extras.add(extra);
            }
            return this;
        }

        public Builder pickupLocation(String pickupLocation) {
            if (pickupLocation == null || pickupLocation.isBlank()) {
                throw new IllegalArgumentException("Pickup location cannot be blank");
            }
            this.pickupLocation = pickupLocation;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = Objects.requireNonNull(notes, "Notes cannot be null");
            return this;
        }

        public RentalRequest build() {
            return new RentalRequest(this);
        }
    }
}
