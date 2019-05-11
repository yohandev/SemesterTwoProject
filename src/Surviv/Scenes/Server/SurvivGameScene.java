package Surviv.Scenes.Server;

import Surviv.Entities.Server.*;
import Surviv.Scenes.SurvivMap;
import Util.Engine.Engine;
import Util.Engine.Input;
import Util.Engine.Networking.Server.ServerNetManager;
import Util.Engine.Scene;
import com.esotericsoftware.kryonet.Connection;


public class SurvivGameScene extends Scene
{
	private boolean gameStarted = false;


	public SurvivGameScene()
	{
		super("Surviv Game");

		SurvivMap.generateMap(this);
	}


	@Override
	public void onSceneLoad()
	{
		super.onSceneLoad();
	}


	@Override
	public void update()
	{
		super.update();

		if (Input.getButtonDown(' ') && !gameStarted)
		{
			for (Connection c : ((ServerNetManager)Engine.netManager()).getConnections())
			{
				addEntity(new Player(this, c));
			}

			gameStarted = true;
		}
	}
}
