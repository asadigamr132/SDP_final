package offer;

public abstract class RentalDecorator implements RentalOffer {

    protected final RentalOffer rentalOffer;

    public RentalDecorator(RentalOffer rentalOffer) {
        this.rentalOffer = rentalOffer;
    }

    @Override
    public double getPrice() {
        return rentalOffer.getPrice();
    }

    @Override
    public String getDescription() {
        return rentalOffer.getDescription();
    }
}