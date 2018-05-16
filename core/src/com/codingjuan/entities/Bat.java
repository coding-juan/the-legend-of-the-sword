package com.codingjuan.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.codingjuan.ai.BehaviourState;
import com.codingjuan.ai.BehaviourTree;
import com.codingjuan.manager.ConfigurationManager;
import com.codingjuan.manager.LevelManager;
import com.codingjuan.manager.ResourceManager;

public class Bat extends Character implements Actor {

    Animation<TextureRegion> animation;
    BehaviourTree behaviourTree;
    Player target;
    int lives = 2;

    public Circle detectArea = new Circle();
    public Circle attackArea = new Circle();

    Vector2 patrolCenter = new Vector2();
    Vector2 attackStart = new Vector2(0, 0);
    Vector2 knockBackStart = new Vector2(0, 0);
    private final float patrolRotateSpeed = 1f;
    private final float patrolRadius = 30.1f;
    private float patrolAngle;

    public Bat(Vector2 position) {
        behaviourTree = new BehaviourTree(BehaviourState.PATROLING);

        this.transform.set(position);
        this.patrolCenter.set(position);
        Texture walkSheet = new Texture(Gdx.files.internal("bat.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / 2,
                walkSheet.getHeight() / 1);
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = tmp[0][0];
        frames[1] = tmp[0][1];
        animation = new Animation<TextureRegion>(0.25f, frames);
        bounds.set(this.transform.x, this.transform.y, tmp[0][0].getRegionWidth(), tmp[0][0].getRegionHeight());
        detectArea.set(this.transform.x + (bounds.getWidth() / 2), this.transform.y + (bounds.getHeight() / 2), 75);
        attackArea.set(this.transform.x + (bounds.getWidth() / 2), this.transform.y + (bounds.getHeight() / 2), 50f);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        this.stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, this.transform.x + (12 - currentFrame.getRegionWidth()/2), this.transform.y);
    }

    public void patrol(float deltaTime) {
        patrolAngle += patrolRotateSpeed * deltaTime;
        Vector2 offset = new Vector2(MathUtils.sin(patrolAngle) * patrolRadius, MathUtils.cos(patrolAngle) * patrolRadius);

        transform.set(patrolCenter.x + offset.x, patrolCenter.y + offset.y);
    }

    public void lockTarget(Player target) {
        this.target = target;
        behaviourTree.targetLocked = true;
    }

    public void approach(float deltaTime) {
        if (target == null) return;

        Vector2 dir = new Vector2((transform.x - target.transform.x) * -1, (transform.y - target.transform.y) * -1);
        Vector2 _new = new Vector2();
        _new.set(dir.x * 0.5f * deltaTime, dir.y * 0.5f * deltaTime);
        transform.add(_new);
    }

    public void setToAttack() {
        if (target != null)
            behaviourTree.atRange = Intersector.overlaps(attackArea, target.bounds);
    }

    public void attack(float deltaTime) {
        if (target == null) return;
        if (attackStart.equals(new Vector2(0, 0))) {
            attackStart.set(transform.x, transform.y);
        }
        Vector2 dir = new Vector2((attackStart.x - target.transform.x) * -1, (attackStart.y - target.transform.y) * -1);
        Vector2 end = new Vector2(attackStart.x + (100 * MathUtils.cos(dir.angle() * MathUtils.degRad)), attackStart.y + (100 * MathUtils.sin(dir.angle() * MathUtils.degRad)));
        dir = new Vector2((attackStart.x - end.x) * -1, (attackStart.y - end.y) * -1);
        float distance = Vector2.dst(attackStart.x, attackStart.y, end.x, end.y);
        Vector2 _new = new Vector2();
        _new.set(dir.x * 3.5f * deltaTime, dir.y * 3.5f * deltaTime);
        transform.add(_new);
        if (Vector2.dst(transform.x, transform.y, attackStart.x, attackStart.y) >= distance) {
            transform.set(end);
            attackStart.set(0, 0);
            behaviourTree.attackCompleted();
        }
    }

    public void hit() {
        if (!behaviourTree.getState().equals(BehaviourState.HIT)) {
            behaviourTree.hit();
            lives--;
            if (lives <= 0) {
                die();
            } else
                ResourceManager.getSound("enemy_hurt").play(ConfigurationManager.getSoundVolume() / 100);
        }
    }

    public void knockBack(float deltaTime) {
        if (knockBackStart.equals(new Vector2(0, 0))) {
            knockBackStart.set(transform.x, transform.y);
        }
        Vector2 dir = new Vector2((target.transform.x - knockBackStart.x) * -1, (target.transform.y - knockBackStart.y) * -1);
        Vector2 end = new Vector2(knockBackStart.x + (75 * MathUtils.cos(dir.angle() * MathUtils.degRad)), knockBackStart.y + (75 * MathUtils.sin(dir.angle() * MathUtils.degRad)));
        dir = new Vector2((knockBackStart.x - end.x) * -1, (knockBackStart.y - end.y) * -1);
        float distance = Vector2.dst(knockBackStart.x, knockBackStart.y, end.x, end.y);
        Vector2 _new = new Vector2();
        _new.set(dir.x * 5.5f * deltaTime, dir.y * 5.5f * deltaTime);
        transform.add(_new);
        if (Vector2.dst(transform.x, transform.y, knockBackStart.x, knockBackStart.y) >= distance) {
            transform.set(end);
            knockBackStart.set(0, 0);
            behaviourTree.knockBackCompleted();
        }
    }

    public void stand(float deltaTime) {

    }

    @Override
    public void update(float deltaTime) {
        behaviourTree.update(deltaTime);

        switch (behaviourTree.getState()) {
            case PATROLING:
                patrol(deltaTime);
                break;

            case TARGET_LOCKED:
                stand(deltaTime);
                break;

            case APPROACHING:
                approach(deltaTime);
                break;

            case ATTACKING:
                attack(deltaTime);
                break;

            case ATTACK_COMPLETE:
                stand(deltaTime);
                break;

            case HIT:
                knockBack(deltaTime);
                break;
        }

        setToAttack();
        bounds.set(this.transform.x, this.transform.y, bounds.getWidth(), bounds.getHeight());
        detectArea.set(this.transform.x + (bounds.getWidth() / 2), this.transform.y + (bounds.getHeight() / 2), 75);
        attackArea.set(this.transform.x + (bounds.getWidth() / 2), this.transform.y + (bounds.getHeight() / 2), 50f);
    }

    @Override
    public void die() {
        ResourceManager.getSound("enemy_killed").play(ConfigurationManager.getSoundVolume() / 100);
        dispose();
    }

    @Override
    public void dispose() {
        bounds = null;
        attackArea = null;
        detectArea = null;
        LevelManager.enemies.removeValue(this, false);
    }
}
