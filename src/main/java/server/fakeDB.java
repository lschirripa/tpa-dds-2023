package server;

import java.util.HashMap;
import java.util.Map;

public class fakeDB {
  public static Map<String, String> users = new HashMap<>();
  static {
    // hardcoded list of users
    users.put("john", "password123");
    users.put("jane", "letmein");
  }
}
