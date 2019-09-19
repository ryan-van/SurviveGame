package survive.Core;

import survive.Engine.Renderer;
import survive.Engine.TileX;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

// add new menu option, I (instructions) along with B (back)
// make new Tiless (try to find good color scheme)
// replace fov with old Tiless (grayed out)
// TODO: instead of losing instantly, change an encounter with an enemy to be -# hp
// TODO: enemies can eat food, increase food amounts
// if want to make game more interesting, add key to find before entering door
// less life ==> less radius of vision

public class Game implements Serializable {
    protected static Renderer ter = new Renderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85, HEIGHT = 50;
    protected static Random random;
    protected static TileX[][] world;
    protected static TileX[][] renWorld;
    protected static Map map;
    protected static Player player;
    protected static ArrayList<Enemy> enemies = new ArrayList<>();
    protected static LinkedList<Character> a = new LinkedList<>();

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // initialize main menu
        Menu menu = new Menu(WIDTH, HEIGHT);
        menu.run();

        // initialize the map from the given seed
        ter.initialize(WIDTH, HEIGHT + 1, 0, 0);

        // add interactive stuff

        // run the player
        player.run();

        if (!Player.alive) {
            menu.gameOver();
        } else {
            menu.win();
        }
    }

    public TileX[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String actions = readInput(input);
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        if (!actions.equals("")) {
            Map test = new Map(Long.parseLong(actions), WIDTH, HEIGHT);
            test.generate();
            world = test.getWorld();
            player = new Player();
            ter.renderFrame(world);
            Enemy.spawn();
            player.run(actions);
        } else {
            ter.renderFrame(world);
            player.run(actions);
        }
        return world;
    }

    private String readInput(String input) {
        int l = input.length();
        String actions = "";
        if (input.substring(0, 1).equalsIgnoreCase("n")) {
            for (int i = 0; i < l - 1; i++) {
                if (Character.isDigit(input.charAt(i))) {
                    actions += input.charAt(i);
                }
                if (input.substring(i, i + 1).equalsIgnoreCase("s")) {
                    for (int j = i; j < l; j++) {
                        a.addLast(Character.toLowerCase(input.charAt(j)));
                    }
                    break;
                }
            }
        } else if (input.substring(0, 1).equalsIgnoreCase("l")) {
            File check = new File("map.txt");
            if (check.exists()) {
                Game.random = Data.load("random.txt");
                Game.map = Data.load("map.txt");
                Game.world = Data.load("world.txt");
                Game.player = Data.load("input.txt");
                Game.enemies = Data.load("enemies.txt");
                for (int i = 0; i < l; i++) {
                    a.addLast(Character.toLowerCase(input.charAt(i)));
                }
            }

        }
        return actions;
    }
}
