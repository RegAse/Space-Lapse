package com.spacelapse;

import com.spacelapse.ship.Ship;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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

    public boolean intersects(Ship entity) {
        if (entity == null || entity.position == null || position == null){
            return false;
        }
        Shape shape = new Rectangle(position.x, position.y, 10, 10);
        return shape.intersects(new Rectangle(entity.position.x, entity.position.y, 10, 10));
    }
}
