import java.util.HashMap;
import java.util.Map;

class TokenBucket {

    private int maxTokens;
    private double refillRate;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, int refillPerHour) {
        this.maxTokens = maxTokens;
        this.refillRate = (double) refillPerHour / 3600.0; // tokens per second
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on elapsed time
    private void refill() {
        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        double tokensToAdd = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    // Consume token
    public synchronized boolean allowRequest() {
        refill();

        if (tokens >= 1) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        return (int) tokens;
    }
}

class RateLimiter {

    // clientId -> TokenBucket
    private Map<String, TokenBucket> clients = new HashMap<>();

    private int limitPerHour = 1000;

    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(limitPerHour, limitPerHour));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry later)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "No requests made yet";
        }

        int remaining = bucket.getRemainingTokens();
        int used = limitPerHour - remaining;

        return "{used: " + used +
                ", limit: " + limitPerHour +
                ", remaining: " + remaining + "}";
    }
}

public class DistributedRateLimiter {

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String clientId = "abc123";

        // Simulate API requests
        for (int i = 0; i < 5; i++) {
            System.out.println(
                    "checkRateLimit(clientId=\"" + clientId + "\") → "
                            + limiter.checkRateLimit(clientId)
            );
        }

        // Show rate limit status
        System.out.println("\ngetRateLimitStatus(\"abc123\") → "
                + limiter.getRateLimitStatus(clientId));
    }
}