package com.codingjuan;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.codingjuan.entities.Dialogue;
import com.codingjuan.manager.DialogueManager;
import com.codingjuan.manager.GameController;
import com.codingjuan.manager.GameRenderer;
import com.codingjuan.manager.LevelManager;

public class TheLegendOfTheSword extends Game {

	public GameController gameController;
	public GameRenderer gameRenderer;
	public DialogueManager dialogueManager;

	public MainMenuScreen mainMenuScreen;
	public PauseMenu pauseMenu;
	public GameOverScreen gameOverScreen;
	public boolean paused;
	
	@Override
	public void create () {
		gameOverScreen = new GameOverScreen(this);
		dialogueManager = new DialogueManager();
		gameController = new GameController(this, dialogueManager);
		gameRenderer = new GameRenderer(gameController);

		paused = false;

		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
		pauseMenu = new PauseMenu(this);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameRenderer.resize(width, height);
	}

	@Override
	public void dispose () {
		gameRenderer.dispose();
		System.exit(0);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	public void saveGame(GameController controller) {
		Json json = new Json();
		String save = new Json().prettyPrint(controller);
		FileHandle file = Gdx.files.local("game.dat");
		file.writeString(save, false);
	}

	public void loadGame() {
		FileHandle file = Gdx.files.local("game.dat");
		String save = file.readString();
		Json json = new Json();
		json.setSerializer(GameController.class, new Json.ReadOnlySerializer<GameController>() {
			@Override
			public GameController read(Json json, JsonValue jsonData, Class type) {
				gameController.read(json, jsonData);
				return gameController;
			}
		});
		gameController = json.fromJson(GameController.class, save);
		LevelManager.clearLevel();
		gameController.start(true);
		gameRenderer.start();
	}
}
