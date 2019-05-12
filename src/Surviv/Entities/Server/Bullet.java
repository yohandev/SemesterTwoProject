package Surviv.Entities.Server;

import Surviv.Behaviors.FadeBehavior;
import Surviv.Networking.Packets.SpawnBulletPacket;
import Util.Engine.Networking.Client.ClientGameEntity;
import Util.Engine.Networking.Packet;
import Util.Engine.Networking.Server.ServerGameEntity;
import Util.Engine.Physics.Collider;
import Util.Engine.Physics.Colliders.PointCollider;
import Util.Engine.Physics.CollisionInfo;
import Util.Engine.Scene;
import Util.Engine.Time;
import Util.Math.Vec2f;
import com.esotericsoftware.kryonet.Connection;


public class Bullet extends ServerGameEntity
{
	private static String SPRITE_PATH = "/Assets/Sprites/Environment/Bullets/bullet_short_trail.png";


	private Vec2f origin, dir;
	private float distSquared;

	private FadeBehavior fadeBehavior;

	private PointCollider collider;


	public Bullet(Scene scene, Vec2f origin, float dir, float dist)
	{
		super(scene, SPRITE_PATH, origin, dir, dist);

		anchor = new Vec2f(0.5f, 0.05f);

		this.origin = this.transform.position = origin;
		this.distSquared = dist * dist;

		this.transform.rotation = dir;

		float radDir = (float)Math.toRadians(dir + 90f);
		this.dir = new Vec2f(-(float)Math.cos(radDir), (float)Math.sin(radDir));

		this.transform.scale = new Vec2f(0.15f, 0f);

		fadeBehavior = ((FadeBehavior)addBehavior(new FadeBehavior(this)));

		scene.collisionManager().addCollider(collider = new PointCollider(false, this)
		{
			@Override
			public void onCollision(Collider other, CollisionInfo info)
			{
				super.onCollision(other, info);

				//fadeBehavior.scheduleFade(0.01f, 0f, true);

				// Bounce
				Bullet.this.origin = info.point;
				Bullet.this.dir = info.normal;
				Bullet.this.transform.rotation = (float)Math.toDegrees(Math.atan2(info.normal.y, -info.normal.x)) - 90f;
				Bullet.this.transform.scale.y = 0f;
				Bullet.this.distSquared /= 10;
			}
		});
	}


	@Override
	public Class<? extends ClientGameEntity> getClientCounterpart()
	{
		return Surviv.Entities.Client.Bullet.class;
	}


	@Override
	protected void spawnOverNetwork(Object... params)
	{
		sendUnreliable(new SpawnBulletPacket((Vec2f)params[0], (float)params[1], (float)params[2]));
	}

	@Override
	public int getLayer()
	{
		return -1;
	}


	@Override
	public void update()
	{
		super.update();

		this.transform.position = this.transform.position.add(dir.scale(Time.deltaTime()));

		float distTraveled = Vec2f.distanceSquared(transform.position, origin);

		this.transform.scale.y = Math.min(0.5f, distTraveled / 50000);

		if (distTraveled >= distSquared)
		{
			fadeBehavior.scheduleFade(0.05f, 0f, true);
		}

	}


	private void onCollision(Collider other, CollisionInfo info)
	{
		System.out.println("Bullet has collision!");
	}


	@Override
	public void onDisable()
	{
		super.onDisable();

		scene.collisionManager().removeCollider(collider);
	}


	@Override
	public void onReceivePacket(Connection sender, Packet packet)
	{

	}
}
