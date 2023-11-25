package src;

public class main {

    public static void main(String[] args)
    {
        Game game = Game.getInstance();
        int errorCode = game.run();
    }
}
