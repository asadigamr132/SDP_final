public interface VehicleState {
    void rent(AbstractVehicle vehicle);

    void returnVehicle(AbstractVehicle vehicle);

    boolean isAvailable();

    String getName();
}
