
package com.raishazahra.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioSystem {

    private static AudioSystem instance;

    private final Map<String, Sound> sounds = new HashMap<>();
    private Music bgm;
    private boolean enabled = true;
    private float sfxVolume = 0.8f;
    private float bgmVolume = 0.5f;

    private AudioSystem() {
    }

    public static AudioSystem getInstance() {
        if (instance == null) {
            instance = new AudioSystem();
        }
        return instance;
    }

    public void loadSound(String key, String path) {
        if (sounds.containsKey(key)) return;

        if (Gdx.files.internal(path).exists()) {
            sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
        } else {
            Gdx.app.error("AudioSystem", "Sound not found: " + path);
        }
    }

    public void playSound(String key) {
        Sound sound = sounds.get(key);

        if (enabled && sound != null) {
            sound.play(sfxVolume);
        }
    }

    public void loadBgm(String path) {
        stopBgm();

        if (Gdx.files.internal(path).exists()) {
            bgm = Gdx.audio.newMusic(Gdx.files.internal(path));
            bgm.setLooping(true);
            bgm.setVolume(bgmVolume);
        } else {
            Gdx.app.error("AudioSystem", "BGM not found: " + path);
        }
    }

    public void playBgm() {
        if (enabled && bgm != null && !bgm.isPlaying()) {
            bgm.play();
        }
    }

    public void stopBgm() {
        if (bgm != null) {
            bgm.stop();
            bgm.dispose();
            bgm = null;
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled) {
            stopBgm();
        } else {
            playBgm();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setSfxVolume(float volume) {
        sfxVolume = Math.max(0f, Math.min(1f, volume));
    }

    public void setBgmVolume(float volume) {
        bgmVolume = Math.max(0f, Math.min(1f, volume));

        if (bgm != null) {
            bgm.setVolume(bgmVolume);
        }
    }

    public void dispose() {
        stopBgm();

        for (Sound sound : sounds.values()) {
            sound.dispose();
        }

        sounds.clear();
        instance = null;
    }
}
