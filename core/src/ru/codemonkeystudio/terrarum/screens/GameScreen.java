package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.codemonkeystudio.terrarum.Terrarum;
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
 * Created by maximus on 05.05.2017.
 */

public class GameScreen implements Screen {
    private final Terrarum game;

    private TerrarumControlHandler controlHandler;
    public MusicPlayer musicPlayer;

    private GameWorld gameWorld;
    private GameRenderer renderer;
    private Hud hud;

    private Player player;
    private ArrayList<Food> foodList;
    private ArrayList<Tail> tail;

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public GameScreen(Terrarum game) {
        this.game = game;
        gameWorld = new GameWorld();

        tail = new ArrayList<Tail>();
        renderer = new GameRenderer(game.batch, gameWorld, tail);
        controlHandler = new TerrarumControlHandler();
        musicPlayer = new MusicPlayer(game.getMusicVolume());

        player = new Player(gameWorld.getWorld(), controlHandler, game.isStickControl(), renderer.getRayHandler(), game.getSoundVolume());
        foodList = new ArrayList<Food>();
        for (int i = 0; i < GameWorld.WORLD_SIZE; i++) {
            foodList.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 16));
            foodList.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), GameWorld.WORLD_SIZE * 64 - 16, i * 64 + 48));
        }
        gameWorld.getWorld().setContactListener(new TerrarumContactListener(player));
        hud = new Hud(game.batch, game, this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0.16f, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        update(delta);
        hud.stage.draw();
    }

    private void update(float delta) {
        gameWorld.update(delta);
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
        renderer.update(delta, player.getBody().getPosition().x, player.getBody().getPosition().y, nearest != -1, (nearest == -1 ? new Vector2(0, 0) : controlHandler.vectorSum(foodList.get(nearest).getBody().getPosition().sub(player.getBody().getPosition()))));
        int alive = 0;
        for (int i = 0; i < foodList.size(); i++) {
            foodList.get(i).update(delta);
            if (foodList.get(i).isAlive() && foodList.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                foodList.get(i).die(gameWorld.getWorld(), game.getSoundVolume());
            }
            if (foodList.get(i).isAlive()) alive++;
        }
        hud.update(delta, player.getLives(), alive);
        if (alive <= 0) {
            win();
        }
        else if (player.getLives() < 0) {
            lose();
        }
        musicPlayer.update();
        tail.add(new Tail(15, player.getBody().getPosition().x, player.getBody().getPosition().y));
        Tail a;
        Iterator iterator = tail.iterator();
        while (iterator.hasNext()) {
            a = (Tail) iterator.next();
            a.update();
            if (a.getLive() <= 0) {
                iterator.remove();
            }
        }
    }

    private void lose() {
        musicPlayer.setPlaying(false);
        game.setScreen(new MainMenuScreen(game));
    }

    private void win() {
        musicPlayer.setPlaying(false);
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        controlHandler.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        renderer.dispose();
//        player.dispose();
//        controlHandler.dispose();
//        for (int i = 0; i < foodList.size(); i++) {
//            foodList.get(i).dispose();
//        }
//        musicPlayer.dispose();
    }
}
