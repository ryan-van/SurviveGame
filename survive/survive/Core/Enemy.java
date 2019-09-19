package survive.Core;

import survive.Engine.Tiles;

import java.io.Serializable;
import java.util.ArrayList;

public class Enemy implements Serializable {
    private Tuple oldPos;
    private Tuple pos;

    public Enemy(int number) {
        pos = oldPos = Map.getRooms().get(number).calcCenter();
        Game.world[pos.x][pos.y] = Tiles.ENEMY;
    }

    // spawns initial enemy units
    public static void spawn() {
        Game.enemies = new ArrayList<>();
        for (int i = 1; i < Map.getRooms().size(); i++) {
            Game.enemies.add(new Enemy(i));
        }
    }

    protected void updateEnemy() {
        Game.world[oldPos.x][oldPos.y] = Tiles.FLOOR;
        Game.world[pos.x][pos.y] = Tiles.ENEMY;
    }

    protected void randomMove() {
        numberCheck(Game.random.nextInt(4));
    }

    public void numberCheck(int num) {
        switch (num) {
            case 0: move(new Tuple(0, 1)); break;
            case 1: move(new Tuple(-1, 0)); break;
            case 2: move(new Tuple(0, -1)); break;
            case 3: move(new Tuple(1, 0)); break;
            default: break;
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
        if (Game.world[newX][newY].equals(Tiles.PLAYER)) {
            Player.alive = false;
        }
        return Game.world[newX][newY].equals(Tiles.FLOOR)
                && (!(Game.world[newX - 1][newY].equals(Tiles.WALL)
                && Game.world[newX + 1][newY].equals(Tiles.WALL))
                && !(Game.world[newX][newY + 1].equals(Tiles.WALL)
                && Game.world[newX][newY - 1].equals(Tiles.WALL)));
    }
}
