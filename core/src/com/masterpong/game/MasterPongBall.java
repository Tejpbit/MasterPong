package com.masterpong.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import java.util.Vector;

/**
 * Created by tejp on 25/06/15.
 */
public class MasterPongBall {
    protected ModelInstance modelInstance;
    protected btCollisionShape collisionShape;
    protected btCollisionObject collisionObject;
    protected Vector3 velocity;

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
        this.velocity = new Vector3();

    }

    public MasterPongBall(ModelInstance modelInstance, Vector3 velocity){
        this(modelInstance);
        this.velocity = velocity;
    }


    public ModelInstance getModelInstance(){
        return modelInstance;
    }

    public btCollisionObject getCollisionObject() {
        return collisionObject;
    }

    /**
     * move ball according to it's velocity using the deltaT as time difference
     * @param deltaT
     */
    public void move(float deltaT) {
        move(velocity.cpy().scl(deltaT));
    }

    /**
     * move ball according the the 3D-vector
     * @param vector
     */
    public void move(Vector3 vector) {
        move(vector.x, vector.y, vector.z);
    }

    /**
     * move the ball according to the parameters (floats) x,y,z
     * @param x
     * @param y
     * @param z
     */
    public void move(float x, float y, float z) {
        modelInstance.transform.translate(x, y, z);
        getCollisionObject().setWorldTransform(getModelInstance().transform);
    }

    public void dispose() {
        collisionShape.dispose();
        collisionObject.dispose();
    }

}
