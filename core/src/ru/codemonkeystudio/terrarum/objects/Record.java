package ru.codemonkeystudio.terrarum.objects;

/**
 * Created by maximus on 21.06.2017.
 */

public class Record {
    public String name;
    public float time;
    public int score;

    public Record(String name, float time, int score) {
        this.name = name;
        this.time = time;
        this.score = score;
    }
}
