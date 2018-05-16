package com.codingjuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level1 implements Screen {
    private final int TILES_IN_CAMERA_WIDTH = 16;
    private final int TILES_IN_CAMERA_HEIGHT = 14;
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;

    TheLegendOfTheSword game;
    OrthographicCamera camera;
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    SpriteBatch spriteBatch;

    Character player;

    public Level1(TheLegendOfTheSword game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("levels/test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        spriteBatch = (SpriteBatch)mapRenderer.getBatch();

        mapRenderer.setView(camera);
        player = new Character();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.begin();
        player.render(spriteBatch);
        spriteBatch.end();
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
