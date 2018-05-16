package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Wall implements Actor{
    protected Rectangle bounds = new Rectangle();
    protected Vector2 transform = new Vector2();

    public Wall(Vector2 transform) {
        this.bounds.setHeight(16);
        this.bounds.setWidth(16);
        this.transform = transform;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
//        spriteBatch.draw(s);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void die() {
        return;
    }

    @Override
    public void dispose() {

    }
}
