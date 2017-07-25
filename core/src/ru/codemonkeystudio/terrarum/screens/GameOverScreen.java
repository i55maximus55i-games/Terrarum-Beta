package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Экран проигрыша
 */

public class GameOverScreen implements Screen {
    //сцена содержащая элементы интерфейса
    private OrthographicCamera camera;
    private Stage stage;

    //звуки
    private Sound sound;

    public GameOverScreen(final String gamemode, final Terrarum game, final float time, final int score){
        //инициализация звуков
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        //инициализания сцены, содержащей элементы интерфейса
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        Gdx.input.setInputProcessor(stage);

        //создание разметки заголовка
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //создание стиля для кнопки выхода в гланое меню
        TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
        menuButtonStyle.font = game.font32;
        menuButtonStyle.up = game.skin.getDrawable("btn_default");
        menuButtonStyle.over = game.skin.getDrawable("btn_active");
        menuButtonStyle.down = game.skin.getDrawable("btn_pressed");
        menuButtonStyle.pressedOffsetX = 1;
        menuButtonStyle.pressedOffsetY = -1;

        //создание стиля для поля ввода имени
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = game.font24;
        textFieldStyle.fontColor = Color.GRAY;
        textFieldStyle.focusedFontColor = Color.WHITE;
        textFieldStyle.cursor = game.skin.getDrawable("cursor");

        //создание заголовка
        Label message = new Label(game.bundle.get("gameOverLabel"), new Label.LabelStyle(game.font32, Color.GREEN));

        //создание поля ввода имени
        final TextField textField = new TextField("", textFieldStyle);
        textField.setAlignment(1);
        textField.setMessageText("Enter name");
        textField.setMaxLength(10);

        //создание кнопки выхода в гланое меню
        TextButton menuButton = new TextButton(game.bundle.get("menuLabel"), menuButtonStyle);
        menuButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                stage.dispose();
                StatisticScreen.addRecord(gamemode, textField.getText(), time, score);
                if (textField.getText().equals("old38")) {
                    game.unlockLudum();
                    game.setScreen(new MainMenuScreen(game, true));
                }
                else {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });

        //создание надписей, отображающих время и очки
        int t = (int) time;
        Label timerLabel = new Label(game.bundle.get("timeLabel") + " " + String.format("%02d", t / 60) + ":" + (String.format("%02d", t % 60)), new Label.LabelStyle(game.font32, Color.WHITE));
        Label scoreLabel = new Label(game.bundle.get("scoreLabel") + " " + score, new Label.LabelStyle(game.font32, Color.WHITE));

        //добавление элементов в разметку
        table.add(message).expandX().padTop(32).row();
        table.add(timerLabel).expandX().row();
        table.add(scoreLabel).expandX().row();
        if (StatisticScreen.getRecords(gamemode)[9].score < score) {
            table.add(textField).expandX().center().width(160).row();
        }
        table.add(menuButton).size(Gdx.graphics.getHeight() * 13 / 36, Gdx.graphics.getHeight() / 8).expandX();

        //добавление разметки в сцену
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
        stage.dispose();
        sound.dispose();
    }
}
