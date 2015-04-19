package com.spacelapse.ship;

import com.spacelapse.GameClient;
import com.spacelapse.Response;
import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;

public class Fighter extends Ship{

    /**
     * Constructor for Fighter
     */
    public Fighter(int x, int y, float speed) throws SlickException {
        super(x, y, speed);
    }

    /**
     * Render method
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        Image texture = Textures.getFighter();
        Input input = gameContainer.getInput();
        graphics.drawString(Integer.toString(this.id), position.getX(), position.getY() - (texture.getHeight()/ 2) - 20);

        texture.setRotation(rotation + 90f);
        texture.drawCentered(position.getX(), position.getY());

        renderShots(Textures.getBullet());
    }

    /**
     * updatePositionToServer
     */
    @Override
    public void updatePositionToServer()
    {
        if (GameClient.isInitialized && hasChanged) {
            //Response response = new Response(this);
            //response.sendData();
        }
    }
}
