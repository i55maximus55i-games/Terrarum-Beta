package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Created by 1 on 24.06.2017.
 */

public class LanguageSelection implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera gamecam;

    //menu assets
    private Sound sound;
    private Stage stage;
    private BitmapFont font_16,font_24,font_32;
    private TextureAtlas atlas;
    private Skin skin;
    private Texture back;

    public LanguageSelection(final Terrarum game){
        stage = new Stage();
        batch = game.batch;
        gamecam = new OrthographicCamera();

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
        font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
        font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

        stage = new Stage(new FitViewport(800,600, gamecam));
        Gdx.input.setInputProcessor(stage);

        final Table table = new Table();
        table.center();
        table.setFillParent(true);

        final Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
        skin.addRegions(atlas);

        final Label label = new Label(game.bundle.get("LSLabel"), new Label.LabelStyle(font_32, Color.WHITE));

        final Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = skin.getDrawable("btn_left");
        exitStyle.down = skin.getDrawable("btn_left_pressed");
        exitStyle.pressedOffsetX = 1;
        exitStyle.pressedOffsetY = -1;

        final Button exit = new Button(exitStyle);
        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sound.play(game.getSoundVolume());
                game.setScreen(new MainMenuScreen(game));
            }
        });

        final List.ListStyle listStyle = new List.ListStyle();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.GRAY;
        listStyle.font = font_24;
        listStyle.selection = skin.getDrawable("knob");

        List langList = new List(listStyle);
        langList.setItems(new String[]{"English","Русский"});

        tableTop.add(exit).size(72, 72).left().expandX();
        tableTop.add(label).center().expandX();
        tableTop.add().size(72, 72).right().expandX();

        table.add(langList).center();

        stage.addActor(tableTop);
        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gamecam.update();
        batch.setProjectionMatrix(gamecam.combined);

        stage.act(delta);
        stage.setDebugAll(true);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
