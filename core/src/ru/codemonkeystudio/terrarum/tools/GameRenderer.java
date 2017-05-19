package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.RayHandler;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;

/**
 * Created by maximus on 13.05.2017.
 */

public class GameRenderer implements Disposable {
    private GameWorld world;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;

    private RayHandler rayHandler;

    //assets
    private Texture worldTexture;
    private TextureRegion[] worldTiles;

    public GameRenderer(SpriteBatch batch, GameWorld gameWorld) {
        this.world = gameWorld;

        this.batch = batch;
        shapeRenderer = new ShapeRenderer();

        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        cam.zoom = 7;
        debugRenderer = new Box2DDebugRenderer();
        initAssets();

        rayHandler = new RayHandler(world.getWorld());
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(false);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(1);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0);
//        rayHandler.setAmbientLight(0.3f);
    }

    private void initAssets() {
        worldTexture = new Texture("textures/boardTexture.png");
        worldTiles = new TextureRegion[12];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                worldTiles[y * 4 + x] = new TextureRegion(worldTexture, x * 16, y * 16, 16, 16);
                worldTiles[y * 4 + x].flip(false, true);
            }
        }
    }

    public void render() {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
        batch.begin();
        drawWorld();
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(cam.position.x, cam.position.y, Player.SIZE);
        shapeRenderer.end();
        rayHandler.updateAndRender();
//        debugRenderer.render(world.getWorld(), cam.combined);
    }

    private void drawWorld() {
        for (int y = 0; y < GameWorld.WORLD_SIZE; y++) {
            for (int x = 0; x < GameWorld.WORLD_SIZE; x++) {
                batch.draw(worldTiles[world.grid[x][y]], x * 16 * 4, y * 16 * 4, 16 * 4, 16 * 4);
            }
        }
    }

    public void resize(int width, int height) {
        cam.viewportWidth = 30f;
        cam.viewportHeight = 30f * height / width;
        cam.update();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
        worldTexture.dispose();
    }

    public void update(float delta, float camX, float camY) {
        cam.position.x = camX;
        cam.position.y = camY;
        rayHandler.setCombinedMatrix(cam);
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }
}
