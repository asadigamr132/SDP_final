public interface PricingStrategy {
    double calculate(Vehicle vehicle, int units);
    String getName();
}
