package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Created by vanchok on 21.05.2017.
 */
public class PauseScreen implements Screen {
    private SpriteBatch batch;
    private Terrarum game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //menu assets
    private Sound sound;
    private ImageButton icon;
    private ImageButton.ImageButtonStyle iconstyle;
    private Stage stage;
    private TextButton MainMenu, Continue, Achievements, Settings;
    private TextButton.TextButtonStyle MenuStyle, ContinueStyle, AchievementsStyle, SettingsStyle;
    private BitmapFont font_16,font_24,font_32;
    private Label label;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;

    public PauseScreen(Terrarum game) {
        stage = new Stage();
        this.game = game;
        batch = game.batch;
        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gamecam);

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(800,600, gamecam));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        iconstyle = new ImageButton.ImageButtonStyle();
        iconstyle.up = skin.getDrawable("icon_paused");

        ContinueStyle = new TextButton.TextButtonStyle();
        ContinueStyle.font = font_24;
        ContinueStyle.up = skin.getDrawable("btn_default");
        ContinueStyle.over = skin.getDrawable("btn_active");
        ContinueStyle.down = skin.getDrawable("btn_pressed");
        ContinueStyle.pressedOffsetX = 1;
        ContinueStyle.pressedOffsetY = -1;


        AchievementsStyle = new TextButton.TextButtonStyle();
        AchievementsStyle.font = font_24;
        AchievementsStyle.up = skin.getDrawable("btn_default");
        AchievementsStyle.over = skin.getDrawable("btn_active");
        AchievementsStyle.down = skin.getDrawable("btn_pressed");
        AchievementsStyle.pressedOffsetX = 1;
        AchievementsStyle.pressedOffsetY = -1;

        SettingsStyle = new TextButton.TextButtonStyle();
        SettingsStyle.font = font_24;
        SettingsStyle.up = skin.getDrawable("btn_default");
        SettingsStyle.over = skin.getDrawable("btn_active");
        SettingsStyle.down = skin.getDrawable("btn_pressed");
        SettingsStyle.pressedOffsetX = 1;
        SettingsStyle.pressedOffsetY = -1;

        MenuStyle = new TextButton.TextButtonStyle();
        MenuStyle.font = font_24;
        MenuStyle.up = skin.getDrawable("btn_default");
        MenuStyle.over = skin.getDrawable("btn_active");
        MenuStyle.down = skin.getDrawable("btn_pressed");
        MenuStyle.pressedOffsetX = 1;
        MenuStyle.pressedOffsetY = -1;

        icon = new ImageButton(iconstyle);
        icon.setSize(364, 126);
        icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

        Continue = new TextButton("Continue", ContinueStyle);
        Continue.setSize(260, 90);
        Continue.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
        Continue.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                sound.play();
                game.setScreen(new GameScreen(game));
            }
        });

        Achievements = new TextButton("Achievements", AchievementsStyle);
        Achievements.setSize(260, 90);
        Achievements.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*3, 1);
        Achievements.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				stage.dispose();
                sound.play();
//				game.setScreen(new Achievements(game));
            }
        });

        Settings = new TextButton("Settings", SettingsStyle);
        Settings.setSize(260, 90);
        Settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);
        Settings.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                sound.play();
                game.setScreen(new SettingsScreen(game));
            }
        });

        MainMenu = new TextButton("Main Menu", MenuStyle);
        MainMenu.setSize(260, 90);
        MainMenu.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
        MainMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                sound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(icon);
        stage.addActor(Continue);
        stage.addActor(Achievements);
        stage.addActor(Settings);
        stage.addActor(MainMenu);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gamecam.update();
        batch.setProjectionMatrix(gamecam.combined);

        stage.act(delta);
        stage.setDebugAll(false);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    }
}
