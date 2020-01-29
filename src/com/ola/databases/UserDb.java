package com.ola.databases;

import com.ola.dataStructures.User;
import com.ola.luceneIndex.UserSearchIndex;

import java.io.IOException;
import java.util.*;

public class UserDb {
    private HashMap<Integer, User> _users;
    private int _maxId;
    private UserSearchIndex _searchIndex;
    private ArrayList<User> _newUsers;


    public UserDb(Iterable<User> users){
        _users = new HashMap<>();
        for (User user: users) {
            if(user.IsValid())
                _users.put(user.Id, user);
            else {
                System.out.println("Invalid User:");
                System.out.println(user.toString());
                continue;
            }
            if(user.Id > _maxId) _maxId = user.Id;
        }
        _newUsers = new ArrayList<>();
    }
    public int size(){
        return _users.size();
    }
    public User GetUser(int id){
        if(_users.containsKey(id)) return _users.get(id);
        return null;
    }
    public User GetUser(String name){
        for (var user: _users.values()) {
            if(user.Name.equals(name)) return user;
        }
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
        var rand = new Random(13);
        var idIncrement = rand.nextInt(20);

        if (! User.IsValid(_maxId+idIncrement, name, role, email, phone)) return -1;
        _maxId+=idIncrement;
        var user = new User(_maxId, name, role, email, phone);
        _users.put(user.Id, user);
        _newUsers.add(user);
        return user.Id;
        //todo: append to user file
    }

    public UserSearchIndex GetSearchIndex() throws IOException {
        if(_searchIndex == null) BuildSearchIndex();
        return _searchIndex;
    }

    public void BuildSearchIndex() throws IOException {
        _searchIndex = new UserSearchIndex(_users.values());
    }

    public List<User> GetNewRecords(){
        if(_newUsers.size()==0) return null;
        var newUsers = new ArrayList<>(_newUsers);
        _newUsers.clear();
        return newUsers;
    }


}
