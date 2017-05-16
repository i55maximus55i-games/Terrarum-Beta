package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import box2dLight.PointLight;
import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;
import ru.codemonkeystudio.terrarum.tools.MusicPlayer;
import ru.codemonkeystudio.terrarum.tools.TerrarumControlHandler;

/**
 * Created by maximus on 05.05.2017.
 */

public class GameScreen implements Screen {
    private final Terrarum game;

    private TerrarumControlHandler controlHandler;
    private MusicPlayer musicPlayer;

    private GameWorld gameWorld;
    private GameRenderer renderer;

    private Player player;
    private PointLight playerLight;

    public GameScreen(Terrarum game) {
        this.game = game;
        gameWorld = new GameWorld();

        renderer = new GameRenderer(game.batch, gameWorld);
        controlHandler = new TerrarumControlHandler();
        musicPlayer = new MusicPlayer(0f);

        player = new Player(gameWorld.getWorld(), controlHandler, true);
        playerLight = new PointLight(renderer.getRayHandler(), 500, Color.RED, 100, 20, 20);
        playerLight.attachToBody(player.getBody());
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
    }

    private void update(float delta) {
        gameWorld.update(delta);
        player.update(delta);
        renderer.update(delta, player.getBody().getPosition().x, player.getBody().getPosition().y);
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
        musicPlayer.dispose();
    }
}
