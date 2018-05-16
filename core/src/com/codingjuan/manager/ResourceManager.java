package com.codingjuan.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static Map<String, TextureRegion> textureMap = new HashMap<String, TextureRegion>();
    private static Map<String, Sound> soundMap = new HashMap<String, Sound>();
    private static Map<String, Music> musicMap = new HashMap<String, Music>();

    public static void loadTextures() {
        loadObjectTextures();
        loadGuiHeartTextures();
    }

    private static void loadTexture(String name, TextureRegion texture) {
        textureMap.put(name, texture);
    }

    private static void loadObjectTextures() {
        Texture idleSheet = new Texture(Gdx.files.internal("tilesets/cave.entities.png"));
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / (idleSheet.getWidth() / 16),
                idleSheet.getHeight() / (idleSheet.getHeight() / 16));

        loadTexture("up_crock", tmp[5][0]);
        loadTexture("up_obstacle", tmp[5][3]);
        loadTexture("up_obstacle_damaged", tmp[5][6]);
    }

    private static void loadItemsTextures() {
        Texture idleSheet = new Texture(Gdx.files.internal("items.png"));
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / (idleSheet.getWidth() / 46),
                idleSheet.getHeight() / (idleSheet.getHeight() / 7));

        loadTexture("pickup_sword", tmp[0][34]);
    }

    private static void loadGuiHeartTextures() {
        Texture idleSheet = new Texture(Gdx.files.internal("piece_of_heart_icon.png"));
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                (idleSheet.getWidth() / 5),
                (idleSheet.getHeight() / 1));

        loadTexture("heart_empty", tmp[0][0]);
        loadTexture("heart_quarter", tmp[0][1]);
        loadTexture("heart_half", tmp[0][2]);
        loadTexture("heart_three_quarters", tmp[0][3]);
        loadTexture("heart_full", tmp[0][4]);
    }

    public static TextureRegion getTexture(String name) {
        return textureMap.get(name);
    }

    public static void loadSounds() {
        loadSound("crash", Gdx.audio.newSound(Gdx.files.internal("sounds/bomb.ogg")));
        loadSound("heart", Gdx.audio.newSound(Gdx.files.internal("sounds/heart.ogg")));
        loadSound("lift", Gdx.audio.newSound(Gdx.files.internal("sounds/lift.ogg")));
        loadSound("pause_closed", Gdx.audio.newSound(Gdx.files.internal("sounds/pause_closed.ogg")));
        loadSound("pause_open", Gdx.audio.newSound(Gdx.files.internal("sounds/pause_open.ogg")));
        loadSound("item", Gdx.audio.newSound(Gdx.files.internal("sounds/picked_item.ogg")));
        loadSound("run", Gdx.audio.newSound(Gdx.files.internal("sounds/running.ogg")));
        loadSound("sword", Gdx.audio.newSound(Gdx.files.internal("sounds/sword1.ogg")));
        loadSound("throw", Gdx.audio.newSound(Gdx.files.internal("sounds/throw.ogg")));
        loadSound("enemy_hurt", Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hurt.ogg")));
        loadSound("enemy_killed", Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_killed.ogg")));
        loadSound("hero_hurt", Gdx.audio.newSound(Gdx.files.internal("sounds/hero_hurt.ogg")));
        loadSound("hero_dying", Gdx.audio.newSound(Gdx.files.internal("sounds/hero_dying.ogg")));
    }

    private static void loadSound(String name, Sound sound) {
        soundMap.put(name, sound);
    }

    public static Sound getSound(String name) {
        return soundMap.get(name);
    }

    public static void loadMusic() {
        musicMap.put("gameover", Gdx.audio.newMusic(Gdx.files.internal("sounds/game_over.mp3")));
    }

    public static Music getMusic(String name) {
        return musicMap.get(name);
    }
}
