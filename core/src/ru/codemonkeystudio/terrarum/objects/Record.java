package ru.codemonkeystudio.terrarum.objects;

/**
 * объект содержащий в себе рекорд
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
