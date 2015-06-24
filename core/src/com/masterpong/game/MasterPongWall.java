package com.masterpong.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBox2dShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * Created by Magnus on 24/6/2015.
 */
public class MasterPongWall {
    protected ModelInstance model;
    protected btCollisionShape collisionShape;
    protected btCollisionObject collisionObject;

    public MasterPongWall(){
        this(null);
    }

    public MasterPongWall(ModelInstance model){
        this.model = model;
        Vector3 vector = new Vector3();
        model.transform.getTranslation(vector);
        collisionShape = new btBox2dShape(vector);
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(collisionShape);
        collisionObject.setWorldTransform(model.transform);
    }

    public ModelInstance getModel(){
        return model;
    }

    public void dispose() {
        collisionShape.dispose();
        collisionObject.dispose();
    }
}
