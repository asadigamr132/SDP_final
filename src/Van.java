public class Van extends AbstractVehicle {

    public Van(int id, String model) {
        super(id, model);
    }

    @Override
    public String getType() {
        return "CAR";
    }
}