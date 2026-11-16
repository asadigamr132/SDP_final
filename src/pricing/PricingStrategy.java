package pricing;

import vehicle.Vehicle;

public interface PricingStrategy {
    double calculate(Vehicle vehicle, int units);
    String getName();
}
