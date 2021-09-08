/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slutprojektprog2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 * Innehållar alla funktioner och variabler som angår spelaren.
 * @author Totheman
 */
public class Player {

    protected int height = 200;
    protected int width = 100;

    private int jumpForce = SlutprojektProg2.GAME_HEIGHT - 30;
    private boolean jump = false;
    private boolean up = false;
    private boolean sideCollision = false;
    private int currentY;
    private int x;
    private int y;
    private Color color;
    private int speedX = 0;

    private final int XSPEED = 7;
    private final int YSPEED = 10;

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     * @param width bredden på spelaren
     * @param height höjden på spelaren
     * @param x x-koordinaten på spelaren
     * @param y y-koordinaten på spelaren
     * @param color färgen på spelaren
     */
    public Player(int width, int height, int x, int y, Color color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean isSideCollision() {
        return sideCollision;
    }

    public void setSideCollision(boolean sideCollision) {
        this.sideCollision = sideCollision;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(int jumpForce) {
        this.jumpForce = jumpForce;
    }

    public boolean getJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean getUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void render(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), width, height);
    }

    /**
     * @return Rectanglens x-koordinat, y-koordinat, bred och jhöjd
     */
    public Rectangle boundArea() {
        return new Rectangle(getX(), getY(), width, height);
    }

    /**
     * Ger spelarens dens nya x-koordinat
     * Spelaren kan inte få en x-koordinat som är utanför fönstret
     * Anropar jump-metoden
     */
    public void tick() {
        if (!(getX() + getSpeedX() < 0) && !(SlutprojektProg2.GAME_WIDTH - getSpeedX() < getX() + width)) {
            setX(getX() + getSpeedX());
        }
        jump();

    }

    /**
     * Kollar om spelaren hoppar och sedan om den hoppar upp eller inte
     * Om spelare hoppar upp ändras dens y-koordinat tills den ha hoppat 161px
     * och spelaren sätt till att inte hoppa upp.
     * Om spelaren inte hoppar upp anropas fall-metoden.
     */
    public void jump() {
        if (jump) {
            if (up) {
                jumpForce -= YSPEED;
                setY(jumpForce);
                if (getY() <= currentY - 161) {
                    up = false;
                }
            } else {
                fall();
            }
        }
    }

    /**
     * Spelaren faller ner tills den når marken eller metoden avbryts.
     */
    public void fall() {
        jumpForce += YSPEED;
        setY(jumpForce);
        if (getY() >= SlutprojektProg2.GAME_HEIGHT - 50) {
            jump = false;
        }
    }

    /**
     * Om vänster- eller a-tangenten trycks på så förflyttas spelaren till vänster.
     * Om höger- eller d-tangenten trycks på så förflyttas spelaren till höger.
     * Om up-, w- eller space-tangenten trycks på så hämtas spelarens nuvarande
     * y-koordinat och anropar jump-metoden och säger att spelaren ska hoppa up.
     * 
     * @param ke hämtar tangenten som trycks på
     */
    public void movement(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A) {

            setSpeedX(-XSPEED);

        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D) {

            setSpeedX(XSPEED);

        }
        if ((ke.getKeyCode() == KeyEvent.VK_SPACE || ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_W) && jump == false) {
            currentY = getY();
            jump = true;
            up = true;
        }
    }

    /**
     * Om vänster-, höger-, a- eller d-tangent släpps stannar spelaren.
     *
     * @param ke hämtar tangenten du trycker i
     */
    public void movementStop(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A) {
            setSpeedX(0);
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D) {
            setSpeedX(0);
        }
    }
}
