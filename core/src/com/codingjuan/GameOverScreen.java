package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.GameController;
import com.codingjuan.manager.LevelManager;
import com.codingjuan.manager.ResourceManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

public class GameOverScreen implements Screen{
    TheLegendOfTheSword game;
    Stage stage;

    public GameOverScreen(final TheLegendOfTheSword game) {
        this.game = game;

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable tableMain = new VisTable(true);
        tableMain.setFillParent(true);
        stage.addActor(tableMain);

        VisTextButton continueButton = new VisTextButton("Resume");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameController.gameOver = false;
                if (Gdx.files.local("game.dat").exists()) {
                    game.loadGame();
                } else {
                    game.gameController.restart();
                    LevelManager.currentLevel = "1";
                    LevelManager.clearLevel();
                    game.setScreen(new GameScreen(game));
                }
                Gdx.input.setInputProcessor(game.gameController);
            }
        });

        VisTextButton exitButton = new VisTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameController.gameOver = false;
                VisUI.dispose();
                game.setScreen(game.mainMenuScreen);
                dispose();
            }
        });

        tableMain.row();
        tableMain.add(continueButton).center().width(200).height(100).pad(5);
        tableMain.row();
        tableMain.add(exitButton).center().width(200).height(100).pad(5);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act(delta);
        stage.draw();

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
