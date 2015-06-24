package com.masterpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import java.util.ArrayList;
import java.util.List;

public class MasterPongGame extends ApplicationAdapter {

	public ModelBatch modelBatch;
	public PerspectiveCamera cam;
	public Model model;
	public List<MasterPongWall> wallInstances = new ArrayList<MasterPongWall>();
	public List<MasterPongPaddle> paddleInstances = new ArrayList<MasterPongPaddle>();
	public Environment environment;
	public CameraInputController camController;

	public ModelInstance ballInstance;
	public Vector3 ballVelocity = new Vector3(1, 1, 1);

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
		addPaddles(2, 2, 0, 0, 0, 20, 10, 10, mb);

		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.sphere(1f, 1f, 1f, 10, 10);

		model = mb.end();

		ballInstance = new ModelInstance(model, "ball");
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "roof")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "floor")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "right")));
		wallInstances.add(new MasterPongWall(new ModelInstance(model, "left")));
		paddleInstances.add(new MasterPongPaddle(new ModelInstance(model, "paddle1")));
		paddleInstances.add(new MasterPongPaddle(new ModelInstance(model, "paddle2")));

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

	private void addPaddles(float width, float height, float x0, float y0, float z0, float x1, float y1, float z1, ModelBuilder modelBuilder){
		addPlane("paddle1",
				x0, (y0 + y1 - width) / 2, (z0 + z1 - height) / 2,
				x0, (y0 + y1 + width) / 2, (z0 + z1 - height) / 2,
				x0, (y0 + y1 + width) / 2, (z0 + z1 + height) / 2,
				x0, (z0 + z1 - width) / 2, (z0 + z1 + height) / 2,
				modelBuilder);
		addPlane("paddle2",
				x1, (z0 + z1 - width) / 2, (z0 + z1 + height) / 2,
				x1, (y0 + y1 + width) / 2, (z0 + z1 + height) / 2,
				x1, (y0 + y1 + width) / 2, (z0 + z1 - height) / 2,
				x1, (y0 + y1 - width) / 2, (z0 + z1 - height) / 2,
				modelBuilder);
	}

	private void addPaddle(String id, float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, ModelBuilder modelBuilder) {
		return;
	}

	@Override
	public void render() {
		camController.update();
		ballInstance.transform.translate(0.1f, 0.1f, 0.1f);
		collisionHandling();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (MasterPongWall wall : wallInstances) {
			modelBatch.render(wall.getModel(), environment);
		}
		for (MasterPongPaddle paddle : paddleInstances){
			modelBatch.render(paddle.getModel(), environment);
		}
		modelBatch.end();

		modelBatch.render(ballInstance, environment);
		//ballInstance.§
	}

	private void collisionHandling() {
		Vector3 vector = new Vector3();
		ballInstance.transform.getTranslation(vector);
		System.out.println(vector.toString());

//		for (ModelInstance instance : wallInstances) {
//			instance.transform.
//		}
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}
}
