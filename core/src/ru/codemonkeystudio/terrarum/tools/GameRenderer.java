package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

import box2dLight.RayHandler;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.objects.Tail;

/**
 * Отрисовщик игрового мира
 */

public class GameRenderer implements Disposable {
    private GameWorld world;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private TerrarumControlHandler controlHandler;

    private RayHandler rayHandler;
    private ArrayList tail;

    //assets
    private Texture worldTexture;
    private TextureRegion[] worldTiles;
    private boolean isNear;
    private Vector2 near;
    private boolean isStickControl;

    //ludum easter egg
    private ShapeRenderer healthBar;
    private int width;
    private int wr, wb;
    private int lives;

    public GameRenderer(SpriteBatch batch, GameWorld gameWorld, ArrayList tail) {
        this.world = gameWorld;
        this.batch = batch;

        this.tail = tail;
        shapeRenderer = new ShapeRenderer();
        controlHandler = new TerrarumControlHandler();

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

        healthBar = new ShapeRenderer();
        width = Gdx.graphics.getWidth();
        wr = wb = 0;
        lives = 10;
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
        Tail a;
        for (Object aTail : tail) {
            a = (Tail) aTail;
            shapeRenderer.circle(a.getPos().x, a.getPos().y, 1.5f * a.getLive() / 30);
        }
        shapeRenderer.end();
        rayHandler.updateAndRender();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.3f);
        if (isNear) {
            shapeRenderer.circle(cam.position.x + near.x * 10, cam.position.y + near.y * 10, 0.5f);
        }
        shapeRenderer.end();
//        debugRenderer.render(world.getWorld(), cam.combined);
        if (isStickControl) {
            controlHandler.stickControl();
        }

        if (lives * width / 5 <= wr) {
            wr -= 10;
        }
        if (wb >= wr) {
            wb -= 6;
        }
        healthBar.begin(ShapeRenderer.ShapeType.Filled);
        healthBar.setColor(Color.WHITE);
        healthBar.rect(0, 0, wb, 10);
        healthBar.setColor(Color.RED);
        healthBar.rect(0, 0, wr, 10);
        healthBar.end();
    }

    private void drawWorld() {
        for (int y = 0; y < world.WORLD_SIZE; y++) {
            for (int x = 0; x < world.WORLD_SIZE; x++) {
                batch.draw(worldTiles[world.getWorldTiles()[x][y].getId()], x * 16 * 4, y * 16 * 4, 16 * 4, 16 * 4);
            }
        }
    }

    public void resize(int width, int height) {
        cam.viewportWidth = 30f;
        cam.viewportHeight = 30f * height / width;
        cam.update();
        controlHandler.resize();
        rayHandler.resizeFBO(width / 2, height / 2);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
        worldTexture.dispose();
        healthBar.dispose();
    }

    public void update(float delta, float camX, float camY, boolean isNear, Vector2 near, boolean isStickControl) {
        this.isNear = isNear;
        this.near = near;
        cam.position.x = camX;
        cam.position.y = camY;
        rayHandler.setCombinedMatrix(cam);
        this.isStickControl = isStickControl;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void createHealthBar() {
        wr = wb = width;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void update(boolean stickControl) {
        this.isStickControl = stickControl;
    }
}
