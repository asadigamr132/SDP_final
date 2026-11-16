import pricing.DailyPricing;
import pricing.HourlyPricing;
import pricing.PricingStrategy;
import vehicle.Bike;
import vehicle.Car;
import vehicle.Van;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyTest {
    @Test
    void dailyPricingUsesVehicleTypeAndDuration() {
        PricingStrategy strategy = new DailyPricing();

        assertEquals(20000.0, strategy.calculate(new Car(1, "Car"), 2), 0.001);
        assertEquals(4000.0, strategy.calculate(new Bike(2, "Bike"), 2), 0.001);
        assertEquals(30000.0, strategy.calculate(new Van(3, "Van"), 2), 0.001);
    }

    @Test
    void hourlyPricingUsesVehicleTypeAndDuration() {
        PricingStrategy strategy = new HourlyPricing();

        assertEquals(7500.0, strategy.calculate(new Car(1, "Car"), 5), 0.001);
        assertEquals(1500.0, strategy.calculate(new Bike(2, "Bike"), 5), 0.001);
        assertEquals(12500.0, strategy.calculate(new Van(3, "Van"), 5), 0.001);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void dailyPricingRejectsNonPositiveDuration(int duration) {
        assertThrows(IllegalArgumentException.class,
                () -> new DailyPricing().calculate(new Car(1, "Car"), duration));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void hourlyPricingRejectsNonPositiveDuration(int duration) {
        assertThrows(IllegalArgumentException.class,
                () -> new HourlyPricing().calculate(new Car(1, "Car"), duration));
    }
}
