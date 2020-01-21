package com.ola.databases;

import com.ola.dataStructures.User;
import java.util.HashMap;
import java.util.HashSet;

public class UserDb {
    private HashMap<Integer, User> _users;
    private int _maxId;

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

    public HashSet<Integer> GetIds() {
        var ids = new HashSet<Integer>();
        for (int id: _users.keySet()) {
            ids.add(id);
        }
        return ids;
    }

    public String GetUserName(int userId) {
        if(_users.containsKey(userId)) return _users.get(userId).Name;
        return null;
    }

    public int AddNewUser(String name, String role, String email, String phone) {
        if (! User.IsValid(_maxId+1, name, role, email, phone)) return -1;
        _maxId++;
        var user = new User(_maxId, name, role, email, phone);
        _users.put(user.Id, user);
        return user.Id;
        //todo: append to user file
    }
}
