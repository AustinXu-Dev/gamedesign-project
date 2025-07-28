package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Explosion extends Sprite {
    private int visibleFrame = 10;

    public Explosion(int x, int y) {
        var boom = new ImageIcon(IMG_EXPLOSION);

        // Scale explosion image to smaller size
        var scaledImage = boom.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        // Center the explosion by shifting its position
        setX(x - 16);  // half of 32
        setY(y - 16);  // half of 32
    }

    public void visibleCountDown() {
        if (visibleFrame > 0) {
            visibleFrame--;
        } else {
            setVisible(false);
        }
    }
}
