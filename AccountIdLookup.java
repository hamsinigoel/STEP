import java.util.*;

public class AccountIdLookup {

    static int linearComparisons = 0;
    static int binaryComparisons = 0;

    public static int linearFirst(String[] arr, String target) {
        linearComparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }

    public static int linearLast(String[] arr, String target) {
        for (int i = arr.length - 1; i >= 0; i--) {
            linearComparisons++;
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }

    public static int binarySearch(String[] arr, String target) {
        int low = 0, high = arr.length - 1;
        binaryComparisons = 0;

        while (low <= high) {
            binaryComparisons++;
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) return mid;
            else if (arr[mid].compareTo(target) < 0) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    public static int firstOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                high = mid - 1;
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static int lastOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                low = mid + 1;
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static int countOccurrences(String[] arr, String target) {
        int first = firstOccurrence(arr, target);
        int last = lastOccurrence(arr, target);

        if (first == -1) return 0;
        return last - first + 1;
    }

    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        System.out.println("Input Logs: " + Arrays.toString(logs));

        int first = linearFirst(logs, "accB");
        int last = linearLast(logs, "accB");

        System.out.println("\nLinear Search:");
        System.out.println("First occurrence: " + first);
        System.out.println("Last occurrence: " + last);
        System.out.println("Comparisons: " + linearComparisons);

        Arrays.sort(logs);
        System.out.println("\nSorted Logs: " + Arrays.toString(logs));

        int index = binarySearch(logs, "accB");
        int count = countOccurrences(logs, "accB");

        System.out.println("\nBinary Search:");
        System.out.println("Found at index: " + index);
        System.out.println("Comparisons: " + binaryComparisons);
        System.out.println("Total occurrences: " + count);
    }
}