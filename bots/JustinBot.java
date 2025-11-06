package bots;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The HumanBot is a Bot that is under human control and should only be used for test purposes (e.g.
 * have a match with only two Bots - the one you are developing and this one - then move and fire
 * this Bot to test the behaviours of your AI.)<br><Br>
 *
 * When adding this Bot to the Arena, be sure to include the command <i>addKeyListener(Bots[n]);</i>
 * where <i>i</i> is the number of the HumanBot. Failure to do this will mean the Bot will not
 * react to keypresses.
 *
 * @author Sam Scott
 * @version 1.0 (March 3, 2011)
 *
 */

public class JustinBot extends Bot implements KeyListener {

    private int move = BattleBotArena.STAY;
    private int resume;
    private boolean cocked = true;
    private String msg = null;
    private Image graveImage;
    private BotHelper helpme = new BotHelper();
    private double moveDist = 50;
    private double shootDist = 300;
    private double runAwayDist = 150;
    private double lastX = -1;
    private double lastY = -1;
    private int stuckCounter = 0;

        public JustinBot() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if (graveImage != null) {
            g.drawImage(graveImage, x, y, RADIUS*2, RADIUS*2, null);
        } else {
            g.setColor(Color.black);
            g.fillRect(x+2, y+2, RADIUS*2-4, RADIUS*2-4);
            if (!cocked)
            {
                g.setColor(Color.red);
                g.fillRect(x+3, y+3, RADIUS*2-6, RADIUS*2-6);
            }
        }
    }

    @Override
    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots,
            BotInfo[] deadBots, Bullet[] bullets) {
        if (msg != null)
            return BattleBotArena.SEND_MESSAGE;
        cocked = shotOK;

        if (lastX >= 0 && lastY >= 0) {
            if (Math.abs(me.getX() - lastX) < 1 && Math.abs(me.getY() - lastY) < 1)
                stuckCounter++;
            else
                stuckCounter = 0;
        }
        lastX = me.getX();
        lastY = me.getY();

        if (bullets != null && bullets.length > 0) {
            Bullet[] cleanBullets = helpme.cleanArray(bullets);
            
            if (cleanBullets != null && cleanBullets.length > 0) {
                java.util.ArrayList<Bullet> enemyBullets = new java.util.ArrayList<Bullet>();
                for (Bullet bullet : cleanBullets) {
                    double bulletDistX = bullet.getX() - (me.getX() + Bot.RADIUS);
                    double bulletDistY = bullet.getY() - (me.getY() + Bot.RADIUS);
                    double distance = Math.sqrt(bulletDistX * bulletDistX + bulletDistY * bulletDistY);
                    if (distance < Bot.RADIUS * 3) {
                        double dotProduct = bulletDistX * bullet.getXSpeed() + bulletDistY * bullet.getYSpeed();
                        if (dotProduct > 0) {
                            continue;
                        }
                    }
                    enemyBullets.add(bullet);
                }
                if (enemyBullets.size() > 0) {
                    Bullet[] enemyBulletArray = new Bullet[enemyBullets.size()];
                    enemyBullets.toArray(enemyBulletArray);
                    
                    if (enemyBulletArray != null && enemyBulletArray.length > 0) {
                        Bullet closestBullet = helpme.findClosest(me, enemyBulletArray);
                        
                        if (closestBullet != null) {
                            double distX = Math.abs(closestBullet.getX() - me.getX());
                            double distY = Math.abs(closestBullet.getY() - me.getY());
                            int dodgeMove = BattleBotArena.STAY;
                            if (closestBullet.getY() > me.getY() && closestBullet.getY() < me.getY() + moveDist && distX < moveDist) {
                                dodgeMove = BattleBotArena.UP;
                            } else if (closestBullet.getX() > me.getX() && closestBullet.getX() < me.getX() + moveDist && distY < moveDist) {
                                dodgeMove = BattleBotArena.LEFT;
                            } else if (closestBullet.getY() < me.getY() && closestBullet.getY() > me.getY() - moveDist && distX < moveDist) {
                                dodgeMove = BattleBotArena.DOWN;
                            } else if (closestBullet.getX() < me.getX() && closestBullet.getX() > me.getX() - moveDist && distY < moveDist) {
                                dodgeMove = BattleBotArena.RIGHT;
                            }
                            
                            if (dodgeMove != BattleBotArena.STAY) {
                                boolean conflict = false;
                                if (dodgeMove == BattleBotArena.UP && (move == BattleBotArena.DOWN || move == BattleBotArena.FIREDOWN))
                                    conflict = true;
                                else if (dodgeMove == BattleBotArena.DOWN && (move == BattleBotArena.UP || move == BattleBotArena.FIREUP))
                                    conflict = true;
                                else if (dodgeMove == BattleBotArena.LEFT && (move == BattleBotArena.RIGHT || move == BattleBotArena.FIRERIGHT))
                                    conflict = true;
                                else if (dodgeMove == BattleBotArena.RIGHT && (move == BattleBotArena.LEFT || move == BattleBotArena.FIRELEFT))
                                    conflict = true;
                                if (!conflict) {
                                    move = resume;
                                    return dodgeMove;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (deadBots != null && deadBots.length > 0) {
            BotInfo closestDead = helpme.findClosest(me, deadBots);
            if (closestDead != null) {
                double dxDead = closestDead.getX() - me.getX();
                double dyDead = closestDead.getY() - me.getY();
                double manDistDead = helpme.manhattanDist(me.getX(), me.getY(), closestDead.getX(), closestDead.getY());
                int nearThreshold = Bot.RADIUS * 2 + 8; // proximity threshold to consider blockage
                if (stuckCounter >= 3 || manDistDead < nearThreshold) {
                    int avoidMove = BattleBotArena.STAY;
                    if (Math.abs(dxDead) > Math.abs(dyDead)) {
                        avoidMove = (dyDead > 0) ? BattleBotArena.UP : BattleBotArena.DOWN;
                    } else {
                        avoidMove = (dxDead > 0) ? BattleBotArena.LEFT : BattleBotArena.RIGHT;
                    }
                    move = resume;
                    return avoidMove;
                }
            }
        }

        if (liveBots != null && liveBots.length > 0) {
            BotInfo closestBot = helpme.findClosest(me, liveBots);
            
            if (closestBot != null) {
                double dxClose = closestBot.getX() - me.getX();
                double dyClose = closestBot.getY() - me.getY();
                double manDist2CloseBot = helpme.manhattanDist(me.getX(), me.getY(), closestBot.getX(), closestBot.getY());
                int alignTolerance = 3; // pixels
                if (manDist2CloseBot < runAwayDist) {
                    boolean preferHorizontal = Math.abs(dxClose) >= Math.abs(dyClose);
                    if (preferHorizontal) {
                        if (Math.abs(dyClose) > alignTolerance) {
                            move = resume;
                            return dyClose > 0 ? BattleBotArena.DOWN : BattleBotArena.UP;
                        } else if (shotOK) {
                            move = resume;
                            return dxClose > 0 ? BattleBotArena.FIRERIGHT : BattleBotArena.FIRELEFT;
                        }
                    } else {
                        if (Math.abs(dxClose) > alignTolerance) {
                            move = resume;
                            return dxClose > 0 ? BattleBotArena.RIGHT : BattleBotArena.LEFT;
                        } else if (shotOK) {
                            move = resume;
                            return dyClose > 0 ? BattleBotArena.FIREDOWN : BattleBotArena.FIREUP;
                        }
                    }
                }
            }
        }
        
        if (liveBots != null && liveBots.length > 0) {
            BotInfo closestBot = helpme.findClosest(me, liveBots);
            if (closestBot != null) {
                double dx = closestBot.getX() - me.getX();
                double dy = closestBot.getY() - me.getY();
                double manDist = helpme.manhattanDist(me.getX(), me.getY(), closestBot.getX(), closestBot.getY());
                double absDx = Math.abs(dx);
                double absDy = Math.abs(dy);
                if (shotOK && manDist < shootDist) {
                    if (absDx > absDy) {
                        move = resume;
                        return dx > 0 ? BattleBotArena.FIRERIGHT : BattleBotArena.FIRELEFT;
                    } else {
                        move = resume;
                        return dy > 0 ? BattleBotArena.FIREDOWN : BattleBotArena.FIREUP;
                    }
                }
                int approachMove = BattleBotArena.STAY;
                if (absDx > absDy)
                    approachMove = dx > 0 ? BattleBotArena.RIGHT : BattleBotArena.LEFT;
                else
                    approachMove = dy > 0 ? BattleBotArena.DOWN : BattleBotArena.UP;
                move = resume;
                return approachMove;
            }
        }
        
        if (shotOK && liveBots != null && liveBots.length > 0) {
            BotInfo closestBot = helpme.findClosest(me, liveBots);          
            if (closestBot != null) {
                double manDist2CloseBot = helpme.manhattanDist(me.getX(), me.getY(), closestBot.getX(), closestBot.getY());
                double distX = Math.abs(closestBot.getX() - me.getX());
                double distY = Math.abs(closestBot.getY() - me.getY());
                if (manDist2CloseBot < shootDist && move != BattleBotArena.FIREUP && move != BattleBotArena.FIREDOWN 
                        && move != BattleBotArena.FIRELEFT && move != BattleBotArena.FIRERIGHT) {
                    if (closestBot.getX() > me.getX() && distX > distY) {
                        move = resume;
                        return BattleBotArena.FIRERIGHT;
                    } else if (closestBot.getX() < me.getX() && distX > distY) {
                        move = resume;
                        return BattleBotArena.FIRELEFT;
                    } else if (closestBot.getY() < me.getY() && distX < distY) {
                        move = resume;
                        return BattleBotArena.FIREUP;
                    } else if (closestBot.getY() > me.getY() && distX < distY) {
                        move = resume;
                        return BattleBotArena.FIREDOWN;
                    }
                }
            }
        }
        
        int moveNow = move;
        move = resume;
        return moveNow;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "JustinBot";
    }

    @Override
    public String getTeamName() {
        // TODO Auto-generated method stub
        return "JustinBot";
    }

    @Override
    public String[] imageNames() {
            String[] pics = {"dead.png"};
            return pics;
    }

    @Override
    public void incomingMessage(int botNum, String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadedImages(Image[] images) {
        if (images != null && images.length > 0) {
            graveImage = images[0];
        }
    }

    @Override
    public void newRound() {
        msg="Arrow keys to move, WASD or CTRL to fire. Good luck!";
    }

    @Override
    public String outgoingMessage() {
        //TODO Auto-generated method stub
        String x = msg;
        msg = null;
        return x;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
        // case KeyEvent.VK_SPACE:
        //  msg = "Hi I am Devank";
            
        case KeyEvent.VK_UP:
            move = BattleBotArena.UP;
            resume = move;
            break;
        case KeyEvent.VK_DOWN:
            move = BattleBotArena.DOWN;
            resume = move;
            break;
        case KeyEvent.VK_LEFT:
            move = BattleBotArena.LEFT;
            resume = move;
            break;
        case KeyEvent.VK_RIGHT:
            move = BattleBotArena.RIGHT;
            resume = move;
            break;
        case KeyEvent.VK_CONTROL:
            resume = move;
            if (move == BattleBotArena.UP)
                move = BattleBotArena.FIREUP;
            else if (move == BattleBotArena.DOWN)
                move = BattleBotArena.FIREDOWN;
            else if (move == BattleBotArena.LEFT)
                move = BattleBotArena.FIRELEFT;
            else if (move == BattleBotArena.RIGHT)
                move = BattleBotArena.FIRERIGHT;
            break;
        case KeyEvent.VK_W:
            resume = move;
            move = BattleBotArena.FIREUP;
            break;
        case KeyEvent.VK_A:
            resume = move;
            move = BattleBotArena.FIRELEFT;
            break;
        case KeyEvent.VK_S:
            resume = move;
            move = BattleBotArena.FIREDOWN;
            break;
        case KeyEvent.VK_D:
            resume = move;
            move = BattleBotArena.FIRERIGHT;
            break;
        }

    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

}
