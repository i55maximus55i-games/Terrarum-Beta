package ru.codemonkeystudio.terrarum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

public class Terrarum extends Game {
	private float musicVolume;
	private float soundVolume;
	private boolean stickControl;

	private Preferences preferences;

	public I18NBundle bundle;

	@Override
	public void render() {
		super.render();
	}

	public SpriteBatch batch;

	@Override
	public void create () {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/TerrarumBundle");
		Locale locale = new Locale(Locale.getDefault().toString());
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
//		bundle = I18NBundle.createBundle(baseFileHandle, new Locale("ru"));
		preferences = Gdx.app.getPreferences("Terrarum settings");
		updatePref();
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
		Gdx.input.setCatchBackKey(true);
	}

    private void updatePref() {
        musicVolume = preferences.getFloat("musicVolume", 0.5f);
        preferences.putFloat("musicVolume", musicVolume);

        soundVolume = preferences.getFloat("soundVolume", 0.5f);
        preferences.putFloat("soundVolume", soundVolume);

        stickControl = preferences.getBoolean("stickControl", false);
        preferences.putBoolean("stickControl", stickControl);

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
