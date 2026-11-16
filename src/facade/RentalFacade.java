package facade;

import factory.BikeFactory;
import factory.CarFactory;
import factory.VanFactory;
import factory.VehicleFactory;
import observer.AvailabilityNotifier;
import observer.VehicleObserver;
import offer.BaseRentalOffer;
import offer.Extra;
import offer.InsuranceDecorator;
import offer.RentalOffer;
import offer.RoadsideAssistanceDecorator;
import pricing.DailyPricing;
import pricing.PricingStrategy;
import request.RentalRequest;
import vehicle.Vehicle;
import vehicle.VehicleType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RentalFacade {

    private final Map<Integer, Vehicle> fleet = new HashMap<>();
    private final Map<VehicleType, VehicleFactory> vehicleFactories =
            new EnumMap<>(VehicleType.class);
    private final AvailabilityNotifier notifier = new AvailabilityNotifier();
    private PricingStrategy pricingStrategy = new DailyPricing();

    public RentalFacade() {
        vehicleFactories.put(VehicleType.CAR, new CarFactory());
        vehicleFactories.put(VehicleType.BIKE, new BikeFactory());
        vehicleFactories.put(VehicleType.VAN, new VanFactory());
    }

    public void addVehicle(VehicleType type, int id, String model) {
        if (fleet.containsKey(id)) {
            throw new IllegalArgumentException("Vehicle with id " + id + " already exists");
        }

        VehicleFactory factory = vehicleFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        }

        fleet.put(id, factory.createVehicle(id, model));
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

    public RentalOffer getQuote(RentalRequest request) {
        RentalOffer offer = getQuote(request.getVehicleId(), request.getUnits(),
                request.getExtras().toArray(new Extra[0]));
        String description = offer.getDescription()
                + ", Customer: " + request.getCustomer().getName()
                + ", Pickup: " + request.getPickupLocation();
        if (!request.getNotes().isBlank()) {
            description += ", Notes: " + request.getNotes();
        }
        return new BaseRentalOffer(offer.getPrice(), description);
    }

    public RentalOffer rentVehicle(RentalRequest request) {
        RentalOffer offer = getQuote(request);
        findVehicle(request.getVehicleId()).rent();
        return offer;
    }

    public void returnVehicle(int vehicleId) {
        Vehicle vehicle = findVehicle(vehicleId);
        vehicle.returnVehicle();
        notifier.notifyAvailable(vehicle);
    }

    public void subscribe(int vehicleId, VehicleObserver observer) {
        findVehicle(vehicleId);
        notifier.subscribe(vehicleId, observer);
    }

    public void unsubscribe(int vehicleId, VehicleObserver observer) {
        findVehicle(vehicleId);
        notifier.unsubscribe(vehicleId, observer);
    }

    public String getVehicleState(int vehicleId) {
        return findVehicle(vehicleId).getStateName();
    }

    private Vehicle findVehicle(int id) {
        Vehicle vehicle = fleet.get(id);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle " + id + " not found");
        }
        return vehicle;
    }
}
