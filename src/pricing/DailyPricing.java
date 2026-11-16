package pricing;

import vehicle.Vehicle;

public class DailyPricing implements PricingStrategy {

    @Override
    public double calculate(Vehicle vehicle, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");}
        return ratePerDay(vehicle.getType()) * days;}
    private double ratePerDay(String type) {
        return switch (type) {
            case "CAR" -> 10000;
            case "VAN" -> 15000;
            case "BIKE" -> 2000;
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }
    @Override
    public String getName() {
        return "Daily";
    }
}
