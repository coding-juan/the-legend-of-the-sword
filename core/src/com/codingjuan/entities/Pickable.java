package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Pickable extends Object {

    public String item;

    public Pickable(Vector2 transform, TextureRegion sprite, String item) {
        this.transform.set(transform);
        this.interactArea.set(transform.x, transform.y, width, height);
        this.sprite = sprite;
        this.item = item;
    }

    @Override
    public void update(float deltaTime) {

    }

    public void pickup() {
        // Todo: Play pickup sound
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
