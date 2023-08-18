package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;



public class GamePanel extends JPanel implements Runnable {
    
    final int originalTileSize = 16; // 16x16
    final int scale = 3;    


    public int tileSize = originalTileSize * scale;// 48x48
    final int maxScreenCol = 16;
    final int maxScreenRow = 12; // this will give aspect ration 4:3
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,keyH);

    //set player's default positioon
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void startGameThread(){

        gameThread = new Thread(this); // passing GamePanel constructor
        gameThread.start();
    }



    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;

        long lastTime = System.nanoTime();
        long currentTime ;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime )/drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
         if(delta >= 1){
            update();
            repaint();
            delta--;
            drawCount ++;
         }

         if(timer>= 1000000000) {
            System.out.println("FPS: " + drawCount);
            drawCount =0;
            timer = 0;
         }
           
    
        }
    
    }


    public void update(){

        player.update();

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        player.draw(g2);
    
        g2.dispose();

        

    }
}
