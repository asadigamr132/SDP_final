public class HourlyPricing implements PricingStrategy {

    @Override
    public double calculate(Vehicle vehicle, int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");}
        return ratePerHour(vehicle.getType()) * hours;}
    private double ratePerHour(String type) {
        return switch (type) {
            case "CAR" -> 1500;
            case "VAN" -> 2500;
            case "BIKE" -> 300;
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }
    @Override
    public String getName() {
        return "Hourly";
    }
}
