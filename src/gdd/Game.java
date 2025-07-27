package gdd;

import gdd.scene.Scene1;
import gdd.scene.TitleScene;
import javax.swing.JFrame;

public class Game extends JFrame  {

    TitleScene titleScene;
    Scene1 scene1, scene2;

    public Game() {
        titleScene = new TitleScene(this);
        scene1 = new Scene1(this, 1);
        scene2 = new Scene1(this, 2);
        initUI();
        loadTitle();
    }

    private void initUI() {
        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void loadTitle() {
        getContentPane().removeAll();
        add(titleScene);
        titleScene.start();
        revalidate();
        repaint();
    }

    public void loadScene2() {
        getContentPane().removeAll();
        titleScene.stop();
        add(scene1);
        scene1.start();
        revalidate();
        repaint();
    }

    public void loadScene3() {
        getContentPane().removeAll();
        scene1.stop();
        add(scene2);
        scene2.start();
        revalidate();
        repaint();
    }
}