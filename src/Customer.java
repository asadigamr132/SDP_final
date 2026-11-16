public class Customer implements VehicleObserver {
    private final String name;

    public Customer(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(Vehicle vehicle) {
        System.out.println(name + ": " + vehicle.getModel()
                + " (vehicle " + vehicle.getId() + ") is now available");
    }
}
