package com.example.avocado1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
    public static User u;
    private String id;
    private String email;
    private String password;
    private String username;
    private List<String> Preferences;
    private Map<String,Movie> followingMovies;
    private Map<String,TvShow> followingTVshows ;


    public User() {
    }

    public User(String id, String email, String password, String userName) {

    }

    public User(String id, String email, String password, String userName, ArrayList<String> preferences, Map<String, Movie> favmovies, Map<String, TvShow> favTVshows) {
        this.id = id;
        this.email = email;
        this.password = password;
        username = userName;
        Preferences = preferences;
        followingMovies= favmovies;
        followingTVshows= favTVshows;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPreferences() {
        return Preferences;
    }

    public void setPreferences(List<String> preferences) {
        Preferences = preferences;
    }

    public Map<String, Movie> getFollowingMovies() {
        return followingMovies;
    }

    public Map<String, TvShow> getFollowingTVshows() {
        return followingTVshows;
    }

    public void setFollowingMovies(Map<String, Movie> followingMovies) {
        this.followingMovies = followingMovies;
    }

    public void setFollowingTVshows(Map<String, TvShow> followingTVshows) {
        this.followingTVshows = followingTVshows;
    }
}



