package com.example.Szaman.model;

import com.example.Szaman.dataBaseConnection.DatabaseConnector;

import java.util.Comparator;

public class UserComparator implements Comparator<User> {
    DatabaseConnector databaseConnector;
    // porównuje użytkowników pod względem ich loginów
    @Override
    public int compare(User user1, User user2) {
        String login1 = user1.getLogin();
        String login2 = user1.getLogin();
        return login1.compareTo(login2);
    }

}
