package survive.Core;

import survive.Engine.TileX;
import survive.Engine.Tiles;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Menu implements Serializable {
    private static final Color BGCOLOR = new Color(224, 236, 224);
    private int WIDTH, HEIGHT;
    protected Long seed;

    public void run() {
        mainMenu();
        menuInput();
    }

    public Menu(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    private void mainMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Comic Sans", Font.BOLD, 80);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(BGCOLOR);
        StdDraw.setPenColor(79, 175, 255);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "S U R V I V E");

        font = new Font("Comic Sans", Font.PLAIN, 20);
        StdDraw.setFont(font);
        StdDraw.textLeft(WIDTH - 15, 2, "closed beta version 1.02");

        font = new Font("Comic Sans", Font.PLAIN, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2.5, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Instructions (I)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 7.5, "Quit (Q)");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    private void menuInput() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                switch (Character.toLowerCase(StdDraw.nextKeyTyped())) {
                    case 'n':
                        seed = seedEnter();
                        if (seed == -1) {
                            return;
                        }
                        startNewGame();
                        return;
                    case 'l':
                        loadData();
                        return;
                    case 'q':
                        System.exit(0);
                        break;
                    case 'i':
                        instructions();
                        return;
                    default:
                        break;
                }
            }
        }
    }

    private Long seedEnter() {
        seedText();
        StdDraw.show();
        String temp = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char curr = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (curr == 's' && !temp.equals("")) {
                    break;
                }
                if (curr == 'b') {
                    run();
                    temp = "-1";
                    break;
                }
                if (Character.isDigit(curr)) {
                    temp += curr;
                    seedText();
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, temp);
                    StdDraw.show();
                }
            }
        }
        return Long.parseLong(temp);
    }

    private void seedText() {
        StdDraw.clear(BGCOLOR);
        Font font = new Font("Comic Sans", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 7, "Enter a seed");
        font = new Font("Comic Sans", Font.PLAIN, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4.5, "Start (S)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 7, "Back (B)");
    }

    // starts a fresh game
    private void startNewGame() {
        Game.map = new Map(seed, WIDTH, HEIGHT);
        Game.map.generate();
        Game.world = Game.map.getWorld();
        Game.player = new Player();
        Vision.old = new ArrayList<>();
        renWorldInit();
        Player.alive = true;
        Player.win = false;
        Enemy.spawn();
        Game.player.placeObject(Tiles.LOCKED_DOOR);
        Game.player.placeObject(Tiles.KEY);
    }

    private void instructions() {
        Font font = new Font("Comic Sans", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.clear(BGCOLOR);
        StdDraw.text(WIDTH / 2, HEIGHT - 7, "Instructions");
        font = new Font("Comic Sans", Font.PLAIN, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 16, "Back (B)");
        instructionWords();
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char curr = StdDraw.nextKeyTyped();
                if (curr == 'b') {
                    break;
                }
            }
        }
        run();
    }

    private void instructionWords() {
        StdDraw.setPenColor(100, 100, 100);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 12, "W A S D  to move");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 9, "Avoid enemies (☣), touching one is deadly");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enemies' animation occurs at the same as yours (人), but enemies move first");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "Don't let you're hunger bar reach zero");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Each food (☁) gives you +15 hunger");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Your field of vision depends on your hunger level");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Find the key (⚿) to unlock the door (㋬)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 9, "Enter the unlocked door (▢) to win");
        StdDraw.setPenColor(79, 175, 255);
    }

    // game Over screen
    public void gameOver() {
        Font font = new Font("Comic Sans", Font.BOLD, 80);
        StdDraw.setFont(font);
        StdDraw.clear(BGCOLOR);
        StdDraw.setPenColor(79, 175, 180);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 + 7, "G A M E");
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 1, "O V E R");
        font = new Font("Comic Sans", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 8, "Restart (R)");
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 10, "Quit (Q)");
        StdDraw.show();
        restartOrQuit();
    }

    public void win() {
        Font font = new Font("Comic Sans", Font.BOLD, 80);
        StdDraw.setFont(font);
        StdDraw.clear(BGCOLOR);
        StdDraw.setPenColor(79, 175, 180);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 + 7, "C O N G R A T U L A T I O N S");
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 1, "Y O U  W I N !");
        font = new Font("Comic Sans", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 8, "Restart (R)");
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2 - 10, "Quit (Q)");
        StdDraw.show();
        restartOrQuit();
    }

    private void restartOrQuit() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                switch (Character.toLowerCase(StdDraw.nextKeyTyped())) {
                    case 'r': restart(); break;
                    case 'q': System.exit(0);
                }
            }
        }
    }

    private void restart() {
        Game game = new Game();
        game.playWithKeyboard();
    }

    private void renWorldInit() {
        Game.renWorld = new TileX[Game.WIDTH][Game.HEIGHT];
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                Game.renWorld[i][j] = Tiles.NOTHING;
            }
        }
    }

    // loads old game data
    private void loadData() {
        File check = new File("map.txt");
        if (!check.exists()) {
            System.exit(0);
        }
        Player.alive  = true;
        Player.win = false;
        Vision.old = new ArrayList<>();
        Game.random = Data.load("random.txt");
        Game.map = Data.load("map.txt");
        Game.world = Data.load("world.txt");
        Game.player = Data.load("input.txt");
        Game.enemies = Data.load("enemies.txt");
        Game.renWorld = Data.load("renworld.txt");
    }
}
