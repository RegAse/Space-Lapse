package com.spacelapse.resourcemanager;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Textures {

    private static Image fighter;

    public static Image getFighter() throws SlickException {
        if (fighter == null)
            fighter = new Image("data/ships/Fighter/Fighter.png", false);
        return fighter;
    }

    private static Image enforcer;

    public static Image getEnforcer() throws SlickException {
        if (enforcer == null)
            enforcer = new Image("data/ships/Enforcer/Enforcer.png", false);
        return enforcer;
    }

    private static Image asteroid;

    public static Image getAsteroid() throws SlickException {
        if (asteroid == null)
            asteroid = new Image("data/rocks/rock.png", false);
        return asteroid;
    }

    private static Image bullet;

    public static Image getBullet() throws SlickException {
        if (bullet == null)
            bullet = new Image("data/bullets/bullet01_tiny2.png", false);
        return bullet;
    }

    private static Image spaceBackground;

    public static Image getSpaceBackground() throws SlickException {
        if (spaceBackground == null)
            spaceBackground = new Image("data/backgrounds/space_highres.png", false);
        return spaceBackground;
    }
}
