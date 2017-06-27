package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.gamemodes.ArcadeGamemode;
import ru.codemonkeystudio.terrarum.gamemodes.ClassicGamemode;
import ru.codemonkeystudio.terrarum.gamemodes.LudumGamemode;

/**
 * Экран выбора режима игры
 */

class GamemodeSelection implements Screen {
    //сцена содержащая элементы интерфейса
    private OrthographicCamera camera;
    private Stage stage;

    //звуки
    private Sound sound;

    GamemodeSelection(final Terrarum game){
        //инициализация звуков
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        //инициализания сцены, содержащей элементы интерфейса
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        Gdx.input.setInputProcessor(stage);

        //создание разметки с режимами игры
        final Table table = new Table();
        table.center();
        table.setFillParent(true);

        //создание разметки заголовка
        final Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        //создание стиля для кнопки выхода в главное меню
        final Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = game.skin.getDrawable("btn_left");
        exitStyle.down = game.skin.getDrawable("btn_left_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        //создание стиля для кнопок выбора режима
        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font24;
        buttonStyle.up = game.skin.getDrawable("btn_default");
        buttonStyle.over = game.skin.getDrawable("btn_active");
        buttonStyle.down = game.skin.getDrawable("btn_pressed");
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.pressedOffsetY = -1;

        //создание надписи заголовка
        final Label label = new Label(game.bundle.get("GSLabel"), new Label.LabelStyle(game.font32, Color.WHITE));

        //создание кнопки выхода в главное меню
        final Button exit = new Button(exitStyle);
        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                game.setScreen(new MainMenuScreen(game));
            }
        });

        //создание кнопок выбора режима
        final TextButton ClassicGM = new TextButton(game.bundle.get("Classic"), buttonStyle);
        ClassicGM.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new GameScreen(game, new ClassicGamemode(), false));
            }
        });
        final TextButton ArcadeGM = new TextButton(game.bundle.get("Arcade"), buttonStyle);
        ArcadeGM.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new GameScreen(game, new ArcadeGamemode(), false));
            }
        });
        final TextButton LudumGM = new TextButton("OLD38", buttonStyle);
        LudumGM.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new GameScreen(game, new LudumGamemode(), true));
            }
        });

        //добавление элементов интерфейса в разметку заголовка
        tableTop.add(exit).size(Gdx.graphics.getHeight() / 8).left().expandX();
        tableTop.add(label).center().expandX();
        tableTop.add().size(Gdx.graphics.getHeight() / 8).right().expandX();

        //добавление элементов интерфейса в разметку выбора режима
        table.add(ArcadeGM).size(Gdx.graphics.getHeight() * 13 / 36, Gdx.graphics.getHeight() / 8).center().row();
        table.add(ClassicGM).size(Gdx.graphics.getHeight() * 13 / 36, Gdx.graphics.getHeight() / 8).center().row();
        if (game.isLudumUnlocked()) {
            table.add(LudumGM).size(Gdx.graphics.getHeight() * 13 / 36, Gdx.graphics.getHeight() / 8).center().row();
        }

        //добавление разметок на сцену
        stage.addActor(tableTop);
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

    }
}
