package survive.Core;

import survive.Engine.;
import survive.Engine.Tiles;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private Tuple oldPos;
    private Tuple pos;
    protected static boolean alive = true;
    protected static boolean win = false;
    private boolean hasKey = false;
    private String oldTile = "";
    private int hunger = 100;

    private static int round = 0;

    // run keyboard game
    public void run() {
        fogOfWar();
        Game.ter.renderFrame(Game.renWorld);
        while (alive && !win) {
            gui();
            input();
        }
    }

    public void run(String input) {
        if (!Game.a.isEmpty()) {
            Game.a.removeFirst();
        }
        while (Game.a.size() > 0) {
            processStringInput();
            Game.ter.renderFrame(Game.world);
        }
    }

    public Player() {
        pos = oldPos = Map.getRooms().get(0).calcCenter();
        Game.world[pos.x][pos.y] = Tiles.PLAYER;
    }

    private void updatePlayer() {
        Game.world[oldPos.x][oldPos.y] = Tiles.FLOOR;
        Game.world[pos.x][pos.y] = Tiles.PLAYER;
        hunger--;
        if (hunger == 0) {
            alive = false;
        }
    }

    // takes input
    // Using linked list allows for :Q to work more efficiently, could add close to 0 complexity as
    // it is constant time to access the first values;
    private void input() {
        if (StdDraw.hasNextKeyTyped()) {
            Game.a.add(Character.toLowerCase(StdDraw.nextKeyTyped()));
            if (Game.a.peekFirst() == ':') {
                colonQ();
            }
            enemyMove();
            letterCheck(Game.a.removeFirst());
            updatePlayer();
            fogOfWar();
            Game.ter.renderFrame(Game.renWorld);
            round++;
        }
    }

    // moves the player
    private void move(Tuple vec) {
        if (canMove(vec)) {
            oldPos = pos;
            pos = new Tuple(pos.x + vec.x, pos.y + vec.y);
        }
    }

    // checks if can move in direction
    private boolean canMove(Tuple vec) {
        int newX = pos.x + vec.x;
        int newY = pos.y + vec.y;
        if (Game.world[newX][newY].equals(Tiles.ENEMY)) {
            alive = false;
        } else if (Game.world[newX][newY].equals(Tiles.CLOUD)) {
            hunger += 15;
            return true;
        } else if (Game.world[newX][newY].equals(Tiles.KEY)) {
            hasKey = true;
            replaceLockedDoor();
            return true;
        } else if (Game.world[newX][newY].equals(Tiles.UNLOCKED_DOOR) && hasKey) {
            win = true;
            return true;
        }
        return Game.world[newX][newY].equals(Tiles.FLOOR);
    }

    // checks the quit case
    private void colonQ() {
        Game.a.removeFirst();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Game.a.add(Character.toLowerCase(StdDraw.nextKeyTyped()));
                if (Game.a.peekFirst() == 'q') {
                    save();
                    System.exit(0);
                } else {
                    break;
                }
            }
        }
    }

    private void processStringInput() {
        if (Game.a.peekFirst() == ':') {
            colonQString();
        }
        enemyMove();
        letterCheck(Game.a.removeFirst());
        updatePlayer();
        round++;
    }

    // checks the quit case
    private void colonQString() {
        Game.a.removeFirst();
        if (Game.a.peekFirst() == 'q') {
            save();
        }
    }

    private void letterCheck(char letter) {
        switch (letter) {
            case 'w': move(new Tuple(0, 1)); break;
            case 'a': move(new Tuple(-1, 0)); break;
            case 's': move(new Tuple(0, -1)); break;
            case 'd': move(new Tuple(1, 0)); break;
            default: break;
        }
    }

    // find position of mouse and check what tile it is
    private void mouseRead() {
        int x = (int) Math.floor(StdDraw.mouseX());
        int y = (int) Math.floor(StdDraw.mouseY());
        if (y < Game.HEIGHT) {
            displayInfo(tileType(Game.renWorld[x][y]));
        }
    }

    // checks the tile type
    private String tileType(TileX x) {
        if (x.equals(Tiles.PLAYER)) {
            return "player";
        } else if (x.equals(Tiles.ENEMY)) {
            return "enemy";
        } else if (x.equals(Tiles.CLOUD) || x.equals(Tiles.CLOUD_OLD)) {
            return "food";
        } else if (x.equals(Tiles.LOCKED_DOOR) || x.equals(Tiles.LOCKED_DOOR_OLD)) {
            return "locked_door";
        } else if (x.equals(Tiles.UNLOCKED_DOOR) || x.equals(Tiles.UNLOCKED_DOOR_OLD)) {
            return "unlocked_door";
        } else if (x.equals(Tiles.KEY) || x.equals(Tiles.KEY_OLD)) {
            return "key";
        } else if (x.equals(Tiles.FLOOR) || x.equals(Tiles.FLOOR_OLD)) {
            return "floor";
        } else if (x.equals(Tiles.WALL) || x.equals(Tiles.WALL_OLD)) {
            return "wall";
        } else if (x.equals(Tiles.NOTHING)) {
            return "nothing";
        }
        return "";
    }

    // add bar later, for now, displays the tile on the top left
    private void displayInfo(String x) {
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, Game.HEIGHT, "Current tile: " + x);
        if (!oldTile.equals(x)) {
            Game.ter.renderFrame(Game.renWorld);
            StdDraw.show();
        }
        oldTile = x;
        StdDraw.show();
    }

    private void enemyMove() {
        for (Enemy e: Game.enemies) {
            e.randomMove();
            e.updateEnemy();
        }
    }

    private void gui() {
        StdDraw.setFont(new Font("Comic Sans", Font.BOLD, 15));
        mouseRead();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT, "Hunger: " + hunger);
    }

    /**
     * places a door not next to the walls, but somewhere inside the random room
     */
    public void placeObject(TileX type) {
        boolean placed = false;
        Room room = Map.getRooms().get(Game.random.nextInt(Map.getRooms().size() - 1) + 1);
        Tuple c1 = room.get(1);
        Tuple c2 = room.get(4);
        int x1 = c1.x + 2;
        int x2 = c2.x - 2;
        int y1 = c1.y + 2;
        int y2 = c2.y - 2;
        while (!placed) {
            int w = Game.random.nextInt(x2 - x1) + x1;
            int h = Game.random.nextInt(y2 - y1) + y1;
            if (Game.world[w][h].equals(Tiles.FLOOR)) {
                Game.world[w][h] = type;
                placed = true;
            }
        }
    }

    private void replaceLockedDoor() {
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (Game.world[i][j].equals(Tiles.LOCKED_DOOR) || Game.world[i][j].equals(Tiles.LOCKED_DOOR_OLD)) {
                    Game.world[i][j] = Tiles.UNLOCKED_DOOR;
                }
            }
        }
    }

    public void fogOfWar() {
        int visionRadius = hunger / 15;
        if (visionRadius >= 7) {
            visionRadius = 7;
        } else if (visionRadius <= 3) {
            visionRadius = 3;
        }
        Vision.renderWorld(visionRadius);
    }

    public Tuple getPos() {
        return pos;
    }

    // saves the current game state
    private void save() {
        Data.save(Game.random, "random.txt");
        Data.save(Game.world, "world.txt");
        Data.save(this, "input.txt");
        Data.save(Game.map, "map.txt");
        Data.save(Game.enemies, "enemies.txt");
        Data.save(Game.renWorld, "renworld.txt");
    }
}
