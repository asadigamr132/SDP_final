public class Main {
    public static void main(String[] args) {
        Vehicle car = VehicleFactory.createVehicle(
                VehicleType.CAR,
                1,
                "Toyota Camry"
        );

        System.out.println("Model: " + car.getModel());
        System.out.println("Type: " + car.getType());

        RentalOffer offer = new BaseRentalOffer(
            10000,
            "Toyota Camry rental"
        );

        offer = new InsuranceDecorator(offer);

        offer = new RoadsideAssistanceDecorator(offer);

    System.out.println("Price: " + offer.getPrice());
    System.out.println("Description: " + offer.getDescription());
}
}