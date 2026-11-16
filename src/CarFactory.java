public class CarFactory extends VehicleFactory {

    @Override
    public Vehicle createVehicle(int id, String model) {
        return new Car(id, model);
    }
}
