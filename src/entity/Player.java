package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;

    public int standCounter = 0;

    public boolean dodging = false;
    public int dodgeTimer = 0;
    public int dodgeCooldown = 0;
    BufferedImage dodgeRightSprite, dodgeLeftSprite, right, left, up, down;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyHandler = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 5;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage() {

        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");

        try {
            dodgeRightSprite = ImageIO.read(getClass().getResourceAsStream("/player/dash_right.png"));
            dodgeLeftSprite = ImageIO.read(getClass().getResourceAsStream("/player/dash_left.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/player/boy_right.png"));
            left = ImageIO.read(getClass().getResourceAsStream("/player/boy_left.png"));
            up = ImageIO.read(getClass().getResourceAsStream("/player/boy_up.png"));
            down = ImageIO.read(getClass().getResourceAsStream("/player/boy_down.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage setup(String imageName) {
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = utilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void update() {

        // 1. COOLDOWN
        if (dodgeCooldown > 0) dodgeCooldown--;

        boolean isMoving = false;

        // Variables para capturar la INTENCIÓN de movimiento (-1, 0, 1)
        int dx = 0;
        int dy = 0;

        // --- LEER INPUT ---
        if (keyHandler.upPressed)    dy = -1;
        if (keyHandler.downPressed)  dy = 1;
        if (keyHandler.leftPressed)  dx = -1;
        if (keyHandler.rightPressed) dx = 1;

        // Si hay input, nos estamos moviendo
        if (dx != 0 || dy != 0) isMoving = true;

        // --- DASH (Sin cambios) ---
        if (keyHandler.spacePressed && dodgeCooldown == 0 && isMoving) {
            dodging = true;
            dodgeCooldown = 60;
            spriteCounter = 0;
            spriteNum = 0;
        }

        // --- CALCULAR VELOCIDAD FINAL ---
        double finalSpeed = speed;

        if (dodging) {
            finalSpeed *= 2;
            dodgeTimer++;
            if (dodgeTimer > 20) {
                dodging = false;
                dodgeTimer = 0;
                finalSpeed = speed;
                spriteNum = 1;
            }
        }

        // Corrección Pitágoras (Si es diagonal)
        if (dx != 0 && dy != 0) {
            finalSpeed *= 0.7071;
        }

        // Convertimos la velocidad a entero para usarla en colisiones
        int currentSpeed = (int) finalSpeed;


        // --- MOVIMIENTO Y COLISIONES (EJE POR EJE) ---
        // Solo procesamos si hay intención de moverse
        if (isMoving) {

            // Guardamos la dirección "visual" original para no romper la animación
            String originalDirection = direction;

            // Actualizamos la dirección visual basada en el último input
            // (Esto es solo para que el sprite mire al lado correcto al final)
            if(dy == -1) direction = "up";
            if(dy == 1)  direction = "down";
            if(dx == -1) direction = "left";
            if(dx == 1)  direction = "right";


            // === PASO 1: INTENTAR MOVER EN X ===
            if (dx != 0) {
                // 1.1 Forzamos la dirección para que checkTile revise el lado correcto
                if (dx == -1) direction = "left";
                if (dx == 1)  direction = "right";

                // 1.2 Chequeamos colisión
                collisionOn = false;
                gp.collisionChecker.checkTile(this);
                // IMPORTANTE: checkObject requiere la dirección correcta también
                int objIndex = gp.collisionChecker.checkObject(this, true);
                pickupObject(objIndex); // Recogemos objeto si chocamos en X

                // 1.3 Si no hay pared, movemos SOLO en X
                if (collisionOn == false) {
                    worldX += dx * currentSpeed;
                }
            }

            // === PASO 2: INTENTAR MOVER EN Y ===
            if (dy != 0) {
                // 2.1 Forzamos la dirección vertical
                if (dy == -1) direction = "up";
                if (dy == 1)  direction = "down";

                // 2.2 Chequeamos colisión
                collisionOn = false;
                gp.collisionChecker.checkTile(this);
                int objIndex = gp.collisionChecker.checkObject(this, true);
                pickupObject(objIndex); // Recogemos objeto si chocamos en Y

                // 2.3 Si no hay pared, movemos SOLO en Y
                if (collisionOn == false) {
                    worldY += dy * currentSpeed;
                }
            }

            // === PASO 3: RESTAURAR DIRECCIÓN VISUAL ===
            // Si nos movimos en diagonal, la dirección quedó seteada en el último eje revisado (Y).
            // Si quieres priorizar que se vea de lado (left/right) en diagonales:
            if (dx != 0) {
                if (dx == -1) direction = "left";
                if (dx == 1) direction = "right";
            } else if (dy != 0) {
                // Si no se mueve horizontalmente, usamos la vertical
                if (dy == -1) direction = "up";
                if (dy == 1) direction = "down";
            }
        }

        // --- 7. ANIMACIONES ---
        if (dodging == true) {
            spriteCounter++;
            if (spriteCounter > 3) {
                spriteNum++;
                if (spriteNum > 6) spriteNum = 0;
                spriteCounter = 0;
            }
        }
        else {
            if (isMoving == true) {
                spriteCounter++;
                if (spriteCounter > 5) {
                    spriteNum++;
                    if (spriteNum >= 8) spriteNum = 0;
                    spriteCounter = 0;
                }
            } else {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }
    }

//    public void update() {
//
//        // 1. GESTIÓN DEL COOLDOWN
//        if (dodgeCooldown > 0) {
//            dodgeCooldown--;
//        }
//
//        // Variable para saber si el jugador quiere moverse
//        boolean isMoving = false;
//
//        // --- 2. LEER INPUT DE DIRECCIÓN (SIEMPRE) ---
//        // Ya no importa si dodging es true o false, leemos el teclado igual.
//        if (keyHandler.upPressed == true) {
//            direction = "up";
//            isMoving = true;
//        } else if (keyHandler.downPressed == true) {
//            direction = "down";
//            isMoving = true;
//        } else if (keyHandler.leftPressed == true) {
//            direction = "left";
//            isMoving = true;
//        } else if (keyHandler.rightPressed == true) {
//            direction = "right";
//            isMoving = true;
//        }
//
//        // --- 3. ACTIVAR EL DASH ---
//        // Si presiona espacio, tiene cooldown disponible y se está moviendo
//        if (keyHandler.spacePressed == true && dodgeCooldown == 0 && isMoving == true) {
//            dodging = true;
//            dodgeCooldown = 60;
//            spriteCounter = 0;
//            spriteNum = 0;
//        }
//
//        // --- 4. GESTIÓN DEL ESTADO DASH ---
//        int currentSpeed = speed; // Velocidad base
//
//        if (dodging == true) {
//            currentSpeed = speed * 2; // Aumentamos velocidad
//            dodgeTimer++;
//
//            // Opción B: Sigue deslizándose hasta que termine el dash -> Descomenta la línea de abajo:
//             isMoving = true;
//
//            if (dodgeTimer > 20) { // Duración del dash
//                dodging = false;
//                dodgeTimer = 0;
//                currentSpeed = speed;
//                spriteNum = 1; // Reset de animación
//            }
//        }
//
//        // --- 5. COLISIONES ---
//        collisionOn = false;
//        gp.collisionChecker.checkTile(this);
//        int objIndex = gp.collisionChecker.checkObject(this, true);
//        pickupObject(objIndex);
//
//        // --- 6. MOVER AL JUGADOR ---
//        // Solo movemos si hay input de movimiento (isMoving) y no hay colisión
//        if (collisionOn == false && isMoving == true) {
//            switch (direction) {
//                case "up":    worldY -= currentSpeed; break;
//                case "down":  worldY += currentSpeed; break;
//                case "left":  worldX -= currentSpeed; break;
//                case "right": worldX += currentSpeed; break;
//            }
//        }
//
//        // --- 7. ANIMACIONES ---
//        if (dodging == true) {
//            // Animación Dash
//            spriteCounter++;
//            if (spriteCounter > 3) {
//                spriteNum++;
//                if (spriteNum > 6) spriteNum = 0; // Ajusta según frames de tu dash
//                spriteCounter = 0;
//            }
//        }
//        else {
//            // Animación Caminar
//            if (isMoving == true) {
//                spriteCounter++;
//                if (spriteCounter > 5) {
//                    spriteNum++;
//                    if (spriteNum >= 8) spriteNum = 0;
//                    spriteCounter = 0;
//                }
//            } else {
//                // Idle
//                standCounter++;
//                if (standCounter == 20) {
//                    spriteNum = 1;
//                    standCounter = 0;
//                }
//            }
//        }
//    }

    public void pickupObject(int i) {

        if (i != 999) {

            String objName = gp.objects[i].name;

            switch(objName) {
                case "Key":
                    hasKey++;
                    gp.objects[i] = null;
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.objects[i] = null;
                        hasKey--;
                    }
                    break;

            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int sx1 = spriteNum * gp.originalTileSize;
        int sy1 = 0;

        int sx2 = sx1 + gp.originalTileSize;
        int sy2 = sy1 + gp.originalTileSize;

        if (dodging == true) {


            image = dodgeRightSprite;
            g2.drawImage(image, screenX, screenY, screenX + gp.tileSize, screenY + gp.tileSize, sx1, sy1, sx2, sy2,null);

            if (direction == "left") {
                image = dodgeLeftSprite;
                g2.drawImage(image, screenX, screenY, screenX + gp.tileSize, screenY + gp.tileSize, sx1, sy1, sx2, sy2,null);
            }
        } else {
            switch (direction) {
                case "up":
                    image = up;
                    break;
                case "down":
                    image = down;
                    break;
                case "right":
                    image = right;
                    break;
                case "left":
                    image = left;
                    break;
                case "":
                    image = down1;
                break;
            }
            g2.drawImage(image, screenX, screenY, screenX + gp.tileSize, screenY + gp.tileSize, sx1, sy1, sx2, sy2,null);

//            g2.drawImage(image, screenX, screenY, null);
        }
    }
}
