package com.masterpong.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

/**
 * Created by Magnus on 24/6/2015.
 */
public class MasterPongWall {
    private ModelInstance model;
    private btCollisionObject collisionObject;

    public MasterPongWall(ModelInstance model){
        this(model, null);
    }

    public MasterPongWall(ModelInstance model, btCollisionObject collisionObject){
        this.model = model;
        this.collisionObject = collisionObject;
    }

    public ModelInstance getModel(){
        return model;
    }
}