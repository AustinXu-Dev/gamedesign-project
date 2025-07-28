package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Boss extends Enemy {

    private int health = 10;

    public Boss(int x, int y) {
        super(x, y); // ✅ FIX: Call the Enemy constructor

        ImageIcon ii = new ImageIcon("src/images/boss.png");
        Image scaled = ii.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        setImage(scaled);
    }

    @Override
    public void act(int direction) {
        x += dx;
        if (x <= BORDER_LEFT || x >= BOARD_WIDTH - getImage().getWidth(null)) {
            dx = -dx;
        }
    }

    public void takeHit() {
        health--; // must decrement
        System.out.println("Boss HP: " + health);
    }

    public boolean isDead() {
        return health <= 0;
    }
    public int getHealth() {
        return health;
    }

}
