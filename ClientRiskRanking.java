import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    public Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return name + ":" + riskScore;
    }
}

public class ClientRiskRanking {

    public static void bubbleSortAscending(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        System.out.println("Bubble Sort (Ascending): " + Arrays.toString(arr));
        System.out.println("Total Swaps: " + swaps);
    }

    public static void insertionSortDescending(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 &&
                    (arr[j].riskScore < key.riskScore ||
                            (arr[j].riskScore == key.riskScore &&
                                    arr[j].accountBalance < key.accountBalance))) {

                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.print("Insertion Sort (Desc by Risk + Balance): ");
        System.out.println(Arrays.toString(arr));
    }

    public static void printTopRiskClients(Client[] arr, int topN) {
        System.out.print("Top " + topN + " High Risk Clients: ");
        for (int i = 0; i < Math.min(topN, arr.length); i++) {
            System.out.print(arr[i].name + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 2000),
                new Client("clientA", 20, 5000),
                new Client("clientB", 50, 3000)
        };

        System.out.println("Input:");
        System.out.println(Arrays.toString(clients));

        Client[] bubbleArray = Arrays.copyOf(clients, clients.length);
        bubbleSortAscending(bubbleArray);

        Client[] insertionArray = Arrays.copyOf(clients, clients.length);
        insertionSortDescending(insertionArray);

        printTopRiskClients(insertionArray, 10);
    }
}