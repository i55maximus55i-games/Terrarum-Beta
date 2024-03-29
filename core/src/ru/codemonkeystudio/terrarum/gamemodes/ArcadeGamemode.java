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
 * Реализация аркадного игрового режима
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

    //инициализация игрового режима
    @Override
    public void init(Terrarum game, GameRenderer renderer, GameWorld gameWorld, Player player, ArrayList<Food> foods, ArrayList<Enemy> enemies) {
        this.game = game;

        this.foods = foods;
        this.enemies = enemies;

        this.player = player;
        this.renderer = renderer;
        this.gameWorld = gameWorld;

        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));

        score = 0;
        timer = 0;
        eaten = 0;
        addFoodAndEnemyTimer = 0;
        addFoodAndEnemy = true;
    }

    //обновление объектов
    @Override
    public void update(float delta) {
        timer += delta;
        addFoodAndEnemyTimer += delta;

        Iterator iterator;
        Food foodO;
        iterator = foods.iterator();
        while (iterator.hasNext()) {
            foodO = (Food) iterator.next();
            foodO.update(delta, 20);
            if (foodO.getDeathTime() > 10) {
                foodO.destroy();
                iterator.remove();
            }
        }
        Enemy enemyO;
        iterator = enemies.iterator();
        while (iterator.hasNext()) {
            enemyO = (Enemy) iterator.next();
            enemyO.update(delta, 20);
            if (enemyO.getDeathTime() > 10) {
                enemyO.destroy();
                iterator.remove();
            }
        }

        int alive = 0;
        for (int i = 0; i < foods.size(); i++) {
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

    //добавление жёлтых спор в игровой мир
    private void addFood() {
        for (int i = 0; i < gameWorld.WORLD_SIZE; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), gameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
        }
        for(int i = 0; i < gameWorld.WORLD_SIZE - 1; i++) {
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 16, gameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    //добавление синих спор в игровой мир
    private void addEnemy() {
        if (player.getBody().getPosition().dst(16, 16) > player.getBody().getPosition().dst(gameWorld.WORLD_SIZE * 64 - 16, gameWorld.WORLD_SIZE * 64 - 16)) {
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), 16, 16));
        }
        else {
            enemies.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), gameWorld.WORLD_SIZE * 64 - 16, gameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    //добавление жёлтых и синих спор в игровой мир
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

    //проверка на окончание игры
    @Override
    public boolean isGameOver() {
        return player.getLives() < 0;
    }

    //действия при окочнании игры
    @Override
    public void endGame() {
        StatisticScreen.addResult("Arcade", timer, score);
        game.setScreen(new GameOverScreen("Arcade" ,game, timer, score));
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
        //выгрузка объектов из памяти
        eatSound.dispose();
        enemySound.dispose();
    }
}