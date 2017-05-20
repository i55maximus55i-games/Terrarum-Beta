package ru.codemonkeystudio.terrarum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.codemonkeystudio.terrarum.screens.GameScreen;
import ru.codemonkeystudio.terrarum.tools.MusicPlayer;

public class Terrarum extends Game {
	public MusicPlayer musicPlayer;

	@Override
	public void render() {
		super.render();
	}

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		musicPlayer = new MusicPlayer(0.2f);
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		musicPlayer.dispose();
	}
}
