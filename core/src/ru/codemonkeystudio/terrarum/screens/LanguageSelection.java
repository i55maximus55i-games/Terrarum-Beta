package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Экран выбора языка
 */

class LanguageSelection implements Screen {
    //сцена содержащая элементы интерфейса
    private OrthographicCamera camera;
    private Stage stage;

    //звуки
    private Sound sound;

    LanguageSelection(final Terrarum game){
        //инициализация звуков
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        //инициализания сцены, содержащей элементы интерфейса
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(800,600, camera));
        Gdx.input.setInputProcessor(stage);

        //создание разметки выбора языка
        final Table table = new Table();
        table.center();
        table.setFillParent(true);

        //создание разметки заголовка
        final Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        //создание стиля кнопки выхода в главное меню
        final Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = game.skin.getDrawable("btn_left");
        exitStyle.down = game.skin.getDrawable("btn_left_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        //создание стиля списка языков
        final List.ListStyle listStyle = new List.ListStyle();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.GRAY;
        listStyle.font = game.font24;
        listStyle.selection = game.skin.getDrawable("null");

        //создание заголовка
        final Label label = new Label(game.bundle.get("LSLabel"), new Label.LabelStyle(game.font32, Color.WHITE));

        //создание кнопки выхода в главное меню
        final Button exit = new Button(exitStyle);
        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                game.setScreen(new MainMenuScreen(game));
            }
        });

        //создание списка языков
        final List<String> langList = new List<String>(listStyle);
        langList.setItems(game.bundle.get("LSsystem"), "English", "Русский");
        langList.setSelectedIndex(Gdx.app.getPreferences("Terrarum settings").getInteger("langN"));
        langList.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
                String lang;
                switch (langList.getSelectedIndex()) {
                    default:
                        lang = "default";
                        break;
                    case 1:
                        lang = "en";
                        break;
                    case 2:
                        lang = "ru";
                        break;
                }
                preferences.putString("lang", lang);
                preferences.putInteger("langN", langList.getSelectedIndex());
                preferences.flush();
            }
        });

        //заполнение разметки заголовка
        tableTop.add(exit).size(72, 72).left().expandX();
        tableTop.add(label).center().expandX();
        tableTop.add().size(72, 72).right().expandX();

        //заполнение разметки выбора языков
        table.add(langList).center();

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
