package dao;

public class Category {
    private int id;
    private int userID;
    private String name;

    public Category(int id, int userID, String name) {
        this.id = id;
        this.userID = userID;
        this.name = name;
    }

    public Category(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
