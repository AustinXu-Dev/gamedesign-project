package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.SpawnDetails;
import gdd.powerup.MultiShot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.sprite.Alien1;
import gdd.sprite.Alien2;
import gdd.sprite.Alien3;
import gdd.sprite.Boss;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Scene1 extends JPanel {

    private int stage;
    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Alien1.Bomb> bombs;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    private Boss bossEnemy = null;
    // private Shot shot;

    private int bombDropTick = 0;
    private final int BOMB_DROP_INTERVAL = 90; // drop every 90 frames (1.5 sec at 60fps)

    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;

    final int BLOCKS_TO_DRAW = BOARD_HEIGHT / BLOCKHEIGHT;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;
    private final Game game;
    private int shotLevel = 1;

    private int currentRow = -1;
    // TODO load this map from a file
    private int mapOffset = 0;
    private final int[][] MAP = {
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
    };

    private HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();
    private AudioPlayer audioPlayer;
    private int lastRowToShow;
    private int firstRowToShow;

    public Scene1(Game game, int stage) {
        this.game = game;
        this.stage = stage;
        loadSpawnDetails();
    }


    private void initAudio() {
        try {
            String filePath = "src/audio/scene1.wav";
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loadSpawnDetails() {
        spawnMap.clear();

        String fileName = "src/levels/stage" + stage + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header

                String[] parts = line.split(",");

                if (parts.length < 4) continue;

                int frame = Integer.parseInt(parts[0]);
                String type = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);

                spawnMap.put(frame, new SpawnDetails(type, x, y));
            }

        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }

    private void initBoard() {

    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(new Color(2,22,49));

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        gameInit();
        initAudio();
    }

    public void stop() {
        timer.stop();
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        bombs = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();

        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 6; j++) {
        // var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
        // ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
        // enemies.add(enemy);
        // }
        // }
        player = new Player();
        // shot = new Shot();
    }

    private void drawMap(Graphics g) {
        // Draw scrolling starfield background

        // Calculate smooth scrolling offset (1 pixel per frame)
        int scrollOffset = (frame) % BLOCKHEIGHT;

        // Calculate which rows to draw based on screen position
        int baseRow = (frame) / BLOCKHEIGHT;
        int rowsNeeded = (BOARD_HEIGHT / BLOCKHEIGHT) + 2; // +2 for smooth scrolling

        // Loop through rows that should be visible on screen
        for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
            // Calculate which MAP row to use (with wrapping)
            int mapRow = (baseRow + screenRow) % MAP.length;

            // Calculate Y position for this row
            // int y = (screenRow * BLOCKHEIGHT) - scrollOffset;
            int y = BOARD_HEIGHT - ( (screenRow * BLOCKHEIGHT) - scrollOffset );

            // Skip if row is completely off-screen
            if (y > BOARD_HEIGHT || y < -BLOCKHEIGHT) {
                continue;
            }

            // Draw each column in this row
            for (int col = 0; col < MAP[mapRow].length; col++) {
                if (MAP[mapRow][col] == 1) {
                    // Calculate X position
                    int x = col * BLOCKWIDTH;

                    // Draw a cluster of stars
                    drawStarCluster(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
                }
            }
        }

    }

    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        // Set star color to white
        g.setColor(Color.WHITE);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        // Smaller surrounding stars
        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        // Tiny stars for more detail
        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }

    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPowerUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        for (Shot shot : shots) {

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {
        for (Alien1.Bomb bomb : bombs) {
            if (!bomb.isDestroyed()) {
                g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
            }
        }
    }


    private void drawExplosions(Graphics g) {

        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                explosion.visibleCountDown();
                if (!explosion.isVisible()) {
                    toRemove.add(explosion);
                }
            }
        }

        explosions.removeAll(toRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(new Color(2,22,49));
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);

        if (inGame) {
            drawMap(g);  // background

            // HUD
            g.setColor(Color.white);
            g.setFont(g.getFont().deriveFont(14f));
            g.drawString("Score: " + deaths, 10, 30);
            g.drawString("Speed: " + player.getSpeed(), 10, 50);
            g.drawString("Shot Lv: " + player.getMultiShotLevel(), 10, 70);

            if (bossEnemy != null && bossEnemy.isVisible()) {
                g.setColor(Color.red);
                g.drawString("Boss HP: " + bossEnemy.getHealth(), 10, 90);
            }


            drawExplosions(g);
            drawPowerUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        }
        else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2, BOARD_WIDTH / 2);
    }

    private void update() {

        // Spawn logic
        SpawnDetails sd = spawnMap.get(frame);
        if (sd != null) {
            switch (sd.type) {
                case "Alien1":
                    enemies.add(new Alien1(sd.x, sd.y));
                    break;
                case "Alien2":
                    enemies.add(new Alien2(sd.x, sd.y));
                    break;
                case "Alien3":
                    enemies.add(new Alien3(sd.x, sd.y));
                    break;
                case "PowerUp-SpeedUp":
                    powerups.add(new SpeedUp(sd.x, sd.y));
                    break;
                case "PowerUp-MultiShot":
                    powerups.add(new MultiShot(sd.x, sd.y));
                    break;
                case "Boss":
                    Boss boss = new Boss(sd.x, sd.y);
                    enemies.add(boss);
                    bossEnemy = boss;
                    break;
            }
        }

        // Stage transition
        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            inGame = false;
            timer.stop();
            if (stage == 1) {
                message = "Stage 1 Complete!";
                Timer transitionTimer = new Timer(2000, e -> game.loadScene3());
                transitionTimer.setRepeats(false);
                transitionTimer.start();
            } else {
                message = "Game Won!";
            }
        }

        // Player
        player.act();

        // PowerUps
        for (PowerUp p : powerups) {
            if (p.isVisible()) {
                p.act();
                if (p.collidesWith(player)) {
                    p.upgrade(player);
                }
            }
        }

        // Enemy group edge check
        boolean edgeReached = false;
        for (Enemy e : enemies) {
            if (e.isVisible()) {
                int x = e.getX();
                if ((direction == 1 && x >= BOARD_WIDTH - ALIEN_WIDTH - 2) ||
                    (direction == -1 && x <= 2)) {
                    edgeReached = true;
                    break;
                }
            }
        }
        if (edgeReached) {
            direction *= -1;
            for (Enemy e : enemies) {
                e.setY(e.getY() + ALIEN_HEIGHT);
            }
        }

        // Enemy act
        for (Enemy e : enemies) {
            if (e.isVisible()) {
                e.act(direction);
            }
        }

        // 🎯 Bomb drop logic (spread and synced)
        if (bombDropTick >= BOMB_DROP_INTERVAL) {
            bombDropTick = 0;
            List<Alien1> aliens = enemies.stream()
                .filter(e -> e instanceof Alien1 && e.isVisible())
                .map(e -> (Alien1) e)
                .toList();

            if (!aliens.isEmpty()) {
                Alien1 randomAlien = aliens.get(randomizer.nextInt(aliens.size()));
                Alien1.Bomb bomb = randomAlien.getBomb();

                if (bomb.isDestroyed()) {
                    bomb.setDestroyed(false);
                    bomb.setX(randomAlien.getX() + 8);
                    bomb.setY(randomAlien.getY() + 16);
                    bombs.add(bomb);
                }
            }
        }

        // 💣 Bombs move
        for (Alien1.Bomb bomb : bombs) {
            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                    continue;
                }

                int bx = bomb.getX(), by = bomb.getY();
                int px = player.getX(), py = player.getY();

                if (player.isVisible() && bx >= px && bx <= px + PLAYER_WIDTH
                        && by >= py && by <= py + PLAYER_HEIGHT) {
                    var ii = new ImageIcon(IMG_EXPLOSION);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }
        }

        // 🔫 Shot logic
        List<Shot> toRemove = new ArrayList<>();
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                int sx = shot.getX();
                int sy = shot.getY();

                for (Enemy enemy : enemies) {
                    int ex = enemy.getX();
                    int ey = enemy.getY();

                    if (enemy.isVisible() && sx >= ex && sx <= ex + (enemy instanceof Boss ? 80 : ALIEN_WIDTH)
                            && sy >= ey && sy <= ey + (enemy instanceof Boss ? 80 : ALIEN_HEIGHT)) {

                        if (enemy instanceof Boss b) {
                            b.takeHit();
                            if (b.isDead()) {
                                var ii = new ImageIcon(IMG_EXPLOSION);
                                b.setImage(ii.getImage());
                                b.setDying(true);
                                explosions.add(new Explosion(ex, ey));
                                deaths++;
                            }
                        } else {
                            var ii = new ImageIcon(IMG_EXPLOSION);
                            enemy.setImage(ii.getImage());
                            enemy.setDying(true);
                            explosions.add(new Explosion(ex, ey));
                            deaths++;
                        }

                        shot.die();
                        toRemove.add(shot);
                    }
                }

                shot.setY(shot.getY() - 20);
                if (shot.getY() < 0) {
                    shot.die();
                    toRemove.add(shot);
                }
            }
        }
        shots.removeAll(toRemove);
    }

    private void doGameCycle() {
        frame++;
        bombDropTick++;
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Scene2.keyPressed: " + e.getKeyCode());

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame) {
                System.out.println("Shots: " + shots.size());
                if (key == KeyEvent.VK_SPACE && inGame) {
                    if (shots.size() < player.getMultiShotLevel()) {
                        Shot shot = new Shot(player.getX(), player.getY());
                        shots.add(shot);
                    }
                }

            }

        }
    }
}
