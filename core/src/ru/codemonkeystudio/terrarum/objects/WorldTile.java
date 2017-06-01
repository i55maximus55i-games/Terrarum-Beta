package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by maximus on 01.06.2017.
 */

public class WorldTile {
    private int id;
    private ArrayList<Body> walls;

    public WorldTile(World world, int id, int x, int y) {
        this.id = id;
        walls = new ArrayList<Body>();
        switch (id) {
            case 0:
                createWall(world, x * 64 + 4, y * 64 + 32, 4, 32);
                createWall(world, x * 64 + 22, y * 64 + 32, 14, 4);
                break;
            case 1:
                createWall(world, x * 64 + 50, y * 64 + 60, 14, 4);
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                break;
            case 2:
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                break;
            case 3:
                createWall(world, x * 64 + 32, y * 64 + 18, 4, 18);
                createWall(world, x * 64 + 14, y * 64 + 4, 14, 4);
                break;
            case 4:
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                createWall(world, x * 64 + 14, y * 64 + 60, 14, 4);
                break;
            case 5:
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                createWall(world, x * 64 + 50, y * 64 + 60, 14, 4);
                createWall(world, x * 64 + 14, y * 64 + 32, 14, 4);
                break;
            case 6:
                createWall(world, x * 64 + 50, y * 64 + 60, 14, 4);
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                break;
            case 7:
                createWall(world, x * 64 + 32, y * 64 + 60, 32, 4);
                break;
            case 8:
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                createWall(world, x * 64 + 14, y * 64 + 60, 14, 4);
                break;
            case 9:
                createWall(world, x * 64 + 14, y * 64 + 32, 14, 4);
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                break;
            case 10:
                createWall(world, x * 64 + 14, y * 64 + 32, 14, 4);
                createWall(world, x * 64 + 32, y * 64 + 46, 4, 18);
                break;
            case 11:
                createWall(world, x * 64 + 42, y * 64 + 32, 14, 4);
                createWall(world, x * 64 + 60, y * 64 + 46, 4, 18);
                break;
        }
    }

    private void createWall(World world, float x, float y, float sizeX, float sizeY) {
        Body body;
        BodyDef wall;
        PolygonShape shape;

        wall = new BodyDef();
        wall.type = BodyDef.BodyType.StaticBody;

        wall.position.set(x, y);
        body = world.createBody(wall);
        shape = new PolygonShape();
        shape.setAsBox(sizeX, sizeY);
        body.createFixture(shape, 0);
        walls.add(body);
    }

    public void destroy(World world) {
        while (!walls.isEmpty()) {
            world.destroyBody(walls.get(0));
            walls.remove(0);
        }
    }

    public int getId() {
        return id;
    }
}
