package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private TextButton menu;
    private TextButton.TextButtonStyle menuStyle;
    private SpriteBatch batch;
    private BitmapFont font_16, font_24, font_32;
    private Label label;
    private TextureAtlas atlas;
    private Skin skin;
    private Button exit;
    private Button.ButtonStyle exitStyle;
    private Slider musicVolumeSlider;
    private Slider.SliderStyle musicVolumeSliderStyle;
    private Slider soundVolumeSlider;
    private Slider.SliderStyle soundVolumeSliderStyle;
    private Sound sound;

    private float musicVolume;
    private float soundVolume;
    private boolean stickControl;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Label MusicFx;
    private Label SoundFx;
    private CheckBox controlHeading;
    private CheckBox.CheckBoxStyle controlHeadingStyle;

    public SettingsScreen(Terrarum game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(800, 600, gamecam);
        this.batch = game.batch;
        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));


    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(800, 600, gamecam));
        Gdx.input.setInputProcessor(stage);

        label = new Label("Settings", new Label.LabelStyle(font_32, Color.WHITE));
        label.setPosition(400, 600 - label.getHeight() / 2, 1);
        stage.addActor(label);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        skin = new Skin();
        atlas = new TextureAtlas("textures/textureUI.pack");
        skin.addRegions(atlas);

        exitStyle = new Button.ButtonStyle();
        exitStyle.up = skin.getDrawable("btn_back");
        exitStyle.down = skin.getDrawable("btn_back_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        exit = new Button(exitStyle);
        exit.setSize(72, 72);
        exit.setPosition(exit.getWidth() / 2, stage.getHeight() - exit.getHeight() / 2, 1);
        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                game.setScreen(new MainMenuScreen(game));
            }
        });


        musicVolumeSliderStyle = new Slider.SliderStyle();
        musicVolumeSliderStyle.background = skin.getDrawable("slide");
        musicVolumeSliderStyle.knob = skin.getDrawable("knob");

        musicVolumeSlider = new Slider(0, 1f, 0.005f, false, musicVolumeSliderStyle);
        musicVolumeSlider.setSize(176, 24);
//        musicVolumeSlider.setPosition(500, 550);
        musicVolumeSlider.setValue(game.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.updatePref(musicVolumeSlider.getValue(), soundVolume, stickControl);
                musicVolumeSlider.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        sound.play(musicVolume);
                    }
                });
            }
        });

        sound.play(soundVolume);
        soundVolumeSliderStyle = new Slider.SliderStyle();
        soundVolumeSliderStyle.background = skin.getDrawable("slide");
        soundVolumeSliderStyle.knob = skin.getDrawable("knob");

        soundVolumeSlider = new Slider(0, 1f, 0.005f, false, soundVolumeSliderStyle);
        soundVolumeSlider.setSize(220, 30);
//        soundVolumeSlider.setPosition(500, 500);
        soundVolumeSlider.setValue(game.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.updatePref(musicVolume, soundVolumeSlider.getValue(), stickControl);
                soundVolumeSlider.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        sound.play(soundVolume);
                    }
                });
            }
        });

        controlHeadingStyle = new CheckBox.CheckBoxStyle();
        controlHeadingStyle.font = font_16;
        controlHeadingStyle.checkboxOff = skin.getDrawable("btn_default");
        controlHeadingStyle.checkboxOn = skin.getDrawable("btn_active");

        controlHeading = new CheckBox(game.isStickControl() ? "Stick" : "Touch", controlHeadingStyle);
        controlHeading.setChecked(game.isStickControl());
        controlHeading.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(controlHeading.isChecked()){
                    controlHeading.setText("Stick");
                    stickControl = true;
                }
                else {
                    controlHeading.setText("Touch");
                    stickControl = false;
                }
                game.updatePref(musicVolume, soundVolume, stickControl);
            }
        });

        SoundFx = new Label("Sound", new Label.LabelStyle(font_32, Color.WHITE));
        MusicFx = new Label("Music", new Label.LabelStyle(font_32, Color.WHITE));

        stage.addActor(exit);

        table.add(MusicFx).expandX().padTop(10);
        table.add(musicVolumeSlider).expandX().padTop(10);
        table.row();
        table.add(SoundFx).expandX().padTop(10);
        table.add(soundVolumeSlider).expandX().padTop(10);
        table.row();
        table.add(controlHeading);

        stage.addActor(table);
    }

    @Override
    public void render ( float delta){
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gamecam.update();
        batch.setProjectionMatrix(gamecam.combined);

        stage.act(delta);
        stage.draw();

        musicVolume = game.getMusicVolume();
        soundVolume = game.getSoundVolume();
        stickControl = game.isStickControl();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            stickControl = !stickControl;
            game.updatePref(musicVolume, soundVolume, stickControl);
        }
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
