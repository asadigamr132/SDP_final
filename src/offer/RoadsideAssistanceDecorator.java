package offer;

public class RoadsideAssistanceDecorator extends RentalDecorator {

    public RoadsideAssistanceDecorator(RentalOffer rentalOffer) {
        super(rentalOffer);
    }

    @Override
    public double getPrice() {
        return rentalOffer.getPrice() + 1500;
    }

    @Override
    public String getDescription() {
        return rentalOffer.getDescription() + ", Roadside Assistance";
    }
}