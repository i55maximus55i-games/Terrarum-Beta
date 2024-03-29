package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 * Класс, отвечающий за музыкальное сопровождение
 */

public class MusicPlayer implements Disposable {
    private ArrayList<Music> playList;
    private Music music;
    private int mus;
    private float volume;

    private boolean isPlaying;

    public MusicPlayer(float volume) {
        initMusic();
        setVolume(volume);
    }

    private void initMusic() {
        ArrayList<Music> m = new ArrayList<Music>();
        for (int i = 0; i < Gdx.files.internal("music").list().length; i++) {
            m.add(Gdx.audio.newMusic(Gdx.files.internal(Gdx.files.internal("music").list()[i].file().getPath())));
        }

        playList = new ArrayList<Music>();
        while (m.size() > 0) {
            int r = (int) (Math.random() * m.size());
            playList.add(m.get(r));
            m.remove(r);
        }

        mus = 0;
        if (playList.size() > 0) {
            music = playList.get(mus);
        }
        changeMusic();
        isPlaying = true;
    }

    private void changeMusic() {
        if (playList.size() > 0) {
            mus++;
            if (mus >= playList.size())
                mus = 0;
            music.stop();
            music = playList.get(mus);
            music.setVolume(volume);
            music.play();
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (music != null) {
            music.setVolume(volume);
        }
    }

    public void update() {
        if (isPlaying && music != null && !music.isPlaying()) {
            changeMusic();
        }
        else if (!isPlaying && music.isPlaying()) {
            music.stop();
        }
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (!isPlaying && music.isPlaying()) {
            music.stop();
        }
    }

    @Override
    public void dispose() {
        if (music != null) {
            music.stop();
        }
        for (Music m : playList) {
            m.dispose();
        }
        while (playList.size() > 0) {
            playList.remove(0);
        }
    }

    public void pause() {
        if (music != null) {
            music.pause();
        }
    }

    public void unpause() {
        if (music != null) {
            music.play();
        }
    }
}
