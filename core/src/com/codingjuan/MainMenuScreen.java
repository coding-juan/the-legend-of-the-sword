package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.awt.*;

public class MainMenuScreen implements Screen {
    TheLegendOfTheSword game;
    Stage stage;

    MainMenuScreen _this;

    public MainMenuScreen(TheLegendOfTheSword game) {
        this.game = game;
        _this = this;
    }

    @Override
    public void show() {
        if (game.gameController.gameOverMusic.isPlaying())
            game.gameController.gameOverMusic.stop();

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisTextButton newGameButton = new VisTextButton("New game");
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Start game
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        VisTextButton continueButton = new VisTextButton("Continue");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Continue last saved game
                game.setScreen(new GameScreen(game));
                game.loadGame();
                dispose();
            }
        });

        FileHandle file = Gdx.files.local("game.dat");
        continueButton.setDisabled(!file.exists());

        VisTextButton configurationButton = new VisTextButton("Configuration");
        configurationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ConfigurationMenuScreen(game, _this));
                dispose();
            }
        });

        VisTextButton exitButton = new VisTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VisUI.dispose();
                // Todo: Exit the game
                game.dispose();
            }
        });

        table.row();
        table.add(newGameButton).center().width(200).height(100).pad(5);
        table.row();
        table.add(continueButton).center().width(200).height(100).pad(5);
        table.row();
        table.add(configurationButton).center().width(200).height(100).pad(5);
        table.row();
        table.add(exitButton).center().width(200).height(100).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
