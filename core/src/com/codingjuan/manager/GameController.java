package com.codingjuan.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.codingjuan.TheLegendOfTheSword;
import com.codingjuan.entities.*;
import com.codingjuan.entities.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameController implements InputProcessor, Json.Serializable {
    public TheLegendOfTheSword game;
    public DialogueManager dialogueManager;
    public Music gameOverMusic;

    public ArrayList<String> itemsAquired = new ArrayList<String>();
    public int lives = 4;
    public int maxLives = 8;

    Player player;

    public String level = "1";

    public boolean gameOver;

    public GameController(TheLegendOfTheSword game, DialogueManager dialogueManager) {
        this.game = game;
        this.dialogueManager = dialogueManager;

        loadResources();
        gameOverMusic = ResourceManager.getMusic("gameover");

        Gdx.input.setInputProcessor(this);
    }

    private void loadResources() {
        ResourceManager.loadTextures();
        ResourceManager.loadSounds();
        ResourceManager.loadMusic();
    }

    public void restart() {
        lives = 4;
        LevelManager.brokenEntities.clear();
        LevelManager.pickedConsumables.clear();
        itemsAquired.clear();
    }

    public void start() {
        gameOver = false;
        LevelManager.loadMap();
        player = new Player(this, new Vector2(LevelManager.spawnPoint.x - 4, LevelManager.spawnPoint.y));
    }

    public void start(boolean charged) {
        gameOver = false;
        LevelManager.loadMap();
        player = new Player(this, player.getTransform());
    }

    public void gameOver() {
        gameOver = true;
        gameOverMusic.setLooping(true);
        gameOverMusic.setVolume(ConfigurationManager.getSoundVolume() / 100);
        gameOverMusic.play();
    }

    public void resume() {
        // Todo: clearlevel
        start();
    }

    public void update(float deltaTime) {
        if (!gameOver && gameOverMusic.isPlaying())
            gameOverMusic.stop();

        handleInput();

        if (game.paused)
            return;

        player.update(deltaTime);

        // Todo: Check collisions

        // Todo: Check enemies
        for (Bat enemy : LevelManager.enemies)
            enemy.update(deltaTime);

        // Todo: Check Objects
        for (Object object : LevelManager.objects)
            object.update(deltaTime);
    }

    private void handleInput() {
        if (dialogueManager.isDialogReady()) {
            if (player.walking) player.walking = false;
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                dialogueManager.nextParagraph();

            return;
        }

        if (player.attacking) return;

        if (!player.carryingCrock && player.hasSword && Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !player.dead) {
            ResourceManager.getSound("sword").play(ConfigurationManager.getSoundVolume() / 100);
            player.attacking = true;
            player.walking = false;
            return;
        }

        if (isDirectionPressed("UP") && !(isDirectionPressed("DOWN") || isDirectionPressed("RIGHT") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(1, 0));
        } else if (isDirectionPressed("LEFT") && !(isDirectionPressed("RIGHT") || isDirectionPressed("UP") || isDirectionPressed("DOWN"))) {
            player.walking = true;
            player.setDirection(new Vector2(0, -1));
        } else if (isDirectionPressed("DOWN") && !(isDirectionPressed("UP") || isDirectionPressed("RIGHT") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(-1, 0));
        } else if (isDirectionPressed("RIGHT") && !(isDirectionPressed("LEFT") || isDirectionPressed("UP") || isDirectionPressed("DOWN"))) {
            player.walking = true;
            player.setDirection(new Vector2(0, 1));
        } else if (isDirectionPressed("UP") && isDirectionPressed("LEFT") && !(isDirectionPressed("DOWN") || isDirectionPressed("RIGHT"))) {
            player.walking = true;
            player.setDirection(new Vector2(0.5f, -0.5f));
        } else if (isDirectionPressed("LEFT") && isDirectionPressed("UP") && !(isDirectionPressed("DOWN") || isDirectionPressed("RIGHT"))) {
            player.walking = true;
            player.setDirection(new Vector2(0.5f, -0.5f));
        } else if (isDirectionPressed("LEFT") && isDirectionPressed("DOWN") && !(isDirectionPressed("UP") || isDirectionPressed("RIGHT"))) {
            player.walking = true;
            player.setDirection(new Vector2(-0.5f, -0.5f));
        } else if (isDirectionPressed("DOWN") && isDirectionPressed("LEFT") && !(isDirectionPressed("UP") || isDirectionPressed("RIGHT"))) {
            player.walking = true;
            player.setDirection(new Vector2(-0.5f, -0.5f));
        } else if (isDirectionPressed("UP") && isDirectionPressed("RIGHT") && !(isDirectionPressed("DOWN") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(0.5f, 0.5f));
        } else if (isDirectionPressed("RIGHT") && isDirectionPressed("UP") && !(isDirectionPressed("DOWN") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(0.5f, 0.5f));
        } else if (isDirectionPressed("RIGHT") && isDirectionPressed("DOWN") && !(isDirectionPressed("UP") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(-0.5f, 0.5f));
        } else if (isDirectionPressed("DOWN") && isDirectionPressed("RIGHT") && !(isDirectionPressed("UP") || isDirectionPressed("LEFT"))) {
            player.walking = true;
            player.setDirection(new Vector2(-0.5f, 0.5f));
        } else {
            player.walking = false;
        }

        if (player.carryingCrock && player.crock != null && Gdx.input.isKeyJustPressed(Input.Keys.E) && !player.dead) {
            ResourceManager.getSound("throw").play(ConfigurationManager.getSoundVolume() / 100);
            Vector2 throwDir = player.getDirection();
            if (throwDir.equals(new Vector2(0.5f, 0.5f))) throwDir.set(1, 0);
            else if (throwDir.equals(new Vector2(-0.5f, 0.5f))) throwDir.set(-1, 0);
            else if (throwDir.equals(new Vector2(0.5f, -0.5f))) throwDir.set(1, 0);
            else if (throwDir.equals(new Vector2(-0.5f, -0.5f))) throwDir.set(-1, 0);
            player.carryingCrock = false;
            player.crock._throw(throwDir);
            player.crock = null;
        } else if (!player.walking && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            for (Object object : LevelManager.objects) {
                if (player.getBounds().overlaps(object.getInteractArea()) && object instanceof Crock && !player.carryingCrock) {
                    ResourceManager.getSound("lift").play(ConfigurationManager.getSoundVolume() / 100);
                    player.carryingCrock = true;
                    player.crock = (Crock) object;
                }

                if (player.getBounds().overlaps(object.getInteractArea()) && object instanceof Pickable && !player.carryingCrock) {
                    ResourceManager.getSound("item").play(ConfigurationManager.getSoundVolume() / 100);
                    ((Pickable) object).pickup();
                    player.pickupItem(((Pickable) object).item);
                }
            }
            if (player.getBounds().overlaps(LevelManager.savePoint)) {
                dialogueManager.loadDialogue("save");
                game.saveGame(game.gameController);
            }
        }
        for (Object object : LevelManager.objects)
            if (object instanceof Pickable)
                if (itemsAquired.contains(((Pickable) object).item))
                    LevelManager.objects.removeValue(object, false);
    }

    private boolean isDirectionPressed(String direction) {
        if (direction.equals("UP"))
            return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        else if (direction.equals("LEFT"))
            return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        else if (direction.equals("DOWN"))
            return Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
        else if (direction.equals("RIGHT"))
            return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);

        return false;
    }

    public void prepareGame() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void write(Json json) {
        json.writeValue("maxLives", maxLives);
        json.writeValue("lives", lives);
        json.writeValue("level", level);
        json.writeArrayStart("itemsAquired");
        for (String object : itemsAquired)
            json.writeValue(object);
        json.writeArrayEnd();
        json.writeArrayStart("brokenEntities");
        for (Map.Entry<String, ArrayList<Vector2>> entity : LevelManager.brokenEntities.entrySet()) {
            json.writeObjectStart();
            json.writeValue(entity.getKey(), entity.getValue());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();

        json.writeArrayStart("pickedConsumables");
        for (Map.Entry<String, ArrayList<Vector2>> entity : LevelManager.pickedConsumables.entrySet()) {
            json.writeObjectStart();
            json.writeValue(entity.getKey(), entity.getValue());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();

        json.writeValue("playerPos", player.getTransform());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        maxLives = jsonData.get("maxLives").asInt();
        lives = jsonData.get("lives").asInt();
        level = jsonData.get("level").asString();
        itemsAquired = new ArrayList<String>(Arrays.asList(jsonData.get("itemsAquired").asStringArray()));

        Map<String, ArrayList<Vector2>> brokenEntities = new HashMap<String, ArrayList<Vector2>>();
        for (int i = 0; i < jsonData.get("brokenEntities").size; i++) {
            brokenEntities.put(jsonData.get("brokenEntities").get(i).child.name, json.fromJson(ArrayList.class, Vector2.class, jsonData.get("brokenEntities").get(i).child.toString().substring(3)));
        }
        LevelManager.brokenEntities = brokenEntities;

        Map<String, ArrayList<Vector2>> pickedConsumables = new HashMap<String, ArrayList<Vector2>>();
        for (int i = 0; i < jsonData.get("pickedConsumables").size; i++) {
            pickedConsumables.put(jsonData.get("pickedConsumables").get(i).child.name, json.fromJson(ArrayList.class, Vector2.class, jsonData.get("pickedConsumables").get(i).child.toString().substring(3)));
        }
        LevelManager.pickedConsumables = pickedConsumables;

        LevelManager.currentLevel = level;
        player.setTransform(json.fromJson(Vector2.class, jsonData.get("playerPos").toString().substring( jsonData.get("playerPos").toString().indexOf("{"))));
    }
}
