import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    public ParkingSpot() {
        this.licensePlate = null;
        this.occupied = false;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int operations = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity; // linear probing
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        operations++;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (plate.equals(table[index].licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = Math.ceil(hours) * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedCount--;

                System.out.println("exitVehicle(\"" + plate + "\") → Spot #" +
                        index + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" + fee);
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot (from entrance = index 0)
    public void findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking lot full.");
    }

    // Statistics
    public void getStatistics() {

        double occupancy = (occupiedCount * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : (double) totalProbes / operations;

        System.out.println("\nParking Statistics:");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }
}

public class ParkingLotOpenAddressing {

    public static void main(String[] args) throws InterruptedException {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking duration

        lot.exitVehicle("ABC-1234");

        lot.findNearestSpot();

        lot.getStatistics();
    }
}