package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Llama extends Entity{

    GamePanel gp;

    public BufferedImage image;

    public Llama(GamePanel gp) {

        this.gp = gp;

        worldX = 24 * gp.tileSize;
        worldY = 22 * gp.tileSize;

        try {
            System.out.println("intentando cargar imagen");
            image = ImageIO.read(getClass().getResourceAsStream("/player/llama_48.png"));
            System.out.println("se cargó la imagen correctamente");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
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

            g2.drawImage(image, screenX, screenY, screenX + gp.tileSize * 2, screenY + gp.tileSize * 2, sx1, sy1, sx2, sy2,null);
        }

    }
}
