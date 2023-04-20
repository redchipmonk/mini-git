import java.util.*;

/**
 * This class represents a mock repository in Git. It implements a linked list using an internal node class. This class
 * supports a subset of operations supported by real Git repositories.
 *
 * @author Alvin Le
 */

public class Repository {
    private final String name;
    private Commit head;
    private int count = 0;

    /**
     * Constructor that initializes and name repository
     * @param name String to name repository
     * @throws IllegalArgumentException exception is String is empty or null
     */
    public Repository(String name) throws IllegalArgumentException {
        this.name = name;
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter for the current id of the current head of repository
     * @return String ID of the current head of repository
     */
    public String getRepoHead() {
        if (head == null) {
            return null;
        }
        return this.head.id;
    }

    /**
     * Overridden toString() method to return a String representation of the repo by returning the name and current head
     * @return String representation of repo
     */
    @Override
    public String toString() {
        if (count == 0) {
            return name + " - No commits";
        }
        return name + " - Current head: " + head.toString();
    }

    /**
     * String that consists of the most recent number of commits in the repository
     * @param nHist int number of commits to return, if bigger than number of commits, returns all commits
     * @return String history of commits
     * @throws IllegalArgumentException exception if nHist is negative
     */
    public String getHistory(int nHist) throws IllegalArgumentException {
        if (nHist < 0) {
            throw new IllegalArgumentException();
        }
        if (count == 0) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        int counter = 0;
        if (nHist > count) {
            nHist = count;
        }
        Commit current = head;
        while (counter < nHist && current != null) {
            counter++;
            str.append(current).append("\n");
            current = current.past;
        }
        return str.toString();
    }

    /**
     * Adds a new commit to the repo becoming the current head
     * @param message String message of commit
     * @return new generated id of added commit
     */

    public String commit(String message) {
        head = new Commit(message, head);
        count++;
        return head.id;
    }

    /**
     * Deletes the most recent commits to be a certain number in the past
     * @param nReset int number of commits to delete, if bigger than number of commits, resets entire repository
     * @throws IllegalArgumentException exception if nReset is negative
     */
    public void reset(int nReset) throws IllegalArgumentException {
        if (nReset < 0) {
            throw new IllegalArgumentException();
        }
        if (nReset > count) {
            head = null;
            nReset = 0;
            count = 0;
        }
        int counter = 0;
        while (counter < nReset && head != null) {
            head = head.past;
            counter++;
        }
        count -= nReset;

    }

    /**
     * Removes the commit with the target id
     * @param idDrop String id of commit to delete
     * @return id of dropped commit
     */

    public String drop(String idDrop) {
        if (head.id.equals(idDrop)) {
            head = head.past;
            count--;
            return idDrop;
        }
        Commit current = head;
        Commit previous = null;
        boolean temp = true;
        while (current.past != null && temp) {
            previous = current;
            current = current.past;
            if (current.id.equals(idDrop)) {
                temp = false;
            }


        }
        if (temp) {
            return null;
        }
        previous.past = current.past;
        count--;
        return idDrop;
    }

    /**
     * Creates a new commit with a unique id that combines a targeted commit with the one before it. If target commit
     * has no commit before it, nothing is done.
     * @param idSquash String target id to squash
     * @return String id of the earlier of the two replaces commits
     */
    public String squash(String idSquash) {
        Commit current = head;
        Commit previous = null;
        boolean temp = true;
        while (current != null && temp) {
            if (current.id.equals(idSquash)) {
            temp = false;
        }
            previous = current;
            current = current.past;

        }
        if (temp || current == null) return null;
        current.message = "SQUASHED: " + previous.message + " / " + current.message;
        String s = current.id;
        current.id = Commit.getNewId();
        drop(s);
        drop(previous.id);
        return s;
    }


    /**
     * Internal node class which holds a unique generated id, a commit message, and a link to the previous commit
     */
    public static class Commit {
        public String id;
        public String message;
        public Commit past;

        /**
         * Constructor to add new commit before a previous commit
         * @param message String commit message
         * @param past Commit previous commit
         */
        public Commit(String message, Commit past) {
            this.id = getNewId();
            this.message = message;
            this.past = past;
        }

        /**
         * Overloaded constructor that initialized new commit with no past
         * @param message String commit message
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Overridden toString() to return a string representation of commit
         * @return String representation of commit
         */
        public String toString() {
            return id + ": " + message;
        }

        /**
         * Static method to generate new id
         * @return String id
         */
        private static String getNewId() {
            return UUID.randomUUID().toString();
        }
    }
}