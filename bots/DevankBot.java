package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/*
Class: DodgeyBot

A bot that moves randomly and fires at bots on the same axis.
*/
public class devBot extends Bot {

    int tickCounter = 0;
    int moveCounter = 0;
    int moveDirection = BattleBotArena.STAY;
    Random random = new Random();

    /*
    This method is called at the beginning of each round. Use it to perform
    any initialization that you require when starting a new round.
    */
    public void newRound() {
        tickCounter = 0;
        moveCounter = 0;
        moveDirection = BattleBotArena.STAY;
    }

    /**
    This method is called every cycle by the BattleBotArena to find out what you want your Bot to do.
    Returns a legal move (use the constants defined in BattleBotArena)
    */
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        tickCounter++;

        // Check for bullets and dodge
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            double bx = b.getX();
            double by = b.getY();
            double mx = me.getX();
            double my = me.getY();

            if (Math.abs(bx - mx) < 100 && Math.abs(by - my) < 10) {
                return BattleBotArena.UP;
            }
            if (Math.abs(by - my) < 100 && Math.abs(bx - mx) < 10) {
                return BattleBotArena.RIGHT;
            }
        }

        // Fire at bots on same axis
        if (shotOK) {
            for (int i = 0; i < liveBots.length; i++) {
                BotInfo bot = liveBots[i];
                if (Math.abs(bot.getX() - me.getX()) < 10) {
                    if (bot.getY() < me.getY()) {
                        return BattleBotArena.FIREUP;
                    } else {
                        return BattleBotArena.FIREDOWN;
                    }
                }
                if (Math.abs(bot.getY() - me.getY()) < 10) {
                    if (bot.getX() < me.getX()) {
                        return BattleBotArena.FIRELEFT;
                    } else {
                        return BattleBotArena.FIRERIGHT;
                    }
                }
            }
        }

        // Random movement
        if (moveCounter == 0) {
            int choice = random.nextInt(4);
            if (choice == 0) {
                moveDirection = BattleBotArena.UP;
            } else if (choice == 1) {
                moveDirection = BattleBotArena.DOWN;
            } else if (choice == 2) {
                moveDirection = BattleBotArena.LEFT;
            } else {
                moveDirection = BattleBotArena.RIGHT;
            }
            moveCounter = random.nextInt(5) + 1;
        }

        moveCounter = moveCounter - 1;
        return moveDirection;
    }

    /*
    Called when it is time to draw the Bot.
    */
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.orange);
        g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
    }

    /*
    This method will only be called once, just after your Bot is created
    to set your name permanently for the entire match.
    */
    public String getName() {
        return "DevBot";
    }

    /*
    Method: getTeamName
    returns the current team name to BattleBotArena.java. Only used for team events
    */
    public String getTeamName() {
        return "DevBot";
    }

    /*
    Method: outgoingMessage
    return The message you want to broadcast
    */  
    public String outgoingMessage() {
        return null;
    }

    /*
    Method: incomingMessage
    Called whenever the referee or a Bot sends a broadcast message.
    */
    public void incomingMessage(int botNum, String msg) {
    }

    /*
    Method: imageNames
    Loads the names of images into an array
    */
    public String[] imageNames() {
        return null;
    }

    /*
    Method: loadedImages
    Loads the names of images from an array and stores them
    */
    public void loadedImages(Image[] images) {
    }
}