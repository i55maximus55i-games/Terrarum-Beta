package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.Enemy;
import ru.codemonkeystudio.terrarum.objects.Food;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.objects.Tail;
import ru.codemonkeystudio.terrarum.scenes.Hud;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;
import ru.codemonkeystudio.terrarum.tools.MusicPlayer;
import ru.codemonkeystudio.terrarum.tools.TerrarumContactListener;
import ru.codemonkeystudio.terrarum.tools.TerrarumControlHandler;

/**
 * Основной игровой экран
 */

public class GameScreen implements Screen {
    private final Terrarum game;

    private Sound eatSound;
    private Sound enemySound;

    private TerrarumControlHandler controlHandler;
    public MusicPlayer musicPlayer;

    private GameWorld gameWorld;
    private GameRenderer renderer;
    private Hud hud;

    private Player player;
    private ArrayList<Food> foodList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Tail> tail;
    private boolean paused;
    private float soundVolume;
    private int score;
    private float timer;
    private float foodEnemyTimer;
    private float destroyEnemyTimer;
    private int eat;

    GameScreen(Terrarum game) {
        this.game = game;
        timer = 0;
        foodEnemyTimer = 0;
        gameWorld = new GameWorld();
        eat = 0;

        paused = false;
        tail = new ArrayList<Tail>();
        renderer = new GameRenderer(game.batch, gameWorld, tail);
        controlHandler = new TerrarumControlHandler();
        musicPlayer = new MusicPlayer(game.getMusicVolume());

        player = new Player(gameWorld.getWorld(), controlHandler, game.isStickControl(), renderer.getRayHandler(), game.getSoundVolume());
        foodList = new ArrayList<Food>();
        enemyList = new ArrayList<Enemy>();
        addFoodAndEnemy();

        gameWorld.getWorld().setContactListener(new TerrarumContactListener(player));
        hud = new Hud(game, this);
        soundVolume = game.getSoundVolume();
        destroyEnemyTimer = 0;
        score = 0;
        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));
    }

    private void addFoodAndEnemy() {
        for (int i = 0; i < GameWorld.WORLD_SIZE; i++) {
            foodList.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
            enemyList.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 48));
        }
        for(int i = 0; i < GameWorld.WORLD_SIZE - 1; i++) {
            foodList.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 16, GameWorld.WORLD_SIZE * 64 - 16));
            enemyList.add(new Enemy(gameWorld.getWorld(), renderer.getRayHandler(), i * 64 + 48, GameWorld.WORLD_SIZE * 64 - 16));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0.16f, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        if (!paused) {
            update(delta);
        }
        hud.stage.draw();
    }

    private void update(float delta) {
        timer += delta;
        foodEnemyTimer += delta;
        destroyEnemyTimer += delta;
        if (foodEnemyTimer > 30) {
            foodEnemyTimer -= 30;
            addFoodAndEnemy();
        }
        musicPlayer.update();
        gameWorld.update(delta, player.getBody().getPosition());
        player.update(delta);
        int nearest = -1;
        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).isAlive()) {
                if (nearest == -1) {
                    nearest = i;
                }
                else {
                    if (player.getBody().getPosition().dst(foodList.get(i).getBody().getPosition()) < player.getBody().getPosition().dst(foodList.get(nearest).getBody().getPosition())) {
                        nearest = i;
                    }
                }
            }
        }
        renderer.update(delta, player.getBody().getPosition().x, player.getBody().getPosition().y, nearest != -1, (nearest == -1 ? new Vector2(0, 0) : controlHandler.vectorSinCos(foodList.get(nearest).getBody().getPosition().sub(player.getBody().getPosition()))));
        int alive = 0;
        for (int i = 0; i < foodList.size(); i++) {
            foodList.get(i).update(delta);
            if (foodList.get(i).isAlive() && foodList.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                foodList.get(i).die(gameWorld.getWorld());
                eatSound.play(game.getSoundVolume());
                eat++;
                score += player.getLives() * 10;
                if (eat >= 25) {
                    eat -= 25;
                    player.addLive();
                }
            }
            if (foodList.get(i).isAlive()) alive++;
        }
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).update(delta);
            if (enemyList.get(i).isAlive() && enemyList.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                enemyList.get(i).die(gameWorld.getWorld());
                enemySound.play(game.getSoundVolume());
                player.hit(false);
            }
        }
        hud.update(timer, player.getLives(), score);
        if (player.getLives() < 0) {
            lose();
        }
        tail.add(new Tail(20, player.getBody().getPosition().x, player.getBody().getPosition().y));

        Tail tailO;
        Iterator iterator = tail.iterator();
        while (iterator.hasNext()) {
            tailO = (Tail) iterator.next();
            tailO.update();
            if (tailO.getLive() <= 0) {
                iterator.remove();
            }
        }

        Food foodO;
        iterator = foodList.iterator();
        while (iterator.hasNext()) {
            foodO = (Food) iterator.next();
            if (foodO.getDeathTime() > 15) {
                foodO.destroy();
                iterator.remove();
            }
        }

        Enemy enemyO;
        iterator = enemyList.iterator();
        while (iterator.hasNext()) {
            enemyO = (Enemy) iterator.next();
            if (enemyO.getDeathTime() > 15) {
                enemyO.destroy();
                iterator.remove();
            }
        }

        if (destroyEnemyTimer > 45) {
            destroyEnemyTimer = 0;
            iterator = enemyList.iterator();
            while (iterator.hasNext()) {
                enemyO = (Enemy) iterator.next();
                enemyO.die(gameWorld.getWorld());
                enemyO.destroy();
                iterator.remove();
            }
        }
    }

    private void lose() {
        musicPlayer.setPlaying(false);
        game.setScreen(new LoseScreen(game, hud.getTimer(), score));
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        controlHandler.resize();
    }

    @Override
    public void pause() {
        if (!paused) {
            paused = true;
            hud.pause();
            musicPlayer.pause();
        }
    }

    public void unpause() {
        paused = false;
        hud.resume();
        musicPlayer.unpause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        player.dispose();
        controlHandler.dispose();
        gameWorld.dispose();
        hud.dispose();
        for (int i = 0; i < foodList.size(); i++) {
            foodList.get(i).dispose();
        }
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).dispose();
        }
        musicPlayer.dispose();
        eatSound.dispose();
        enemySound.dispose();
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
        player.setVolume(soundVolume);
    }

    public void setMusicVolume(float musicVolume) {
        musicPlayer.setVolume(musicVolume);
    }

    public void setStickControl(boolean stickControl) {
        player.setStickControl(stickControl);
    }
}
