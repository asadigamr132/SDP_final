public class RentalDemo {
    public static void main(String[] args) {
        RentalFacade facade = new RentalFacade();
        facade.addVehicle(VehicleType.CAR, 1, "Toyota Camry");
        facade.addVehicle(VehicleType.BIKE, 2, "Trek FX");
        facade.addVehicle(VehicleType.VAN, 3, "Ford Transit");
        System.out.println("Vehicle 1 state: " + facade.getVehicleState(1));

        print(facade.getQuote(1, 2));

        facade.setPricingStrategy(new HourlyPricing());
        print(facade.getQuote(1, 5));

        facade.setPricingStrategy(new DailyPricing());
        Customer renter = new Customer("Altair");
        RentalRequest request = new RentalRequest.Builder(renter, 1, 2)
                .addExtra(Extra.INSURANCE)
                .addExtra(Extra.ROADSIDE_ASSISTANCE)
                .pickupLocation("Astana airport")
                .notes("Pickup at 10:00")
                .build();
        print(facade.rentVehicle(request));
        System.out.println("Vehicle 1 state: " + facade.getVehicleState(1));

        try {
            facade.rentVehicle(1, 1);
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        Customer customer = new Customer("Olzhas");
        Customer secondCustomer = new Customer("Alisher");
        facade.subscribe(1, customer);
        facade.subscribe(1, secondCustomer);
        facade.unsubscribe(1, secondCustomer);

        facade.returnVehicle(1);
        System.out.println("Returned vehicle 1");
        System.out.println("Vehicle 1 state: " + facade.getVehicleState(1));
    }
    private static void print(RentalOffer offer) {
        System.out.println(offer.getDescription() + " -> " + offer.getPrice());
    }
}
