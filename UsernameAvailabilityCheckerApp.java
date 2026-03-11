import java.util.*;

class UsernameChecker {

    // username -> userId
    private HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt count
    private HashMap<String, Integer> attempts = new HashMap<>();

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    // Register username
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }
}

public class UsernameAvailabilityCheckerApp {

    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        // Existing users
        checker.registerUser("john_doe", 1001);
        checker.registerUser("admin", 1002);

        System.out.println("Username Availability Checker\n");

        // Check usernames
        System.out.println("checkAvailability(\"john_doe\") → " +
                checker.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") → " +
                checker.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("\nsuggestAlternatives(\"john_doe\") → "
                + checker.suggestAlternatives("john_doe"));

        // Simulate attempts
        for (int i = 0; i < 5; i++) checker.checkAvailability("admin");
        for (int i = 0; i < 3; i++) checker.checkAvailability("john_doe");

        // Most attempted
        System.out.println("\ngetMostAttempted() → " +
                checker.getMostAttempted());
    }
}