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

//    public void draw(Graphics2D g2) {
//        // 1. DEFINIR ESCALA:
//        // Como el sprite es 16x16, si lo dibujas tal cual será minúsculo.
//        // Necesitamos un multiplicador. Si en tu GamePanel (gp) tienes definido
//        // un 'tileSize' (ej. 48px), la escala sería: gp.tileSize / 16.
//        // Por seguridad, aquí usaré un 3 fijo (16x3 = 48px de tamaño final).
//        int pixelScale = 3;
//
//        // Si tu gp tiene tileSize público, descomenta la siguiente línea:
//        // int pixelScale = gp.tileSize / 16;
//
//        // 2. RECORRER LA MATRIZ
//        for (int row = 0; row < PLAYER_SPRITE.length; row++) {
//            for (int col = 0; col < PLAYER_SPRITE[0].length; col++) {
//
//                int colorIndex = PLAYER_SPRITE[row][col];
//
//                // Si el índice es 0, es transparente, así que saltamos
//                if (colorIndex != 0) {
//
//                    // A. Seleccionar el color
//                    g2.setColor(getColor(colorIndex));
//
//                    // B. Calcular la posición exacta de este "pixel"
//                    // 'x' e 'y' son las coordenadas del jugador en la pantalla.
//                    // Sumamos (col * escala) para desplazar el pixel a la derecha
//                    // Sumamos (row * escala) para desplazar el pixel hacia abajo
//                    int drawX = (int)x + (col * pixelScale);
//                    int drawY = (int)y + (row * pixelScale);
//
//                    // C. Dibujar el pixel (que en realidad es un cuadrado escalado)
//                    g2.fillRect(drawX, drawY, pixelScale, pixelScale);
//                }
//            }
//        }
//    }
//
//    // Método auxiliar para mantener el draw limpio
//    private Color getColor(int index) {
//        switch (index) {
//            case 1: return Color.BLACK;             // Bordes
//            case 2: return Color.DARK_GRAY;         // Casco
//            case 3: return new Color(200, 200, 200); // Espada (Gris claro)
//            case 4: return new Color(255, 200, 150); // Piel
//            case 5: return new Color(0, 100, 200);   // Ropa Azul
//            case 6: return new Color(139, 69, 19);   // Botas Marrones
//            default: return Color.MAGENTA; // Color de error por si se nos pasa un número
//        }
//    }
}
