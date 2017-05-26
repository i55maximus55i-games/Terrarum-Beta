package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.codemonkeystudio.terrarum.Terrarum;

/**
 * Created by maximus on 22.04.17.
 */
public class MainMenuScreen implements Screen {
	private SpriteBatch batch;
	private Terrarum game;
	private OrthographicCamera gamecam;
	private Viewport gamePort;

	//menu assets
	private Sound sound;
	private ImageButton icon;
	private ImageButton.ImageButtonStyle iconstyle;
	private Stage stage;
	private TextButton Exit, NewGame, Statistic, Settings;
	private TextButton.TextButtonStyle ExitStyle, NewGameStyle, StatisticStyle, SettingsStyle;
	private BitmapFont font_16,font_24,font_32;
	private Label label;
	private TextureAtlas atlas;
	private Skin skin;
    private Texture back;

	public MainMenuScreen(Terrarum game) {
		stage = new Stage();
		this.game = game;
		this.batch = game.batch;
		gamecam = new OrthographicCamera();

		gamePort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gamecam);

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        back = new Texture("textures/back.jpg");

		Table table = new Table();
		table.top();
		table.setFillParent(true);

		font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
		font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
		font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(800,600, gamecam));
		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		table.center();
		table.setFillParent(true);

		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
		skin.addRegions(atlas);

		iconstyle = new ImageButton.ImageButtonStyle();
		iconstyle.up = skin.getDrawable("icon_terrarum");

		NewGameStyle = new TextButton.TextButtonStyle();
		NewGameStyle.font = font_24;
		NewGameStyle.up = skin.getDrawable("btn_default");
		NewGameStyle.over = skin.getDrawable("btn_active");
		NewGameStyle.down = skin.getDrawable("btn_pressed");
		NewGameStyle.pressedOffsetX = 1;
		NewGameStyle.pressedOffsetY = -1;

		StatisticStyle = new TextButton.TextButtonStyle();
		StatisticStyle.font = font_24;
		StatisticStyle.up = skin.getDrawable("btn_default");
		StatisticStyle.over = skin.getDrawable("btn_active");
		StatisticStyle.down = skin.getDrawable("btn_pressed");
		StatisticStyle.pressedOffsetX = 1;
		StatisticStyle.pressedOffsetY = -1;

		SettingsStyle = new TextButton.TextButtonStyle();
		SettingsStyle.font = font_24;
		SettingsStyle.up = skin.getDrawable("btn_default");
		SettingsStyle.over = skin.getDrawable("btn_active");
		SettingsStyle.down = skin.getDrawable("btn_pressed");
		SettingsStyle.pressedOffsetX = 1;
		SettingsStyle.pressedOffsetY = -1;

		ExitStyle = new TextButton.TextButtonStyle();
		ExitStyle.font = font_24;
		ExitStyle.up = skin.getDrawable("btn_default");
		ExitStyle.over = skin.getDrawable("btn_active");
		ExitStyle.down = skin.getDrawable("btn_pressed");
		ExitStyle.pressedOffsetX = 1;
		ExitStyle.pressedOffsetY = -1;

		icon = new ImageButton(iconstyle);
		icon.setSize(364, 126);
		icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

		NewGame = new TextButton("New game", NewGameStyle);
		NewGame.setSize(260, 90);
		NewGame.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
		NewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new GameScreen(game));
			}
		});

		Statistic = new TextButton("Statistic", StatisticStyle);
		Statistic.setSize(260, 90);
		Statistic.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*3, 1);
		Statistic.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				sound.play(game.getSoundVolume());
			}
		});

		Settings = new TextButton("Settings", SettingsStyle);
		Settings.setSize(260, 90);
		Settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);
		Settings.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new SettingsScreen(game));
			}
		});

		Exit = new TextButton("Exit", ExitStyle);
		Exit.setSize(260, 90);
		Exit.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
		Exit.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sound.play(game.getSoundVolume());
				stage.dispose();
				Gdx.app.exit();
			}
		});

		table.add(icon).size(364, 126).row();
		table.add(NewGame).size(260, 90).row();
		table.add(Statistic).size(260, 90).row();
		table.add(Settings).size(260, 90).row();
		table.add(Exit).size(260, 90).row();

		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gamecam.update();
		batch.setProjectionMatrix(gamecam.combined);

		batch.begin();
		batch.draw();
		batch.end();

		stage.act(delta);
		stage.setDebugAll(false);
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
