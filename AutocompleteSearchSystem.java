import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}

class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    // query -> frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into Trie
    public void addQuery(String query, int freq) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + freq);

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfWord = true;
    }

    // Update frequency when searched
    public void updateFrequency(String query) {
        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
        addQuery(query, 0);
    }

    // Get top 10 suggestions
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        collectWords(node, prefix, results);

        // Min heap based on frequency
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(a) - frequencyMap.get(b));

        for (String word : results) {
            pq.offer(word);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> topResults = new ArrayList<>();

        while (!pq.isEmpty()) {
            topResults.add(pq.poll());
        }

        Collections.reverse(topResults);
        return topResults;
    }

    // DFS to collect words from Trie
    private void collectWords(TrieNode node, String prefix, List<String> result) {

        if (node.isEndOfWord) {
            result.add(prefix);
        }

        for (char c : node.children.keySet()) {
            collectWords(node.children.get(c), prefix + c, result);
        }
    }

    // Print suggestions with frequency
    public void displaySuggestions(String prefix) {

        List<String> suggestions = search(prefix);

        System.out.println("search(\"" + prefix + "\") →");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". \"" + s + "\" (" +
                    frequencyMap.get(s) + " searches)");
            rank++;
        }
    }
}

public class AutocompleteSearchSystem {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Initial search queries
        system.addQuery("java tutorial", 1234567);
        system.addQuery("javascript", 987654);
        system.addQuery("java download", 456789);
        system.addQuery("java 21 features", 1);

        // Search suggestions
        system.displaySuggestions("jav");

        // Update frequency
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("\nUpdated Frequency:");
        system.displaySuggestions("java");
    }
}