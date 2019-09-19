package survive.Core;
import survive.Engine.Tiles;

import java.io.Serializable;
import java.util.ArrayList;

public class Vision implements Serializable {
    protected static ArrayList<Tuple> old = new ArrayList<>();

    private static boolean isBlocked(Tuple point) {
        return Game.world[point.x][point.y].equals(Tiles.WALL);
    }

    private static ArrayList<Tuple> generateVisiblePoints(double Radius, double dTheta, double dr) {
        ArrayList<Tuple> visiblePoints = new ArrayList<>();
        for (double theta = 0; theta <= 2 * Math.PI; theta += dTheta) {
            for (double r = 0; r <= Radius; r += dr) {
                Tuple point = new Tuple((int) Math.round(Game.player.getPos().x + r * Math.cos(theta)),
                        (int) Math.round(Game.player.getPos().y + r * Math.sin(theta)));
                    if (isBlocked(point)) {
                        if (checkInside(visiblePoints, point)) {
                            visiblePoints.add(point);
                        }
                        break;
                    } else {
                        if (checkInside(visiblePoints, point)) {
                            visiblePoints.add(point);
                        }
                    }
            }
        }
        return visiblePoints;
    }

    private static boolean checkInside(ArrayList<Tuple> vis, Tuple tup) {
        for (Tuple t: vis) {
            if (t.x == tup.x && t.y == tup.y && t != tup) {
                return false;
            }
        }
        return true;
    }

    protected static void renderWorld(int rad) {
        ArrayList<Tuple> points = generateVisiblePoints(rad, Math.toRadians(7.3), .1);
        for (int i = 0; i < points.size(); i++) {
            if (checkInside(old, points.get(i))) {
                old.add(points.get(i));
            }
        }
        for (Tuple pt: old) {
            replaceTile(pt);
        }
        for (Tuple pt: points) {
            Game.renWorld[pt.x][pt.y] = Game.world[pt.x][pt.y];
        }
    }

    private static void replaceTile(Tuple pt) {
        if (Game.world[pt.x][pt.y].equals(Tiles.WALL)) {
            Game.renWorld[pt.x][pt.y] = Tiles.WALL_OLD;
        } else if (Game.world[pt.x][pt.y].equals(Tiles.CLOUD)){
            Game.renWorld[pt.x][pt.y] = Tiles.CLOUD_OLD;
        } else if (Game.world[pt.x][pt.y].equals(Tiles.LOCKED_DOOR)){
            Game.renWorld[pt.x][pt.y] = Tiles.LOCKED_DOOR_OLD;
        } else if (Game.world[pt.x][pt.y].equals(Tiles.UNLOCKED_DOOR)){
            Game.renWorld[pt.x][pt.y] = Tiles.UNLOCKED_DOOR_OLD;
        } else if (Game.world[pt.x][pt.y].equals(Tiles.KEY)){
            Game.renWorld[pt.x][pt.y] = Tiles.KEY_OLD;
        } else {
            Game.renWorld[pt.x][pt.y] = Tiles.FLOOR_OLD;
        }
    }

    /*

    start at player position: let's call it x, y
    initialize a radius r for field of view (do we want it to depend on the dim(curr_room), etc?)

    for loop that goes to 2pi that starts at 0 and += a very small dtheta
            for 0 to radius
            if r*sin(theta), r*cos(theta) correspondes to a blocked position
                add point to running point list
                break
            else
                add r*sin(theta), r*cos(theta) to a running list of points

    render only the points in the running list

    */
}
