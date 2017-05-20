package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import ru.codemonkeystudio.terrarum.tools.TerrarumControlHandler;

/**
 * Created by maximus on 16.05.2017.
 */

public class Player implements Disposable{
    public static final float SIZE = 3;
    private final PointLight playerLight;
    private boolean isStickControl;
    private Body body;
    private int lives;
    private TerrarumControlHandler controlHandler;
    private Sound hitSound;

    public Player(World world, TerrarumControlHandler controlHandler, boolean isStickControl, RayHandler rayHandler) {
        this.isStickControl = isStickControl;
        this.controlHandler = controlHandler;
        lives = 5;

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(16, 16);
        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(SIZE);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.friction = 0;
        fDef.restitution = 1;
        fDef.density = 0;

        body.createFixture(fDef);

        playerLight = new PointLight(rayHandler, 500, Color.RED, 100, 20, 20);
        playerLight.attachToBody(body);

        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
    }

    public void update(float delta) {
        control();
        friction();

        if (playerLight.getDistance() <= 100) {
            playerLight.setColor(Color.RED);
            playerLight.setDistance(100);
        }
        else {
            playerLight.setDistance(playerLight.getDistance() - 20);
        }
    }

    private void friction() {
        float s = 0.8f;
        Vector2 velocity = new Vector2();
        velocity.set(body.getLinearVelocity());
        Vector2 v = new Vector2(velocity);
        velocity.sub(controlHandler.vectorSum(velocity).x * s, controlHandler.vectorSum(velocity).y * s);
        if ((v.x > 0 && velocity.x < 0) || (v.x < 0 && velocity.x > 0) || (v.y > 0 && velocity.y < 0) || (v.y < 0 && velocity.y > 0))
        {
            velocity.set(0, 0);
        }
        body.setLinearVelocity(velocity);
    }

    private void control() {
        Vector2 c = controlHandler.keyControl();
        if (isStickControl) {
            c.add(controlHandler.stickControl());
        }
        else {
            c.add(controlHandler.touchControl());
        }
        if (c.x * c.x + c.y * c.y > 1) {
            c = controlHandler.vectorSum(c);
        }
        c.x *= 100;
        c.y *= 100;
        body.applyForceToCenter(c, true);
    }

    public Body getBody() {
        return body;
    }

    public void setStickControl(boolean s) {
        isStickControl = s;
    }

    public void hit() {
        lives--;
        playerLight.setDistance(1000);
        playerLight.setColor(Color.WHITE);
        hitSound.play(0.2f);
    }

    public int getLives() {
        return lives;
    }

    @Override
    public void dispose() {
        playerLight.dispose();
    }
}
