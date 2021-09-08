/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slutprojektprog2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Fungerar som klasser fast representerar grupper av konstanter
 *
 * @author Oliver Toth
 */
enum GameState {
    StartScreen, ControlScreen, GameScreen
};

/**
 * Huvudklassen i projektet som extendar ifrån Canvas klassen
 *
 * @author Oliver Toth
 */
public class SlutprojektProg2 extends Canvas implements Runnable, KeyListener {

    public static final int GAME_WIDTH = 1000; //20x 
    public static final int GAME_HEIGHT = 800;

    private static GameState gameState = GameState.StartScreen;
    private long startTime, timer;
    private final double AMOUNT_OF_TICKS = 60;
    private final double NUMBER_OF_SECONDS = 1000000000 / AMOUNT_OF_TICKS;
    private double delta;
    private boolean gameOn = false;

    private final BufferedImage IMAGE
            = new BufferedImage(GAME_WIDTH, GAME_HEIGHT,
                    BufferedImage.TYPE_INT_BGR);
    private final ArrayList<Buttons> menuButtons = new ArrayList<>();
    private final ArrayList<Block> block = new ArrayList<>();

    private Thread thread;
    private Player player;
    private BufferedImage backgroundImage;
    private int points = 0;
    private int level = 1;
    private boolean victory = false;
    private int menu = 1;

    /**
     * Startar programmet
     * @param args skapar en ny instans av programmet
     */
    public static void main(String[] args) {
        SlutprojektProg2 game = new SlutprojektProg2();
        game.init();
    }

    private void init() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setMaximumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setMinimumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        JFrame boardFrame = new JFrame();
        boardFrame.add(this);
        boardFrame.pack();
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setResizable(false);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
        this.start();
        this.setFocusable(true);
    }

    private synchronized void start() {
        if (gameOn) {
            return;
        }
        gameOn = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!gameOn) {
            return;
        }
        gameOn = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Startar tiden då programmet kör 
     * Om spelet är igång så körs programmet
     * Om spelet inte är igång stannar det programmet
     */
    @Override
    public void run() {
        loader();
        startTime = System.nanoTime();
        delta = 0;
        timer = System.currentTimeMillis();
        while (gameOn) {
            speed();
        }
        stop();
    }

    private void speed() {
        long lapTime = System.nanoTime();
        delta += (lapTime - startTime) / NUMBER_OF_SECONDS;
        startTime = lapTime;
        if (delta >= 1) {
            tick();
            delta--;
        }

        render();
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
        }
    }

    private void tick() {
        if (gameState == GameState.GameScreen) {
            player.tick();
            collision();
        }
    }

    private void loader() {
        addKeyListener(this);
        menuButtons.add(new Buttons(500, 300, "PLAY GAME", Color.BLUE));
        menuButtons.add(new Buttons(500, 400, "CONTROLS", Color.WHITE));
        menuButtons.add(new Buttons(500, 500, "QUIT GAME", Color.WHITE));
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.PINK);
        g.setFont(new Font("Arial", Font.ITALIC, 20));

        g.drawImage(IMAGE, 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);

        switch (gameState) {
            case StartScreen:
                g.drawString("Space --> Switch", 10, 800);
                g.drawString("Enter --> Select", 850, 800);

                g.setColor(Color.CYAN);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Budget Ice Climbers!", 270, 200);
                for (Buttons button : menuButtons) {
                    button.render(g);
                }
                break;

            case GameScreen:
                player.render(g);
                for (Block b : block) {
                    b.render(g);
                }
                g.setColor(Color.PINK);
                g.drawString("Points: " + points, 900, 30);

                if (victory) {
                    g.drawString("VICTORY!", 420, 400);
                    g.setFont(new Font("Arial", Font.ITALIC, 15));
                    g.drawString("Press ENTER for next lvl", 400, 450);
                }
                break;

            case ControlScreen:
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Move Rigth: D or --> ", 350, 300);
                g.drawString("Move Left: A or <-- ", 350, 400);
                g.drawString("Jump: W, SPACE or ^ ", 350, 500);

                g.setColor(Color.PINK);
                g.setFont(new Font("Arial", Font.ITALIC, 20));
                g.drawString("Enter: Go Back", 10, 800);

                break;
            default:
                break;
        }
        g.dispose();
        bs.show();
    }

    private void collision() {
        Rectangle p = player.boundArea();

        for (Block b : block) {
            if (p.intersects(b.boundArea())) {
                if (b.getColor() == Color.BLUE) {
                    if (player.getY() + b.height / 2 < b.getY()) {
                        player.setJump(false);
                        player.setY(b.getY() - b.height - 1);
                        break;
                    } else if (player.getY() - b.height / 2 > b.getY()) {
                        player.setUp(false);
                        break;
                    }
                    if (player.getX() - b.width / 2 >= b.getX()) {
                        player.setX(b.getX() + b.width + 2);
                        player.setJump(false);
                    } else if (player.getX() + b.width / 2 <= b.getX()) {
                        player.setX(b.getX() - b.width - 2);
                        player.setJump(false);
                    }
                }
                if (b.getColor() == Color.CYAN) {
                    if (player.getY() < b.getY()) {
                        player.setJump(false);
                        break;
                    } else if (player.getY() > b.getY()) {
                        player.setUp(false);
                        block.remove(b);
                        break;
                    }
                    if (player.getX() - 50 > b.getX()) {
                        player.setX(b.getX() + b.width + 2);
                    } else if (player.getX() + 50 < b.getX()) {
                        player.setX(b.getX() - b.width - 2);
                    }
                }

                if (b.getColor() == Color.YELLOW) {
                    points++;
                    block.remove(b);
                    break;
                }

                if (b.getColor() == Color.GREEN && points >= 3) {
                    victory = true;

                }

            }
            if (!(p.intersects(b.boundArea()))) {
                if (!(player.getY() >= GAME_HEIGHT - 45)) {
                    player.setJump(true);
                    player.setSideCollision(false);
                }
            }
        }
    }

    private void gameLoader() {
        String fileLocation = "";

        switch (level) {
            case 1:
                fileLocation = "level1.txt";
                break;
            case 2:
                fileLocation = "level2.txt";
                break;
            case 3:
                fileLocation = "level3.txt";
                break;
            default:
                break;
        }
        level(fileLocation);
    }

    private void level(String fileLocation) {
        int j = 0;
        try {
            File path = new File(fileLocation);
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(new FileReader(path));
            String row;
            block.clear();
            while ((row = br.readLine()) != null) {

                String[] rowSplit = row.split(", ");
                for (int i = 0; i < 20; i++) {
                    switch (rowSplit[i]) {
                        case "1":
                            block.add(new SolidBlocks(50, 40, 50 * i, 40 * j, Color.BLUE));
                            break;
                        case "2":
                            block.add(new BreakableBlocks(50, 40, 50 * i, 40 * j, Color.CYAN));
                            break;
                        case "3":
                            block.add(new PointBlocks(50, 40, 50 * i, 40 * j, Color.YELLOW));
                            break;
                        case "4":
                            block.add(new GoalBlock(50, 40, 50 * i, 40 * j, Color.GREEN));
                            break;
                        case "x":
                            player = new Player(50, 50, 50 * i, 40 * j, Color.PINK);
                            break;
                        default:
                            break;
                    }

                }
                j++;
            }

        } catch (IOException ex) {
            System.out.println("Level doesn't exist");
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    /**
     * Om Space tangenten trycks vid startskärmen byter tas alla menyer bort och 
     * nya skapas samt så byts alternativet för menyn.
     * Om Enter tangenten trycks vid startskärmen byts sceen till den valda
     * Om Enter tangenten trycks vid spelskärmen och du har klarat en level 
     * byter den till nästa level.
     * Om Enter tangenten trycks vid kontrollskärmen går den tillbaka till
     * startskärmen.
     * Om Escape tangenten trycks vid spelskärmen går den tillbaka till 
     * startskärmen.
     * Tangentfunktioner hämtas från Player klassen.
     * @param ke hämtar tangenten som trycks på
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameState == GameState.StartScreen) {
                menuButtons.clear();
                switch (menu) {
                    case 1:
                        menu = 2;
                        menuButtons.add(new Buttons(500, 300, "PLAY GAME", Color.WHITE));
                        menuButtons.add(new Buttons(500, 400, "CONTROLS", Color.BLUE));
                        menuButtons.add(new Buttons(500, 500, "QUIT GAME", Color.WHITE));
                        break;
                    case 2:
                        menu = 3;
                        menuButtons.add(new Buttons(500, 300, "PLAY GAME", Color.WHITE));
                        menuButtons.add(new Buttons(500, 400, "CONTROLS", Color.WHITE));
                        menuButtons.add(new Buttons(500, 500, "QUIT GAME", Color.BLUE));
                        break;
                    case 3:
                        menu = 1;
                        menuButtons.add(new Buttons(500, 300, "PLAY GAME", Color.BLUE));
                        menuButtons.add(new Buttons(500, 400, "CONTROLS", Color.WHITE));
                        menuButtons.add(new Buttons(500, 500, "QUIT GAME", Color.WHITE));
                        break;
                    default:
                        break;
                }
            } else if (gameState == GameState.GameScreen) {
                player.movement(ke);
            }
        }

        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                switch (menu) {
                    case 1:
                        if (gameState == GameState.StartScreen) {
                            gameLoader();
                            gameState = GameState.GameScreen;
                        } else if (gameState == GameState.GameScreen) {
                            if (victory) {
                                level++;
                                if (level > 3) {
                                    level = 1;
                                }
                                points = 0;
                                victory = false;
                                gameLoader();
                            }

                        }
                        break;
                    case 2:
                        if (gameState == GameState.StartScreen) {
                            gameState = GameState.ControlScreen;
                        } else {
                            gameState = GameState.StartScreen;
                        }
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        break;
                }
            } catch(Exception e)  {
                System.out.println("new level didnt load");
            }

        }
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameState = GameState.StartScreen;
            points = 0;
            victory = false;
        }

        if (gameState == GameState.GameScreen) {
            player.movement(ke);
        }
    }

    /**
     * Tangentfunktioner hämtas från Player klassen.
     * @param ke hämtar tangenten som du släpper
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        if (gameState == GameState.GameScreen) {
            player.movementStop(ke);
        }
    }

}
