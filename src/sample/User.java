package sample;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static String userName;
    private static String role;
    private static List<String> options = new ArrayList<>();

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        User.role = role;
    }

    public static List<String> getOptions() {
        return options;
    }

    public static void setOptions(List<String> options) {
        User.options = options;
    }
}
