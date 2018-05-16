package com.codingjuan.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.codingjuan.manager.*;

import java.util.Map;

public class Player extends Character implements Actor {

    public Rectangle[] colDirCheckers = new Rectangle[4];
    public Rectangle attackArea = new Rectangle();

    private TextureRegion idleUp;
    private TextureRegion idleLeft;
    private TextureRegion idleDown;
    private TextureRegion idleRight;
    private TextureRegion idleUpCrock;
    private TextureRegion idleLeftCrock;
    private TextureRegion idleDownCrock;
    private TextureRegion idleRightCrock;

    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkRight;
    private Animation<TextureRegion> walkUpLeft;
    private Animation<TextureRegion> walkDownLeft;
    private Animation<TextureRegion> walkUpRight;
    private Animation<TextureRegion> walkDownRight;

    private Animation<TextureRegion> walkUpCrock;
    private Animation<TextureRegion> walkLeftCrock;
    private Animation<TextureRegion> walkDownCrock;
    private Animation<TextureRegion> walkRightCrock;

    private Animation<TextureRegion> attackUp;
    private Animation<TextureRegion> attackLeft;
    private Animation<TextureRegion> attackDown;
    private Animation<TextureRegion> attackRight;

    private Animation<TextureRegion> swordUp;
    private Animation<TextureRegion> swordLeft;
    private Animation<TextureRegion> swordDown;
    private Animation<TextureRegion> swordRight;


    private Animation<TextureRegion> death;

    public boolean hasSword = false;
    public boolean walking = false;
    public boolean attacking = false;
    public boolean carryingCrock = false;
    public Crock crock = null;

    private float attackTime = 0f;
    private float deathTime = 0f;
    private float walkTime = 0f;
    private float hurtTime = 0f;
    private boolean damagedRender = false;
    public boolean dead = false;

    private Array<Rectangle> tiles = new Array<Rectangle>();
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };

    public Player(GameController gameController, Vector2 position) {
        getTilesPosition(tiles);
        setGameController(gameController);
        this.transform = new Vector2(position.x, position.y);
        bounds.setHeight(this.width - 1);
        bounds.setWidth(this.width - 1);
        bounds.setPosition(transform.x + 4, transform.y);

        colDirCheckers[0] = new Rectangle(bounds.x, bounds.y + bounds.height, bounds.width, 1);
        colDirCheckers[1] = new Rectangle(bounds.x + bounds.width, bounds.y, 1, bounds.height);
        colDirCheckers[2] = new Rectangle(bounds.x, bounds.y - 1, bounds.width, 1);
        colDirCheckers[3] = new Rectangle(bounds.x - 1, bounds.y, 1, bounds.height);

        // Idle states textures
        Texture idleSheet = new Texture(Gdx.files.internal("stopped.tunic.png"));
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / 5,
                idleSheet.getHeight() / 3);
        idleUp = new Sprite(tmp[0][1]);
        idleLeft = new Sprite(tmp[0][3]);
        idleDown = new Sprite(tmp[0][4]);
        idleRight = new Sprite(tmp[0][0]);

        // Idle carrying crock states textures
        Texture idleCrockSheet = new Texture(Gdx.files.internal("carrying.png"));
        tmp = TextureRegion.split(idleCrockSheet,
                idleCrockSheet.getWidth() / 6,
                idleCrockSheet.getHeight() / 9);
        idleUpCrock = new Sprite(tmp[1][0]);
        idleLeftCrock = new Sprite(tmp[0][3]);
        idleDownCrock = new Sprite(tmp[2][0]);
        idleRightCrock = new Sprite(tmp[0][0]);

        // Walking animations
        walkUp = getAnimationFromSheet(new Vector2(1, 0), new Vector2(1, 7));
        walkLeft = getAnimationFromSheet(new Vector2(3, 0), new Vector2(3, 7));
        walkDown = getAnimationFromSheet(new Vector2(4, 0), new Vector2(4, 7));
        walkRight = getAnimationFromSheet(new Vector2(0, 0), new Vector2(0,7));
        walkUpLeft = getAnimationFromSheet(new Vector2(1, 8), new Vector2(1, 10));
        walkDownLeft = getAnimationFromSheet(new Vector2(3, 8), new Vector2(3, 10));
        walkUpRight = getAnimationFromSheet(new Vector2(0, 8), new Vector2(0, 10));
        walkDownRight = getAnimationFromSheet(new Vector2(4, 8), new Vector2(4, 10));

        // Walking Crock animations
        walkUpCrock = getCrockAnimationFromSheet(idleCrockSheet, new Vector2(1, 0), new Vector2(1, 5));
        walkLeftCrock = getCrockAnimationFromSheet(idleCrockSheet, new Vector2(0, 3), new Vector2(0, 5));
        walkDownCrock = getCrockAnimationFromSheet(idleCrockSheet, new Vector2(2, 0), new Vector2(2, 5));
        walkRightCrock = getCrockAnimationFromSheet(idleCrockSheet, new Vector2(0, 0), new Vector2(0,2));

        // Sword attack animations
        attackUp = getAttackingAnimationFromSheet(new Vector2(1, 0), new Vector2(1, 11));
        attackLeft = getAttackingAnimationFromSheet(new Vector2(2, 0), new Vector2(2, 11));
        attackDown = getAttackingAnimationFromSheet(new Vector2(3, 0), new Vector2(3, 11));
        attackRight = getAttackingAnimationFromSheet(new Vector2(0, 0), new Vector2(0, 11));
        swordUp = getSwordAnimationFromSheet(new Vector2(1, 0), new Vector2(1, 11));
        swordLeft = getSwordAnimationFromSheet(new Vector2(2, 0), new Vector2(2, 11));
        swordDown = getSwordAnimationFromSheet(new Vector2(3, 0), new Vector2(3, 11));
        swordRight = getSwordAnimationFromSheet(new Vector2(0, 0), new Vector2(0, 11));

        // Death animation
        death = getDeathAnimationFromSheet(new Vector2(0, 0), new Vector2(0, 9));

        for (String item : gameController.itemsAquired)
            pickupItem(item);
    }

    private Animation<TextureRegion> getAnimationFromSheet(Vector2 startRegion, Vector2 endRegion) {
        Texture walkSheet = new Texture(Gdx.files.internal("walking.tunic.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / 11,
                walkSheet.getHeight() / 15);
        TextureRegion[] frames = new TextureRegion[(int)(endRegion.y - startRegion.y + 1) * (int)(endRegion.x - startRegion.x + 1)];
        int index = 0;
        for (int i = (int)startRegion.x; i <= endRegion.x; i++) {
            for (int j = (int)startRegion.y; j <= endRegion.y; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<TextureRegion>(0.075f, frames);
    }

    private Animation<TextureRegion> getCrockAnimationFromSheet(Texture sheet, Vector2 startRegion, Vector2 endRegion) {
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / 6,
                sheet.getHeight() / 9);
        TextureRegion[] frames = new TextureRegion[(int)(endRegion.y - startRegion.y + 1) * (int)(endRegion.x - startRegion.x + 1)];
        int index = 0;
        for (int i = (int)startRegion.x; i <= endRegion.x; i++) {
            for (int j = (int)startRegion.y; j <= endRegion.y; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<TextureRegion>(0.075f, frames);
    }

    private Animation<TextureRegion> getAttackingAnimationFromSheet(Vector2 startRegion, Vector2 endRegion) {
        Texture attackSheet = new Texture(Gdx.files.internal("sword.tunic1.png"));
        TextureRegion[][] tmp = TextureRegion.split(attackSheet,
                attackSheet.getWidth() / 12,
                attackSheet.getHeight() / 4);
        TextureRegion[] frames = new TextureRegion[(int)(endRegion.y - startRegion.y + 1) * (int)(endRegion.x - startRegion.x + 1)];
        int index = 0;
        for (int i = (int)startRegion.x; i <= endRegion.x; i++) {
            for (int j = (int) startRegion.y; j <= endRegion.y; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<TextureRegion>(0.0325f, frames);
    }

    private Animation<TextureRegion> getSwordAnimationFromSheet(Vector2 startRegion, Vector2 endRegion) {
        Texture swordSheet = new Texture(Gdx.files.internal("sword.sword1.png"));
        TextureRegion[][] tmp = TextureRegion.split(swordSheet,
                swordSheet.getWidth() / 12,
                swordSheet.getHeight() / 4);
        TextureRegion[] frames = new TextureRegion[(int)(endRegion.y - startRegion.y +1) * (int)(endRegion.x - startRegion.x + 1)];
        int index = 0;
        for (int i = (int)startRegion.x; i <= endRegion.x; i++) {
            for (int j = (int) startRegion.y; j <= endRegion.y; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<TextureRegion>(0.0325f, frames);
    }

    private Animation<TextureRegion> getDeathAnimationFromSheet(Vector2 startRegion, Vector2 endRegion) {
        Texture swordSheet = new Texture(Gdx.files.internal("dying.png"));
        TextureRegion[][] tmp = TextureRegion.split(swordSheet,
                swordSheet.getWidth() / 10,
                swordSheet.getHeight() / 3);
        TextureRegion[] frames = new TextureRegion[(int)(endRegion.y - startRegion.y +1) * (int)(endRegion.x - startRegion.x + 1)];
        int index = 0;
        for (int i = (int)startRegion.x; i <= endRegion.x; i++) {
            for (int j = (int) startRegion.y; j <= endRegion.y; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.12f, frames);
    }

    public void pickupItem(String item) {
        if (!gameController.itemsAquired.contains(item)) gameController.itemsAquired.add(item);
        if (item.equals("sword")) {
            // Todo: Change sprites and animation to the ones suposed to the sword, overlap the sword and unblock the attack system
            hasSword = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        this.stateTime += Gdx.graphics.getDeltaTime();
        if (attacking) this.attackTime += Gdx.graphics.getDeltaTime();
        if (dead) this.deathTime += Gdx.graphics.getDeltaTime();

        if (attackUp.isAnimationFinished(attackTime) || attackLeft.isAnimationFinished(attackTime) ||
                attackDown.isAnimationFinished(attackTime) ||attackRight.isAnimationFinished(attackTime)) {
            attacking = false;
            attackTime = 0;
        }

        switch ((int)this.direction.angle()) {
            case 0:
                if (attacking) {
                    currentFrame = attackUp.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordUp.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkUpCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkUp.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleUpCrock;
                    else
                        currentFrame = idleUp;
                break;
            case 45:
                if (attacking) {
                    currentFrame = attackUp.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordUp.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkUpCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkUpRight.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleUpCrock;
                    else
                        currentFrame = idleUp;
                break;
            case 90:
                if (attacking) {
                    currentFrame = attackRight.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordRight.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkRightCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkRight.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleRightCrock;
                    else
                        currentFrame = idleRight;
                break;
            case 135:
                if (attacking) {
                    currentFrame = attackDown.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordDown.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkDownCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkDownRight.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleDownCrock;
                    else
                        currentFrame = idleDown;
                break;
            case 180:
                if (attacking) {
                    currentFrame = attackDown.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordDown.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkDownCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkDown.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleDownCrock;
                    else
                    currentFrame = idleDown;
                break;
            case 225:
                if (attacking) {
                    currentFrame = attackDown.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordDown.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkDownCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkDownLeft.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleDownCrock;
                    else
                        currentFrame = idleDown;
                break;
            case 270:
                if (attacking) {
                    currentFrame = attackLeft.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordLeft.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkLeftCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame =  walkLeft.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleLeftCrock;
                    else
                        currentFrame = idleLeft;
                break;
            case 315:
                if (attacking) {
                    currentFrame = attackUp.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordUp.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkUpCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkUpLeft.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleUpCrock;
                    else
                        currentFrame = idleUp;
                break;
            case 360:
                if (attacking) {
                    currentFrame = attackUp.getKeyFrame(attackTime, false);
                    spriteBatch.draw(swordUp.getKeyFrame(attackTime, false), this.transform.x - 24, this.transform.y - 24);
                    break;
                }
                if (walking)
                    if (carryingCrock)
                        currentFrame = walkUpCrock.getKeyFrame(stateTime, true);
                    else
                        currentFrame = walkUp.getKeyFrame(stateTime, true);
                else
                    if (carryingCrock)
                        currentFrame = idleUpCrock;
                    else
                        currentFrame = idleUp;
                break;
        }

        if (dead)
            currentFrame = death.getKeyFrame(deathTime, false);

        if (dead && deathTime >= (death.getAnimationDuration() * 2) && !gameController.gameOver)
            gameController.gameOver();

        if (hurtTime > 0f && !dead)
            damagedRender = !damagedRender;
        else if (dead)
            damagedRender = false;

        if (hurtTime == 0f && damagedRender)
            damagedRender = false;

        if (!damagedRender)
            spriteBatch.draw(currentFrame, this.transform.x + (12 - currentFrame.getRegionWidth()/2), this.transform.y);

        if (carryingCrock && crock != null)
            crock.render(spriteBatch);

    }

    @Override
    public void update(float deltaTime) {

        if (dead) return;

        for (Map.Entry<Rectangle, String> dialog : LevelManager.dialogs.entrySet()) {
            if (bounds.overlaps(dialog.getKey()) && !gameController.dialogueManager.isDialogReady()) {
                gameController.dialogueManager.loadDialogue(dialog.getValue());
                LevelManager.dialogs.remove(dialog.getKey());
                break;
            }
        }

        if (gameController.lives <= 0 && !dead) {
            die();
        }

        if (hurtTime > 0f)
            hurtTime -= deltaTime;
        else if (hurtTime <= 0f)
            hurtTime = 0f;

        for (Bat enemy : LevelManager.enemies) {
            if (Intersector.overlaps(enemy.detectArea, bounds)) {
                enemy.lockTarget(this);
            }

            if (bounds.overlaps(enemy.bounds) && hurtTime == 0f) {
                ResourceManager.getSound("hero_hurt").play(ConfigurationManager.getSoundVolume() / 100);
                gameController.lives--;
                hurtTime = 2f;
            }
        }

        if (attacking) {
            float attackAnimQuarter = attackDown.getAnimationDuration() / 4;
            if (attackTime >= attackAnimQuarter && attackTime <= attackAnimQuarter * 3) {
                Vector2 dir = new Vector2(direction);
                if (dir.equals(new Vector2(0.5f, 0.5f)) ||dir.equals(new Vector2(0.5f, -0.5f)))
                    dir.set(1f, 0f);
                else if (dir.equals(new Vector2(-0.5f, -0.5f)) ||dir.equals(new Vector2(-0.5f, 0.5f)))
                    dir.set(-1f, 0f);
                attackArea.set(dir.y == 0? (transform.x + height / 4) : (transform.x + (dir.y * 14)), dir.x == 0? (transform.y + width / 4) : (transform.y + (dir.x * 14)),
                        12 + ((dir.y == -1f? 1 : dir.y) * 12), 12 + ((dir.x == -1f? 1 : dir.x) * 12) + 2);
            } else
                attackArea.set(0, 0, 0, 0);

            for (Object object : LevelManager.objects) {
                if (object instanceof Breakable)
                    if (attackArea.overlaps(object.bounds))
                        ((Breakable) object).damage();
                    else
                        ((Breakable) object).damagedOnCurrentAttack = false;
            }

            for (Bat enemy : LevelManager.enemies) {
                if (attackArea.overlaps(enemy.bounds))
                    enemy.hit();
            }
        }

        if (walking) {
            float xMov = (speed - (carryingCrock? speed / 2 : 0)) * MathUtils.sin(direction.angle() * MathUtils.degRad) * deltaTime;
            float yMov = (speed - (carryingCrock? speed / 2 : 0)) * MathUtils.cos(direction.angle() * MathUtils.degRad) * deltaTime;

            Vector2 start = new Vector2(), end = new Vector2();
            bounds.setPosition(bounds.x + xMov, bounds.y + yMov);
            for (Rectangle tile : tiles) {
                if (bounds.overlaps(tile)) {
                    if (colDirCheckers[0].overlaps(tile) || colDirCheckers[2].overlaps(tile)) {
                        yMov = 0;
                        xMov /= 2;
                    }

                    if (colDirCheckers[1].overlaps(tile) || colDirCheckers[3].overlaps(tile)) {
                        xMov = 0;
                        yMov /= 2;
                    }
                }
            }

            for (Object obj : LevelManager.objects) {
                if (obj.equals(crock))
                    continue;
                
                if (bounds.overlaps(obj.getBounds())) {
                    if (colDirCheckers[0].overlaps(obj.getBounds()) || colDirCheckers[2].overlaps(obj.getBounds())) {
                        yMov = 0;
                        xMov /= 2;
                    }

                    if (colDirCheckers[1].overlaps(obj.getBounds()) || colDirCheckers[3].overlaps(obj.getBounds())) {
                        xMov = 0;
                        yMov /= 2;
                    }
                }

                if (getBounds().overlaps(obj.getInteractArea()) && obj instanceof Consumable && gameController.lives < gameController.maxLives) {
                    ResourceManager.getSound("heart").play(ConfigurationManager.getSoundVolume() / 100);
                    for (int i = 0; i < 4; i++) {
                        if (gameController.lives < gameController.maxLives)
                            gameController.lives++;
                    }
                    ((Consumable) obj).pickup();
                }
            }

            for (Map.Entry<Rectangle, String> lvlExit : LevelManager.lvlExits.entrySet()) {
                if (bounds.overlaps(lvlExit.getKey())) {
                    LevelManager.finishLevel(lvlExit.getValue());
                    gameController.level = lvlExit.getValue();
                    gameController.start();
                    gameController.game.gameRenderer.start();
                    break;
                }
            }

            transform.add(xMov, yMov);
            colDirCheckers[0].setPosition(colDirCheckers[0].x + xMov, colDirCheckers[0].y + yMov);
            colDirCheckers[1].setPosition(colDirCheckers[1].x + xMov, colDirCheckers[1].y + yMov);
            colDirCheckers[2].setPosition(colDirCheckers[2].x + xMov, colDirCheckers[2].y + yMov);
            colDirCheckers[3].setPosition(colDirCheckers[3].x + xMov, colDirCheckers[3].y + yMov);
            bounds.setPosition(transform.x + 4, transform.y);

            if (walkTime >= 0.25f && walkTime <= 0.3f) {
                ResourceManager.getSound("run").play((ConfigurationManager.getSoundVolume() / 4) / 100);
                walkTime = 0f;
            }
            walkTime += deltaTime;
        }
        if (carryingCrock && crock != null)
            crock.setTransform(new Vector2(this.transform.x + 4, this.transform.y + 12));
    }

    @Override
    public void die() {
        ResourceManager.getSound("hero_dying").play(ConfigurationManager.getSoundVolume() / 100);
        dead = true;
    }

    @Override
    public void dispose() {

    }

    private void getTilesPosition(Array<Rectangle> tiles) {
        tiles.clear();
        for (int x = 0; x < TiledMapManager.collisionLayer.getWidth(); x++) {
            for (int y = 0; y < TiledMapManager.collisionLayer.getHeight(); y++) {
                if (TiledMapManager.collisionLayer.getCell(x, y) != null) {
                    Rectangle rectangle = rectPool.obtain();
                    rectangle.set(x * 16, y * 16, 16, 16);
                    tiles.add(rectangle);
                }
            }
        }
    }
}
