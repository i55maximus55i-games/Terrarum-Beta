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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.codemonkeystudio.terrarum.Terrarum;
import ru.codemonkeystudio.terrarum.gamemodes.ArcadeGamemode;
import ru.codemonkeystudio.terrarum.gamemodes.LudumGamemode;

/**
 * Экран главного меню
 */
public class MainMenuScreen implements Screen {
	private SpriteBatch batch;
	private OrthographicCamera gamecam;

	//menu assets
	private Sound sound;
    private Stage stage;
    private BitmapFont font_16,font_24,font_32;
	private TextureAtlas atlas;
	private Skin skin;
    private Texture back;

	private boolean ludum;

	public MainMenuScreen(final Terrarum game) {
		stage = new Stage();
		batch = game.batch;
		gamecam = new OrthographicCamera();

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

        back = new Texture("textures/back.jpg");

		font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
		font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
		font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

		stage = new Stage(new FitViewport(800,600, gamecam));
		Gdx.input.setInputProcessor(stage);

		final Table table = new Table();
		table.center();
		table.setFillParent(true);

		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
		skin.addRegions(atlas);

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font_16;
        final Label label = new Label("Are you sure left the game?", labelStyle);
        label.setAlignment(1);

		ImageButton.ImageButtonStyle iconstyle = new ImageButton.ImageButtonStyle();
		iconstyle.up = skin.getDrawable("icon_terrarum");

		TextButton.TextButtonStyle newGameStyle = new TextButton.TextButtonStyle();
		newGameStyle.font = font_24;
		newGameStyle.up = skin.getDrawable("btn_default");
		newGameStyle.over = skin.getDrawable("btn_active");
		newGameStyle.down = skin.getDrawable("btn_pressed");
		newGameStyle.pressedOffsetX = 1;
		newGameStyle.pressedOffsetY = -1;

		TextButton.TextButtonStyle statisticStyle = new TextButton.TextButtonStyle();
		statisticStyle.font = font_24;
		statisticStyle.up = skin.getDrawable("btn_default");
		statisticStyle.over = skin.getDrawable("btn_active");
		statisticStyle.down = skin.getDrawable("btn_pressed");
		statisticStyle.pressedOffsetX = 1;
		statisticStyle.pressedOffsetY = -1;

		final TextButton.TextButtonStyle settingsStyle = new TextButton.TextButtonStyle();
		settingsStyle.font = font_24;
		settingsStyle.up = skin.getDrawable("btn_default");
		settingsStyle.over = skin.getDrawable("btn_active");
		settingsStyle.down = skin.getDrawable("btn_pressed");
		settingsStyle.pressedOffsetX = 1;
		settingsStyle.pressedOffsetY = -1;

		TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
		exitStyle.font = font_24;
		exitStyle.up = skin.getDrawable("btn_default");
		exitStyle.over = skin.getDrawable("btn_active");
		exitStyle.down = skin.getDrawable("btn_pressed");
		exitStyle.pressedOffsetX = 1;
		exitStyle.pressedOffsetY = -1;

		ImageButton icon = new ImageButton(iconstyle);
		icon.setSize(364, 126);
		icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

		TextButton newGame = new TextButton(game.bundle.get("newGameLabel"), newGameStyle);
		newGame.setSize(260, 90);
		newGame.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
		newGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new GameScreen(game, new ArcadeGamemode(), false));
			}
		});

		TextButton statistic = new TextButton(game.bundle.get("statisticLabel"), statisticStyle);
		statistic.setSize(260, 90);
		statistic.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*3, 1);
		statistic.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new StatisticScreen(game));
			}
		});

		TextButton settings = new TextButton(game.bundle.get("settingsLabel"), settingsStyle);
		settings.setSize(260, 90);
		settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);
		settings.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new SettingsScreen(game));
			}
		});

		TextButton exit = new TextButton(game.bundle.get("exitLabel"), exitStyle);
		exit.setSize(260, 90);
		exit.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
		exit.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				sound.play(game.getSoundVolume());
				Gdx.app.exit();
			}
		});

		table.add(icon).size(364, 126).row();
		table.add(newGame).size(260, 90).row();
		table.add(statistic).size(260, 90).row();
		table.add(settings).size(260, 90).row();
		table.add(exit).size(260, 90).row();

		stage.addActor(table);
	}

	public MainMenuScreen(final Terrarum game, final boolean ludum) {
		stage = new Stage();
		this.batch = game.batch;
		gamecam = new OrthographicCamera();

		this.ludum = ludum;

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));

		font_16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
		font_24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
		font_32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

		stage = new Stage(new FitViewport(800,600, gamecam));
		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		table.center();
		table.setFillParent(true);

		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal("textures/textureLU.pack"));
		skin.addRegions(atlas);

		ImageButton.ImageButtonStyle iconstyle = new ImageButton.ImageButtonStyle();
		iconstyle.up = skin.getDrawable("Terrarum");

		TextButton.TextButtonStyle newGameStyle = new TextButton.TextButtonStyle();
		newGameStyle.font = font_24;
		newGameStyle.up = skin.getDrawable("newGame_ps");
		newGameStyle.down = skin.getDrawable("newGame_ac");
		newGameStyle.pressedOffsetX = 1;
		newGameStyle.pressedOffsetY = -1;

		TextButton.TextButtonStyle statisticStyle = new TextButton.TextButtonStyle();
		statisticStyle.font = font_24;
		statisticStyle.up = skin.getDrawable("achievments_ps");
		statisticStyle.down = skin.getDrawable("achievments_ac");
		statisticStyle.pressedOffsetX = 1;
		statisticStyle.pressedOffsetY = -1;

		TextButton.TextButtonStyle settingsStyle = new TextButton.TextButtonStyle();
		settingsStyle.font = font_24;
		settingsStyle.up = skin.getDrawable("settings_ps");
		settingsStyle.down = skin.getDrawable("settings_ac");
		settingsStyle.pressedOffsetX = 1;
		settingsStyle.pressedOffsetY = -1;

		TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
		exitStyle.font = font_24;
		exitStyle.up = skin.getDrawable("exit_ps");
		exitStyle.down = skin.getDrawable("exit_ac");
		exitStyle.pressedOffsetX = 1;
		exitStyle.pressedOffsetY = -1;

		ImageButton icon = new ImageButton(iconstyle);
		icon.setSize(364, 126);
		icon.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*5, 1);

		TextButton newGame = new TextButton("", newGameStyle);
		newGame.setSize(260, 90);
		newGame.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*4, 1);
		newGame.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				stage.dispose();
				sound.play(game.getSoundVolume());
				game.setScreen(new GameScreen(game, new LudumGamemode(), ludum));
			}
		});

		TextButton statistic = new TextButton("", statisticStyle);
		statistic.setSize(260, 90);
		statistic.setPosition(stage.getWidth() / 2, (stage.getHeight() / 6) * 3, 1);

		TextButton settings = new TextButton("", settingsStyle);
		settings.setSize(260, 90);
		settings.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*2, 1);

		TextButton exit = new TextButton("", exitStyle);
		exit.setSize(260, 90);
		exit.setPosition(stage.getWidth()/2, (stage.getHeight()/6)*1, 1);
		exit.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sound.play(game.getSoundVolume());
				stage.dispose();
				Gdx.app.exit();
			}
		});

		table.add(icon).size(364, 126).row();
		table.add(newGame).size(260, 90).row();
		table.add(statistic).size(260, 90).row();
		table.add(settings).size(260, 90).row();
		table.add(exit).size(260, 90).row();

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

		if (!ludum) {
			batch.begin();
			batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}

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
		sound.dispose();
		stage.dispose();
		atlas.dispose();
		skin.dispose();
		back.dispose();

		font_16.dispose();
		font_24.dispose();
		font_32.dispose();
	}
}

