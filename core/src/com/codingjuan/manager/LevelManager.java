package com.codingjuan.manager;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.codingjuan.entities.*;
import com.codingjuan.entities.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    public static final String LEVEL_DIR = "levels";
    public static final String LEVEL_PRFEIX = "level";
    public static final String LEVEL_EXTENSION = ".tmx";

    public static ArrayList<String> levelsCleared = new ArrayList<String>();
    public static Map<String, ArrayList<Vector2>> brokenEntities = new HashMap<String, ArrayList<Vector2>>();
    public static Map<String, ArrayList<Vector2>> pickedConsumables = new HashMap<String, ArrayList<Vector2>>();
    public static Map<Rectangle, String> dialogs = new HashMap<Rectangle, String>();

    public static Array<Object> objects = new Array<Object>();
    public static Array<Bat> enemies = new Array<Bat>();
    public static Rectangle spawnPoint = new Rectangle();
    public static Map<Rectangle, String> lvlExits = new HashMap<Rectangle, String>();
    public static Rectangle savePoint = new Rectangle(0, 0, 0, 0);

    public static TiledMap map;
//    public static int currentLevel = 1;
    public static String currentLevel = "1";

//    public static void passLevel() {
//        currentLevel++;
//    }

    public static void setLevel(String level) {
        currentLevel = level;
    }

    public static void loadMap() {
        LevelManager.map = new TmxMapLoader().load(LEVEL_DIR + "/" + LEVEL_PRFEIX + currentLevel + LEVEL_EXTENSION);
        TiledMapManager.collisionLayer = (TiledMapTileLayer) LevelManager.map.getLayers().get("Walls");
        TiledMapManager.objectLayer = (MapLayer) LevelManager.map.getLayers().get("Objects");
        // Todo: Load tiles with more logic
        loadObjects();
    }

    public static void loadObjects() {
        Object object = null;

        objectlopp:
        for (MapObject obj : TiledMapManager.objectLayer.getObjects()) {
            if (obj instanceof TextureMapObject) {
                Rectangle rectangle = new Rectangle(((TextureMapObject) obj).getX(), ((TextureMapObject) obj).getY(), 16, 16);

                if (obj.getProperties().containsKey("breakable")) {
                    if (brokenEntities.get(currentLevel) != null)
                        for (Vector2 breakable : brokenEntities.get(currentLevel))
                            if (breakable.x == rectangle.x && breakable.y == rectangle.y)
                                continue objectlopp;

                    object = new Breakable(new Vector2(rectangle.x, rectangle.y), ((TextureMapObject) obj).getTextureRegion(), ResourceManager.getTexture("up_obstacle_damaged"));
                } else if (obj.getProperties().containsKey("pickable")) {
                    object = new Pickable(new Vector2(rectangle.x, rectangle.y), ((TextureMapObject) obj).getTextureRegion(), obj.getProperties().get("item").toString());
                } else if (obj.getProperties().containsKey("holdable")) {
                    InteractArea interactArea = InteractArea.SQUARE;
                    if (obj.getProperties().get("interaction").equals("vertical"))
                        interactArea = InteractArea.VERTICAL;
                    else if (obj.getProperties().get("interaction").equals("horizontal"))
                        interactArea = InteractArea.HORIZONTAL;
                    else if (obj.getProperties().get("interaction").equals("square"))
                        interactArea = InteractArea.SQUARE;

                    object = new Crock(interactArea, new Vector2(rectangle.x, rectangle.y), ((TextureMapObject) obj).getTextureRegion());
                } else if (obj.getProperties().containsKey("consumable")) {
                    if (pickedConsumables.get(currentLevel) != null)
                        for (Vector2 consumable : pickedConsumables.get(currentLevel))
                            if (consumable.x == rectangle.x && consumable.y == rectangle.y)
                                continue objectlopp;
                    object = new Consumable(new Vector2(rectangle.x, rectangle.y), ((TextureMapObject) obj).getTextureRegion());
                }

                LevelManager.objects.add(object);
            } else if (obj instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) obj).getRectangle();

                if (obj.getProperties().containsKey("spawnpoint"))
                    spawnPoint.set(rectangle);
                else if (obj.getProperties().containsKey("exitlevel"))
                    lvlExits.put(rectangle, obj.getProperties().get("nextlvl").toString());
                else if (obj.getProperties().containsKey("savepoint"))
                    savePoint.set(rectangle);
                else if (obj.getProperties().containsKey("bat"))
                    enemies.add(new Bat( new Vector2(rectangle.x, rectangle.y)));
                else if (obj.getProperties().containsKey("dialog"))
                    dialogs.put(rectangle, obj.getProperties().get("name").toString());
            }
        }
    }

    public static void clearLevel() {
        // Todo: Clear enemies/items/interactables/etc
        spawnPoint.set(new Rectangle());
        objects.clear();
        lvlExits.clear();
        enemies.clear();
//        brokenEntities.clear();
//        pickedConsumables.clear();
    }

    public static void finishLevel(String newLevel) {
        // Todo: Play exit level sound
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        levelsCleared.add(currentLevel);
        currentLevel = newLevel;
        LevelManager.clearLevel();
    }
}
