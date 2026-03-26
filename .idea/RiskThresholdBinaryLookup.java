import java.util.*;

public class RiskThresholdBinaryLookup {

    static int linearComparisons = 0;
    static int binaryComparisons = 0;

    public static int linearSearch(int[] arr, int target) {
        linearComparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i] == target) return i;
        }
        return -1;
    }

    public static int findInsertionPoint(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        binaryComparisons = 0;

        while (low <= high) {
            binaryComparisons++;
            int mid = (low + high) / 2;

            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }

        return low;
    }

    public static Integer findFloor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        Integer floor = null;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) return arr[mid];
            else if (arr[mid] < target) {
                floor = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return floor;
    }

    public static Integer findCeiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        Integer ceil = null;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) return arr[mid];
            else if (arr[mid] > target) {
                ceil = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ceil;
    }

    public static void main(String[] args) {

        int[] unsorted = {50, 10, 100, 25};
        int[] sorted = {10, 25, 50, 100};
        int target = 30;

        System.out.println("Unsorted Risks: " + Arrays.toString(unsorted));

        int linearResult = linearSearch(unsorted, target);
        System.out.println("\nLinear Search (threshold = 30): " +
                (linearResult == -1 ? "Not Found" : "Found at index " + linearResult));
        System.out.println("Comparisons: " + linearComparisons);

        System.out.println("\nSorted Risks: " + Arrays.toString(sorted));

        int insertionPoint = findInsertionPoint(sorted, target);
        Integer floor = findFloor(sorted, target);
        Integer ceiling = findCeiling(sorted, target);

        System.out.println("\nBinary Search:");
        System.out.println("Insertion Point: index " + insertionPoint);
        System.out.println("Comparisons: " + binaryComparisons);
        System.out.println("Floor (<=30): " + floor);
        System.out.println("Ceiling (>=30): " + ceiling);
    }
}