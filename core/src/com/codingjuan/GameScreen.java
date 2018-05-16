package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.ResourceManager;

public class GameScreen implements Screen {
    final TheLegendOfTheSword game;

    public GameScreen(TheLegendOfTheSword game) {
        this.game = game;
    }

    public void load() {
        game.loadGame();
    }

    @Override
    public void show() {
        game.gameController.start();
        game.gameRenderer.start();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.paused = !game.paused;

            if (game.paused) {
                ResourceManager.getSound("pause_open").play(ConfigurationManager.getSoundVolume() / 100);
                game.pauseMenu.configOpen = false;
            } else
                ResourceManager.getSound("pause_closed").play(ConfigurationManager.getSoundVolume() / 100);
        }

        if (!game.paused)
            game.gameController.update(delta);

        Gdx.gl.glClearColor(0,0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.gameRenderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
