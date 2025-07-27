package gdd.sprite;

import static gdd.Global.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;
    private int currentSpeed = 2;
    private int multiShotLevel = 1;
    private final int MAX_SHOTS = 4;


    private Rectangle bounds = new Rectangle(175,135,17,32);

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        ImageIcon ii = new ImageIcon(IMG_PLAYER);

        // Scale to fixed 40x40 pixels regardless of original image size
        Image scaledImage = ii.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        setImage(scaledImage);
        setX(START_X);
        setY(START_Y);
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        if (speed < 1) {
            speed = 1; // Ensure speed is at least 1
        }
        this.currentSpeed = speed;
        return currentSpeed;
    }

    public void act() {
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        // Limit movement to board width
        if (x >= BOARD_WIDTH - getImage().getWidth(null)) {
            x = BOARD_WIDTH - getImage().getWidth(null);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -currentSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = currentSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
    public void increaseMultiShot() {
        if (multiShotLevel < MAX_SHOTS) {
            multiShotLevel++;
        }
    }

    public int getMultiShotLevel() {
        return multiShotLevel;
    }

}
