package com.spacelapse;

import com.spacelapse.ship.Ship;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Survival extends BasicGameState {

    public static ArrayList<Ship> ships;
    public static int my_id;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        /** Setup ships **/
        ships = new ArrayList<Ship>();
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        for(Ship ship : ships) {
            ship.render(gc, graphics);
        }

        graphics.drawString("PlayerCount: " + GameSession.playerCount, 40, 40);
    }


    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();

        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).id == my_id) {
                ships.get(i).Controller(gameContainer, delta);
                ships.get(i).rotateTowardsMouse(gameContainer);
                ships.get(i).updatePositionToServer();
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
