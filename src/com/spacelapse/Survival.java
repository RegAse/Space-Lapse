package com.spacelapse;

import com.spacelapse.entities.Bullet;
import com.spacelapse.entities.Entity;
import com.spacelapse.entities.Ship;
import com.spacelapse.resourcemanager.Fonts;
import com.spacelapse.resourcemanager.Textures;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Survival extends BasicGameState{

    public static ArrayList<Entity> entities = new ArrayList<>();
    //public static LinkedBlockingQueue<Entity> entities = new LinkedBlockingQueue<>();
    public static ArrayList<Integer> entitiesToBeDestroyed = new ArrayList<>();
    /*
    * TODO need to add modification queue that is synchronized then emptied in the update function
    * TODO need to add destroy queue that is synchronized then emptied in the update function
    * */
    public static int my_id;

    public synchronized void changeEntity(Integer i, Entity entity) {
        entities.set(i, entity);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        graphics.drawImage(Textures.getSpaceBackground(), -700, -700);

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.render(gc, graphics);
            if (entity instanceof Ship && entity.id == my_id) {
                ((Ship)entity).renderShipUI(gc, graphics);
            }
        }

        graphics.drawString("PlayerCount: " + entities.size(), 40, 40);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();
        int i3 = - 1;
        while (++i3 < entities.size()) {
            Entity entity = entities.get(i3);
            if (entitiesToBeDestroyed.contains(entity.id)) {
                entities.remove(entity);
                entitiesToBeDestroyed.remove((Integer)entity.id);
            }
        }

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


                for (int i2 = entities.size() - 1; i2 >= 0; i2--) {
                    Entity entity2 = entities.get(i2);

                    if (!(entity2 instanceof Bullet) && entity2.id != bullet.ownerId && entity2.intersects(bullet)) {
                        if (entity2.health <= 0) {
                            entitiesToBeDestroyed.add(entity2.id);
                        }
                        break;
                    }
                }
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
