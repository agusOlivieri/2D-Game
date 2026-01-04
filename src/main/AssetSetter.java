package main;

import object.OBJ_Key;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObject() {
        gamePanel.objects[0] = new OBJ_Key();
        gamePanel.objects[0].worldX = 23 * gamePanel.tileSize;
        gamePanel.objects[0].worldY = 7 * gamePanel.tileSize;

        gamePanel.objects[1] = new OBJ_Key();
        gamePanel.objects[1].worldX = 23 * gamePanel.tileSize;
        gamePanel.objects[1].worldY = 40 * gamePanel.tileSize;

//        gamePanel.objects[2] = new OBJ_Key();
//        gamePanel.objects[2].worldX = 23 * gamePanel.tileSize;
//        gamePanel.objects[2].worldY = 7 * gamePanel.tileSize;
    }

}
