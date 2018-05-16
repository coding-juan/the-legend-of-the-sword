package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codingjuan.manager.LevelManager;


public class Object implements Actor {
    protected Rectangle bounds = new Rectangle();
    protected Rectangle interactArea = new Rectangle();
    protected Vector2 transform = new Vector2();
    protected InteractArea interactAreaType;
    TextureRegion sprite;

    protected float width = 16f;
    protected float height = 16f;

    protected boolean beingInteractedWith = false;

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getInteractArea() {
        return interactArea;
    }

    public void setInteractArea(Rectangle interactArea) {
        this.interactArea = interactArea;
    }

    public Vector2 getTransform() {
        return transform;
    }

    public void setTransform(Vector2 transform) {
        this.transform = transform;
    }

    public InteractArea getInteractAreaType() {
        return interactAreaType;
    }

    public void setInteractAreaType(InteractArea interactAreaType) {
        this.interactAreaType = interactAreaType;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isBeingInteractedWith() {
        return beingInteractedWith;
    }

    public void setBeingInteractedWith(boolean beingInteractedWith) {
        this.beingInteractedWith = beingInteractedWith;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(sprite, this.transform.x, this.transform.y);
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.set(transform.x, transform.y, width, height);
        if (this.interactAreaType.equals(InteractArea.VERTICAL))
            this.interactArea.set(transform.x, transform.y - height / 2, this.bounds.width, this.bounds.height * 2);
        else if (this.interactAreaType.equals(InteractArea.HORIZONTAL))
            this.interactArea.set(transform.x - width / 2, transform.y, this.bounds.width * 2, this.bounds.height);
        else if (this.interactAreaType.equals(InteractArea.SQUARE))
            this.interactArea.set(transform.x - width / 2, transform.y - height /2, this.bounds.width * 2, this.bounds.height * 2);
    }

    @Override
    public void die() {

    }

    @Override
    public void dispose() {
        bounds = null;
        interactArea = null;
        LevelManager.objects.removeValue(this, false);
    }
}
