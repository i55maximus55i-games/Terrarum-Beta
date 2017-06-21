package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;

import java.util.Arrays;
import java.util.Comparator;

import ru.codemonkeystudio.terrarum.objects.Record;

/**
 * Created by maximus on 20.06.2017.
 */

public class StatisticScreen implements Screen {
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    private Record[] getRecords (String gamemode) {
        Record[] records = new Record[10];
        Preferences preferences = Gdx.app.getPreferences("Terrarum records " + gamemode);
        for (int i = 0; i < 10; i++) {
            records[i] = new Record(preferences.getString("name" + i, ""), preferences.getFloat("time" + i, 0f), preferences.getInteger("score" + i, 0));
        }
        return records;
    }
}
