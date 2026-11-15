public class VehicleFactory {

    public static Vehicle createVehicle(
            VehicleType type,
            int id,
            String model
    ) {
        return switch (type) {
            case CAR -> new Car(id, model);
            case VAN -> new Van(id, model);
            case BIKE -> new Bike(id, model);
            default -> throw new IllegalArgumentException("Wrong vehicle type");
        };
    }
}
