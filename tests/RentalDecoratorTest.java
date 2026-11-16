import offer.BaseRentalOffer;
import offer.InsuranceDecorator;
import offer.RentalOffer;
import offer.RoadsideAssistanceDecorator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RentalDecoratorTest {
    @Test
    void baseOfferKeepsPriceAndDescription() {
        RentalOffer offer = new BaseRentalOffer(0, "Free rental");

        assertEquals(0.0, offer.getPrice(), 0.001);
        assertEquals("Free rental", offer.getDescription());
    }

    @Test
    void insuranceAddsItsFeeWithoutChangingBaseOffer() {
        RentalOffer base = new BaseRentalOffer(10000, "Car rental");
        RentalOffer offer = new InsuranceDecorator(base);

        assertEquals(12000.0, offer.getPrice(), 0.001);
        assertEquals("Car rental, Insurance", offer.getDescription());
        assertEquals(10000.0, base.getPrice(), 0.001);
        assertEquals("Car rental", base.getDescription());
    }

    @Test
    void roadsideAssistanceAddsItsFee() {
        RentalOffer offer = new RoadsideAssistanceDecorator(
                new BaseRentalOffer(10000, "Car rental"));

        assertEquals(11500.0, offer.getPrice(), 0.001);
        assertEquals("Car rental, Roadside Assistance", offer.getDescription());
    }

    @Test
    void bothExtrasCanBeCombinedInEitherOrder() {
        RentalOffer base = new BaseRentalOffer(20000, "Car rental");
        RentalOffer first = new InsuranceDecorator(new RoadsideAssistanceDecorator(base));
        RentalOffer second = new RoadsideAssistanceDecorator(new InsuranceDecorator(base));

        assertEquals(23500.0, first.getPrice(), 0.001);
        assertEquals(first.getPrice(), second.getPrice(), 0.001);
        assertTrue(first.getDescription().contains("Insurance"));
        assertTrue(first.getDescription().contains("Roadside Assistance"));
    }

    @Test
    void baseOfferRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new BaseRentalOffer(-1, "Car rental"));
    }
}
