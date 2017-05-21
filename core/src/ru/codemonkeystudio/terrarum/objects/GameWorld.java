package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maximus on 06.05.2017.
 */

public class GameWorld implements Disposable{
    public static final int WORLD_SIZE = 16;

    public final int[][] grid;

    private World world;

    public GameWorld() {
        world = new World(new Vector2(0, 0), true);

        grid = new int[WORLD_SIZE][WORLD_SIZE];
        List<Integer> cells = new ArrayList<Integer>();
        for (int i = 0; i < (int)(WORLD_SIZE * WORLD_SIZE * 2.625 / 12); i++) {
            for (int j = 0; j < 12; j++) {
                cells.add(j);
            }
        }

        createWall(-4, 32 * WORLD_SIZE, 4, 32 * WORLD_SIZE + 8);
        createWall(4 + 64 * WORLD_SIZE, 32 * WORLD_SIZE,4, 32 * WORLD_SIZE + 8);
        createWall(32 * WORLD_SIZE, - 4,32 * WORLD_SIZE, 4);
        createWall(32 * WORLD_SIZE, 4 + 64 * WORLD_SIZE, 32 * WORLD_SIZE, 4);

        for (int y = 0; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                int r = (int) (Math.random() * 12);
                grid[x][y] = Integer.parseInt(cells.get(r).toString());
                cells.remove(r);
                switch (grid[x][y]) {
                    case 0:
                        createWall(x * 64 + 4, y * 64 + 32, 4, 32);
                        createWall(x * 64 + 22, y * 64 + 32, 14, 4);
                        break;
                    case 1:
                        createWall(x * 64 + 50, y * 64 + 60, 14, 4);
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        break;
                    case 2:
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        break;
                    case 3:
                        createWall(x * 64 + 32, y * 64 + 18, 4, 18);
                        createWall(x * 64 + 14, y * 64 + 4, 14, 4);
                        break;
                    case 4:
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        createWall(x * 64 + 14, y * 64 + 60, 14, 4);
                        break;
                    case 5:
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        createWall(x * 64 + 50, y * 64 + 60, 14, 4);
                        createWall(x * 64 + 14, y * 64 + 32, 14, 4);
                        break;
                    case 6:
                        createWall(x * 64 + 50, y * 64 + 60, 14, 4);
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        break;
                    case 7:
                        createWall(x * 64 + 32, y * 64 + 60, 32, 4);
                        break;
                    case 8:
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        createWall(x * 64 + 14, y * 64 + 60, 14, 4);
                        break;
                    case 9:
                        createWall(x * 64 + 14, y * 64 + 32, 14, 4);
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        break;
                    case 10:
                        createWall(x * 64 + 14, y * 64 + 32, 14, 4);
                        createWall(x * 64 + 32, y * 64 + 46, 4, 18);
                        break;
                    case 11:
                        createWall(x * 64 + 42, y * 64 + 32, 14, 4);
                        createWall(x * 64 + 60, y * 64 + 46, 4, 18);
                        break;
                }
            }
        }
    }

    public void update(float delta) {
        world.step(delta, 10, 5);
    }

    private void createWall(float x, float y, float sizeX, float sizeY) {
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
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public World getWorld() {
        return world;
    }
}
