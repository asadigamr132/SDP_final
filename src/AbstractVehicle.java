public abstract class AbstractVehicle implements Vehicle {

    protected final int id;
    protected final String model;

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
}