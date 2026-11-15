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
        print(facade.rentVehicle(1, 2, Extra.INSURANCE, Extra.ROADSIDE_ASSISTANCE));
        System.out.println("Vehicle 1 state: " + facade.getVehicleState(1));

        try {
            facade.rentVehicle(1, 1);
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        facade.returnVehicle(1);
        System.out.println("Returned vehicle 1");
        System.out.println("Vehicle 1 state: " + facade.getVehicleState(1));
    }
    private static void print(RentalOffer offer) {
        System.out.println(offer.getDescription() + " -> " + offer.getPrice());
    }
}
