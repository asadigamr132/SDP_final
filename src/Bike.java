public class Bike extends AbstractVehicle {

    public Bike(int id, String model) {
        super(id, model);
    }

    @Override
    public String getType() {
        return "BIKE";
    }
}