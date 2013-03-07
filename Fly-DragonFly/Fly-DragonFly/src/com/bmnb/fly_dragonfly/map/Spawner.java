package com.bmnb.fly_dragonfly.map;

import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.objects.Bird;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.objects.Spider;
import com.bmnb.fly_dragonfly.objects.VenusFlytrap;
import com.bmnb.fly_dragonfly.screens.GameScreen;

public class Spawner {
	MapLoader _map;
	GameScreen _gs;
	float _y;
	float _scrollSpeed;
	Player _player;
	BoidsModel _bm;
	public Spawner(MapLoader map, BoidsModel boidsModel, GameScreen gs, float speed, Player player){
		_map = map;
		_gs = gs;
		_scrollSpeed = speed;
		_bm = boidsModel;
		_player = player;
	}
	public void update(float delta){
		_y += _scrollSpeed * delta;
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
				_map.getGameObjects().remove(i--);
			}	
		}
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
