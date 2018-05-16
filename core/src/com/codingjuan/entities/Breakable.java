package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.LevelManager;
import com.codingjuan.manager.ResourceManager;

import java.util.ArrayList;

public class Breakable extends Object {
    private int lives = 2;
    private TextureRegion damaged;

    public boolean damagedOnCurrentAttack = false;

    public Breakable(Vector2 transform, TextureRegion sprite, TextureRegion damaged) {
        this.transform.set(transform);
        this.sprite = sprite;
        this.damaged = damaged;
        this.bounds.set(transform.x, transform.y, width, height);
    }

    public void damage() {
        if (damagedOnCurrentAttack) return;
        lives--;
        damagedOnCurrentAttack = true;
    }

    @Override
    public void update(float deltaTime) {
        if (lives == 1)
            sprite = damaged;
        if (lives == 0)
            die();
    }

    @Override
    public void die() {
        ResourceManager.getSound("crash").play(ConfigurationManager.getSoundVolume() / 100);
        if (LevelManager.brokenEntities.containsKey(LevelManager.currentLevel))
            LevelManager.brokenEntities.get(LevelManager.currentLevel).add(transform);
        else {
            LevelManager.brokenEntities.put(LevelManager.currentLevel, new ArrayList<Vector2>());
            LevelManager.brokenEntities.get(LevelManager.currentLevel).add(transform);
        }
        dispose();
    }
}
