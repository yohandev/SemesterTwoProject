package Util.Engine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scene implements IEngineEventListener, IDrawable
{
	private String name;

	private List<GameEntity> entities;
	private Camera activeCamera;


	public Scene(String name)
	{
		this.name = name;
		this.entities = new ArrayList<>();

		// Camera ~ Only MUST have entity
		// Also adds to array directly instead of using addEntity() to avoid double-callbacks
		entities.add(activeCamera = new Camera(this, Color.GREEN));
	}


	public void addEntity(GameEntity entity)
	{
		entities.add(entity);
		entities.sort(Comparator.comparing(IDrawable::getLayer)); // Sorts by layer
		entity.awake();
		entity.start();
	}


	public void removeEntity(GameEntity entity)
	{
		entity.onDisable();
		entities.remove(entity);
	}


	@Override
	public void onSceneLoad()
	{
		for (GameEntity entity : entities)
		{
			entity.onSceneLoad();
			entity.awake();
		}

		for (GameEntity entity : entities)
		{
			entity.start();
		}
	}


	@Override
	public void awake() { }


	@Override
	public void start() { }


	@Override
	public void update()
	{
		for (GameEntity entity : entities)
		{
			entity.update();
		}
	}


	@Override
	public void onDisable() { }


	@Override
	public void onSceneUnload()
	{
		for (GameEntity entity : entities)
		{
			entity.onSceneUnload();
		}
	}


	@Override
	public void onApplicationQuit()
	{
		for (GameEntity entity : entities)
		{
			entity.onApplicationQuit();
		}
	}


	@Override
	public void draw(BufferedImage renderBuffer, AffineTransform cameraMatrix)
	{
		activeCamera.renderFrame((List<IDrawable>)(List<? extends IDrawable>) entities, renderBuffer);
	}


	@Override
	public int getLayer() { return 0; }
}
