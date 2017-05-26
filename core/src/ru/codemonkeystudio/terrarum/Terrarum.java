package ru.codemonkeystudio.terrarum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

public class Terrarum extends Game {
	private float musicVolume;
	private float soundVolume;
	private boolean stickControl;

	private Preferences preferences;

    @Override
	public void render() {
		super.render();
	}

	public SpriteBatch batch;

	@Override
	public void create () {
		preferences = Gdx.app.getPreferences("Terrarum settings");
		updatePref();
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

    private void updatePref() {
        preferences.putFloat("musicVolume", preferences.getFloat("musicVolume", 0.5f));
        musicVolume = preferences.getFloat("musicVolume");
        preferences.putFloat("soundVolume", preferences.getFloat("soundVolume", 0.5f));
        soundVolume = preferences.getFloat("soundVolume");
        preferences.putBoolean("stickControl", preferences.getBoolean("stickControl", false));
        stickControl = preferences.getBoolean("stickControl");
		preferences.flush();
    }

    public void updatePref(float musicVolume, float soundVolume, boolean stickControl) {
		if (musicVolume > 1f) {
			musicVolume = 1f;
		}
		else if (musicVolume < 0f) {
			musicVolume = 0f;
		}
		if (soundVolume > 1f) {
			soundVolume = 1f;
		}
		else if (soundVolume < 0f) {
			soundVolume = 0f;
		}
        preferences.putFloat("musicVolume", musicVolume);
        this.musicVolume = musicVolume;
        preferences.putFloat("soundVolume", soundVolume);
        this.soundVolume = soundVolume;
        preferences.putBoolean("stickControl", stickControl);
        this.stickControl = stickControl;
		preferences.flush();
    }

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public boolean isStickControl() {
		return stickControl;
	}
}
