package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
    private Label[] name, score, time;
    private Label avgTime, avgScore, gameCount;


    private int cursor;
    private String[] gamemodes = {"Arcade", "Classic"};
    private Sound sound;

    StatisticScreen(final Terrarum game) {
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(800,600, camera));
        Gdx.input.setInputProcessor(stage);
        batch = game.batch;

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Table table1 = new Table();
        table1.top();
        table1.setFillParent(true);


        Table table3 = new Table();
        table3.bottom();
        table3.setFillParent(true);

        Table table2 = new Table();
        table2.setPosition(0, 155);
        table2.setFillParent(true);


        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font_24;
        labelStyle.fontColor = Color.WHITE;

        avgTime = new Label("", labelStyle);
        avgScore = new Label("", labelStyle);
        gameCount = new Label("", labelStyle);


        final Label label1 = new Label(game.bundle.get(gamemodes[gamemodes.length - 1]), labelStyle);
        label1.setColor(Color.GRAY);
        label1.setAlignment(1);
        final Label label2 = new Label(game.bundle.get(gamemodes[0]), labelStyle);
        label2.setAlignment(1);
        final Label label3 = new Label(game.bundle.get(gamemodes[1]), labelStyle);
        label3.setColor(Color.GRAY);
        label3.setAlignment(1);
        final Label num = new Label("1234567890", labelStyle);
        final Label labeln = new Label(game.bundle.get("nameRecLabel"), labelStyle);
        labeln.setAlignment(1);
        final Label labelt = new Label(game.bundle.get("timeRecLabel"), labelStyle);
        labelt.setAlignment(1);
        final Label labels = new Label(game.bundle.get("scoreRecLabel"), labelStyle);
        labels.setAlignment(1);

        name = new Label[10];
        score = new Label[10];
        time = new Label[10];
        for (int i = 0; i < 10; i++) {
            name[i] = new Label("", labelStyle);
            time[i] = new Label("", labelStyle);
            score[i] = new Label("", labelStyle);
        }

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
                super.touchUp(event, x, y, pointer, button);
                cursor--;
                if (cursor < 0) {
                    cursor = gamemodes.length - 1;
                }
                label2.setText(game.bundle.get(gamemodes[cursor]));
                if (cursor <= 0) {
                    label1.setText(game.bundle.get(gamemodes[gamemodes.length - 1]));
                }
                else {
                    label1.setText(game.bundle.get(gamemodes[cursor - 1]));
                }
                if (cursor >= gamemodes.length - 1) {
                    label3.setText(game.bundle.get(gamemodes[0]));
                }
                else {
                    label3.setText(game.bundle.get(gamemodes[cursor + 1]));
                }
                updateTable(gamemodes[cursor]);
                sound.play(game.getSoundVolume());
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
                super.touchUp(event, x, y, pointer, button);
                cursor++;
                if (cursor >= gamemodes.length) {
                    cursor = 0;
                }
                label2.setText(game.bundle.get(gamemodes[cursor]));
                if (cursor <= 0) {
                    label1.setText(game.bundle.get(gamemodes[gamemodes.length - 1]));
                }
                else {
                    label1.setText(game.bundle.get(gamemodes[cursor - 1]));
                }
                if (cursor >= gamemodes.length - 1) {
                    label3.setText(game.bundle.get(gamemodes[0]));
                }
                else {
                    label3.setText(game.bundle.get(gamemodes[cursor + 1]));
                }
                updateTable(gamemodes[cursor]);
                sound.play(game.getSoundVolume());
            }
        });

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
                sound.play(game.getSoundVolume());
                stage.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table1.add(leftButton).size(72, 72).left().expandX();
        table1.add(label1).center().expandX();
        table1.add(label2).center().expandX();
        table1.add(label3).center().expandX();
        table1.add(rightButton).size(72, 72).right().expandX();
        table1.row();

        table2.add(new Label(game.bundle.get("gameCount"), labelStyle)).center().expandX();
        table2.add(gameCount).center().expandX();
        table2.row();
        table2.add(new Label(game.bundle.get("timeAvg"), labelStyle)).center().expandX();
        table2.add(avgTime).center().expandX();
        table2.row();
        table2.add(new Label(game.bundle.get("scoreAvg"), labelStyle)).center().expandX();
        table2.add(avgScore).center().expandX();
        table2.row();

        table3.add().center().expandX();
        table3.add(labeln).width(num.getWidth()).expandX().center();
        table3.add(labelt).center().expandX();
        table3.add(labels).width(num.getWidth()).expandX();
        table3.add().expandX().center();
        table3.row();

        for (int i = 0; i < 10; i++) {
            table3.add(new Label((i + 1) + ".", labelStyle)).center().expandX();
            table3.add(name[i]).center().expandX();
            table3.add(time[i]).center();
            table3.add(score[i]).center().expandX();
            table3.add(new Label((i + 1) + ".", labelStyle)).center().expandX();
            table3.row();
        }

        table3.add().center().expandX();
        table3.add().center().expandX();
        table3.add(menuButton).size(260, 90).center().expandX();
        table3.add().center().expandX();
        table3.add().center().expandX();


        stage.addActor(table1);
        stage.addActor(table2);
        stage.addActor(table3);

//        table.add(table1);
//        table.add(table2);
//        table.add(table3);

        stage.addActor(table);

        updateTable(gamemodes[cursor]);
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
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        sound.dispose();

        font_16.dispose();
        font_24.dispose();
        font_32.dispose();
    }

    static void addRecord(String gamemode, String playerName, float time, int score) {
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

    public static void addResult(String gamemode, float time, int score) {
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);

        long tm = preferences.getLong("time", 0);
        tm += (int)(time);
        preferences.putLong("time", tm);
        long count = preferences.getLong("count", 0);
        count++;
        preferences.putLong("count", count);
        long scr = preferences.getLong("score", 0);
        scr += score;
        preferences.putLong("score", scr);
        preferences.flush();
    }

    static Record[] getRecords(String gamemode) {
        Record[] records = new Record[13];
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);
        for (int i = 0; i < 10; i++) {
            records[i] = new Record(preferences.getString("name" + i, ""), preferences.getFloat("time" + i, 0f), preferences.getInteger("score" + i, 0));
        }
        return records;
    }

    private void updateTable(String gamemode) {
        Record[] records = getRecords(gamemode);
        for (int i = 0; i < 10; i++) {
            name[i].setText(records[i].name);
            time[i].setText(String.format("%02d", (int)(records[i].time) / 60) + ":" + (String.format("%02d", (int)(records[i].time) % 60)));
            score[i].setText(records[i].score + "");
        }
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);
        gameCount.setText(preferences.getLong("count") + "");
        if (preferences.getLong("count") == 0) {
            avgTime.setText("00:00");
            avgScore.setText("0");
        }
        else {
            int avg;
            avg = (int) (preferences.getLong("time") / preferences.getLong("count"));
            avgTime.setText(String.format("%02d", avg / 60) + ":" + (String.format("%02d", avg % 60)));
            avg = (int) (preferences.getLong("score") / preferences.getLong("count"));
            avgScore.setText(avg + "");
        }
    }


}
