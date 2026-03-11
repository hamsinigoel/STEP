import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // access-order
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

class MultiLevelCache {

    // L1: memory cache
    private LRUCache<String, VideoData> L1 = new LRUCache<>(10000);

    // L2: SSD cache
    private LRUCache<String, VideoData> L2 = new LRUCache<>(100000);

    // L3: database simulation
    private HashMap<String, VideoData> database = new HashMap<>();

    private HashMap<String, Integer> accessCount = new HashMap<>();

    // Statistics
    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public MultiLevelCache() {

        // Preload database
        database.put("video_123", new VideoData("video_123", "Movie Data A"));
        database.put("video_999", new VideoData("video_999", "Movie Data B"));
    }

    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 Check
        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 Check
        if (L2.containsKey(videoId)) {

            l2Hits++;
            System.out.println("L2 Cache HIT");

            VideoData video = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, video);
            System.out.println("Promoted to L1");

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        VideoData video = database.get(videoId);

        if (video != null) {
            l3Hits++;
            System.out.println("L3 Database HIT");

            L2.put(videoId, video);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);
        }

        long end = System.nanoTime();

        System.out.println("Lookup time: " +
                ((end - start) / 1_000_000.0) + " ms\n");

        return video;
    }

    // Cache invalidation
    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // Statistics
    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\nCache Statistics:");

        System.out.println("L1 Hits: " + l1Hits);
        System.out.println("L2 Hits: " + l2Hits);
        System.out.println("L3 Hits: " + l3Hits);

        double hitRate = total == 0 ? 0 :
                ((l1Hits + l2Hits) * 100.0) / total;

        System.out.println("Overall Hit Rate: " +
                String.format("%.2f", hitRate) + "%");
    }
}

public class MultiLevelCacheSystem {

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        System.out.println("getVideo(\"video_123\")");
        cache.getVideo("video_123");

        System.out.println("getVideo(\"video_123\") [second request]");
        cache.getVideo("video_123");

        System.out.println("getVideo(\"video_999\")");
        cache.getVideo("video_999");

        cache.getStatistics();

        cache.invalidate("video_123");
    }
}