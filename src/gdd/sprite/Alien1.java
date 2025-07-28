package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Alien1 extends Enemy {

    private Bomb bomb;

    public Alien1(int x, int y) {
        super(x, y);
        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        ImageIcon ii = new ImageIcon(IMG_ENEMY0);
        Image scaledImage = ii.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void act(int direction) {
        this.x += direction; // group horizontal movement
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
            Image scaledBomb = ii.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setImage(scaledBomb);
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }

        public void act() {
            if (!destroyed) {
                this.y += 2; // slower and more consistent than 1/frame
            }
        }
    }

}
