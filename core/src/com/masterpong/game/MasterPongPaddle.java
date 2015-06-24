package com.masterpong.game;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Magnus on 24/6/2015.
 */
public class MasterPongPaddle extends MasterPongWall{

    private void setX(float x){
        Vector3 location = model.transform.getTranslation(new Vector3());
        location.x = x;
        model.transform.setTranslation(location);
    }

    private void setY(float y){
        Vector3 location = model.transform.getTranslation(new Vector3());
        location.y = y;
        model.transform.setTranslation(location);
    }

    private void setZ(float z){
        Vector3 location = model.transform.getTranslation(new Vector3());
        location.z = z;
        model.transform.setTranslation(location);
    }

    private void move(float x, float y, float z){
        model.transform.translate(x, y, z);
    }

    private void move(Vector3 translation){
        model.transform.translate(translation);
    }

    private void moveX(float x){
        move(x, 0, 0);
    }

    private void moveY(float y){
        move(0 , y, 0);
    }

    private void moveZ(float z){
        move(0, 0, z);
    }
}
