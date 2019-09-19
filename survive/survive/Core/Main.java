package survive.Core;

import survive.Engine.TileX;

public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TileX[][] worldState = game.playWithInputString(args[0]);
            System.out.println(TileX.toString(worldState));
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
