package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Arrays;
import java.util.Comparator;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.Record;

/**
 * Created by maximus on 20.06.2017.
 */

public class StatisticScreen implements Screen {
    private TextButton rightButton;
    private SpriteBatch batch;
    private static Stage stage;
    private OrthographicCamera camera;

    private Skin skin;
    private TextureAtlas atlas;
    private BitmapFont font_16,font_24,font_32;

    private TextButton leftButton;

    public StatisticScreen(Terrarum game) {
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(800,600, camera));
        Gdx.input.setInputProcessor(stage);
        batch = game.batch;

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        TextButton.TextButtonStyle leftButtonStyle = new TextButton.TextButtonStyle();
        leftButtonStyle.font = font_24;
        leftButtonStyle.up = skin.getDrawable("btn_left");
        leftButtonStyle.down = skin.getDrawable("btn_left_pressed");
        leftButtonStyle.pressedOffsetX = 1;
        leftButtonStyle.pressedOffsetY = -1;

        leftButton = new TextButton("", leftButtonStyle);
        leftButton.setSize(90, 90);
        leftButton.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
        leftButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        TextButton.TextButtonStyle rightButtonStyle = new TextButton.TextButtonStyle();
        rightButtonStyle.font = font_24;
        rightButtonStyle.up = skin.getDrawable("btn_right");
        rightButtonStyle.down = skin.getDrawable("btn_right_pressed");
        rightButtonStyle.pressedOffsetX = 1;
        rightButtonStyle.pressedOffsetY = -1;

        rightButton = new TextButton("", rightButtonStyle);
        rightButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font_24;
        labelStyle.fontColor = Color.WHITE;

        Label label1 = new Label("gamemode", labelStyle);
        label1.setColor(Color.GRAY);
        Label label2 = new Label("gamemode", labelStyle);
        Label label3 = new Label("gamemode", labelStyle);
        label3.setColor(Color.GRAY);

        TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
        menuButtonStyle.font = font_24;
        menuButtonStyle.up = skin.getDrawable("btn_default");
        menuButtonStyle.over = skin.getDrawable("btn_active");
        menuButtonStyle.down = skin.getDrawable("btn_pressed");
        menuButtonStyle.pressedOffsetX = 1;
        menuButtonStyle.pressedOffsetY = -1;

        TextButton menuButton = new TextButton(game.bundle.get("menuLabel"), menuButtonStyle);
        menuButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        table.add(leftButton).padRight(16).size(72, 72).left();
        table.add(label1).expandX();
        table.add(label2).expandX();
        table.add(label3).expandX();
        table.add(rightButton).padLeft(16).size(72, 72).left();
        table.row();
        table.add();
        table.add();
        table.add(menuButton).size(260, 90).center().expandX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.setDebugAll(true);
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

    public static void addRecord(String gamemode, String playerName, float time, int score) {
        Record[] records = new Record[11];
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);
        for (int i = 0; i < 10; i++) {
            records[i] = new Record(preferences.getString("name" + i, ""), preferences.getFloat("time" + i, 0f), preferences.getInteger("score" + i, 0));
        }
        records[10] = new Record(playerName, time, score);

        Arrays.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record record, Record t1) {
                if (record.score > t1.score) {
                    return -1;
                }
                else if (record.score < t1.score){
                    return 1;
                }
                else {
                    if (record.time > t1.time) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            preferences.putString("name" + i, records[i].name);
            preferences.putFloat("time" + i, records[i].time);
            preferences.putInteger("score" + i, records[i].score);
        }
        preferences.flush();
    }

    public static Record[] getRecords (String gamemode) {
        Record[] records = new Record[10];
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);
        for (int i = 0; i < 10; i++) {
            records[i] = new Record(preferences.getString("name" + i, ""), preferences.getFloat("time" + i, 0f), preferences.getInteger("score" + i, 0));
        }
        return records;
    }
}
