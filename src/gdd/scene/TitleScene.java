package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TitleScene extends JPanel {

    private int frame = 0;
    private Image image;
    private AudioPlayer audioPlayer;
    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;
    private Game game;

    public TitleScene(Game game) {
        this.game = game;
    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        initTitle();
        initAudio();
    }

    public void stop() {
        try {
            if (timer != null) timer.stop();
            if (audioPlayer != null) audioPlayer.stop();
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void initTitle() {
        var ii = new ImageIcon(IMG_TITLE);
        image = ii.getImage();
    }

    private void initAudio() {
        try {
            String filePath = "src/audio/title.wav";
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing title audio: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.drawImage(image, 0, -80, d.width, d.height, this);

        // Flashing "Press SPACE to Start"
        g.setFont(new Font("Helvetica", Font.BOLD, 32));
        g.setColor((frame % 60 < 30) ? Color.red : Color.white);
        String text = "Press SPACE to Start";
                // Team Member Names
        g.setFont(new Font("Helvetica", Font.PLAIN, 18));
        g.setColor(Color.white);
        g.drawString("Team Members:", 50, 540);
        g.drawString("Pyae Phyo Aung - 6530069", 70, 565);
        g.drawString("Phyu Thandar Khin - 6520271", 70, 590);
        int textX = (d.width - g.getFontMetrics().stringWidth(text)) / 2;
        g.drawString(text, textX, 600);


        // Footer credit
        g.setFont(new Font("Helvetica", Font.PLAIN, 10));
        g.setColor(Color.gray);
        g.drawString("Game by Chayapol", 10, 650);

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {
        frame++;
    }

    private void doGameCycle() {
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
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                game.loadScene2();
            }
        }
    }
}
