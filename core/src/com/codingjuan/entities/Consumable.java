package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.codingjuan.manager.LevelManager;

import java.util.ArrayList;

public class Consumable extends Object {
    public Consumable(Vector2 transform, TextureRegion sprite) {
        this.transform.set(transform);
        this.interactArea.set(transform.x, transform.y, width, height);
        this.sprite = sprite;
    }

    @Override
    public void update(float deltaTime) {

    }

    public void pickup() {
        if (LevelManager.pickedConsumables.containsKey(LevelManager.currentLevel))
            LevelManager.pickedConsumables.get(LevelManager.currentLevel).add(transform);
        else {
            LevelManager.pickedConsumables.put(LevelManager.currentLevel, new ArrayList<Vector2>());
            LevelManager.pickedConsumables.get(LevelManager.currentLevel).add(transform);
        }
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
