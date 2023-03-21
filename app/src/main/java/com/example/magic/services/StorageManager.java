package com.example.magic.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.magic.models.Game;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.Location;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private Context context;

    public SharedPreferences preferences;

    public StorageManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public void newGame() {
        Game game = new Game();
        game.setHealth(3);
        game.setItems(new ArrayList<>());
        game.setLastLocation(Location.MARKET);
        game.setCurrentLevel(Level.MUM);
        game.setHelpForOldMan(null);

        saveGame(game);
    }

    public void saveGame(Game game) {
        preferences.edit()
                .putString(GAME_KEY, new Gson().toJson(game))
                .apply();
    }

    public Game getGame() {
        return new Gson().fromJson(
                preferences.getString(GAME_KEY, ""),
                Game.class
        );
    }

    public void updateHealth(int health) {
        Game game = getGame();
        game.setHealth(health);
        saveGame(game);
    }

    public void nextLevel() {
        Game game = getGame();
        Level[] levels = Level.values();

        int currentLevelIndex = 0;

        for (int i = 0; i < levels.length; i++) {
            if (levels[i] == game.getCurrentLevel()) {
                currentLevelIndex = i;
            }
        }

        int nextLevelIndex = currentLevelIndex + 1;

        if (currentLevelIndex == levels.length - 1) {
            game.setGameOver(true);
        } else {
            game.setCurrentLevel(
                    Level.values()[nextLevelIndex]
            );
        }
        saveGame(game);
    }

    public void addToInventory(Item item) {
        Game game = getGame();
        List<Item> inventory = game.getItems();

        inventory.add(item);
        saveGame(game);
    }

    public void removeForInventory(Item item) {
        Game game = getGame();
        List<Item> inventory = game.getItems();

        inventory.remove(item);
        saveGame(game);
    }

    public void setLastLocation(Location lastLocation) {
        Game game = getGame();
        game.setLastLocation(lastLocation);
        saveGame(game);
    }

    public void gameOver(boolean victory) {
        Game game = getGame();
        game.setVictory(victory);

        saveGame(game);
    }


    private static final String PREFERENCES_KEY = "preferences";
    private static final String GAME_KEY = "game";

}
