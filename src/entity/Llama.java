package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Llama extends Entity{

    GamePanel gp;

    public Llama(GamePanel gp) {

        this.gp = gp;

        worldX = 24 * gp.tileSize;
        worldY = 22 * gp.tileSize;

        try {
            System.out.println("intentando cargar imagen");
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/llama_right.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/llama_left.png"));
            System.out.println("se cargó la imagen correctamente");
        }catch (IOException e) {
            e.printStackTrace();
        }

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 21;
        worldY = gp.tileSize * 21;
        speed = 1;
        direction = "right";
    }

    public void update() {

        // CHECK TILE COLLISION
        collisionOn = false;
        gp.collisionChecker.checkTile(this);

        if (collisionOn == false) {
            switch (direction) {
                case "right":
                    worldX += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
            }
        } else {
            if (direction == "right") {
                direction = "left";
            } else if (direction == "left") {
                direction = "right";
            }
        }

        spriteCounter++;

        if (spriteCounter > 6) {
            spriteNum++;

            if (spriteNum >= 6) {
                spriteNum = 0;
            }

            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            int sx1 = spriteNum * gp.tileSize;
            int sy1 = 0;

            int sx2 = sx1 + gp.tileSize;
            int sy2 = sy1 + gp.tileSize;

            switch (direction) {
                case "right":
                    g2.drawImage(right1, screenX, screenY, screenX + gp.tileSize * 2, screenY + gp.tileSize * 2, sx1, sy1, sx2, sy2,null);
                    break;
                case "left":
                    g2.drawImage(left1, screenX, screenY, screenX + gp.tileSize * 2, screenY + gp.tileSize * 2, sx1, sy1, sx2, sy2,null);
                    break;
            }
        }

    }
}
