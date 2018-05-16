package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codingjuan.manager.ConfigurationManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

public class ConfigurationMenuScreen implements Screen {
    TheLegendOfTheSword game;
    Stage stage;
    MainMenuScreen mainMenuScreen;

    public ConfigurationMenuScreen(TheLegendOfTheSword game, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        final VisLabel volumeLbl = new VisLabel("Sound Volume");
        final VisSlider volumeSld = new VisSlider(0, 100, 1, false);

        final VisLabel soundLbl = new VisLabel("Sound Enabled");
        final VisCheckBox soundCb = new VisCheckBox("");

        final VisLabel debugLbl = new VisLabel("Debug Enabled");
        final VisCheckBox debugCb = new VisCheckBox("");

        volumeSld.setValue(ConfigurationManager.getSoundVolume());
        soundCb.setChecked(ConfigurationManager.isSoundEnabled());
        debugCb.setChecked(ConfigurationManager.isDebugEnabled());

        VisTextButton exitButton = new VisTextButton("Return");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VisUI.dispose();
                ConfigurationManager.setSoundVolume(volumeSld.getValue());
                ConfigurationManager.setSoundEnabled(soundCb.isChecked());
                ConfigurationManager.setDebugEnabled(debugCb.isChecked());
                ConfigurationManager.savePreferences();
                game.setScreen(mainMenuScreen);
            }
        });

        table.row();
        table.add(volumeLbl).left().width(200).height(100).pad(5);
        table.add(volumeSld).right();
        table.row();
        table.add(soundLbl).left().width(200).height(100).pad(5);
        table.add(soundCb).right();
        table.row();
        table.add(debugLbl).left().width(200).height(100).pad(5);
        table.add(debugCb).right();
        table.row();
        table.add(exitButton).center().width(200).height(100).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
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
