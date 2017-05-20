package ru.codemonkeystudio.terrarum.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
	private TextButton Exit, NewGame, Achievements, Settings;
	private TextButton.TextButtonStyle ExitStyle, NewGameStyle, AchievementsStyle, SettingsStyle;
	private BitmapFont font_16,font_24,font_32;
	private Label label;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;

	public MainMenuScreen(Terrarum game) {
		stage = new Stage();
		this.game = game;
		this.batch = game.batch;
		gamecam = new OrthographicCamera();

		gamePort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gamecam);

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

		font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
		font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
		font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(800,600, gamecam));
		Gdx.input.setInputProcessor(stage);

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

		AchievementsStyle = new TextButton.TextButtonStyle();
		AchievementsStyle.font = font_16;
		AchievementsStyle.up = skin.getDrawable("btn_default");
		AchievementsStyle.over = skin.getDrawable("btn_active");
		AchievementsStyle.down = skin.getDrawable("btn_pressed");
		AchievementsStyle.pressedOffsetX = 1;
		AchievementsStyle.pressedOffsetY = -1;

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
		icon.setSize(156, 54);
		icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

		NewGame = new TextButton("New Game", NewGameStyle);
		NewGame.setSize(156, 54);
		NewGame.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
		NewGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play();
				game.setScreen(new GameScreen(game));
			}
		});

		Achievements = new TextButton("Achievements", AchievementsStyle);
		Achievements.setSize(156, 54);
		Achievements.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*3, 1);
		Achievements.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				stage.dispose();
				sound.play();
//				game.setScreen(new Achievements(game));
			}
		});

		Settings = new TextButton("Settings", SettingsStyle);
		Settings.setSize(156, 54);
		Settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);
		Settings.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play();
				game.setScreen(new SettingsScreen(game));
			}
		});

		Exit = new TextButton("Exit", ExitStyle);
		Exit.setSize(156, 54);
		Exit.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
		Exit.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play();
				Gdx.app.exit();
			}
		});

		stage.addActor(icon);
		stage.addActor(NewGame);
		stage.addActor(Achievements);
		stage.addActor(Settings);
		stage.addActor(Exit);
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gamecam.update();
		batch.setProjectionMatrix(gamecam.combined);

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
