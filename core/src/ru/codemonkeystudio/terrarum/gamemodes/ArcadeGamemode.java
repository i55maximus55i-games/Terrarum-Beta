package ru.codemonkeystudio.terrarum.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Iterator;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.Enemy;
import ru.codemonkeystudio.terrarum.objects.Food;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;

/**
 * Created by maximus on 20.06.2017.
 */

public class ArcadeGamemode implements Gamemode {
    private Terrarum game;

    //звуки
    private Sound eatSound;
    private Sound enemySound;

    //вспомогательные объекты
    private GameRenderer renderer;

    //игровые объекты
    private GameWorld gameWorld;
    private Player player;
    private ArrayList<Food> foods;
    private ArrayList<Enemy> enemies;

    //счётчики
    private float timer;
    private float addFoodAndEnemyTimer;
    private int score;
    private int eaten;
    private boolean addFoodAndEnemy;

    @Override
    public void init(Terrarum game, GameRenderer gameRenderer, GameWorld gameWorld, Player player) {
        this.game = game;

        foods = new ArrayList<Food>();
        enemies = new ArrayList<Enemy>();

        renderer = gameRenderer;
        this.gameWorld = gameWorld;
        this.player = player;

        score = 0;
        eaten = 0;
        timer = 0;
        addFoodAndEnemyTimer = 0;
        addFoodAndEnemy = true;

        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));
    }

    @Override
    public void update(float delta) {
        timer += delta;
        addFoodAndEnemyTimer += delta;

        Iterator iterator;
        Food foodO;
        iterator = foods.iterator();
        while (iterator.hasNext()) {
            foodO = (Food) iterator.next();
            foodO.update(delta);
            if (foodO.getDeathTime() > 10) {
                foodO.destroy();
                iterator.remove();
            }
        }
        Enemy enemyO;
        iterator = enemies.iterator();
        while (iterator.hasNext()) {
            enemyO = (Enemy) iterator.next();
            enemyO.update(delta);
            if (enemyO.getDeathTime() > 10) {
                enemyO.destroy();
                iterator.remove();
            }
        }

        int alive = 0;
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).update(delta);
            if (foods.get(i).isAlive() && foods.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                foods.get(i).die(gameWorld.getWorld());
                eatSound.play(game.getSoundVolume());
                eaten++;
                score += player.getLives() * 10;
                if (eaten >= 25) {
                    eaten -= 25;
                    player.addLive();
                }
            }
            if (foods.get(i).isAlive()) alive++;
        }
        if (alive <= 0 && !addFoodAndEnemy) {
            addFood();
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(delta);
            if (enemies.get(i).isAlive() && enemies.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                enemies.get(i).die(gameWorld.getWorld());
                enemySound.play(game.getSoundVolume());
                player.hit(false);
            }
        }

        if (enemies.size() < 45 && !addFoodAndEnemy) {
            addEnemy();
        }

        if (addFoodAndEnemyTimer > 5 && addFoodAndEnemy) {
            addFoodAndEnemyTimer -= 5;
            if (enemies.size() < 45) {
                addFoodAndEnemy();
            }
            else {
                addFoodAndEnemy = false;
            }
        }
    }

    private void addFood() {
        for (int i = 0; i < GameWorld.WORLD_SIZE; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
        }
        for(int i = 0; i < GameWorld.WORLD_SIZE - 1; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 16, GameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    private void addEnemy() {
        if (player.getBody().getPosition().dst(16, 16) > player.getBody().getPosition().dst(GameWorld.WORLD_SIZE * 64 - 16, GameWorld.WORLD_SIZE * 64 - 16)) {
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), 16, 16));
        }
        else {
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, GameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    private void addFoodAndEnemy() {
        for (int i = 0; i < GameWorld.WORLD_SIZE; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 48));
        }
        for(int i = 0; i < GameWorld.WORLD_SIZE - 1; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 16, GameWorld.WORLD_SIZE * 64 - 16));
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 48, GameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    @Override
    public boolean isGameOver() {
        if (player.getLives() < 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void endGame() {

    }

    @Override
    public ArrayList<Food> getFoods() {
        return foods;
    }

    @Override
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public float getTimer() {
        return timer;
    }

    @Override
    public void dispose() {
        eatSound.dispose();
        enemySound.dispose();
    }
}