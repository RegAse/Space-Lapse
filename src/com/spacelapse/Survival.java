package com.spacelapse;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Survival extends BasicGameState {

    private ArrayList<Ship> ships;
    private ArrayList<Ship> serverShips;
    public static int playerCount;

    public Survival(int state)
    {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        ships = new ArrayList<Ship>();
        ships.add(new Ship(new Vector2f(100 ,100), new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false), 0.5f));
        ships.add(new Ship(new Vector2f(200 ,130), new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false), 0.5f));
        ships.add(new Ship(new Vector2f(300 ,150), new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false), 0.5f));
        ships.add(new Ship(new Vector2f(400 ,200), new Image("data/playerships/Enforcer/Enforcer_idle_128x.png", false), 0.5f));
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        for(Ship ship : ships)
        {
            ship.render(g);
        }

        g.drawString("PlayerCount: " + playerCount, 40, 40);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();

        ships.get(0).Controller(gc, delta);
    }

    public int getID()
    {
        return 2;
    }
}
