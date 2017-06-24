package ru.codemonkeystudio.terrarum.gamemodes;

import java.util.ArrayList;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.objects.Enemy;
import ru.codemonkeystudio.terrarum.objects.Food;
import ru.codemonkeystudio.terrarum.objects.GameWorld;
import ru.codemonkeystudio.terrarum.objects.Player;
import ru.codemonkeystudio.terrarum.tools.GameRenderer;

/**
 * Интерфейс реализующий методы игрового режима
 */

public interface Gamemode {
    void init(Terrarum game, GameRenderer renderer, GameWorld gameWorld, Player player, ArrayList<Food> foods, ArrayList<Enemy> enemies);

    void update(float delta);
    boolean isGameOver();
    void endGame();

    int getScore();
    float getTimer();
    int getWorldSize();

    void dispose();
}
