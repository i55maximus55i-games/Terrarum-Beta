package ru.codemonkeystudio.terrarum.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.screens.GameScreen;
import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

/**
 * Игровой интерфейс
 */

public class Hud implements Disposable {
    public Stage stage;

    private float timer;
    private Terrarum game;
    private Label timeLabel;
    private Label timerLabel;
    private Label liveLabel;
    private Label livesLabel;
    private Label scoresLabel;
    private Label scoreLabel;
    private Skin skin;
    private TextureAtlas atlas;
    private Button pause;
    private Button.ButtonStyle pauseStyle;
    private final GameScreen screen;
    private Table table, table2;

    private BitmapFont font_16,font_24,font_32;
    private ImageButton icon;
    private TextButton continueButton;
    private TextButton mainMenuButton;
    private TextButton settingsButton;
    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private Sound sound;

    private float musicVolume;
    private float soundVolume;
    private boolean stickControl;
    private CheckBox controlHeading;

    public Hud(final Terrarum game, final GameScreen screen) {
        this.game = game;
        this.screen = screen;
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
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

        timeLabel = new Label(game.bundle.get("timeLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        timerLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        liveLabel = new Label(game.bundle.get("livesLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        livesLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        scoresLabel = new Label(game.bundle.get("scoreLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        scoreLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));

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
        table.add(scoresLabel).expandX().padTop(10);
        table.row();
        table.add().expandX();
        table.add(livesLabel).expandX();
        table.add(timerLabel).expandX();
        table.add(scoreLabel).expandX();

        stage.addActor(table);
    }

    public void update(float timer, int lives, int score) {
        this.timer = timer;
        int t = (int) timer;
        timerLabel.setText(String.format("%02d", t / 60) + ":" + (String.format("%02d", t % 60)));
        livesLabel.setText(Integer.toString(lives));
        scoreLabel.setText(Integer.toString(score));
    }

    public void pause() {
        timeLabel.setColor(Color.GRAY);
        timerLabel.setColor(Color.GRAY);
        liveLabel.setColor(Color.GRAY);
        livesLabel.setColor(Color.GRAY);
        scoresLabel.setColor(Color.GRAY);
        scoreLabel.setColor(Color.GRAY);

        pause.setVisible(false);

        table2 = new Table();
        table2.center();
        table2.setFillParent(true);

        ImageButton.ImageButtonStyle iconstyle = new ImageButton.ImageButtonStyle();
        iconstyle.up = skin.getDrawable("icon_paused");
        icon = new ImageButton(iconstyle);
        icon.setSize(364, 126);
        icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);


        TextButton.TextButtonStyle continueStyle = new TextButton.TextButtonStyle();
        continueStyle.font = font_24;
        continueStyle.up = skin.getDrawable("btn_default");
        continueStyle.over = skin.getDrawable("btn_active");
        continueStyle.down = skin.getDrawable("btn_pressed");
        continueStyle.pressedOffsetX = 1;
        continueStyle.pressedOffsetY = -1;
        continueButton = new TextButton(game.bundle.get("continueLabel"), continueStyle);
        continueButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.unpause();
            }
        });

        TextButton.TextButtonStyle settingsStyle = new TextButton.TextButtonStyle();
        settingsStyle.font = font_24;
        settingsStyle.up = skin.getDrawable("btn_default");
        settingsStyle.over = skin.getDrawable("btn_active");
        settingsStyle.down = skin.getDrawable("btn_pressed");
        settingsStyle.pressedOffsetX = 1;
        settingsStyle.pressedOffsetY = -1;
        settingsButton = new TextButton(game.bundle.get("settingsLabel"), settingsStyle);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                table.clear();
                table.remove();
                table2.clear();
                table2.remove();
                settings();
            }
        });


        TextButton.TextButtonStyle mainMenuStyle = new TextButton.TextButtonStyle();
        mainMenuStyle.font = font_24;
        mainMenuStyle.up = skin.getDrawable("btn_default");
        mainMenuStyle.over = skin.getDrawable("btn_active");
        mainMenuStyle.down = skin.getDrawable("btn_pressed");
        mainMenuStyle.pressedOffsetX = 1;
        mainMenuStyle.pressedOffsetY = -1;
        mainMenuButton = new TextButton(game.bundle.get("menuLabel"), mainMenuStyle);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                screen.musicPlayer.setPlaying(false);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table2.add(icon).size(364, 126).row();
        table2.add(continueButton).size(260, 90).row();
        table2.add(settingsButton).size(260, 90).row();
        table2.add(mainMenuButton).size(260, 90).row();

        stage.addActor(table2);

    }

    public void settings(){

        final Table table3 = new Table();
        table3.center();
        table3.setFillParent(true);

        final Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        Label label = new Label(game.bundle.get("settingsLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        label.setPosition(400, 600 - label.getHeight() / 2, 1);

        Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = skin.getDrawable("btn_left");
        exitStyle.down = skin.getDrawable("btn_left_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        Button exit = new Button(exitStyle);
        exit.setSize(72, 72);
        exit.setPosition(exit.getWidth() / 2, stage.getHeight() - exit.getHeight() / 2, 1);
        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());

                tableTop.clear();
                tableTop.remove();
                table3.clear();
                table3.remove();

                table2.add(icon).size(364, 126).row();
                table2.add(continueButton).size(260, 90).row();
                table2.add(settingsButton).size(260, 90).row();
                table2.add(mainMenuButton).size(260, 90).row();
                stage.addActor(table2);

            }
        });

        Slider.SliderStyle musicVolumeSliderStyle = new Slider.SliderStyle();
        musicVolumeSliderStyle.background = skin.getDrawable("slide");
        musicVolumeSliderStyle.knob = skin.getDrawable("knob");

        musicVolumeSlider = new Slider(0, 1f, 0.005f, false, musicVolumeSliderStyle);
        musicVolumeSlider.setValue(game.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.updatePref(musicVolumeSlider.getValue(), game.getSoundVolume(), stickControl);
                screen.setMusicVolume(musicVolumeSlider.getValue());
            }
        });
        musicVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(musicVolumeSlider.getValue());
            }
        });

        sound.play(soundVolume);
        Slider.SliderStyle soundVolumeSliderStyle = new Slider.SliderStyle();
        soundVolumeSliderStyle.background = skin.getDrawable("slide");
        soundVolumeSliderStyle.knob = skin.getDrawable("knob");


        soundVolumeSlider = new Slider(0, 1f, 0.005f, false, soundVolumeSliderStyle);
        soundVolumeSlider.setValue(game.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.updatePref(game.getMusicVolume(), soundVolumeSlider.getValue(), stickControl);
                screen.setSoundVolume(soundVolumeSlider.getValue());
            }
        });
        soundVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(soundVolumeSlider.getValue());
            }
        });

        CheckBox.CheckBoxStyle controlHeadingStyle = new CheckBox.CheckBoxStyle();
        controlHeadingStyle.font = font_32;

        controlHeading = new CheckBox(game.isStickControl() ? game.bundle.get("stickLabel") : game.bundle.get("touchLabel"), controlHeadingStyle);
        controlHeading.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                musicVolume = game.getMusicVolume();
                soundVolume = game.getSoundVolume();
                stickControl = game.isStickControl();
                sound.play(soundVolume);
                if (stickControl) {
                    stickControl = false;
                    controlHeading.setText(game.bundle.get("touchLabel"));
                    game.updatePref(musicVolume, soundVolume, false);
                    screen.setStickControl(false);
                }
                else {
                    stickControl = true;
                    controlHeading.setText(game.bundle.get("stickLabel"));
                    game.updatePref(musicVolume, soundVolume, true);
                    screen.setStickControl(true);
                }
            }
        });

        Label soundFx = new Label(game.bundle.get("soundVolumeLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        Label musicFx = new Label(game.bundle.get("musicVolumeLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        Label handle = new Label(game.bundle.get("controlLabel"), new Label.LabelStyle(font_32, Color.WHITE));

        tableTop.add(exit).size(72, 72).left();
        tableTop.add(label).center().expandX();

        table3.add(musicFx).expandX().padTop(10).center();
        table3.add(musicVolumeSlider).expandX().padTop(10).center();
        table3.row();
        table3.add(soundFx).expandX().padTop(10).center();
        table3.add(soundVolumeSlider).expandX().padTop(10).center();
        table3.row();
        table3.add(handle).expandX().padTop(10).center();
        table3.add(controlHeading).padTop(10).center().expandX().width(98).row();

        stage.addActor(tableTop);
        stage.addActor(table3);

    }

    public void resume() {
        timeLabel.setColor(Color.WHITE);
        timerLabel.setColor(Color.WHITE);
        liveLabel.setColor(Color.WHITE);
        livesLabel.setColor(Color.WHITE);
        scoresLabel.setColor(Color.WHITE);
        scoreLabel.setColor(Color.WHITE);
        
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
        table.add(scoresLabel).expandX().padTop(10);
        table.row();
        table.add().expandX();
        table.add(livesLabel).expandX();
        table.add(timerLabel).expandX();
        table.add(scoreLabel).expandX();

        stage.addActor(table);

        table2.clear();
        table2.remove();
    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        sound.dispose();

        font_16.dispose();
        font_24.dispose();
        font_32.dispose();
        skin.dispose();
    }

    public float getTimer() {
        return timer;
    }
}
