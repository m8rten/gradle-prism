package com.m8rten.gradle.prism.repository

import com.m8rten.gradle.prism.model.User
import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection

class UserRepository {

    final DB db

    final JacksonDBCollection<User, String> users

    UserRepository(DB db) {
        this.db = db
        users = JacksonDBCollection.wrap(db.getCollection("users"), User.class, String.class)
    }

    boolean contains(String userId) {
        users.findOneById(userId) != null
    }

    List<User> getAll() {
        users.find().sort(DBSort.desc("lastInvocation")).toArray();
    }

    void insert(User user){
        users.insert(user)
    }

    User get(String userName){
        users.findOneById(userName)
    }

    void update(User user){
        users.updateById(user.userId, user)
    }
}
