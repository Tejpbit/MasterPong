package com.masterpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;

import java.util.ArrayList;
import java.util.List;

public class MasterPongGame extends ApplicationAdapter {

	public ModelBatch modelBatch;
	public PerspectiveCamera cam;
	public Model model;
	public Environment environment;
	public CameraInputController camController;

	public List<MasterPongWall> wallInstances = new ArrayList<>();

	public MasterPongBall ball;

	btCollisionConfiguration collisionConfig;;
	btDispatcher dispatcher;

	@Override
	public void create () {
		Bullet.init();
		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 0f);
		cam.lookAt(10f,5f,5f);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		addPlanes(0, 0, 0, 20, 10, 10, mb);

		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.sphere(1f, 1f, 1f, 20, 20);

		model = mb.end();

		ball = new MasterPongBall(new ModelInstance(model, "ball"));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "roof")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "floor")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "right")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "left")));


		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	private void addPlanes(float x0, float y0, float z0, float x1, float y1, float z1, ModelBuilder modelBuilder){
		addPlane("left",
				x0, y0, z0,
				x1, y0, z0,
				x1, y1, z0,
				x0, y1, z0, modelBuilder);
		addPlane("floor",
				x0, y0, z0,
				x0, y0, z1,
				x1, y0, z1,
				x1, y0, z0,
				modelBuilder);
		addPlane("right",
				x0, y1, z1,
				x1, y1, z1,
				x1, y0, z1,
				x0, y0, z1,
				modelBuilder);
		addPlane("roof",
				x0, y1, z0,
				x1, y1, z0,
				x1, y1, z1,
				x0, y1, z1,
				modelBuilder);
	}

	private void addPlane(String id, float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, ModelBuilder modelBuilder) {
		Color[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.CYAN};

		modelBuilder.node().id = id;
		modelBuilder.part("plane", GL20.GL_TRIANGLES,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(colors[(int) (Math.random() * colors.length)])))
				.rect(
						x00, y00, z00,
						x10, y10, z10,
						x11, y11, z11,
						x01, y01, z01,
						5, 5, 5);
	}

	boolean collision;

	@Override
	public void render() {
		final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

		if (!collision) {
			ball.getModelInstance().transform.translate(0f, -delta, 0f);
			ball.getCollisionObject().setWorldTransform(ball.getModelInstance().transform);

			collision = checkCollision();
		}

		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (MasterPongWall wall : wallInstances) {
			modelBatch.render(wall.getModel(), environment);
		}
		modelBatch.end();

		modelBatch.render(ball.getModelInstance(), environment);
	}

	private boolean checkCollision() {
		return false;
	}

	@Override
	public void dispose() {
		dispatcher.dispose();
		collisionConfig.dispose();

		ball.dispose();

		for (MasterPongWall wall : wallInstances) {
			wall.dispose();
		}
//		TODO dispose of all shapes and objects used for collision detection, the ones created in master pong ball and wall
		modelBatch.dispose();
		model.dispose();
	}
}
