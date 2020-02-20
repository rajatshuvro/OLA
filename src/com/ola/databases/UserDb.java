package com.ola.databases;

import com.ola.dataStructures.User;
import com.ola.luceneIndex.DocumentSearchIndex;
import com.ola.luceneIndex.ISearchDocument;

import java.io.IOException;
import java.util.*;

public class UserDb {
    private HashMap<Integer, User> _users;
    private int _maxId;
    private DocumentSearchIndex _searchIndex;
    private ArrayList<User> _newUsers;
    public static final int NewUserId=99999;

    public UserDb(Iterable<User> users){
        _users = new HashMap<>();
        for (User user: users) {
            if(user== null) continue;

            _users.put(user.Id, user);
            if(user.Id != NewUserId && user.Id > _maxId) _maxId = user.Id;
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
        ids.addAll(_users.keySet());
        return ids;
    }

    public String GetUserName(int userId) {
        if(_users.containsKey(userId)) return _users.get(userId).Name;
        return null;
    }

    private Random _rand = new Random();
    public int AddNewUser(String name, String role, String email, String phone) {
        var idIncrement = _rand.nextInt(20)+1;//preventing having a inc of 0

        var user = User.Create(_maxId+idIncrement, name, role, email, phone);
        if(user == null) return -1;//invalid user data
        _maxId+=idIncrement;
        _users.put(user.Id, user);
        _newUsers.add(user);
        return user.Id;
    }

    public DocumentSearchIndex GetSearchIndex() throws IOException {
        if(_searchIndex == null) BuildSearchIndex();
        return _searchIndex;
    }

    public Iterable<ISearchDocument> GetAllDocuments(){
        return new ArrayList<>(_users.values());
    }

    public void BuildSearchIndex() throws IOException {
        _searchIndex = new DocumentSearchIndex(GetAllDocuments());
    }

    public List<User> GetNewRecords(){
        if(_newUsers.size()==0) return null;
        var newUsers = new ArrayList<>(_newUsers);
        _newUsers.clear();
        return newUsers;
    }


}
