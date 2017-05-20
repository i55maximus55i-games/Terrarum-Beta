package ru.codemonkeystudio.terrarum.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.screens.GameScreen;
import ru.codemonkeystudio.terrarum.screens.PauseScreen;

/**
 * Created by maximus on 13.05.2017.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private float timer;
    private Terrarum game;
    private Label timeLabel;
    private Label timerLabel;
    private Label liveLabel;
    private Label livesLabel;
    private Label foodLabel;
    private Label foodsLabel;
    private Skin skin;
    private TextureAtlas atlas;
    private Image heart_alive, heart_dead;
    private Button Exit;
    private Button.ButtonStyle ExitStyle;
    private Sound sound;

    private BitmapFont font_16,font_24,font_32;

    public Hud(SpriteBatch batch, final Terrarum game, final GameScreen screen) {
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
        
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label("Time", new Label.LabelStyle(font_32, Color.WHITE));
        timerLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        liveLabel = new Label("Lives", new Label.LabelStyle(font_32, Color.WHITE));
        livesLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        foodLabel = new Label("Food", new Label.LabelStyle(font_32, Color.WHITE));
        foodsLabel = new Label("", new Label.LabelStyle(font_32, Color.WHITE));
        heart_alive = new Image(skin, "icon_heart_alive");
        heart_dead = new Image(skin, "icon_heart_dead");
        heart_alive.setSize(60, 60);

        skin = new Skin();
        atlas = new TextureAtlas("textures/textureUI.pack");
        skin.addRegions(atlas);

        ExitStyle = new Button.ButtonStyle();
        ExitStyle.up = skin.getDrawable("btn_pause");
        ExitStyle.down = skin.getDrawable("btn_pause_pressed");
        ExitStyle.pressedOffsetX = 1;
        ExitStyle.pressedOffsetY = -1;

        Exit = new Button(ExitStyle);
        Exit.setSize(72, 72);
        Exit.setPosition(Exit.getWidth()/2, stage.getHeight() - Exit.getHeight()/2, 1);
        Exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play();
                screen.musicPlayer.setPlaying(false);
                game.setScreen(new PauseScreen(game));
            }
        });

        stage.addActor(Exit);

        table.add(liveLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(foodLabel).expandX().padTop(10);
        table.row();
        table.add(livesLabel).expandX();
        table.add(timerLabel).expandX();
        table.add(foodsLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta, int lives, int food) {
        timer += delta;
        int t = (int) timer;
        timerLabel.setText(String.format("%02d", t / 60) + ":" + (String.format("%02d", t % 60)));
        livesLabel.setText(Integer.toString(lives));
        foodsLabel.setText(Integer.toString(food));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
