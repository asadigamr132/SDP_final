public abstract class AbstractVehicle implements Vehicle {

    protected final int id;
    protected final String model;
    private VehicleState state = new AvailableState();

    public AbstractVehicle(int id, String model) {
        this.id = id;
        this.model = model;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void rent() {
        state.rent(this);
    }

    @Override
    public void returnVehicle() {
        state.returnVehicle(this);
    }

    @Override
    public boolean isAvailable() {
        return state.isAvailable();
    }

    @Override
    public String getStateName() {
        return state.getName();
    }

    void setState(VehicleState state) {
        this.state = java.util.Objects.requireNonNull(state, "State cannot be null");
    }
}
