package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Класс отвечающий за управление игрока
 */

public class TerrarumControlHandler implements Disposable {
    private ShapeRenderer shapeRenderer;

    private int x, y;
    private int stickSizeBig;
    private int stickSizeSmall;

    public TerrarumControlHandler() {
        x = -1;
        y = -1;

        stickSizeBig = 150;
        stickSizeSmall = stickSizeBig / 3;
        shapeRenderer = new ShapeRenderer();
    }

    public Vector2 touchControl() {
        Vector2 ctrl = new Vector2();
        for (int i = 0; i < 10; i++) {
            if (Gdx.input.isTouched(i)) {
                ctrl.add(Gdx.input.getX(i) - Gdx.graphics.getWidth() / 2, Gdx.input.getY(i) - Gdx.graphics.getHeight() / 2);
            }
        }
        ctrl = vectorSinCos(ctrl);
        return ctrl;
    }

    public Vector2 stickControl() {
        if (Gdx.input.isTouched(0)) {
            if (x == -1) {
                x = Gdx.input.getX(0);
                y = Gdx.graphics.getHeight() - Gdx.input.getY(0);
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.3f);
            shapeRenderer.circle(x, y, stickSizeBig);
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.6f);
            Vector2 ctrl = new Vector2();
            ctrl.set(Gdx.input.getX(0), Gdx.graphics.getHeight() - Gdx.input.getY(0));
            Vector2 c = new Vector2(ctrl.x - x, ctrl.y - y);
            if (ctrl.dst(x, y) < stickSizeBig - stickSizeSmall - 3) {
                shapeRenderer.circle(ctrl.x, ctrl.y, stickSizeSmall);
            }
            else {
                c = vectorSinCos(c);
                float a = stickSizeBig - stickSizeSmall - 4;
                shapeRenderer.circle(x + c.x * a, y + c.y * a, stickSizeSmall);
            }
            shapeRenderer.end();
            c.y = -c.y;
            c = vectorSinCos(c);
            if (ctrl.dst(x, y) < stickSizeBig) {
                c.x *= ctrl.dst(x, y) / (stickSizeBig);
                c.y *= ctrl.dst(x, y) / (stickSizeBig);
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
        return vectorSinCos(ctrl);
    }

    public void resize() {
        shapeRenderer.dispose();
        shapeRenderer = new ShapeRenderer();
    }

    public Vector2 vectorSinCos(Vector2 vector) {
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

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
