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
 * Created by maximus on 05.06.2017.
 */

public class GameScreen implements Screen {
    private final Terrarum game;

    //звуки
    private Sound eatSound;
    private Sound enemySound;

    //вспомогательные объекты
    private TerrarumControlHandler controlHandler;
    public MusicPlayer musicPlayer;
    private GameRenderer renderer;

    //игровые объекты
    private GameWorld gameWorld;
    private Hud hud;
    private Player player;
    private ArrayList<Tail> tails;
    private ArrayList<Food> foods;
    private ArrayList<Enemy> enemies;

    //счётчики
    private int score;
    private float timer;
    private float addFoodAndEnemyTimer;
    private int eaten;
    private boolean addFoodAndEnemy;
    private boolean paused;

    //инициализация игры
    public GameScreen(Terrarum game) {
        this.game = game;

        score = 0;
        timer = 0;
        eaten = 0;
        addFoodAndEnemyTimer = 0;
        addFoodAndEnemy = true;
        paused = false;

        tails = new ArrayList<Tail>();
        foods = new ArrayList<Food>();
        enemies = new ArrayList<Enemy>();

        gameWorld = new GameWorld();
        controlHandler = new TerrarumControlHandler();
        renderer = new GameRenderer(game.batch, gameWorld, tails);
        hud = new Hud(game, this);
        musicPlayer = new MusicPlayer(game.getMusicVolume());

        player = new Player(gameWorld.getWorld(), controlHandler, game.isStickControl(), renderer.getRayHandler(), game.getSoundVolume());
        gameWorld.getWorld().setContactListener(new TerrarumContactListener(player));

        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0.16f, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!paused) {
            update(delta);
        }
        else {
            renderer.update(false);
        }
        renderer.render();
        hud.stage.draw();
    }

    private void update(float delta) {
        timer += delta;
        addFoodAndEnemyTimer += delta;

        //проверка окончания игры
        if (player.getLives() < 0) {
            lose();
        }

        int nearest = -1;
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).isAlive()) {
                if (nearest == -1) {
                    nearest = i;
                }
                else {
                    if (player.getBody().getPosition().dst(foods.get(i).getBody().getPosition()) < player.getBody().getPosition().dst(foods.get(nearest).getBody().getPosition())) {
                        nearest = i;
                    }
                }
            }
        }

        //обновление объектов
        musicPlayer.update();
        player.update(delta);
        gameWorld.update(delta, player.getBody().getPosition());
        renderer.update(delta, player.getBody().getPosition().x, player.getBody().getPosition().y, nearest != -1, (nearest == -1 ? new Vector2(0, 0) : controlHandler.vectorSinCos(foods.get(nearest).getBody().getPosition().sub(player.getBody().getPosition()))), game.isStickControl());
        hud.update(timer, player.getLives(), score);

        Iterator iterator;
        //обновление хвоста
        tails.add(new Tail(20, player.getBody().getPosition().x, player.getBody().getPosition().y));
        Tail tailO;
        iterator = tails.iterator();
        while (iterator.hasNext()) {
            tailO = (Tail) iterator.next();
            tailO.update();
            if (tailO.getLive() <= 0) {
                iterator.remove();
            }
        }


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
        eatSound.dispose();
        enemySound.dispose();
        controlHandler.dispose();
        musicPlayer.dispose();
        renderer.dispose();
        gameWorld.dispose();
        hud.dispose();
        player.dispose();
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).dispose();
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).dispose();
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

    public void setSoundVolume(float soundVolume) {
        player.setVolume(soundVolume);
    }

    public void setMusicVolume(float musicVolume) {
        musicPlayer.setVolume(musicVolume);
    }

    public void setStickControl(boolean stickControl) {
        player.setStickControl(stickControl);
    }
}
