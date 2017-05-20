package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Created by IOne on 23.04.2017.
 */
public class SettingsScreen implements Screen {

    private Terrarum game;
    private Stage stage;
    private TextButton Menu;
    private TextButton.TextButtonStyle MenuStyle;
    private SpriteBatch batch;
    private BitmapFont font_16, font_24, font_32;
    private Label label;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private Button Exit;
    private Button.ButtonStyle ExitStyle;
    private Sound sound;
    private Slider volumeSlider;


    private OrthographicCamera gamecam;
    private Viewport gamePort;

    public SettingsScreen(Terrarum game) {
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(800, 600, gamecam);

        batch = new SpriteBatch();
        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        this.game = game;

    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(800, 600, gamecam));
        Gdx.input.setInputProcessor(stage);

        label = new Label("Settings", new Label.LabelStyle(font_32, Color.WHITE));
        label.setPosition(400, 600 - label.getHeight() / 2, 1);
        stage.addActor(label);

        skin = new Skin();
        atlas = new TextureAtlas("textures/textureUI.pack");
        skin.addRegions(atlas);

        ExitStyle = new Button.ButtonStyle();
        ExitStyle.up = skin.getDrawable("btn_back");
        ExitStyle.down = skin.getDrawable("btn_back_pressed");
        ExitStyle.pressedOffsetX = 1;
        ExitStyle.pressedOffsetY = -1;

        Exit = new Button(ExitStyle);
        Exit.setSize(72, 72);
        Exit.setPosition(Exit.getWidth()/2, stage.getHeight() - Exit.getHeight()/2, 1);
        Exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.dispose();
                sound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(Exit);
    }

    @Override
    public void render ( float delta){
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gamecam.update();
        batch.setProjectionMatrix(gamecam.combined);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize ( int width, int height){
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {

    }
}
