package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class SpeedUp extends PowerUp {

    public SpeedUp(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(IMG_POWERUP_SPEEDUP).getImage();
    }

    @Override
    public void upgrade(Player player) {
        player.increaseSpeed();
        this.setDying(true);
    }
}
