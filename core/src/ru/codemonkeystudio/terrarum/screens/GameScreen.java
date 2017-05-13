package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.tools.MusicPlayer;
import ru.codemonkeystudio.terrarum.tools.TerrarumControlHandler;

/**
 * Created by maximus on 05.05.2017.
 */

public class GameScreen implements Screen {
    private final Terrarum game;

    //box2D world

    //game assets

    //game objects
    private TerrarumControlHandler controlHandler;
    private MusicPlayer musicPlayer;

    private ShapeRenderer shapeRenderer;

    public GameScreen(Terrarum game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        controlHandler = new TerrarumControlHandler();
        musicPlayer = new MusicPlayer(0.2f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0.16f, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controlHandler.stickControl(shapeRenderer);
    }

    @Override
    public void resize(int width, int height) {

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
        musicPlayer.dispose();
        shapeRenderer.dispose();
    }
}
