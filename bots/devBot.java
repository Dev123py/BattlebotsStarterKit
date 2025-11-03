package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/*
Class: DodgeyBot

A bot that moves in a square and fires in the direction it's turning
*/
public class devBot extends Bot {

    BotHelper helpme = new BotHelper(); //creates a new bothelper class
    double moveDist = 100; //sets a move distance to 100 pixels
    int tickCounter = 0; // counts frames
    int cycle = 0; // which repeat cycle we're in
    int step = 0; // step inside the cycle

    /*
    This method is called at the beginning of each round. Use it to perform
    any initialization that you require when starting a new round.
    */
    public void newRound() {
        tickCounter = 0;
        cycle = 0;
        step = 0;
    }

    /**
    This method is called every cycle by the BattleBotArena to find out what you want your Bot to do.
    Returns a legal move (use the constants defined in BattleBotArena)
    */
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        tickCounter++;

        // Reset cycle every 5 steps (normal square + 1 repeat)
        if (step >= 5) {
            step = 0;
            cycle++;
            if (cycle > 3) {
                cycle = 0;
            }
        }

        // Fire at start of each direction
        if (step == 0 && shotOK) {
            return BattleBotArena.FIRERIGHT;
        }
        if (step == 1 && shotOK) {
            return BattleBotArena.FIREDOWN;
        }
        if (step == 2 && shotOK) {
            return BattleBotArena.FIRELEFT;
        }
        if (step == 3 && shotOK) {
            return BattleBotArena.FIREUP;
        }

        // Movement logic
        if (cycle == 0) {
            // Repeat UP
            if (step == 0) {
                step++;
                return BattleBotArena.RIGHT;
            } else if (step == 1) {
                step++;
                return BattleBotArena.DOWN;
            } else if (step == 2) {
                step++;
                return BattleBotArena.LEFT;
            } else if (step == 3) {
                step++;
                return BattleBotArena.UP;
            } else if (step == 4) {
                step++;
                return BattleBotArena.UP;
            }
        }

        if (cycle == 1) {
            // Repeat RIGHT
            if (step == 0) {
                step++;
                return BattleBotArena.RIGHT;
            } else if (step == 1) {
                step++;
                return BattleBotArena.RIGHT;
            } else if (step == 2) {
                step++;
                return BattleBotArena.DOWN;
            } else if (step == 3) {
                step++;
                return BattleBotArena.LEFT;
            } else if (step == 4) {
                step++;
                return BattleBotArena.UP;
            }
        }

        if (cycle == 2) {
            // Repeat DOWN
            if (step == 0) {
                step++;
                return BattleBotArena.RIGHT;
            } else if (step == 1) {
                step++;
                return BattleBotArena.DOWN;
            } else if (step == 2) {
                step++;
                return BattleBotArena.DOWN;
            } else if (step == 3) {
                step++;
                return BattleBotArena.LEFT;
            } else if (step == 4) {
                step++;
                return BattleBotArena.UP;
            }
        }

        if (cycle == 3) {
            // Repeat LEFT
            if (step == 0) {
                step++;
                return BattleBotArena.RIGHT;
            } else if (step == 1) {
                step++;
                return BattleBotArena.DOWN;
            } else if (step == 2) {
                step++;
                return BattleBotArena.LEFT;
            } else if (step == 3) {
                step++;
                return BattleBotArena.LEFT;
            } else if (step == 4) {
                step++;
                return BattleBotArena.UP;
            }
        }

        // Default fallback
        return BattleBotArena.STAY;
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
        return "DodgeyBot";
    }

    /*
    Method: getTeamName
    returns the current team name to BattleBotArena.java. Only used for team events
    */
    public String getTeamName() {
        return "DodgeyBot";
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