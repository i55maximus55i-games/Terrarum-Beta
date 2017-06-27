package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Экран настроек
 */

class SettingsScreen implements Screen {
    //сцена содержащая элементы интерфейса
    private Stage stage;
    private OrthographicCamera camera;

    //звуки
    private Sound sound;

    SettingsScreen(final Terrarum game) {
        //инициализация звуков
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        //инициализания сцены, содержащей элементы интерфейса
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        Gdx.input.setInputProcessor(stage);

        //создание разметки заголовка
        Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        //создание разметки настроек
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //создание стиля для надписей
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font32;
        labelStyle.fontColor = Color.WHITE;

        //создание стиля для кнопки выхода в гланое меню
        Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = game.skin.getDrawable("btn_left");
        exitStyle.down = game.skin.getDrawable("btn_left_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        //создание стиля для настройки управления
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = game.font32;

        //создание стиля кнопки выбора языка
        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font24;

        //создание стиля для слайдеров громкости
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = game.skin.getDrawable("slide");
        sliderStyle.knob = game.skin.getDrawable("knob");

        //создание кнопки выхода в главное меню
        Button exit = new Button(exitStyle);
        exit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        //создание заголовочной надписи
        Label logo = new Label(game.bundle.get("settingsLabel"), labelStyle);

        //создание слайдера громкости музыки
        final Slider musicVolumeSlider = new Slider(0f, 1f, 0.005f, false, sliderStyle);
        musicVolumeSlider.setValue(game.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusicVolume(musicVolumeSlider.getValue());
            }
        });
        musicVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getMusicVolume());
            }
        });

        //создание слайдера громкости звуков
        final Slider soundVolumeSlider = new Slider(0f, 1f, 0.005f, false, sliderStyle);
        soundVolumeSlider.setValue(game.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSoundVolume(soundVolumeSlider.getValue());
            }
        });
        soundVolumeSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
            }
        });

        //создание переключателя метода управления
        final CheckBox controlSelect = new CheckBox(game.isStickControl() ? game.bundle.get("stickLabel") : game.bundle.get("touchLabel"), checkBoxStyle);
        controlSelect.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                game.setStickControl(!game.isStickControl());
                controlSelect.setText(game.isStickControl() ? game.bundle.get("stickLabel") : game.bundle.get("touchLabel"));
                sound.play(game.getSoundVolume());
            }
        });

        //создание кнопки выбора языка
        final TextButton langButton = new TextButton(game.bundle.get("LSLabel"), buttonStyle);
        langButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new LanguageSelectionScreen(game));
            }
        });

        //создание надписей настроек
        Label soundFx = new Label(game.bundle.get("soundVolumeLabel"), labelStyle);
        Label musicFx = new Label(game.bundle.get("musicVolumeLabel"), labelStyle);
        Label handle = new Label(game.bundle.get("controlLabel"), labelStyle);

        //добавление элементов в заголовочную разметку
        tableTop.add(exit).size(Gdx.graphics.getHeight() / 8).left();
        tableTop.add(logo).center().expandX();

        //добавление элементов в разметку настроек
        table.add(musicFx).expandX().padTop(10).center();
        table.add(musicVolumeSlider).expandX().padTop(10).center();
        table.row();
        table.add(soundFx).expandX().padTop(10).center();
        table.add(soundVolumeSlider).expandX().padTop(10).center();
        table.row();
        table.add(handle).expandX().padTop(10).center();
        table.add(controlSelect).padTop(10).center().expandX().width(98);
        table.row();
        table.add(langButton).size(Gdx.graphics.getHeight() * 13 / 36, Gdx.graphics.getHeight() / 8).center();

        //добавление разметок в сцену
        stage.addActor(tableTop);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //очистка экрана
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //обновление камеры
        camera.update();

        //обновление сцены
        stage.act(delta);
        stage.setDebugAll(false);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //обновление интерфейса при изменении размера экрана
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
        //выгрузка объектов из памяти
        sound.dispose();
        stage.dispose();
    }
}
