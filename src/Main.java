import facade.RentalFacade;
import factory.CarFactory;
import factory.VehicleFactory;
import observer.Customer;
import offer.Extra;
import offer.RentalOffer;
import pricing.DailyPricing;
import pricing.HourlyPricing;
import request.RentalRequest;
import vehicle.Vehicle;
import vehicle.VehicleType;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" VEHICLE RENTAL SERVICE - 7 DESIGN PATTERNS");
        System.out.println("========================================");

        RentalFacade facade = new RentalFacade();


        System.out.println("\n1. FACTORY METHOD");
        VehicleFactory carFactory = new CarFactory();
        Vehicle previewCar = carFactory.createVehicle(99, "Toyota Camry");
        System.out.println("Created by CarFactory: "
                + previewCar.getModel() + " (" + previewCar.getType() + ")");


        facade.addVehicle(VehicleType.CAR, 1, "Toyota Camry");
        facade.addVehicle(VehicleType.BIKE, 2, "Trek FX");
        facade.addVehicle(VehicleType.VAN, 3, "Ford Transit");
        System.out.println("Fleet created through RentalFacade.");


        System.out.println("\n2. STRATEGY");
        facade.setPricingStrategy(new DailyPricing());
        printOffer("Daily pricing", facade.getQuote(1, 2));

        facade.setPricingStrategy(new HourlyPricing());
        printOffer("Hourly pricing", facade.getQuote(1, 5));


        System.out.println("\n3. BUILDER");
        Customer renter = new Customer("Altair");
        RentalRequest request = new RentalRequest.Builder(renter, 1, 2)
                .addExtra(Extra.INSURANCE)
                .addExtra(Extra.ROADSIDE_ASSISTANCE)
                .pickupLocation("Astana airport")
                .notes("Pickup at 10:00")
                .build();
        System.out.println("Rental request built for: "
                + request.getCustomer().getName());


        System.out.println("\n4. FACADE");
        System.out.println("All rental operations are performed through RentalFacade.");

        System.out.println("\n5. DECORATOR");
        facade.setPricingStrategy(new DailyPricing());
        RentalOffer decoratedOffer = facade.getQuote(request);
        printOffer("Daily rental with insurance and roadside assistance",
                decoratedOffer);


        System.out.println("\n6. OBSERVER");
        Customer firstSubscriber = new Customer("Olzhas");
        Customer secondSubscriber = new Customer("Alisher");
        facade.subscribe(1, firstSubscriber);
        facade.subscribe(1, secondSubscriber);
        facade.unsubscribe(1, secondSubscriber);
        System.out.println("Olzhas subscribed; Alisher unsubscribed.");


        System.out.println("\n7. STATE");
        System.out.println("State before rental: " + facade.getVehicleState(1));

        RentalOffer rental = facade.rentVehicle(request);
        printOffer("Successful rental", rental);
        System.out.println("State after rental: " + facade.getVehicleState(1));

        try {
            facade.rentVehicle(1, 1);
        } catch (IllegalStateException e) {
            System.out.println("Second rental rejected: " + e.getMessage());
        }

        facade.returnVehicle(1);
        System.out.println("State after return: " + facade.getVehicleState(1));

        System.out.println("\n========================================");
        System.out.println(" ALL 7 PATTERNS HAVE BEEN DEMONSTRATED");
        System.out.println("========================================");
    }

    private static void printOffer(String label, RentalOffer offer) {
        System.out.println(label + ": " + offer.getDescription()
                + " -> " + offer.getPrice() + " tenge");
    }
}
