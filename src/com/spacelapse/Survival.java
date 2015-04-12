package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Survival extends BasicGameState {

    public static ArrayList<Ship> ships;
    public static int my_id;

    /** Images **/
    private static Image texture;
    private static Image bulletTexture;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        /** Setup images **/
        texture = new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false);
        bulletTexture = new Image("data/bullets/bullet01_tiny2.png", false);

        /** Setup ships **/
        ships = new ArrayList<Ship>();
        //ships.add(new Ship(new Vector2f(100 ,100), 0.5f));
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics graphics) throws SlickException {
        for(Ship ship : ships)
        {
            ship.render(graphics, texture, bulletTexture);
        }

        graphics.drawString("PlayerCount: " + GameSession.playerCount, 40, 40);
    }

    public void update(GameContainer gameContainer, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gameContainer.getInput();

        for (int i = 0; i < ships.size(); i++){
            if (ships.get(i).id == my_id){
                ships.get(i).Controller(gameContainer, delta, texture);
            }
        }
    }

    public Survival(int state) {

    }

    public int getID() {
        return 2;
    }
}
