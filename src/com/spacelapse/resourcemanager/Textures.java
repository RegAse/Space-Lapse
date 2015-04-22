package com.spacelapse.resourcemanager;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Textures {

    private static Image Fighter;

    public static Image getFighter() throws SlickException {
        if (Fighter == null)
            Fighter = new Image("data/ships/Fighter/Fighter.png", false);
        return Fighter;
    }

    private static Image Enforcer;

    public static Image getEnforcer() throws SlickException {
        if (Enforcer == null)
            Enforcer = new Image("data/ships/Enforcer/Enforcer.png", false);
        return Enforcer;
    }

    private static Image Asteroid;

    public static Image getAsteroid() throws SlickException {
        if (Asteroid == null)
            Asteroid = new Image("data/rocks/rock.png", false);
        return Asteroid;
    }

    private static Image Bullet;

    public static Image getBullet() throws SlickException {
        if (Bullet == null)
            Bullet = new Image("data/bullets/bullet01_tiny2.png", false);
        return Bullet;
    }
}
