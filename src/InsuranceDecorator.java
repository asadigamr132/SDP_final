public class InsuranceDecorator extends RentalDecorator {

    public InsuranceDecorator(RentalOffer rentalOffer) {
        super(rentalOffer);
    }

    @Override
    public double getPrice() {
        return rentalOffer.getPrice() + 2000;
    }

    @Override
    public String getDescription() {
        return rentalOffer.getDescription() + ", Insurance";
    }
}