package com.spacelapse;

/**
 * GameSession you get server details from here
 */
public class GameSession {

    public int playerCount;
    public int score;
    public int wave;

    public GameSession(int playerCount, int score, int wave) {
        this.playerCount = playerCount;
        this.score = score;
        this.wave = wave;
    }
}
