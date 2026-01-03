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

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyHandler = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
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
            worldY -= speed;
        } else if (keyHandler.downPressed == true) {
            direction = "down";
            worldY += speed;
        } else if (keyHandler.rightPressed == true) {
            direction = "down";
            worldX += speed;
        } else if (keyHandler.leftPressed == true) {
            direction = "down";
            worldX -= speed;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = down;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
