package survive.Engine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tiles.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tiles implements Serializable {
    public static final TileX PLAYER = new TileX('人', new Color(56, 132, 255), new Color(147, 255, 226), "player");
    public static final TileX WALL = new TileX('■', new Color(79, 79, 79), new Color(30, 30, 30),
            "wall");
    public static final TileX FLOOR = new TileX('▒', new Color(119, 61, 0), new Color(145, 74, 0),
            "floor");
    public static final TileX NOTHING = new TileX(' ', Color.black, Color.black, "nothing");
    public static final TileX ENEMY = new TileX('☣', Color.RED, Color.white, "enemy");
    public static final TileX CLOUD = new TileX('☁', new Color(149, 198, 0), new Color(80, 80, 80), "cloud");
    public static final TileX KEY = new TileX('⚿', new Color(255,215,0), new Color(50, 50, 50), "key");
    public static final TileX LOCKED_DOOR = new TileX('㋬', new Color(216, 240, 255), Color.black,
            "locked door");
    public static final TileX UNLOCKED_DOOR = new TileX('▢', new Color(216, 240, 255), Color.black,
            "unlocked door");


    public static final TileX WALL_OLD = new TileX('■', new Color(40, 40, 40), new Color(12, 12, 12),
            "old_wall");
    public static final TileX FLOOR_OLD = new TileX('▒', new Color(70, 30, 0), new Color(124, 63, 0),
            "old_floor");
    public static final TileX CLOUD_OLD = new TileX('☁', new Color(88, 138, 0), new Color(30, 30, 30), "old_star");
    public static final TileX KEY_OLD = new TileX('⚿', new Color(200,165,0), new Color(10, 10, 10), "old_key");
    public static final TileX LOCKED_DOOR_OLD = new TileX('㋬', new Color(150, 180, 190), Color.black,
            "locked door");
    public static final TileX UNLOCKED_DOOR_OLD = new TileX('▢', new Color(150, 180, 190), Color.black,
            "unlocked door");


    public static final TileX GRASS = new TileX('"', Color.green, Color.black, "grass");
    public static final TileX WATER = new TileX('≈', Color.blue, Color.black, "water");
    public static final TileX FLOWER = new TileX('❀', Color.magenta, Color.pink, "flower");
    public static final TileX SAND = new TileX('▒', Color.yellow, Color.black, "sand");
    public static final TileX MOUNTAIN = new TileX('▲', Color.gray, Color.black, "mountain");
    public static final TileX TREE = new TileX('♠', Color.green, Color.black, "tree");
}


