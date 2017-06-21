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
import ru.codemonkeystudio.terrarum.screens.GameOverScreen;
import ru.codemonkeystudio.terrarum.screens.StatisticScreen;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;

/**
 * Created by maximus on 21.06.2017.
 */

public class ClassicGamemode implements Gamemode {
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
    private int score;

    @Override
    public void init(Terrarum game, GameRenderer renderer, GameWorld gameWorld, Player player, ArrayList<Food> foods, ArrayList<Enemy> enemies) {
        this.game = game;

        this.renderer = renderer;
        this.gameWorld = gameWorld;

        this.player = player;
        this.foods = foods;
        this.enemies = enemies;

        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));

        score = 0;
        timer = 0;
        addFoodAndEnemy();
    }

    @Override
    public void update(float delta) {
        timer += delta;

        Iterator iterator;
        Food foodO;
        iterator = foods.iterator();
        while (iterator.hasNext()) {
            foodO = (Food) iterator.next();
            foodO.update(delta);
        }
        Enemy enemyO;
        iterator = enemies.iterator();
        while (iterator.hasNext()) {
            enemyO = (Enemy) iterator.next();
            enemyO.update(delta);
        }


        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).update(delta);
            if (foods.get(i).isAlive() && foods.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                foods.get(i).die(gameWorld.getWorld());
                eatSound.play(game.getSoundVolume());
                score += player.getLives() * 10;
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(delta);
            if (enemies.get(i).isAlive() && enemies.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                enemies.get(i).die(gameWorld.getWorld());
                enemySound.play(game.getSoundVolume());
                player.hit(false);
            }
        }
    }

    @Override
    public boolean isGameOver() {
        boolean isOver = false;
        if (player.getLives() < 0) {
            isOver = true;
        }
        int alive = 0;
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).isAlive()) {
                alive++;
            }
        }
        if (alive == 0) {
            isOver = true;
        }
        return isOver;
    }

    @Override
    public void endGame() {
        int score = 0;
        if (timer < 150f && player.getLives() >= 0) {
            score = (int) ((150 - timer) * 50);
        }
        game.setScreen(new GameOverScreen("Classic", game, timer, this.score + score));
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
    public int getWorldSize() {
        return 8;
    }

    @Override
    public void dispose() {
        eatSound.dispose();
        enemySound.dispose();
    }

    private void addFoodAndEnemy() {
        for (int i = 0; i < gameWorld.WORLD_SIZE; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), gameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), gameWorld.WORLD_SIZE * 64 - 16, i * 64 + 48));
        }
        for(int i = 0; i < gameWorld.WORLD_SIZE - 1; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 16, gameWorld.WORLD_SIZE * 64 - 16));
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 48, gameWorld.WORLD_SIZE * 64 - 16));
        }
    }
}
