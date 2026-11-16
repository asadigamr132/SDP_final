package vehicle;

public class Car extends AbstractVehicle {

    public Car(int id, String model) {
        super(id, model);
    }

    @Override
    public String getType() {
        return "CAR";
    }
}