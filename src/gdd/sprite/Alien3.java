package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Alien3 extends Enemy {

    private Bomb bomb;

    public Alien3(int x, int y) {
        super(x, y);
        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        ImageIcon ii = new ImageIcon(IMG_ENEMY2);

        // Scale to fixed size (adjust as needed)
        Image scaledImage = ii.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void act(int direction) {
        this.y++; // Or use this.x += direction if horizontal scroll is needed
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {
            initBomb(x, y);
        }

        private void initBomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;

            String bombImg = "src/images/bomb.png";
            ImageIcon ii = new ImageIcon(bombImg);
            Image scaledBomb = ii.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH); // optional scaling
            setImage(scaledBomb);
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}
