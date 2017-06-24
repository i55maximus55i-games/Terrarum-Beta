package ru.codemonkeystudio.terrarum;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import ru.codemonkeystudio.terrarum.screens.MainMenuScreen;

public class Terrarum extends Game {
	public SpriteBatch batch;

	//Текстуры для элементов интерфейса
	private TextureAtlas atlas, atlas1;
	public Skin skin;

	//шрифты
	public BitmapFont font16;
	public BitmapFont font24;
	public BitmapFont font32;

	//настройки
	private float musicVolume;
	private float soundVolume;
	private boolean stickControl;

	//локализация
	public I18NBundle bundle;

	@Override
	public void create() {
		batch = new SpriteBatch();

		//загрузка текстур
		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal("textures/textureUI.pack"));
		atlas1 = new TextureAtlas(Gdx.files.internal("textures/textureLU.pack"));
		skin.addRegions(atlas);
		skin.addRegions(atlas1);

		//загрузка шрифтов
		font16 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_16.fnt"), Gdx.files.internal("fonts/Terrarum_16.png"), false);
		font24 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_24.fnt"), Gdx.files.internal("fonts/Terrarum_24.png"), false);
		font32 = new BitmapFont(Gdx.files.internal("fonts/Terrarum_32.fnt"), Gdx.files.internal("fonts/Terrarum_32.png"), false);

		//загрузка настроек
		updatePref();

		//загрузка файлов локализации
		FileHandle baseFileHandle = Gdx.files.internal("i18n/TerrarumBundle");
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		String lang = preferences.getString("lang", "default");
		if (lang.equals("default")) {
			lang = Locale.getDefault().toString();
		}
		Locale locale = new Locale(lang);
		bundle = I18NBundle.createBundle(baseFileHandle, locale);

		//захват кнопки "назад"
		Gdx.input.setCatchBackKey(true);

		//установка экрана главного меню
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		//выгрузка объектов из памяти
		super.dispose();

		batch.dispose();

		atlas.dispose();
		atlas1.dispose();
		skin.dispose();

		font16.dispose();
		font24.dispose();
		font32.dispose();
	}

	//загрузка настроек
	private void updatePref() {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		musicVolume = preferences.getFloat("musicVolume", 0.5f);
		soundVolume = preferences.getFloat("soundVolume", 0.5f);
		stickControl = preferences.getBoolean("stickControl", false);
	}


	//установка настроек
	public void updatePref(float musicVolume, float soundVolume, boolean stickControl) {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
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

	//установка громкости музыки
	public void setMusicVolume(float musicVolume) {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		if (musicVolume > 1f) {
			musicVolume = 1f;
		}
		else if (musicVolume < 0f) {
			musicVolume = 0f;
		}
		preferences.putFloat("musicVolume", musicVolume);
		preferences.flush();
		this.musicVolume = musicVolume;
	}

	//установка громкости звука
	public void setSoundVolume(float soundVolume) {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		if (soundVolume > 1f) {
			soundVolume = 1f;
		}
		else if (soundVolume < 0f) {
			soundVolume = 0f;
		}
		preferences.putFloat("soundVolume", soundVolume);
		preferences.flush();
		this.soundVolume = soundVolume;
	}

	//установка метода управления
	public void setStickControl(boolean stickControl) {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		preferences.putBoolean("stickControl", stickControl);
		preferences.flush();
		this.stickControl = stickControl;
	}

	public void unlockLudum() {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		preferences.putBoolean("old38", true);
		preferences.flush();
	}

	public boolean isLudumUnlocked() {
		Preferences preferences = Gdx.app.getPreferences("Terrarum settings");
		return preferences.getBoolean("old38", false);
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
