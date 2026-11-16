package offer;

public class BaseRentalOffer implements RentalOffer {
    private final double price;
    private final String description;

    public BaseRentalOffer(double price, String description){
        if(price < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        this.description = description;
    }


    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
