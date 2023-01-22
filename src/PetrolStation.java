import java.util.concurrent.ThreadLocalRandom;

public class PetrolStation {
    private double availableFuel;
    private static short numberOfRequests;

    public PetrolStation(double availableFuel) {
        this.availableFuel = availableFuel;
        numberOfRequests = 0;
    }


    void doRefuel(int refuelQuantity) {
        if (++numberOfRequests > 3) {
            throw new IndexOutOfBoundsException();
        }

        if (refuelQuantity > availableFuel) {
            throw new IllegalArgumentException();
        }

        availableFuel -= refuelQuantity;
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(3000, 10000));
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        numberOfRequests--;
    }
}
