import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    int time; // minutes from start of day
    String account;

    public Transaction(int id, int amount, String merchant, int time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("findTwoSum(target=" + target + ") →");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("(" + other.id + ", " + t.id + ") → "
                        + other.amount + " + " + t.amount);
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with time window (60 minutes)
    public void findTwoSumTimeWindow(int target) {

        System.out.println("\nTwo-Sum within 1 hour:");

        for (int i = 0; i < transactions.size(); i++) {

            for (int j = i + 1; j < transactions.size(); j++) {

                Transaction a = transactions.get(i);
                Transaction b = transactions.get(j);

                if (Math.abs(a.time - b.time) <= 60 &&
                        a.amount + b.amount == target) {

                    System.out.println("(" + a.id + ", " + b.id + ")");
                }
            }
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        System.out.println("\nDuplicate Transactions:");

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate → amount=" +
                        list.get(0).amount + ", merchant=" +
                        list.get(0).merchant + " accounts: ");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }

                System.out.println();
            }
        }
    }

    // K-Sum (simple recursive)
    public void findKSum(int k, int target) {

        System.out.println("\nfindKSum(k=" + k + ", target=" + target + ")");

        backtrack(new ArrayList<>(), 0, k, target);
    }

    private void backtrack(List<Transaction> current,
                           int start, int k, int remaining) {

        if (current.size() == k && remaining == 0) {

            System.out.print("(");

            for (Transaction t : current) {
                System.out.print(t.id + " ");
            }

            System.out.println(")");
            return;
        }

        if (current.size() >= k) return;

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t);

            backtrack(current, i + 1,
                    k, remaining - t.amount);

            current.remove(current.size() - 1);
        }
    }
}

public class FinancialTransactionAnalyzer {

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "Store A", 600, "acc1"));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", 615, "acc2"));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", 630, "acc3"));
        analyzer.addTransaction(new Transaction(4, 500, "Store A", 700, "acc4"));

        analyzer.findTwoSum(500);

        analyzer.findTwoSumTimeWindow(500);

        analyzer.detectDuplicates();

        analyzer.findKSum(3, 1000);
    }
}