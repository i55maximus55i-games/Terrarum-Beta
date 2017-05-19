package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by maximus on 19.05.2017.
 */

public class Food implements Disposable {
    private PointLight light;
    private Body body;

    private boolean isAlive;

    public Food(World world, RayHandler rayHandler, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);
        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(3);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.friction = 0;
        fDef.restitution = 1;
        fDef.density = 0;

        body.createFixture(fDef);

        light = new PointLight(rayHandler, 500, Color.YELLOW, 25, 0, 0);
        isAlive = true;
    }

    public void update(float delta) {
        if (isAlive) {
            if (body.getLinearVelocity().x < 0) {
                body.setLinearVelocity(-20, body.getLinearVelocity().y);
            }
            else {
                body.setLinearVelocity(20, body.getLinearVelocity().y);
            }
            if (body.getLinearVelocity().y < 0) {
                body.setLinearVelocity(body.getLinearVelocity().x, -20);
            }
            else {
                body.setLinearVelocity(body.getLinearVelocity().x, 20);
            }
            light.setPosition(body.getPosition());
        }
        else {
            if (light.getDistance() > 1) {
                light.setDistance(light.getDistance() - 3);
            }
        }
    }

    public void die(World world) {
        if (isAlive) {
            isAlive = false;
            world.destroyBody(body);
        }
    }

    @Override
    public void dispose() {
        light.dispose();
    }

    public PointLight getLight() {
        return light;
    }

    public Body getBody() {
        return body;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
