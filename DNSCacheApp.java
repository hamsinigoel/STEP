import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        misses++;

        // Simulate upstream DNS query
        String newIP = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, newIP, 10); // TTL = 10 sec
        cache.put(domain, newEntry);

        return "Cache MISS → Query upstream → " + newIP;
    }

    // Simulated upstream DNS
    private String queryUpstreamDNS(String domain) {
        Random rand = new Random();
        return "172.217.14." + (rand.nextInt(200) + 1);
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("\nCache Statistics:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }
}

public class DNSCacheApp {

    public static void main(String[] args) throws InterruptedException {

        DNSCache dnsCache = new DNSCache(5);

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com")); // HIT

        Thread.sleep(11000); // wait for TTL expiration

        System.out.println(dnsCache.resolve("google.com")); // expired

        System.out.println(dnsCache.resolve("openai.com"));
        System.out.println(dnsCache.resolve("github.com"));
        System.out.println(dnsCache.resolve("stackoverflow.com"));

        dnsCache.getCacheStats();
    }
}