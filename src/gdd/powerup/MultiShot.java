package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;
import java.awt.Image;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class MultiShot extends PowerUp {

    public MultiShot(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(IMG_POWERUP_MULTISHOT).getImage();
    }

    @Override
    public void upgrade(Player player) {
        player.upgradeMultiShot();
        this.setDying(true);
    }
}
