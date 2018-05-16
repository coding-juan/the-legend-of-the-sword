package com.codingjuan.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.codingjuan.entities.Bat;
import com.codingjuan.entities.Object;

import java.util.Map;

public class GameRenderer implements Disposable{
    private final int TILES_IN_CAMERA_WIDTH = 16;
    private final int TILES_IN_CAMERA_HEIGHT = 14;
    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;

    public OrthographicCamera camera;
    public static float CAMERA_OFFSET = 0;
    public SpriteBatch spriteBatch;
    private GameController gameController;

    OrthogonalTiledMapRenderer mapRenderer;

    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        // Todo: fonts

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        Gdx.gl.glCullFace(GL20.GL_CULL_FACE);

        start();
    }

    public void start() {
        mapRenderer = new OrthogonalTiledMapRenderer(LevelManager.map);
        spriteBatch = (SpriteBatch) mapRenderer.getBatch();
    }

    public void render() {
        // Todo: Set camera position to be centered in the player
        camera.position.set(gameController.player.getTransform().x + 18 / 2, gameController.player.getTransform().y + 18 / 2, 0);

        camera.zoom = 1 / 2f;
        camera.update();
        mapRenderer.setView(camera);
        int[] layers = new int[LevelManager.map.getLayers().size()-1];
        for (int i = 0; i < layers.length; i++) layers[i] = i;
        mapRenderer.render(layers);

        spriteBatch.begin();
        // Todo: Render NPCs
        for (Bat enemy : LevelManager.enemies) {
            enemy.render(spriteBatch);
        }

        // Todo: Render objects
        for (Object object : LevelManager.objects) {
            object.render(spriteBatch);
        }
        gameController.player.render(spriteBatch);

        // Todo: Render GUI
        if (gameController.lives == 8) {
            spriteBatch.draw(ResourceManager.getTexture("heart_full"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);
            spriteBatch.draw(ResourceManager.getTexture("heart_full"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
        }
        else if (gameController.lives >= 4) {
            spriteBatch.draw(ResourceManager.getTexture("heart_full"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);
            if (gameController.lives == 5)
                spriteBatch.draw(ResourceManager.getTexture("heart_quarter"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
            else if (gameController.lives == 6)
                spriteBatch.draw(ResourceManager.getTexture("heart_half"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
            else if (gameController.lives == 7)
                spriteBatch.draw(ResourceManager.getTexture("heart_three_quarters"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
            else
                spriteBatch.draw(ResourceManager.getTexture("heart_empty"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
        } else {
            if (gameController.lives == 1)
                spriteBatch.draw(ResourceManager.getTexture("heart_quarter"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);
            else if (gameController.lives == 2)
                spriteBatch.draw(ResourceManager.getTexture("heart_half"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);
            else if (gameController.lives == 3)
                spriteBatch.draw(ResourceManager.getTexture("heart_three_quarters"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);
            else
                spriteBatch.draw(ResourceManager.getTexture("heart_empty"), camera.position.x - (camera.viewportWidth / 4), camera.position.y +(camera.viewportHeight / 4) - 16);

            spriteBatch.draw(ResourceManager.getTexture("heart_empty"), camera.position.x - (camera.viewportWidth / 4) + 18, camera.position.y +(camera.viewportHeight / 4) - 16);
        }

        if (gameController.dialogueManager.isDialogReady())
            gameController.dialogueManager.render(spriteBatch, camera);

        spriteBatch.end();
        if (ConfigurationManager.isDebugEnabled()) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(gameController.player.getBounds().x, gameController.player.getBounds().y, gameController.player.getBounds().width, gameController.player.getBounds().height);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for (Rectangle colCheck : gameController.player.colDirCheckers) {
                shapeRenderer.rect(colCheck.x, colCheck.y, colCheck.width, colCheck.height);
            }
            shapeRenderer.setColor(Color.WHITE);
            for (Object object : LevelManager.objects) {
                shapeRenderer.rect(object.getTransform().x, object.getTransform().y, object.getWidth(), object.getHeight());
            }
            shapeRenderer.setColor(Color.YELLOW);
            for (Object object : LevelManager.objects) {
                shapeRenderer.rect(object.getInteractArea().x, object.getInteractArea().y, object.getInteractArea().width, object.getInteractArea().height);
            }
            for (Map.Entry<Rectangle, String> lvlExit : LevelManager.lvlExits.entrySet())
                shapeRenderer.rect(lvlExit.getKey().x, lvlExit.getKey().y, lvlExit.getKey().width, lvlExit.getKey().height);
            for (Bat enemy : LevelManager.enemies) {
                shapeRenderer.circle(enemy.detectArea.x, enemy.detectArea.y, enemy.detectArea.radius);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(enemy.getBounds().x, enemy.getBounds().y, enemy.getBounds().width, enemy.getBounds().height);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(enemy.attackArea.x, enemy.attackArea.y, enemy.attackArea.radius);
            }
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(gameController.player.attackArea.x, gameController.player.attackArea.y, gameController.player.attackArea.width, gameController.player.attackArea.height);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(LevelManager.savePoint.x, LevelManager.savePoint.y, LevelManager.savePoint.width, LevelManager.savePoint.height);
            shapeRenderer.end();
        }

        if (gameController.game.paused) {
            // Todo: Render pause menu
            gameController.game.pauseMenu.render(Gdx.graphics.getDeltaTime());
        }

        if (gameController.gameOver) {
            gameController.game.gameOverScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.update();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        // Todo: dispose fonts?
    }
}
