package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.ResourceManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

public class PauseMenu implements Screen {
    TheLegendOfTheSword game;
    Stage stage, stage2;

    public boolean configOpen = false;

    public PauseMenu(final TheLegendOfTheSword game) {
        this.game = game;

        configOpen = false;

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
                // Todo: Continue last saved game
                ResourceManager.getSound("pause_closed").play(ConfigurationManager.getSoundVolume() / 100);
                game.paused = false;
                Gdx.input.setInputProcessor(game.gameController);
            }
        });

        VisTextButton configurationButton = new VisTextButton("Configuration");
        configurationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                configOpen = true;
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

        tableMain.row();
        tableMain.add(continueButton).center().width(200).height(100).pad(5);
        tableMain.row();
        tableMain.add(configurationButton).center().width(200).height(100).pad(5);
        tableMain.row();
        tableMain.add(exitButton).center().width(200).height(100).pad(5);

        stage2 = new Stage();

        VisTable tableConfig = new VisTable(true);
        tableConfig.setFillParent(true);
        stage2.addActor(tableConfig);

        final VisLabel volumeLbl = new VisLabel("Sound Volume");
        final VisSlider volumeSld = new VisSlider(0, 100, 1, false);

        final VisLabel soundLbl = new VisLabel("Sound Enabled");
        final VisCheckBox soundCb = new VisCheckBox("");

        final VisLabel debugLbl = new VisLabel("Debug Enabled");
        final VisCheckBox debugCb = new VisCheckBox("");

        volumeSld.setValue(ConfigurationManager.getSoundVolume());
        soundCb.setChecked(ConfigurationManager.isSoundEnabled());
        debugCb.setChecked(ConfigurationManager.isDebugEnabled());

        VisTextButton exitConfigButton = new VisTextButton("Return");
        exitConfigButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ConfigurationManager.setSoundVolume(volumeSld.getValue());
                ConfigurationManager.setSoundEnabled(soundCb.isChecked());
                ConfigurationManager.setDebugEnabled(debugCb.isChecked());
                ConfigurationManager.savePreferences();
                configOpen = false;
            }
        });

        tableConfig.row();
        tableConfig.add(volumeLbl).left().width(200).height(100).pad(5);
        tableConfig.add(volumeSld).right();
        tableConfig.row();
        tableConfig.add(soundLbl).left().width(200).height(100).pad(5);
        tableConfig.add(soundCb).right();
        tableConfig.row();
        tableConfig.add(debugLbl).left().width(200).height(100).pad(5);
        tableConfig.add(debugCb).right();
        tableConfig.row();
        tableConfig.add(exitConfigButton).center().width(200).height(100).pad(5);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (!configOpen) {
            Gdx.input.setInputProcessor(stage);
            stage.act(delta);
            stage.draw();
        } else {
            Gdx.input.setInputProcessor(stage2);
            stage2.act(delta);
            stage2.draw();
        }
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
