package Surviv.Entities.Client;

import Surviv.Networking.Packets.TestRequestPacket;
import Util.Engine.Networking.Client.ClientGameEntity;
import Util.Engine.Networking.Client.ClientNetTransform;
import Util.Engine.Networking.Packet;
import Util.Engine.Scene;
import Util.Math.Vec2f;
import com.esotericsoftware.kryonet.Connection;


public class Player extends ClientGameEntity
{
	private static final String SPRITE_PATH = "/Assets/Sprites/character.png";


	public Player(Scene scene, short networkId)
	{
		super(scene, SPRITE_PATH, networkId);
		transform.scale = new Vec2f(0.1f, 0.1f);

		addBehavior(new ClientNetTransform(this));
	}


	@Override
	public int getLayer()
	{
		return 0;
	}


	@Override
	public void onReceivePacket(Connection sender, Packet packet)
	{
		if (packet instanceof TestRequestPacket)
		{
			System.out.println(((TestRequestPacket)packet).message);
		}
	}


	@Override
	public void update()
	{
		super.update();

//		float horizontal = Engine.input().getAxis("Horizontal");
//		float vertical = Engine.input().getAxis("Vertical");
//
//		transform.position.x += horizontal * Time.deltaTime();
//		transform.position.y += vertical * Time.deltaTime();

		//transform.rotation += Time.deltaTime();
	}
}
