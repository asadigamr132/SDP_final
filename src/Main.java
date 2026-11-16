public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" FACTORY METHOD + DECORATOR DEMONSTRATION");
        System.out.println("========================================");

        VehicleFactory carFactory = new CarFactory();
        Vehicle car = carFactory.createVehicle(99, "Toyota Camry");

        System.out.println("Created vehicle: " + car.getModel());
        System.out.println("Vehicle type: " + car.getType());

        RentalOffer offer = new BaseRentalOffer(
                10000,
                car.getModel() + " rental"
        );
        offer = new InsuranceDecorator(offer);
        offer = new RoadsideAssistanceDecorator(offer);

        System.out.println("Decorated price: " + offer.getPrice());
        System.out.println("Description: " + offer.getDescription());

        System.out.println();
        System.out.println("========================================");
        System.out.println(" COMPLETE VEHICLE RENTAL SYSTEM DEMO");
        System.out.println("========================================");

        RentalDemo.main(args);
    }
}
