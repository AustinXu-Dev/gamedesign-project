// gdd/powerup/PowerUp.java
package gdd.powerup;

import gdd.sprite.Player;
import java.awt.Image;

public abstract class PowerUp {
    protected int x, y;
    protected boolean visible = true;
    protected boolean dying = false;
    protected Image image;

    public abstract void upgrade(Player player);

    public void act() {
        y += 1; // falls down slowly
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean d) {
        dying = d;
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean collidesWith(Player player) {
        return (x < player.getX() + 40 &&
                x + 30 > player.getX() &&
                y < player.getY() + 30 &&
                y + 30 > player.getY());
    }
    public void die() {
        setVisible(false);
    }

}
