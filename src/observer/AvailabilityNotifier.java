package observer;

import vehicle.Vehicle;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AvailabilityNotifier {
    private final Map<Integer, Set<VehicleObserver>> subscribers = new HashMap<>();

    public void subscribe(int vehicleId, VehicleObserver observer) {
        Objects.requireNonNull(observer, "Observer cannot be null");
        subscribers.computeIfAbsent(vehicleId, id -> new LinkedHashSet<>()).add(observer);
    }

    public void unsubscribe(int vehicleId, VehicleObserver observer) {
        Set<VehicleObserver> observers = subscribers.get(vehicleId);
        if (observers != null) {
            observers.remove(observer);
            if (observers.isEmpty()) {
                subscribers.remove(vehicleId);
            }
        }
    }

    public void notifyAvailable(Vehicle vehicle) {
        Set<VehicleObserver> observers = subscribers.get(vehicle.getId());
        if (observers != null) {
            for (VehicleObserver observer : new LinkedHashSet<>(observers)) {
                observer.update(vehicle);
            }
        }
    }
}
