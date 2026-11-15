public class RentedState implements VehicleState {
    @Override
    public void rent(AbstractVehicle vehicle) {
        throw new IllegalStateException("Vehicle " + vehicle.getId() + " is already rented");
    }

    @Override
    public void returnVehicle(AbstractVehicle vehicle) {
        vehicle.setState(new AvailableState());
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String getName() {
        return "Rented";
    }
}
