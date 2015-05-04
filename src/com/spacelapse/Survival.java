package com.spacelapse;

import com.spacelapse.entities.*;
import com.spacelapse.resourcemanager.Fonts;
import com.spacelapse.resourcemanager.Textures;
import org.newdawn.slick.*;
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
    public static Enforcer my_ship;
    public static float health;
    public Player player;
    public static GameSession gameSession;
    public static boolean gameOver = false;
    public static float gameOverScore;
    private Font font30;
    private Font font22;

    public synchronized void changeEntity(Integer i, Entity entity) {
        entities.set(i, entity);
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        player = player.loadPlayer();
        gameSession = new GameSession(0, 0, 0);
        font22 = Fonts.getImpact(22);
        font30 = Fonts.getImpact(30);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        graphics.drawImage(Textures.getSpaceBackground(), -700, -700);
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (entity != null) {
                entity.render(gc, graphics);
                if (entity instanceof Ship && entity.id == my_id) {
                    // Do something if this is my ship
                    health = entity.health;
                    my_ship = (Enforcer) entity;
                }
            }
        }
        renderHUD(gc, sbg, graphics);
        // if gameOver is false then show message on screen
        if (gameOver) {
            graphics.setFont(font30);
            graphics.drawString("Score: " + gameOverScore, 300, 270);
            graphics.drawString("Game Over", 300, 300);
        }
    }

    /**
     * Renders the heads up display
     * @param gameContainer GameContainer context
     * @param sbg StateBasedGame context
     * @param graphics Graphics context
     * @throws SlickException
     */
    public void renderHUD(GameContainer gameContainer, StateBasedGame sbg, Graphics graphics) throws SlickException {
        graphics.setFont(font22);
        //graphics.drawString(player.screenName + " Level " + player.level, 10, 10);
        graphics.drawString("Score: " + gameSession.score, 10, 20);
        graphics.drawString(health + "%", gameContainer.getWidth() - 100, 20);
        //graphics.drawString("PlayerCount: " + entities.size(), 50, 50);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();
        int i3 = - 1;
        while (++i3 < entities.size()) {
            Entity entity = entities.get(i3);
            if (entity != null && entitiesToBeDestroyed.contains(entity.id)) {
                entities.remove(entity);
                entitiesToBeDestroyed.remove((Integer)entity.id);
                if (entity.id == my_id) {
                    gameOver = true;
                    gameOverScore = gameSession.score;
                    Ship ship = (Ship)entity;
                    health = ship.health;
                    my_ship = (Enforcer)ship;
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if (entity != null && entity instanceof Ship && entity.id == my_id) {
                Ship ship = (Ship) entity;
                ship.Controller(gameContainer, delta);
                ship.rotateTowardsMouse(gameContainer);
                ship.updatePositionToServer();
            }
            else if (entity != null && entity instanceof Bullet) {
                Bullet bullet = (Bullet) entity;


                for (int i2 = entities.size() - 1; i2 >= 0; i2--) {
                    Entity entity2 = entities.get(i2);

                    if (entity2 != null && !(entity2 instanceof Bullet) && entity2.id != bullet.ownerId && entity2.intersects(bullet)) {
                        // if health is 0 or below then tell the game to destroy the ship
                        if (entity2.health <= 0) {
                            entitiesToBeDestroyed.add(entity2.id);
                        }
                        break;
                    }
                }
                // Add force to bullets
                bullet.addForceToBullet(gameContainer, delta);
            }
            else if (entity != null && entity instanceof Asteroid) {
                Asteroid asteroid = (Asteroid)entity;

                // Make asteroid move towards it's target
                asteroid.moveTowardsTarget(0.190f);

                if (my_ship != null && my_ship.intersects(asteroid) && health > 0) {
                    my_ship.health -= 2;
                    if (my_ship.health <= 0) {
                        gameOver = true;
                    }
                    Response response = new Response(my_ship);
                    response.sendData();
                }
            }
        }
    }

    public Survival(int state) {

    }

    public int getID() {
        return 2;
    }
}
