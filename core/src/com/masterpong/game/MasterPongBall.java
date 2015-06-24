package com.masterpong.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

/**
 * Created by tejp on 25/06/15.
 */
public class MasterPongBall {
    protected ModelInstance modelInstance;
    protected btCollisionShape collisionShape;
    protected btCollisionObject collisionObject;

    public MasterPongBall(){
        this(null);
    }

    public MasterPongBall(ModelInstance modelInstance){
        this.modelInstance = modelInstance;
        Vector3 vector = new Vector3();
        modelInstance.transform.getScale(vector);
        collisionShape = new btSphereShape(vector.x/2);
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(collisionShape);
        collisionObject.setWorldTransform(modelInstance.transform);
    }

    public ModelInstance getModelInstance(){
        return modelInstance;
    }

    public btCollisionObject getCollisionObject() {
        return collisionObject;
    }

    public void dispose() {
        collisionShape.dispose();
        collisionObject.dispose();
    }
}
