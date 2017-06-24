package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.gamemodes.LudumGamemode;

/**
 * Экран главного меню
 */

public class MainMenuScreen implements Screen {
    //сцена содержащая элементы интерфейса
    private Stage stage;
    private OrthographicCamera camera;

    //звуки
    private Sound sound;

    public MainMenuScreen(final Terrarum game) {
        //инициализация звуков
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        //инициализания сцены, содержащей элементы интерфейса
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(800, 600, camera));
        Gdx.input.setInputProcessor(stage);

        //создание разметки
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //инициализация стиля для логотипа
        ImageButton.ImageButtonStyle logoStyle = new ImageButton.ImageButtonStyle();
        logoStyle.up = game.skin.getDrawable("icon_terrarum");

        //инициализация стиля для кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font24;
        textButtonStyle.up = game.skin.getDrawable("btn_default");
        textButtonStyle.over = game.skin.getDrawable("btn_active");
        textButtonStyle.down = game.skin.getDrawable("btn_pressed");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;

        //создание логотипа
        ImageButton logo = new ImageButton(logoStyle);

        //создание кнопок и назначение действия при нажатии
        TextButton newGame = new TextButton(game.bundle.get("newGameLabel"), textButtonStyle);
        newGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new GamemodeSelection(game));
            }
        });
        TextButton statistic = new TextButton(game.bundle.get("statisticLabel"), textButtonStyle);
        statistic.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new StatisticScreen(game));
            }
        });
        TextButton settings = new TextButton(game.bundle.get("settingsLabel"), textButtonStyle);
        settings.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new SettingsScreen(game));
            }
        });
        TextButton exit = new TextButton(game.bundle.get("exitLabel"), textButtonStyle);
        exit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sound.play(game.getSoundVolume());
                stage.dispose();
                Gdx.app.exit();
            }
        });

        //добавление элементов интерфейса в разметку
        table.add(logo).size(364, 126).row();
        table.add(newGame).size(260, 90).row();
        table.add(statistic).size(260, 90).row();
        table.add(settings).size(260, 90).row();
        table.add(exit).size(260, 90).row();

        //добавление разметки в сцену
        stage.addActor(table);
    }

    //old38 easter egg
    MainMenuScreen(final Terrarum game, final boolean ludum) {
        stage = new Stage();
        camera = new OrthographicCamera();

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        stage = new Stage(new FitViewport(800,600, camera));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        ImageButton.ImageButtonStyle iconstyle = new ImageButton.ImageButtonStyle();
        iconstyle.up = game.skin.getDrawable("Terrarum");

        TextButton.TextButtonStyle newGameStyle = new TextButton.TextButtonStyle();
        newGameStyle.font = game.font24;
        newGameStyle.up = game.skin.getDrawable("newGame_ps");
        newGameStyle.down = game.skin.getDrawable("newGame_ac");
        newGameStyle.pressedOffsetX = 1;
        newGameStyle.pressedOffsetY = -1;

        TextButton.TextButtonStyle statisticStyle = new TextButton.TextButtonStyle();
        statisticStyle.font = game.font24;
        statisticStyle.up = game.skin.getDrawable("achievments_ps");
        statisticStyle.down = game.skin.getDrawable("achievments_ac");
        statisticStyle.pressedOffsetX = 1;
        statisticStyle.pressedOffsetY = -1;

        TextButton.TextButtonStyle settingsStyle = new TextButton.TextButtonStyle();
        settingsStyle.font = game.font24;
        settingsStyle.up = game.skin.getDrawable("settings_ps");
        settingsStyle.down = game.skin.getDrawable("settings_ac");
        settingsStyle.pressedOffsetX = 1;
        settingsStyle.pressedOffsetY = -1;

        TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
        exitStyle.font = game.font24;
        exitStyle.up = game.skin.getDrawable("exit_ps");
        exitStyle.down = game.skin.getDrawable("exit_ac");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        ImageButton icon = new ImageButton(iconstyle);
        icon.setSize(364, 126);
        icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

        TextButton newGame = new TextButton("", newGameStyle);
        newGame.setSize(260, 90);
        newGame.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
        newGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                sound.play(game.getSoundVolume());
                game.setScreen(new GameScreen(game, new LudumGamemode(), ludum));
            }
        });

        TextButton statistic = new TextButton("", statisticStyle);
        statistic.setSize(260, 90);
        statistic.setPosition(stage.getWidth() / 2, (stage.getHeight() / 6) * 3, 1);

        TextButton settings = new TextButton("", settingsStyle);
        settings.setSize(260, 90);
        settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);

        TextButton exit = new TextButton("", exitStyle);
        exit.setSize(260, 90);
        exit.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
        exit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                Gdx.app.exit();
            }
        });

        table.add(icon).size(364, 126).row();
        table.add(newGame).size(260, 90).row();
        table.add(statistic).size(260, 90).row();
        table.add(settings).size(260, 90).row();
        table.add(exit).size(260, 90).row();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //очистка экрана
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //обновление камеры
        camera.update();

        //обновление сцены
        stage.act(delta);
        stage.setDebugAll(true);
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
        stage.dispose();
        sound.dispose();
    }
}
