package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maximus on 10.05.2017.
 */

public class TerrarumControlHandler {
    private ShapeRenderer shapeRenderer;

    private int x, y;
    private int stickSizeBig;
    private int stickSizeSmall;

    public TerrarumControlHandler() {
        x = -1;
        y = -1;

        stickSizeBig = 150;
        stickSizeSmall = 40;
        shapeRenderer = new ShapeRenderer();
    }

    public Vector2 touchControl() {
        Vector2 ctrl = new Vector2();
        if (Gdx.input.isTouched()) {
            ctrl.set(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.input.getY() - Gdx.graphics.getHeight() / 2);
        }
        else {
            ctrl.set(0, 0);
        }
        ctrl = vectorSum(ctrl);
        return ctrl;
    }

    public Vector2 stickControl() {
        if (Gdx.input.isTouched()) {
            if (x == -1) {
                x = Gdx.input.getX();
                y = Gdx.graphics.getHeight() - Gdx.input.getY();
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.3f);
            shapeRenderer.circle(x, y, stickSizeBig);
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.6f);
            Vector2 ctrl = new Vector2();
            ctrl.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            Vector2 c = new Vector2(ctrl.x - x, ctrl.y - y);
            if (ctrl.dst(x, y) < stickSizeBig - stickSizeSmall - 3) {
                shapeRenderer.circle(ctrl.x, ctrl.y, stickSizeSmall);
            }
            else {
                c = vectorSum(c);
                float a = stickSizeBig - stickSizeSmall - 4;
                shapeRenderer.circle(x + c.x * a, y + c.y * a, stickSizeSmall);
            }
            shapeRenderer.end();
            c.y = -c.y;
            c = vectorSum(c);
            if (ctrl.dst(x, y) < stickSizeBig + stickSizeSmall * 2) {
                c.x *= ctrl.dst(x, y) / (stickSizeBig + stickSizeSmall * 2);
                c.y *= ctrl.dst(x, y) / (stickSizeBig + stickSizeSmall * 2);
                return c;
            } else {
                return c;
            }
        }
        else {
            x = -1;
            y = -1;
            return new Vector2(0, 0);
        }
    }

    public Vector2 keyControl() {
        Vector2 ctrl = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) ctrl.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) ctrl.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) ctrl.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ctrl.x += 1;
        return vectorSum(ctrl);
    }

    public void resize(int width, int height) {
        shapeRenderer = new ShapeRenderer();
    }

    public Vector2 vectorSum(Vector2 vector) {
        Vector2 result = new Vector2(vector);

        if (result.x == 0) {
            if (result.y > 0) {
                return new Vector2(0, 1);
            }
            else if (result.y < 0){
                return new Vector2(0, -1);
            }
            else if (result.y == 0) {
                return new Vector2(0, 0);
            }
        }
        else if (result.y == 0) {
            if (result.x > 0) {
                return new Vector2(1, 0);
            }
            else if (result.x < 0){
                return new Vector2(-1, 0);
            }
        }
        else {
            float angle = 0;
            angle += Math.abs(Math.toDegrees(Math.atan(result.y / result.x)));

            float xx, yy;
            if (result.x > 0) {
                xx = (float) Math.cos(Math.toRadians(angle));
            }
            else {
                xx = (float) - Math.cos(Math.toRadians(angle));
            }
            if (result.y > 0) {
                yy = (float) Math.sin(Math.toRadians(angle));
            }
            else {
                yy = (float) - Math.sin(Math.toRadians(angle));
            }
            return new Vector2(xx, yy);
        }
        return null;
    }
}
