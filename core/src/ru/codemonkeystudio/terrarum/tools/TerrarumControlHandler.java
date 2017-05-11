package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maximus on 10.05.2017.
 */

public class TerrarumControlHandler {
    private int x, y;
    private int stickSizeBig;
    private int stickSizeSmall;

    public TerrarumControlHandler() {
        x = -1;
        y = -1;

        stickSizeBig = 40;
        stickSizeSmall = 5;
    }

    public Vector2 touchControl() {
        if (Gdx.input.isTouched()) {
            Vector2 ctrl = new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.input.getY() - Gdx.graphics.getHeight() / 2);

            if (ctrl.x == 0) {
                if (ctrl.y > 0) {
                    return new Vector2(0, 1);
                }
                else if (ctrl.y < 0){
                    return new Vector2(0, -1);
                }
                else if (ctrl.y == 0) {
                    return new Vector2(0, 0);
                }
            }
            else if (ctrl.y == 0) {
                if (ctrl.x > 0) {
                    return new Vector2(1, 0);
                }
                else if (ctrl.x < 0){
                    return new Vector2(-1, 0);
                }
            }
            else {
                float angle = 0;
                angle += Math.abs(Math.toDegrees(Math.atan(ctrl.y / ctrl.x)));

                float xx, yy;
                if (ctrl.x > 0) {
                    xx = (float) Math.cos(Math.toRadians(angle));
                }
                else {
                    xx = (float) - Math.cos(Math.toRadians(angle));
                }
                if (ctrl.y > 0) {
                    yy = (float) Math.sin(Math.toRadians(angle));
                }
                else {
                    yy = (float) - Math.sin(Math.toRadians(angle));
                }
                return new Vector2(xx, yy);
            }
        }
        else {
            return new Vector2(0, 0);
        }
        return null;
    }

    public Vector2 stickControl(ShapeRenderer shapeRenderer) {
        if (Gdx.input.isTouched()) {
            if (x == -1) {
                x = Gdx.input.getX();
                y = Gdx.graphics.getHeight() - Gdx.input.getY();
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.1f);
            shapeRenderer.circle(x, y, stickSizeBig);
            shapeRenderer.setColor(1, 0, 0, 0.4f);
            Vector2 ctrl = new Vector2();
            ctrl.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if (ctrl.dst(x, y) < stickSizeBig - stickSizeSmall - 3) {
                shapeRenderer.circle(ctrl.x, ctrl.y, stickSizeSmall);
            }
            else {
                Vector2 c = new Vector2(ctrl.x - x, ctrl.y - y);
                if (c.x == 0) {
                    if (c.y > 0) {
                        shapeRenderer.circle(x, y + stickSizeBig - stickSizeSmall - 4, stickSizeSmall);
                    }
                    else if (c.y < 0){
                        shapeRenderer.circle(x, y - stickSizeBig - stickSizeSmall - 4, stickSizeSmall);
                    }
                    else if (c.y == 0) {
                        shapeRenderer.circle(x, y, stickSizeSmall);
                    }
                }
                else if (c.y == 0) {
                    if (c.x > 0) {
                        shapeRenderer.circle(x + stickSizeBig - stickSizeSmall - 4, y, stickSizeSmall);
                    }
                    else if (c.x < 0){
                        shapeRenderer.circle(x - stickSizeBig - stickSizeSmall - 4, y, stickSizeSmall);
                    }
                }
                else {
                    float angle = 0;
                    angle += Math.abs(Math.toDegrees(Math.atan(c.y / c.x)));
                    float xx, yy;
                    if (c.x > 0) {
                        xx = (float) (x + (stickSizeBig - stickSizeSmall - 4) * Math.cos(Math.toRadians(angle)));
                    }
                    else {
                        xx = (float) (x - (stickSizeBig - stickSizeSmall - 4) * Math.cos(Math.toRadians(angle)));
                    }
                    if (c.y > 0) {
                        yy = (float) (y + (stickSizeBig - stickSizeSmall - 4) * Math.sin(Math.toRadians(angle)));
                    }
                    else {
                        yy = (float) (y - (stickSizeBig - stickSizeSmall - 4) * Math.sin(Math.toRadians(angle)));
                    }
                    shapeRenderer.circle(xx, yy, stickSizeSmall);
                }
            }
            shapeRenderer.end();
            Vector2 c = new Vector2(ctrl.x - x, ctrl.y - y);
            if (c.x == 0) {
                if (c.y > 0) {
                    return new Vector2(0, 1);
                }
                else if (c.y < 0){
                    return new Vector2(0, -1);
                }
                else if (c.y == 0) {
                    return new Vector2(0, 0);
                }
            }
            else if (c.y == 0) {
                if (c.x > 0) {
                    return new Vector2(1, 0);
                }
                else if (c.x < 0){
                    return new Vector2(-1, 0);
                }
            }
            else {
                float angle = 0;
                angle += Math.abs(Math.toDegrees(Math.atan(c.y / c.x)));
                float xx, yy;
                if (c.x > 0) {
                    xx = (float) Math.cos(Math.toRadians(angle));
                }
                else {
                    xx = (float) - Math.cos(Math.toRadians(angle));
                }
                if (c.y > 0) {
                    yy = (float) Math.sin(Math.toRadians(angle));
                }
                else {
                    yy = (float) - Math.sin(Math.toRadians(angle));
                }
                return new Vector2(xx, yy);
            }
        }
        else {
            x = -1;
            y = -1;
            return new Vector2(0, 0);
        }
        return null;
    }

    public Vector2 keyControl() {
        Vector2 ctrl = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) ctrl.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) ctrl.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) ctrl.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ctrl.x += 1;

        if (ctrl.x == 0) {
            if (ctrl.y > 0) {
                return new Vector2(0, 1);
            }
            else if (ctrl.y < 0){
                return new Vector2(0, -1);
            }
            else if (ctrl.y == 0) {
                return new Vector2(0, 0);
            }
        }
        else if (ctrl.y == 0) {
            if (ctrl.x > 0) {
                return new Vector2(1, 0);
            }
            else if (ctrl.x < 0){
                return new Vector2(-1, 0);
            }
        }
        else {
            float angle = 0;
            angle += Math.abs(Math.toDegrees(Math.atan(ctrl.y / ctrl.x)));

            float xx, yy;
            if (ctrl.x > 0) {
                xx = (float) Math.cos(Math.toRadians(angle));
            }
            else {
                xx = (float) - Math.cos(Math.toRadians(angle));
            }
            if (ctrl.y > 0) {
                yy = (float) Math.sin(Math.toRadians(angle));
            }
            else {
                yy = (float) - Math.sin(Math.toRadians(angle));
            }
            return new Vector2(xx, yy);
        }
        return ctrl;
    }
}
