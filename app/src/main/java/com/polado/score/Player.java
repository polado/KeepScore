package com.polado.score;

/**
 * Created by PolaDo on 8/14/2017.
 */

public class Player {
    String name;
    int score;
    int id;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Player(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
