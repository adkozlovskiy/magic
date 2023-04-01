package com.example.magic.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.magic.models.Game;
import com.example.magic.models.Item;
import com.example.magic.models.Level;
import com.example.magic.models.Location;
import com.example.magic.models.Transition;
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

    public MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    public void saveBackgroundImage(String uri) {
        preferences.edit().putString("background", uri).apply();
    }

    public String getBackgroundImage() {
        return preferences.getString("background", null);
    }

    public void setMusicEnabled(boolean enabled) {
        preferences.edit().putBoolean("music", enabled).apply();
    }

    public boolean isMusicEnabled() {
        return preferences.getBoolean("music", true);
    }

    public void newGame() {
        Game game = new Game();
        game.setHealth(3);
        game.setItems(new ArrayList<>());
        game.setLastTransition(
                new Transition(null, Location.HOME)
        );
        game.setCurrentLevel(Level.MUM);
        game.setHelpForOldMan(null);

        saveGame(game);
    }

    public void saveGame(Game game) {
        preferences.edit()
                .putString(GAME_KEY, new Gson().toJson(game))
                .apply();
        gameLiveData.postValue(game);
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

    public void setLastTransition(Transition transition) {
        Game game = getGame();
        game.setLastTransition(transition);
        saveGame(game);
    }

    public void rightLocation(Location from) {
        Location location = Location.MARKET;
        Game game = getGame();
        if (game.getLastTransition().getTo() == Location.MARKET) {
            location = Location.FOREST;
        } else if (game.getLastTransition().getTo() == Location.FOREST) {
            location = Location.CAVE;
        }
        setLastTransition(
                new Transition(from, location)
        );
    }

    public void leftLocation(Location from) {
        Location location = Location.HOME;
        Game game = getGame();
        if (game.getLastTransition().getTo() == Location.FOREST) {
            location = Location.MARKET;
        } else if (game.getLastTransition().getTo() == Location.CAVE) {
            location = Location.FOREST;
        }
        setLastTransition(
                new Transition(from, location)
        );
    }

    public void gameOver(boolean victory) {
        Game game = getGame();
        game.setVictory(victory);

        saveGame(game);
    }


    private static final String PREFERENCES_KEY = "preferences";
    private static final String GAME_KEY = "game";

}
