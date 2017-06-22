package ru.codemonkeystudio.terrarum.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Игровой мир
 */

public class GameWorld implements Disposable{
    public final int WORLD_SIZE;

    private WorldTile[][] worldTiles;

    private World world;

    private float timer;

    public GameWorld(int worldSize) {
        WORLD_SIZE = worldSize;
        timer = 0;
        world = new World(new Vector2(0, 0), true);
        worldTiles = new WorldTile[WORLD_SIZE][WORLD_SIZE];
        genWorld();
        createWalls();
    }

    private void genWorld() {
        int[][] grid = new int[WORLD_SIZE][WORLD_SIZE];
        List<Integer> cells = new ArrayList<Integer>();
        for (int i = 0; i < (int)(WORLD_SIZE * WORLD_SIZE * 2.625 / 12); i++) {
            for (int j = 0; j < 12; j++) {
                cells.add(j);
            }
        }

        for (int y = 0; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                int r = (int) (Math.random() * cells.size());
                grid[x][y] = cells.get(r);
                cells.remove(r);
            }
        }

        for (int y = 0; y < WORLD_SIZE - 1; y++) {
            for (int x = 0; x < WORLD_SIZE - 1; x++) {
                if (grid[x][y] == 7 && grid[x + 1][y] == 7) {
                    do {
                        int r = (int) (Math.random() * cells.size());
                        grid[x + 1][y] = cells.get(r);
                        if (cells.get(r) == 7) {
                            cells.remove(r);
                        }
                    } while (grid[x + 1][y] == 7);
                }
            }
        }

        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int y = 0; y < WORLD_SIZE - 1; y++) {
                if (grid[x][y] == 0 && grid[x][y + 1] == 0) {
                    do {
                        int r = (int) (Math.random() * cells.size());
                        grid[x][y + 1] = cells.get(r);
                        if (cells.get(r) == 0) {
                            cells.remove(r);
                        }
                    } while (grid[x][y + 1] == 0);
                }
            }
        }

        do {
            int r = (int) (Math.random() * cells.size());
            grid[1][0] = cells.get(r);
            if (cells.get(r) == 0) {
                cells.remove(r);
            }
        } while (grid[1][0] == 0);

        int yy = WORLD_SIZE - 1;
        for (int x = 0; x < WORLD_SIZE; x++) {
            if (grid[x][yy] == 0 || grid[x][yy] == 5 || grid[x][yy] == 9 || grid[x][yy] == 10) {
                do {
                    int r = (int) (Math.random() * cells.size());
                    grid[x][yy] = cells.get(r);
                    if (grid[x][yy] == 0 || grid[x][yy] == 5 || grid[x][yy] == 9 || grid[x][yy] == 10) {
                        cells.remove(r);
                    }
                } while (grid[x][yy] == 0 || grid[x][yy] == 5 || grid[x][yy] == 9 || grid[x][yy] == 10);
            }
        }
        for (int y = 0; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                worldTiles[x][y] = new WorldTile(world, grid[x][y], x, y);
            }
        }
    }

    private void destroyWorld() {
        for (int y = 0; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                worldTiles[x][y].destroy(world);
            }
        }
    }

    private void createWalls() {
        createWall(-4, 32 * WORLD_SIZE, 4, 32 * WORLD_SIZE + 8);
        createWall(4 + 64 * WORLD_SIZE, 32 * WORLD_SIZE,4, 32 * WORLD_SIZE + 8);
        createWall(32 * WORLD_SIZE, - 4,32 * WORLD_SIZE, 4);
        createWall(32 * WORLD_SIZE, 4 + 64 * WORLD_SIZE, 32 * WORLD_SIZE, 4);
    }

    public void update(float delta, Vector2 playerPos, boolean regen) {
        timer += delta;
        if (timer > 4 && regen) {
            int xx = (int) (playerPos.x / 64);
            int yy = (int) (playerPos.y / 64);
            int[][] grid = new int[WORLD_SIZE][WORLD_SIZE];
            for (int y = 0; y < WORLD_SIZE; y++) {
                for (int x = 0; x < WORLD_SIZE; x++) {
                    grid[x][y] = -1;
                }
            }
            for (int y = yy - 1; y <= yy + 1; y++) {
                for (int x = xx - 1; x <= xx + 1; x++) {
                    if (x >= 0 && y >= 0 && x < WORLD_SIZE && y < WORLD_SIZE) {
                        grid[x][y] = worldTiles[x][y].getId();
                    }
                }
            }
            destroyWorld();
            genWorld();
            for (int y = yy - 1; y <= yy + 1; y++) {
                for (int x = xx - 1; x <= xx + 1; x++) {
                    if (x >= 0 && y >= 0 && x < WORLD_SIZE && y < WORLD_SIZE) {
                        worldTiles[x][y].destroy(world);
                        worldTiles[x][y] = new WorldTile(world, grid[x][y], x, y);
                    }
                }
            }
            timer -= 4;
        }
        world.step(delta, 50, 25);
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

    public WorldTile[][] getWorldTiles() {
        return worldTiles;
    }
}
