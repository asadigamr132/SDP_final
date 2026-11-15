public class Main {

    public static void main(String[] args) {

        Vehicle car = VehicleFactory.createVehicle(
                VehicleType.CAR, 1, "Toyota Camry"
        );

        System.out.println(car.getModel());
        System.out.println(car.getType());
    }
}
