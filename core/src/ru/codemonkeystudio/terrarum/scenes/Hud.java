package ru.codemonkeystudio.terrarum.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.screens.GameScreen;
import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

/**
 * Created by maximus on 13.05.2017.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private float timer;
    private Terrarum game;
    private Label timeLabel;
    private Label timerLabel;
    private Label liveLabel;
    private Label livesLabel;
    private Label foodLabel;
    private Label foodsLabel;
    private Skin skin;
    private TextureAtlas atlas;
    private Image heart_alive, heart_dead;
    private Button pause;
    private Button.ButtonStyle pauseStyle;
    private Sound sound;
    private final GameScreen screen;
    private Table table;

    private BitmapFont font_16,font_24,font_32;
    private ImageButton icon;
    private TextButton continueButton;
    private TextButton mainMenuButton;

    public Hud(SpriteBatch batch, final Terrarum game, final GameScreen screen) {
        this.game = game;
        this.screen = screen;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label("Time", new Label.LabelStyle(font_32, Color.WHITE));
        timerLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        liveLabel = new Label("Lives", new Label.LabelStyle(font_32, Color.WHITE));
        livesLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        foodLabel = new Label("Food", new Label.LabelStyle(font_32, Color.WHITE));
        foodsLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        heart_alive = new Image(skin, "icon_heart_alive");
        heart_dead = new Image(skin, "icon_heart_dead");
        heart_alive.setSize(60, 60);

        pauseStyle = new Button.ButtonStyle();
        pauseStyle.up = skin.getDrawable("btn_pause");
        pauseStyle.down = skin.getDrawable("btn_pause_pressed");
        pauseStyle.pressedOffsetX = 1;
        pauseStyle.pressedOffsetY = -1;

        pause = new Button(pauseStyle);
        pause.setSize(72, 142);
        pause.setPosition(pause.getWidth()/2, stage.getHeight() - pause.getHeight()/2, 1);
        pause.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.pause();
//                screen.musicPlayer.setPlaying(false);
//                game.setScreen(new PauseScreen(game));
            }
        });


        table.add(pause).expandX().padTop(10).size(72, 72);
        table.add(liveLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(foodLabel).expandX().padTop(10);
        table.row();
        table.add().expandX();
        table.add(livesLabel).expandX();
        table.add(timerLabel).expandX();
        table.add(foodsLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta, int lives, int food) {
        timer += delta;
        int t = (int) timer;
        timerLabel.setText(String.format("%02d", t / 60) + ":" + (String.format("%02d", t % 60)));
        livesLabel.setText(Integer.toString(lives));
        foodsLabel.setText(Integer.toString(food));
    }

    public void pause() {
        timeLabel.setColor(Color.GRAY);
        timerLabel.setColor(Color.GRAY);
        liveLabel.setColor(Color.GRAY);
        livesLabel.setColor(Color.GRAY);
        foodLabel.setColor(Color.GRAY);
        foodsLabel.setColor(Color.GRAY);

        pause.setVisible(false);

        ImageButton.ImageButtonStyle iconstyle = new ImageButton.ImageButtonStyle();
        iconstyle.up = skin.getDrawable("icon_paused");
        icon = new ImageButton(iconstyle);
        icon.setSize(364, 126);
        icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);
        stage.addActor(icon);

        TextButton.TextButtonStyle continueStyle = new TextButton.TextButtonStyle();
        continueStyle.font = font_24;
        continueStyle.up = skin.getDrawable("btn_default");
        continueStyle.over = skin.getDrawable("btn_active");
        continueStyle.down = skin.getDrawable("btn_pressed");
        continueStyle.pressedOffsetX = 1;
        continueStyle.pressedOffsetY = -1;
        continueButton = new TextButton("Continue", continueStyle);
        continueButton.setSize(260, 90);
        continueButton.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
        continueButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.unpause();
            }
        });
        stage.addActor(continueButton);

        TextButton.TextButtonStyle mainMenuStyle = new TextButton.TextButtonStyle();
        mainMenuStyle.font = font_24;
        mainMenuStyle.up = skin.getDrawable("btn_default");
        mainMenuStyle.over = skin.getDrawable("btn_active");
        mainMenuStyle.down = skin.getDrawable("btn_pressed");
        mainMenuStyle.pressedOffsetX = 1;
        mainMenuStyle.pressedOffsetY = -1;
        mainMenuButton = new TextButton("Main Menu", mainMenuStyle);
        mainMenuButton.setSize(260, 90);
        mainMenuButton.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*3, 1);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.musicPlayer.setPlaying(false);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(mainMenuButton);
    }

    public void resume() {
        timeLabel.setColor(Color.WHITE);
        timerLabel.setColor(Color.WHITE);
        liveLabel.setColor(Color.WHITE);
        livesLabel.setColor(Color.WHITE);
        foodLabel.setColor(Color.WHITE);
        foodsLabel.setColor(Color.WHITE);
        
        pauseStyle = new Button.ButtonStyle();
        pauseStyle.up = skin.getDrawable("btn_pause");
        pauseStyle.down = skin.getDrawable("btn_pause_pressed");
        pauseStyle.pressedOffsetX = 1;
        pauseStyle.pressedOffsetY = -1;
        pause = new Button(pauseStyle);
        pause.setSize(72, 72);
        pause.setPosition(pause.getWidth()/2, stage.getHeight() - pause.getHeight()/2, 1);
        pause.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.pause();
            }
        });

        table.clear();
        table.remove();
        table.add(pause).expandX().padTop(10).size(72);
        table.add(liveLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(foodLabel).expandX().padTop(10);
        table.row();
        table.add().expandX();
        table.add(livesLabel).expandX();
        table.add(timerLabel).expandX();
        table.add(foodsLabel).expandX();

        stage.addActor(table);

        icon.remove();
        continueButton.remove();
        mainMenuButton.remove();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
