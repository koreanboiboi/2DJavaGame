package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tiles.TileManager;



public class GamePanel extends JPanel implements Runnable {
    
    final int originalTileSize = 16; // 16x16
    final int scale = 3;    


    public int tileSize = originalTileSize * scale;// 48x48
    public int maxScreenCol = 16;
    public int maxScreenRow = 12; // this will give aspect ration 4:3
    public int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;


    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisonChecker colChecker = new CollisonChecker(this);
    public Player player = new Player(this,keyH);

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

        //we draw tile first then player otherwise background tile will hide player
        tileM.draw(g2);
        player.draw(g2);
    
        g2.dispose();

        

    }
}
