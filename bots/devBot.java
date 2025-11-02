package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;
/*

Class: DodgeyBot

A bot that will attempt to dodge bullets


 */
public class devBot extends Bot {

    BotHelper helpme = new BotHelper(); //creates a new bothelper class
    double moveDist = 50; //sets a move distance to 100 pixels

    /*
    This method is called at the beginning of each round. Use it to perform
    any initialization that you require when starting a new round.
     */
    public void newRound() {
    }

     /**
     This method is called every cycle by the BattleBotArena to find out what you want your Bot to do.
     The legal moves are defined in constants in the BattleBotArena:

        Move your bot at BOT_SPEED set in BattleBotArena:
            BattleBotArena.STAY
            BattleBotArena.UP
            BattleBotArena.DOWN
            BattleBotArena.LEFT
            BattleBotArena.RIGHT

        The Fire moves cause a bullet to be created (if there are
        not too many of your bullets on the screen at the moment). Each bullet
        moves at speed set by the BULLET_SPEED constant in BattleBotArena.

        Fire:
            BattleBotArena.FIREUP
            BattleBotArena.FIREDOWN
            BattleBotArena.FIRELEFT
            BattleBotArena.FIRERIGHT
       
        The SEND_MESSAGE move (if allowed by the Arena) will cause a call-back
        to this Bot's outgoingMessage() method, which should return the message
        you want the Bot to broadcast. This will be followed with a call to
        incomingMessage(String) which will be the echo of the broadcast message
        coming back to the Bot  

        Messaging:
            BattleBotArena.SEND_MESSAGE
   
    Parameters:
    me       A BotInfo object with all publicly available info about this Bot
    shotOK   True iff a FIRE move is currently allowed
    liveBots An array of BotInfo objects for the other Bots currently in play
    deadBots An array of BotInfo objects for the dead Bots littering the arena
    bullets  An array of all Bullet objects currently in play

    Returns:
    must return a legal move (use the constants defined in BattleBotArena)
     */
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        Bullet closestbullet = helpme.findClosest(me, bullets); //finds the closest bot

        double manDist2Closebot = helpme.manhattanDist(me.getX(), me.getY(), closestbullet.getX(), closestbullet.getY()); //finds the distance to the closest bot
        double distX, distY;

        //get the absolute difference between x and y of closest bot
        distX = Math.abs(closestbullet.getX() - me.getX());
        distY = Math.abs(closestbullet.getY() - me.getY());

        // if statement will find out if the bot is above/under and or left/right of me and move away if the
        // distance between the closest is less than the desired move distance
        if (closestbullet.getY() > me.getY() && closestbullet.getY() < me.getY()+moveDist){
            return BattleBotArena.RIGHT;
        }




        // if (closestbullet.getY() > me.getY() && closestbullet.getY() < me.getY()+moveDist && distX < moveDist) {
        //     return BattleBotArena.UP;
        // } else if (closestbullet.getX() > me.getX() && closestbullet.getX() < me.getX()+moveDist && distY < moveDist) {
        //     return BattleBotArena.RIGHT;
        // } else if (closestbullet.getX() > me.getX() && manDist2Closebot < moveDist && distX > distY) {
        //     return BattleBotArena.LEFT;
        // } else if (closestbullet.getX() < me.getX() && manDist2Closebot < moveDist && distX > distY) {
        //     return BattleBotArena.RIGHT;
        // } else if (closestbullet.getY() < me.getY() && manDist2Closebot < moveDist && distX < distY) {
        //     return BattleBotArena.RIGHT;
        // } else if (closestbullet.getY() > me.getY() && manDist2Closebot < moveDist && distX < distY) {
        //     return BattleBotArena.RIGHT;
        // } else {
        //     return 9;
        // }
        return 9;
    }

    /*
    Called when it is time to draw the Bot. Your Bot should be (mostly)
    within a circle inscribed inside a square with top left coordinates
    (x,y) and a size of RADIUS * 2. If you are using an image,
    just put null for the ImageObserver - the arena has some special features
    to make sure your images are loaded before you will use them.

    parameters:
    g = The Graphics object to draw yourself on.
    x The x location of the top left corner of the drawing area
    y The y location of the top left corner of the drawing area
   
     */
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.orange);
        g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
    }

    /*
    This method will only be called once, just after your Bot is created
    to set your name permanently for the entire match.
   
    return The Bot's name

     */
    public String getName() {
        return "DodgeyBot";
    }

    /*
    Method: getTeamName

    returns the current team name to BattleBotArena.java. Only used for team events

    return: The Bot's current team name
     */
    public String getTeamName() {
        return "DodgeyBot";
    }

     /*
    Method: outgoingMessage

    Method call: This is only called after you have requested a SEND_MESSAGE move in the getMove method

    return The message you want to broadcast
    */  
    public String outgoingMessage() {
        return null;
    }

    /*
    Method: loadedImages

    Method call: This is called whenever the referee or a Bot sends a broadcast message.

    parameters:
    botNum = The ID of the Bot who sent the message, or <i>BattleBotArena.SYSTEM_MSG</i> if the message is from the referee.
    msg = The text of the message that was broadcast.
    */
    public void incomingMessage(int botNum, String msg) {
    }

    /*
    Method: loadedImages

    Method call: called by BatttleBotArena.java

    This method will load the names of images into an array

    return: pics = an array of Strings containing the names of the images
    */
    public String[] imageNames() {
            return null;
    }

    /*
    Method: loadedImages

    Method call: called by BatttleBotArena.java

    This method will load the names of images from an array and then use the names to store images
    into the previously created Image type variables current, up, down, right, left

    parameters:
    images = an inputted array of images created by previous imageNames() method
    */
    public void loadedImages(Image[] images) {
    }
}