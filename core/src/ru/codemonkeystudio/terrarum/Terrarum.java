package ru.codemonkeystudio.terrarum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

public class Terrarum extends Game {
	@Override
	public void render() {
		super.render();
	}

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}
