package survive.Core;

import survive.Engine.TileX;
import survive.Engine.Tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Map generation algorithim
 * Our idea was to randomly place rooms and then connect hallways using the center
 * of each room. Once a room has been connected, the room it connected to will connect
 * to the next closest room.
 */
public class Map implements Serializable {
    private static final int MINSIZE = 7, MAXSIZE = 12;
    private static final int THRESH = 1; //threshold for intersection

    private Long seed;
    private int width, height;
    private static ArrayList<Room> rooms;
    private TileX[][] world;

    public Map(Long seed, int width, int height) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        Game.random = new Random(seed);
        rooms = new ArrayList<>();
        world = new TileX[width][height];
    }

    /**
     * generate a room, have to check the size of the room
     * it has to be connected to hallways
     * we have to leave an opening somewhere and link up the room and hallway
     */
    private Room generateRoom() {
        int rwidth = MINSIZE + Game.random.nextInt(MAXSIZE - MINSIZE + 1);
        int rlength = MINSIZE + Game.random.nextInt(MAXSIZE - MINSIZE + 1);
        int rx = Game.random.nextInt(width);
        int ry = Game.random.nextInt(height);

        if (rx < (width - MAXSIZE) && ry < (height - MAXSIZE)) {
            return new Room(new Tuple(rx, ry), new Tuple(rx + rwidth, ry),
                new Tuple(rx, ry + rlength), new Tuple(rx + rwidth, ry + rlength));
        }
        return generateRoom();
    }

    // adds the room to the map
    // change later makesure work
    private void addToMap(Room room) {
        for (int i = room.get(1).x; i < room.get(2).x - 1; i++) {
            for (int j = room.get(1).y; j < room.get(3).y - 1; j++) {
                world[i][j] = Tiles.FLOOR;
            }
        }
        // adding walls to the room // temporarily do not need this
        for (int i = room.get(1).x; i < room.get(2).x; i++) {
            world[i][room.get(1).y] = Tiles.WALL;
            world[i][room.get(3).y - 1] = Tiles.WALL;
        }
        for (int i = room.get(1).y; i < room.get(3).y; i++) {
            world[room.get(1).x][i] = Tiles.WALL;
            world[room.get(2).x - 1][i] = Tiles.WALL;
        }
    }

    /**
     * Adds rooms to both an arraylist and the map
     * @param numRooms takes in the max amount of rooms per map
     */
    private void addRooms(int numRooms) {
        while (rooms.size() < numRooms && rooms.size() <= maxRooms()) {
            Room room = generateRoom();
            if (rooms.size() == 0 || !room.intersect(rooms, THRESH)) {
                rooms.add(room);
                addToMap(room);
            }
        }
    }

    // Used to calculate the maximum amount of rooms
    private int maxRooms() {
        int max = (MAXSIZE + THRESH) * (MAXSIZE + THRESH);
        int area = width * height;
        return area / max;
    }

    /**
     * generate a hallway, have to check the size of the hallway
     * it has to be connected to room
     * hallways are probably with a width of three and random length (pref not too long)
     * might split into two methods, vertical and horizontal
     * can override walls to connect rooms
     * @param type refers to horizontal (0) or vertical (1)
     */
    private Hallway generateHallway(Room r1, Room r2, int type) {
        int args1 = 0, args2 = 0, args3 = 0;
        if (type == 0) {
            args1 = r1.calcCenter().x;
            args2 = r2.calcCenter().x;
            args3 = r2.calcCenter().y;
        }
        if (type == 1) {
            args1 = r1.calcCenter().y;
            args2 = r2.calcCenter().y;
            args3 = r2.calcCenter().x;
        }
        return new Hallway(args1, args2, args3, type);
    }

    /**
     * connects hallways to rooms, using a copy arraylist to make a room not connect to
     * a room that has already been connected
     * randomly selects which room to begin with
     */
    // can alternatively do rooms.size() - 1 to not connect the last room (may be good for gameplay)
    private void addHallways() {
        ArrayList<Room> copy = new ArrayList<>(rooms);
        int temp = Game.random.nextInt(rooms.size());
        for (int i = 0; i < rooms.size(); i++) {
            int index = rooms.get(temp).calcNearest(rooms, copy, temp);
            int rand = Game.random.nextInt(2);
            Hallway hw = generateHallway(rooms.get(temp), rooms.get(index), rand);
            Hallway hw2 = generateHallway(rooms.get(index), rooms.get(temp), Math.abs(rand - 1));
            addToMap(hw);
            addToMap(hw2);
            copy.remove(rooms.get(temp));
            copy.add(temp, new Room(new Tuple(1000, 1000), new Tuple(1000, 1000),
                    new Tuple(1000, 1000), new Tuple(1000, 1000)));
            temp = index;
        }
    }

    /**
     * adds hallway to map, checks if horizontal or vertical (0 or 1)
     * @param hw the current hallway, parameters passed in checks the type
     */
    private void addToMap(Hallway hw) {
        int min = Math.min(hw.getP1(), hw.getP2());
        int max = Math.max(hw.getP1(), hw.getP2());
        if (hw.getType() == 0) {
            for (int i = min; i <= max; i++) {
                world[i][hw.getLw()] = Tiles.FLOOR;
            }
        } else {
            for (int i = min; i <= max; i++) {
                world[hw.getLw()][i] = Tiles.FLOOR;
            }
        }
        fillWalls();
    }

    /**
     * changes all nothing tiles to walls that surround a floor
     */
    private void fillWalls() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (world[i][j].equals(Tiles.NOTHING) && checkSurroundings(i, j)) {
                    world[i][j] = Tiles.WALL;
                }
            }
        }
    }

    /**
     * looks at the eight blocks surrounding, if its a floor, surround it with a wall
     * @param x,y the tile being checked
     * @return true/false for whether or not it should be changed
     */
    private boolean checkSurroundings(int x, int y) {
        boolean check = false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int nx = x + i;
                int ny = y + j;

                if (i == 0 && j == 0) {
                    continue;
                } else if (nx <= 0 || ny <= 0 || nx >= width || ny >= height) {
                    continue;
                } else if (world[nx][ny].equals(Tiles.FLOOR)) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Creates empty grid for starting map
     * should only be called on first round
     */
    private void fillEmpty() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tiles.NOTHING;
            }
        }
    }

    private void addFood(double amount) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (Game.random.nextDouble() < amount && world[i][j].equals(Tiles.FLOOR)) {
                    world[i][j] = Tiles.CLOUD;
                }
            }
        }
    }

    public void generate() {
        fillEmpty();
        addRooms(15);
        addHallways();
        addFood(.03);
    }

    public TileX[][] getWorld() {
        return world;
    }

    public static ArrayList<Room> getRooms() {
        return rooms;
    }
}
