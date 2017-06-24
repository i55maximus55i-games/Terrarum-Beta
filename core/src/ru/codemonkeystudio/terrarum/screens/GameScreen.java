package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.gamemodes.Gamemode;
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

    private Gamemode gm;

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
    private boolean paused;
    private boolean ludum;

    //инициализация игры
    GameScreen(Terrarum game, Gamemode gamemode, boolean ludum) {
        this.game = game;

        this.ludum = ludum;
        paused = false;

        tails = new ArrayList<Tail>();
        foods = new ArrayList<Food>();
        enemies = new ArrayList<Enemy>();

        controlHandler = new TerrarumControlHandler();
        hud = new Hud(game, this);
        musicPlayer = new MusicPlayer(game.getMusicVolume());

        gm = gamemode;

        gameWorld = new GameWorld(gm.getWorldSize());
        renderer = new GameRenderer(game.batch, gameWorld, tails, gameWorld.WORLD_SIZE);
        player = new Player(gameWorld.getWorld(), controlHandler, game.isStickControl(), renderer.getRayHandler(), game.getSoundVolume());
        gameWorld.getWorld().setContactListener(new TerrarumContactListener(player));

        gm.init(game, renderer, gameWorld, player, foods, enemies);
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
        if (!ludum) {
            hud.stage.draw();
        }
    }

    private void update(float delta) {
        gm.update(delta);

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
        gameWorld.update(delta, player.getBody().getPosition(), !ludum);
        renderer.update(delta, player.getBody().getPosition().x, player.getBody().getPosition().y, nearest != -1, (nearest == -1 ? new Vector2(0, 0) : controlHandler.vectorSinCos(foods.get(nearest).getBody().getPosition().sub(player.getBody().getPosition()))), game.isStickControl());
        hud.update(gm.getTimer(), player.getLives(), gm.getScore());

        //обновление хвоста
        tails.add(new Tail(30, player.getBody().getPosition().x, player.getBody().getPosition().y));
        Tail tailO;
        Iterator iterator = tails.iterator();
        while (iterator.hasNext()) {
            tailO = (Tail) iterator.next();
            tailO.update();
            if (tailO.getLive() <= 0) {
                iterator.remove();
            }
        }

        if (gm.isGameOver()) {
            gameOver();
        }
    }

    private void gameOver() {
        musicPlayer.setPlaying(false);
        gm.endGame();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        controlHandler.resize();
        hud.resize(width, height);
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
        gm.dispose();
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
