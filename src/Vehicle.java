public interface Vehicle {
    int getId();

    String getModel();

    String getType();

    void rent();

    void returnVehicle();

    boolean isAvailable();

    String getStateName();
}
