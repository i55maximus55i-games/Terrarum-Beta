package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Экран настроек
 */
class SettingsScreen implements Screen {

    private Texture back;
    private Terrarum game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font_16, font_24, font_32;
    private TextureAtlas atlas;
    private Skin skin;
    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private Sound sound;

    private float musicVolume;
    private float soundVolume;
    private boolean stickControl;

    private OrthographicCamera cam;
    private CheckBox controlHeading;

    SettingsScreen(Terrarum game) {
        this.game = game;
        cam = new OrthographicCamera();
        this.batch = game.batch;
        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
        back = new Texture("textures/back.jpg");
    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(800, 600, cam));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        skin = new Skin();
        atlas = new TextureAtlas("textures/textureUI.pack");
        skin.addRegions(atlas);

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
                game.setScreen(new MainMenuScreen(game));
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
                musicVolumeSlider.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        sound.play(musicVolumeSlider.getValue());
                    }
                });
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
                soundVolumeSlider.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        sound.play(soundVolumeSlider.getValue());
                    }
                });
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
                }
                else {
                    stickControl = true;
                    controlHeading.setText(game.bundle.get("stickLabel"));
                    game.updatePref(musicVolume, soundVolume, true);
                }
            }
        });

        Label soundFx = new Label(game.bundle.get("soundVolumeLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        Label musicFx = new Label(game.bundle.get("musicVolumeLabel"), new Label.LabelStyle(font_32, Color.WHITE));
        Label handle = new Label(game.bundle.get("controlLabel"), new Label.LabelStyle(font_32, Color.WHITE));

        tableTop.add(exit).size(72, 72).left();
        tableTop.add(label).center().expandX();

        table.add(musicFx).expandX().padTop(10);
        table.add(musicVolumeSlider).expandX().padTop(10);
        table.row();
        table.add(soundFx).expandX().padTop(10);
        table.add(soundVolumeSlider).expandX().padTop(10);
        table.row();
        table.add(handle).expandX().padTop(10);
        table.add(controlHeading).expandX().padTop(10);

        stage.addActor(tableTop);
        stage.addActor(table);
    }

    @Override
    public void render ( float delta){
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        stage.act(delta);
        stage.draw();
        stickControl = game.isStickControl();
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
        back.dispose();
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        sound.dispose();

        font_16.dispose();
        font_24.dispose();
        font_32.dispose();
    }
}
