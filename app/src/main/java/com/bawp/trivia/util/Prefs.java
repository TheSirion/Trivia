package com.bawp.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighScore(int currentScore) {
        int lastScore = preferences.getInt("high_score", 0);

        if (currentScore > lastScore) {
            // get new highscore and save it
            preferences.edit().putInt("high_score", currentScore).apply();
        }
    }

    public int getHighScore() {
        return preferences.getInt("high_score", 0);
    }

    public void setState(int index, int score) {
        preferences.edit().putInt("index_state", index).apply();
        preferences.edit().putInt("score", score).apply();
    }

    public int[] getState() {
        int index = preferences.getInt("index_state", 0);
        int score = preferences.getInt("score", 0);
        return new int[]{index, score};
    }
}
