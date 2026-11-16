public class BikeFactory extends VehicleFactory {

    @Override
    public Vehicle createVehicle(int id, String model) {
        return new Bike(id, model);
    }
}
