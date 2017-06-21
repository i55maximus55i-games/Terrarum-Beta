package ru.codemonkeystudio.terrarum.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.Enemy;
import ru.codemonkeystudio.terrarum.objects.Food;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.screens.GameOverScreen;
import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;

/**
 * Created by maximus on 21.06.2017.
 */

public class LudumGamemode implements Gamemode {
    private Terrarum game;

    //звуки
    private Sound eatSound;
    private Sound winSound;
    private Sound loseSound;

    //вспомогательные объекты
    private GameRenderer renderer;

    //игровые объекты
    private GameWorld gameWorld;
    private Player player;
    private ArrayList<Food> foods;

    //счётчики
    private float timer;
    private boolean destroyed;

    @Override
    public void init(Terrarum game, GameRenderer renderer, GameWorld gameWorld, Player player, ArrayList<Food> foods, ArrayList<Enemy> enemies) {
        this.game = game;

        this.renderer = renderer;
        renderer.createHealthBar();
        this.gameWorld = gameWorld;

        this.player = player;
        player.setLives(5);
        this.foods = foods;

        eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/lose.mp3"));

        timer = 0;
        destroyed = false;

        foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), 64 * gameWorld.WORLD_SIZE - 16, 64 * gameWorld.WORLD_SIZE - 16));
    }

    @Override
    public void update(float delta) {
        timer += delta;
        renderer.setLives(player.getLives());

        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).update(delta, false);
            if (foods.get(i).isAlive() && foods.get(i).getBody().getPosition().dst(player.getBody().getPosition()) < 10) {
                foods.get(i).die(gameWorld.getWorld());
                if (i == 0) {
                    eatSound.play(game.getSoundVolume());
                }
            }
        }
        if (foods.get(0).getDeathTime() > 0.4f && !destroyed) {
            foods.get(0).destroy();
            destroyed = true;
            foods.add(new Food(gameWorld.getWorld(), renderer.getRayHandler(), 16, 16));
        }
    }

    @Override
    public boolean isGameOver() {
        boolean isOver = false;
        if (player.getLives() < 0) {
            isOver = true;
        }
        if (destroyed && !foods.get(1).isAlive()) {
            isOver = true;
        }
        return isOver;
    }

    @Override
    public void endGame() {
        if (player.getLives() < 0) {
            loseSound.play();
        }
        if (destroyed && !foods.get(1).isAlive()) {
            winSound.play();
        }
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public float getTimer() {
        return timer;
    }

    @Override
    public int getWorldSize() {
        return 8;
    }

    @Override
    public void dispose() {
        eatSound.dispose();
        winSound.dispose();
        loseSound.dispose();
    }
}
