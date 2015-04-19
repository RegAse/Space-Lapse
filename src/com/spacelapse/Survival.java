package com.spacelapse;

import com.spacelapse.ship.Ship;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Survival extends BasicGameState {

    public static ArrayList<Ship> ships = new ArrayList<>();
    public static int my_id;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).render(gc, graphics);
        }

        graphics.drawString("PlayerCount: " + ships.size(), 40, 40);
        graphics.draw(new Rectangle(200, 200, 40, 40));
    }


    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();

        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).id == my_id) {
                ships.get(i).Controller(gameContainer, delta);
                ships.get(i).rotateTowardsMouse(gameContainer);
                ships.get(i).updatePositionToServer();
            }

            Ship ship1 = ships.get(i);
            int u = - 1;
            while (++u < ship1.shots.size()) {
                Bullet bull = ship1.shots.get(u);
                for (Ship ship2 : ships) {
                    if (ship2 != ship1 && ship2.intersects(bull)) {
                        ship1.shots.remove(u);
                        System.out.println("Removed bullet.");
                        break;
                    }
                }
            }

            /** Add force to all bullets  **/
            ships.get(i).addForceToBullets(gameContainer, delta);
        }
    }

    public Survival(int state) {

    }

    public int getID() {
        return 2;
    }
}
