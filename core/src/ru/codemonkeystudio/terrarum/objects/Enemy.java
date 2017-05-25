package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
 * Created by vanchok on 25.05.2017.
 */

public class Enemy implements Disposable {
    private PointLight light;
    private Body body;
    private Sound enemySound;

    private boolean isAlive;

    public Enemy(World world, RayHandler rayHandler, float x, float y) {
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

        light = new PointLight(rayHandler, 500, Color.BLUE, 25, 0, 0);
        isAlive = true;

        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy.wav"));
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

    public void die(World world, float volume) {
        if (isAlive) {
            isAlive = false;
            world.destroyBody(body);
            enemySound.play(volume);
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

