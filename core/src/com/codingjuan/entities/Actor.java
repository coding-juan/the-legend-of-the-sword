package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Actor {
    public void render(SpriteBatch spriteBatch);
    public void update(float deltaTime);
    public void die();
    public void dispose();
}
