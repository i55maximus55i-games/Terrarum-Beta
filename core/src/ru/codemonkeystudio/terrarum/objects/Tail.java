package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by maximus on 23.05.2017.
 */

public class Tail {
    private float posX;
    private float posY;
    private int live;

    public Tail(int live, float x, float y) {
        posX = x;
        posY = y;
        this.live = live;
    }

    public void update() {
        live--;
    }

    public int getLive() {
        return live;
    }

    public Vector2 getPos() {
        return new Vector2(posX, posY);
    }
}
