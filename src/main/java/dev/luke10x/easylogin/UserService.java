package dev.luke10x.easylogin;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        User app1 = new User("Alice");
        User app2 = new User("Bob");
        User app3 = new User("Charlie");

        users.add(app1);
        users.add(app2);
        users.add(app3);

        return users;
    }
}
