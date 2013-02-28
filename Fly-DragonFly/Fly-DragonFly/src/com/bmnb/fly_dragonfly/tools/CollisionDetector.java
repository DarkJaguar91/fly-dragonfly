//Collision detection system, with bounding and grid collision tests
//Nathan Floor
//FLRNAT001

package com.bmnb.fly_dragonfly.tools;

import java.util.ArrayList;

import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;

public class CollisionDetector {
	private GridStructure grid = null;

	public CollisionDetector(float screenWidth, float screenHeight) {
		grid = new GridStructure(screenHeight, screenWidth,50);
	}

	// check collisions grid, then bounding box
	public void checkForCollision(ArrayList<GameObject> objects) {

//		Gdx.app.log("in collison detect", "here");
		
		for(int k=0;k<objects.size();k++){
			GameObject obj1 = objects.get(k);
			ArrayList<GameObject> collisions = checkGridCollisions(objects,obj1);
			GameObject tempObj = null;
			
//			Gdx.app.log("check object", (obj1 instanceof Enemy) + "");
			
			for(int i=0;i < collisions.size();i++){		
				tempObj = collisions.get(i);
				
//				Gdx.app.log("check object", (tempObj instanceof Particle) + "");
				if((tempObj instanceof Particle)&&(obj1 instanceof Player)){
					//do nothing
				}
				else if((obj1 instanceof Player) && (tempObj instanceof Enemy)){
					// bounding box check
					if (obj1.getBoundingRectangle().overlaps(tempObj.getBoundingRectangle())) {
						obj1.kill();
					}					
				}
				else if((tempObj instanceof Particle) && (obj1 instanceof Enemy)){	
//					Gdx.app.log("lol", "enemy is particle");
					if(! tempObj.isDead()){
						if (obj1.getBoundingRectangle().overlaps(tempObj.getBoundingRectangle())) {
							tempObj.kill();
							((Enemy)obj1).doDamage(Player.getDamage());
						}
					}						
				}
				else if((obj1 instanceof Enemy) && (tempObj instanceof Enemy)) {
					//do nothing
				}
			}
		}
	}
	

	// check nearby if object1 is close enough to object2 to collide
	public ArrayList<GameObject> checkGridCollisions(
			ArrayList<GameObject> objects, GameObject tempObject1) {
		GameObject tempObject2 = null;
		ArrayList<GameObject> neightbourTiles = new ArrayList<GameObject>();
		ArrayList<GameObject> temp = new ArrayList<GameObject>();

		neightbourTiles = grid.checkNeighbouringBlocks(tempObject1);

		for (int j = 0; j < neightbourTiles.size(); j++) {
			tempObject2 = neightbourTiles.get(j);

			// check if its the same tile
			if (tempObject1 != tempObject2) {
				temp.add(tempObject2);
			}
		}
		return temp;
	}

	public void registerOnGrid(GameObject object) {
		grid.registerObject(object);
	}

	public void deregisterFromGrid(GameObject object) {
		grid.deregisterObject(object);
	}

}
