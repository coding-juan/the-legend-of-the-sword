package com.codingjuan.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.ResourceManager;

public class Crock extends Object {

    private boolean thrown = false;
    private Vector2 throwDir = new Vector2(), throwStart = new Vector2();

    public Crock(InteractArea interactAreaType, Vector2 transform, TextureRegion sprite) {
        this.transform.set(transform);
        this.bounds.set(transform.x, transform.y, width, height);
        this.interactAreaType = interactAreaType;
        if (this.interactAreaType.equals(InteractArea.VERTICAL))
            this.interactArea.set(transform.x, transform.y - height / 2, this.bounds.width, this.bounds.height * 2);
        else if (this.interactAreaType.equals(InteractArea.HORIZONTAL))
            this.interactArea.set(transform.x - width / 2, transform.y, this.bounds.width * 2, this.bounds.height);
        else if (this.interactAreaType.equals(InteractArea.SQUARE))
            this.interactArea.set(transform.x - width / 2, transform.y - height /2, this.bounds.width * 2, this.bounds.height * 2);

        this.sprite = sprite;
    }

    public void _throw(Vector2 dir) {
        thrown = true;
        throwDir.set(dir);
        throwStart.set(transform);
    }

    @Override
    public void update(float deltaTime) {
        if (!thrown)
            super.update(deltaTime);
        else {
            Vector2 end = new Vector2(throwStart.x + (50 * MathUtils.sin(throwDir.angle() * MathUtils.degRad)), throwStart.y + (50 * MathUtils.cos(throwDir.angle() * MathUtils.degRad)));
            float distance = Vector2.dst(throwStart.x, throwStart.y, end.x, end.y);
            Vector2 dir = new Vector2((throwStart.x - end.x) * -1, (throwStart.y - end.y) * -1);
            Vector2 _new = new Vector2();
            _new.set(dir.x * 3.75f * deltaTime, dir.y * 3.75f * deltaTime);
            transform.add(_new);
            if (Vector2.dst(transform.x, transform.y, throwStart.x, throwStart.y) >= distance) {
                transform.set(end);
                die();
            }
        }
    }

    @Override
    public void die() {
        ResourceManager.getSound("crash").play(ConfigurationManager.getSoundVolume() / 100);
        dispose();
    }
}
