package com.example.magic.models;

import java.util.List;

/**
 * Класс, который хранит текущее состояние игры
 */
public class Game {

    // Жизни
    private int health;

    // Инвентарь
    private List<Item> items;

    // Последняя локация
    private Location lastLocation;

    // Текущий уровень
    private Level currentLevel;

    private Boolean helpForOldMan;

    private Boolean gameOver;

    private Boolean victory;

    public Boolean getVictory() {
        return victory;
    }

    public void setVictory(Boolean victory) {
        this.victory = victory;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Boolean getHelpForOldMan() {
        return helpForOldMan;
    }

    public void setHelpForOldMan(Boolean helpForOldMan) {
        this.helpForOldMan = helpForOldMan;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }
}


