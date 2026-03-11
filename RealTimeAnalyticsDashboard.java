import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

class AnalyticsEngine {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> sourceCount = new HashMap<>();

    // Process incoming page event
    public void processEvent(PageEvent event) {

        // Update page view count
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Track traffic source
        sourceCount.put(event.source, sourceCount.getOrDefault(event.source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== Real-Time Analytics Dashboard =====");

        // Top pages using sorting
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("\nTop Pages:");

        int rank = 1;

        for (Map.Entry<String, Integer> entry : list) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url + " - " + views +
                    " views (" + unique + " unique)");

            rank++;

            if (rank > 10) break;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : sourceCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class RealTimeAnalyticsDashboard {

    public static void main(String[] args) throws InterruptedException {

        AnalyticsEngine engine = new AnalyticsEngine();

        // Simulated streaming events
        engine.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        engine.processEvent(new PageEvent("/article/breaking-news", "user_456", "facebook"));
        engine.processEvent(new PageEvent("/sports/championship", "user_789", "google"));
        engine.processEvent(new PageEvent("/sports/championship", "user_999", "direct"));
        engine.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        engine.processEvent(new PageEvent("/tech/ai-news", "user_555", "twitter"));

        // Simulate dashboard update every 5 seconds
        Thread.sleep(5000);

        engine.getDashboard();
    }
}