package com.spacelapse;

import com.spacelapse.entities.Bullet;
import com.spacelapse.entities.Entity;
import com.spacelapse.entities.Ship;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Survival extends BasicGameState {

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static int my_id;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.render(gc, graphics);
            if (entity instanceof Ship && entity.id == my_id) {
                ((Ship)entity).renderShipUI(gc, graphics);
            }
        }

        graphics.drawString("PlayerCount: " + entities.size(), 40, 40);
        graphics.draw(new Rectangle(200, 200, 40, 40));
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Ship && entity.id == my_id) {
                Ship ship = (Ship) entity;
                ship.Controller(gameContainer, delta);
                ship.rotateTowardsMouse(gameContainer);
                ship.updatePositionToServer();
            }
            else if (entity instanceof Bullet) {
                Bullet bullet = (Bullet) entity;

                /*for (int i2 = entities.size() - 1; i2 >= 0; i2--) {
                    Entity entity2 = entities.get(i2);
                    if (!(entity2 instanceof Bullet)) {
                        if (bullet.intersects(entity2)) {

                            break;
                        }
                    }
                }*/
                bullet.addForceToBullet(gameContainer, delta);
            }
        }
    }

    public Survival(int state) {

    }

    public int getID() {
        return 2;
    }
}
