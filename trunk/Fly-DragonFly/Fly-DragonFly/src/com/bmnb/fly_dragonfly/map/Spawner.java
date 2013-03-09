package com.bmnb.fly_dragonfly.map;

import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.objects.Bird;
import com.bmnb.fly_dragonfly.objects.Frog;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.objects.Spider;
import com.bmnb.fly_dragonfly.objects.VenusFlytrap;
import com.bmnb.fly_dragonfly.screens.GameScreen;
/**
 * Spawner class. Takes a map object and spawns the contents at the correct time
 * @author benjamin
 *
 */
public class Spawner {
	MapLoader _map;
	GameScreen _gs;
	float _y;
	float _scrollSpeed;
	Player _player;
	BoidsModel _bm;
	/**
	 * Default constructor
	 * @param map a map object
	 * @param boidsModel handle to the boids model
	 * @param gs handle to the game screen
	 * @param speed scroll speed
	 * @param player handle to the player
	 */
	public Spawner(MapLoader map, BoidsModel boidsModel, GameScreen gs, float speed, Player player){
		_map = map;
		_gs = gs;
		_scrollSpeed = speed;
		_bm = boidsModel;
		_player = player;
	}
	/**
	 * Update method
	 * @param delta
	 */
	public void update(float delta){
		//scroll
		_y += _scrollSpeed * delta;
		//spawn objects:
		for (int i = 0; i < _map.getGameObjects().size(); ++i){
			ObjectSpawner os = _map.getGameObjects().get(i);
			if (os.getY() < _y + GameScreen.height){
				if (os.getType() == ObjectSpawner.Type.OS_SPIDER)
					GameScreen.addObject(new Spider(new Vector2(os.getX(),os.getY()), os.getWidth(), os.getHeight(), 
							_scrollSpeed, GameScreen.width, GameScreen.height,  _player));
				else if (os.getType() == ObjectSpawner.Type.OS_BIRD)
					GameScreen.addObject(new Bird(new Vector2(os.getX(),os.getY()), os.getWidth(), os.getHeight(), 
							_scrollSpeed, GameScreen.width, GameScreen.height,  _player));
				else if (os.getType() == ObjectSpawner.Type.OS_VENUSFT) 
					GameScreen.addObject(new VenusFlytrap(new Vector2(os.getX(),os.getY()), os.getWidth(), os.getHeight(), 
						_scrollSpeed, GameScreen.width, GameScreen.height,  _player));
				else if (os.getType() == ObjectSpawner.Type.OS_FROG) 
					GameScreen.addObject(new Frog(new Vector2(os.getX(),os.getY()), os.getWidth(), os.getHeight(), 
						_scrollSpeed, GameScreen.width, GameScreen.height,  _player));
				_map.getGameObjects().remove(i--);
			}	
		}
		//spawn boids spawnpoint
		for (int i = 0; i < _map.getSpawners().size(); ++i){
			MoziSpawner ms = _map.getSpawners().get(i);
			if (ms.getPos().y < _y + GameScreen.height){
				_bm.spawnBoids(16, 16, GameScreen.width, GameScreen.height, ms.numberOfBoids, ms.getPos().x, 
						ms.getDeviation(), 
						ms.getType() == MoziSpawner.SpawnerType.mosquitoes ? 
								BoidsModel.BoidsType.Mosquitoes : BoidsModel.BoidsType.FireFlies);
			_map.getSpawners().remove(i--);
			}
		}
	}
}
