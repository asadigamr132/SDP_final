package factory;

import vehicle.Vehicle;

public abstract class VehicleFactory {

    public abstract Vehicle createVehicle(int id, String model);
}
