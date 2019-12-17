package com.yzchnb.neo4jdao.NodeEntity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.ArrayList;
import java.util.HashSet;

@NodeEntity
public class User {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "userId")
    private String userId;

    @Property(name = "profileName")
    private HashSet<String> profileNames;

    public User(String userId, HashSet<String> profileNames) {
        this.userId = userId;
        this.profileNames = profileNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashSet<String> getProfileNames() {
        return profileNames;
    }

    public void setProfileNames(HashSet<String> profileNames) {
        this.profileNames = profileNames;
    }
}
