public class VanFactory extends VehicleFactory {

    @Override
    public Vehicle createVehicle(int id, String model) {
        return new Van(id, model);
    }
}
