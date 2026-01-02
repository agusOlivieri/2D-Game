package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyHandler;

//    public static final int[][] PLAYER_SPRITE = {
//            {0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0},
//            {0,0,0,1,2,2,2,2,2,2,1,0,0,0,1,1},
//            {0,0,0,1,2,1,1,1,1,2,1,0,0,1,3,1},
//            {0,0,0,1,2,1,4,4,1,2,1,0,0,1,3,1},
//            {0,0,0,1,2,1,4,4,1,2,1,0,0,1,3,1},
//            {0,0,0,1,2,2,1,1,2,2,1,0,0,1,3,1},
//            {0,0,0,0,1,5,5,5,5,1,0,0,1,3,3,1},
//            {0,0,0,1,5,5,5,5,5,5,1,1,1,1,1,1},
//            {0,0,1,5,5,5,5,5,5,5,5,1,0,0,0,0},
//            {0,1,6,5,5,5,2,2,5,5,5,6,1,0,0,0},
//            {0,1,6,5,5,5,2,2,5,5,5,6,1,0,0,0},
//            {0,0,1,5,5,5,5,5,5,5,5,1,0,0,0,0},
//            {0,0,0,1,1,1,0,0,1,1,1,0,0,0,0,0},
//            {0,0,0,1,6,1,0,0,1,6,1,0,0,0,0,0},
//            {0,0,0,1,6,1,0,0,1,6,1,0,0,0,0,0},
//            {0,0,0,1,1,1,0,0,1,1,1,0,0,0,0,0}
//    };

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyHandler = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {

            down = ImageIO.read(getClass().getResourceAsStream("/player/github-octopuss-2.png"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen del leñador.");
        }
    }

    public void update() {
        if (keyHandler.upPressed == true) {
            direction = "down";
            y -= speed;
        } else if (keyHandler.downPressed == true) {
            direction = "down";
            y += speed;
        } else if (keyHandler.rightPressed == true) {
            direction = "down";
            x += speed;
        } else if (keyHandler.leftPressed == true) {
            direction = "down";
            x -= speed;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = down;
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}
