public class AvailableState implements VehicleState {
    @Override
    public void rent(AbstractVehicle vehicle) {
        vehicle.setState(new RentedState());
    }

    @Override
    public void returnVehicle(AbstractVehicle vehicle) {
        throw new IllegalStateException("Vehicle " + vehicle.getId() + " is not rented");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getName() {
        return "Available";
    }
}
