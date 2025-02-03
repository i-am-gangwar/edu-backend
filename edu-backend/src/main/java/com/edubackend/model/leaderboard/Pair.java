package com.edubackend.model.leaderboard;

import lombok.Data;


@Data
public class Pair<K, V> {
    private final K userName;
    private final V score;

    public K getUserName() {
        return userName;
    }

    public V getScore() {
        return score;
    }
}
