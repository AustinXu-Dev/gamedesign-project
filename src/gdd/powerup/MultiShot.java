package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;
import java.awt.Image;

public class MultiShot extends PowerUp {

    public MultiShot(int x, int y) {
        super(x, y);

        ImageIcon ii = new ImageIcon(IMG_POWERUP_MULTISHOT); // You need to add this image!
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() ,
                ii.getIconHeight() ,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void act() {
        this.y += 2; // Move down
    }

    public void upgrade(Player player) {
        player.increaseMultiShot(); // We'll add this method in Player
        this.die();
    }
}
