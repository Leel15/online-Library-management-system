public class User {
    private static int userID = 0; 

    public static void setUserID(int id) {
        userID = id;
    }

    public static int getUserID() {
        if (userID == 0) { 
            throw new IllegalStateException("User ID is not set yet. Please log in.");
        }
        return userID;
    }
}
