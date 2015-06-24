package com.masterpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class MasterPongGame extends ApplicationAdapter {

	public ModelBatch modelBatch;
	public PerspectiveCamera cam;
	public Model model;
	public List<ModelInstance> instances = new ArrayList<>();
	public Environment environment;
	public CameraInputController camController;

	@Override
	public void create () {
		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 5f, 5f);
		cam.lookAt(5f,5f,5f);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		ModelBuilder modelBuilder = new ModelBuilder();

		addPlane(0, 0, 0,
				20, 0, 0,
				20, 10, 0,
				0, 10, 0, modelBuilder);
		addPlane(
				0, 0, 0,
				0, 0, 10,
				20, 0, 10,
				20, 0, 0,
				modelBuilder);
		addPlane(
				0, 10, 10,
				20, 10, 10,
				20, 0, 10,
				0, 0, 10,
				modelBuilder);
		addPlane(
				0, 10, 0,
				20, 10, 0,
				20, 10, 10,
				0, 10, 10,
				modelBuilder);


		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	private void addPlane(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, ModelBuilder modelBuilder) {
		Color[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.CYAN};
		instances.add(
				new ModelInstance(modelBuilder.createRect(
						x00, y00, z00,
						x10, y10, z10,
						x11, y11, z11,
						x01, y01, z01,
						5, 5, 5,
						new Material(ColorAttribute.createDiffuse(colors[(int)(Math.random() * colors.length)])),
						VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
				)
		);
	}

	@Override
	public void render() {
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (ModelInstance instance : instances) {
			modelBatch.render(instance, environment);
		}
		modelBatch.end();

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}
}
