import java.util.*;

/**
 * This is class is the main class to use the operations from Repository.java within the console. 
 */
public class GitClient {
    private static final List<String> operations = new ArrayList<>();

    public static void main(String[] args) {
        Collections.addAll(operations, "create", "head", "history", "commit", "reset", "drop", "squash", "quit");
        Scanner scan = new Scanner(System.in);
        Map<String, Repository> repos = new HashMap<>();
        String choice = "";
        String name;

        System.out.println("Welcome to the Mini-Git test client!");
        System.out.println();

        while (!choice.equalsIgnoreCase("quit")) {
            System.out.println("Available repositories: ");
            for (Repository repo : repos.values()) {
                System.out.println("\t" + repo);
            }
            System.out.println("Operations: " + operations);
            System.out.print("Enter operation and repository: ");
            String[] input = scan.nextLine().split("\\s+");
            choice = input[0];
            if (input.length > 1) {
                name = input[1];
            }
            else {
                name = "";
            }
            while (!operations.contains(choice) || (!choice.equalsIgnoreCase("create") &&
                    !choice.equalsIgnoreCase("quit") && !repos.containsKey(name))) {
                System.out.println("  **ERROR**: Operation or repository not recognized.");
                System.out.print("Enter operation and repository: ");
                input = scan.nextLine().split("\\s+");
                choice = input[0];
                if (input.length > 1) {
                    name = input[1];
                }
                else {
                    name = "";
                }
            }

            Repository currRepo = repos.get(name);
            switch (choice.toLowerCase()) {
                case "create" -> {
                    if (currRepo != null) {
                        System.out.println("  **ERROR**: Repository with that name already exists.");
                    } 
                    else {
                        try {
                            Repository newRepo = new Repository(name);
                            repos.put(name, newRepo);
                            System.out.println("  New repository created: " + newRepo);
                        }
                        catch (IllegalArgumentException iae) {
                            System.out.println("Please enter a repository name.");
                        }
                    }
                }
                case "head" -> System.out.println(currRepo.getRepoHead());
                case "history" -> {
                    System.out.print("How many commits back? ");
                    int nHist = scan.nextInt();
                    scan.nextLine();
                    try {
                        System.out.println(currRepo.getHistory(nHist));
                    }
                    catch(IllegalArgumentException iae) {
                        System.out.println("Please enter a positive integer");
                    }
                }
                case "commit" -> {
                    System.out.print("Enter commit message: ");
                    String message = scan.nextLine();
                    System.out.println("  New commit: " + currRepo.commit(message));
                }
                case "reset" -> {
                    System.out.print("How many commits back? ");
                    int nReset = scan.nextInt();
                    scan.nextLine();
                    try {
                        currRepo.reset(nReset);
                        System.out.println("  New head: " + currRepo.getRepoHead());
                    }
                    catch(IllegalArgumentException iae) {
                        System.out.println("Please enter a positive integer");
                    }
                }
                case "drop" -> {
                    System.out.print("Enter ID to drop: ");
                    String idDrop = scan.nextLine();
                    String dropped = currRepo.drop(idDrop);
                    if (dropped == null) {
                        System.out.println("  No commit dropped!");
                    } 
                    else {
                        System.out.println("  Dropped " + dropped);
                    }
                }
                case "squash" -> {
                    System.out.print("Enter ID to squash: ");
                    String idSquash = scan.nextLine();
                    String squashed = currRepo.squash(idSquash);
                    if (squashed == null) {
                        System.out.println("  No commits squashed!");
                    } 
                    else {
                        System.out.println("  Squashed and removed " + squashed);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice.toLowerCase());
            }
            System.out.println();
        }
    }
}