package com.spacelapse;

import org.newdawn.slick.geom.Vector2f;

public class Bullet {

    public Vector2f position;
    public float direction;
    public boolean destroyOnCollision;
    public float damage;

    public Bullet(float x, float y, float direction, boolean destroyOnCollision, float damage)
    {
        this.position = new Vector2f(x, y);
        this.direction = direction;
        this.destroyOnCollision = destroyOnCollision;
        this.damage = damage;
    }

}
