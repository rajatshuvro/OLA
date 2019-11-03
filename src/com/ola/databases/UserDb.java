package com.ola.databases;

import com.ola.dataStructures.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDb {
    private HashMap<Integer, User> _users;

    public UserDb(Iterable<User> users){
        _users = new HashMap<>();
        for (User user: users) {
            _users.put(user.Id, user);
        }
    }
    public int size(){
        return _users.size();
    }
    public User GetUser(int id){
        if(_users.containsKey(id)) return _users.get(id);
        return null;
    }
}
