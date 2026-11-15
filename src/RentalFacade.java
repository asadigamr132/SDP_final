import java.util.HashMap;
import java.util.Map;

public class RentalFacade {

    private final Map<Integer, Vehicle> fleet = new HashMap<>();
    private PricingStrategy pricingStrategy = new DailyPricing();
    public void addVehicle(VehicleType type, int id, String model) {
        if (fleet.containsKey(id)) {
            throw new IllegalArgumentException("Vehicle with id " + id + " already exists");
        }
        fleet.put(id, VehicleFactory.createVehicle(type, id, model));
    }
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    public RentalOffer getQuote(int vehicleId, int units, Extra... extras) {
        Vehicle vehicle = findVehicle(vehicleId);
        double basePrice = pricingStrategy.calculate(vehicle, units);

        RentalOffer offer = new BaseRentalOffer(
                basePrice,
                vehicle.getModel() + " (" + vehicle.getType() + "), "
                        + pricingStrategy.getName() + " x" + units
        );
        for (Extra extra : extras) {
            offer = switch (extra) {
                case INSURANCE -> new InsuranceDecorator(offer);
                case ROADSIDE_ASSISTANCE -> new RoadsideAssistanceDecorator(offer);
            };
        }
        return offer;
    }
    public RentalOffer rentVehicle(int vehicleId, int units, Extra... extras) {
        Vehicle vehicle = findVehicle(vehicleId);
        RentalOffer offer = getQuote(vehicleId, units, extras);
        vehicle.rent();
        return offer;
    }
    public void returnVehicle(int vehicleId) {
        findVehicle(vehicleId).returnVehicle();
    }
    public String getVehicleState(int vehicleId) {
        return findVehicle(vehicleId).getStateName();
    }
    private Vehicle findVehicle(int id) {
        Vehicle vehicle = fleet.get(id);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle " + id + " not found");
        }
        return vehicle;}
}
