package com.bmnb.fly_dragonfly.tools;

import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.GameObject;

import java.util.ArrayList;
import java.util.Vector;

//GridStructure for collision detection system
//Nathan Floor
//FLRNAT001

public class GridStructure {
	// instance variables
	private ArrayList<GameObject>[][] grid = null;
	private int GRID_BLOCK_SIZE = 64;
	private int grid_offset = 10;
	private int gridLength;

	public GridStructure(int cubeLength, int max_size) {
		GRID_BLOCK_SIZE = max_size;
		grid_offset = (cubeLength / GRID_BLOCK_SIZE) / 2;
		gridLength = (cubeLength / GRID_BLOCK_SIZE) + 1;
		grid = new ArrayList[gridLength][gridLength];

		// initialise grid structure
		for (int x = 0; x < grid[0].length; x++)
			for (int y = 0; y < grid.length; y++)
				grid[x][y] = new ArrayList<GameObject>();
	}

	// insert object into grid and update pointers to grid-blocks
	public void registerObject(GameObject obj) {
		boolean hasMoved = false;
		// get width/diameter of object in terms of grid blocks
		int objectWidth = (int) Math.ceil(obj.getBoundingRectangle().width
				/ GRID_BLOCK_SIZE);
		int objectHeight = (int) Math.ceil(obj.getBoundingRectangle().height
				/ GRID_BLOCK_SIZE);

		Vector2 oldPos = new Vector2();
		//oldPos = obj.getPreviousPos();

		// convert position coords to grid coords
		int objX_prev = (int) Math.round((oldPos.x) / GRID_BLOCK_SIZE)
				+ grid_offset;
		int objY_prev = (int) Math.round((oldPos.y) / GRID_BLOCK_SIZE)
				+ grid_offset;

		// get object's present coords
		int texX = (int) obj.getX();
		int texY = (int) obj.getY();

		// convert position coords to grid coords
		int objX = (int) Math.round((double) (texX / GRID_BLOCK_SIZE))
				+ grid_offset;
		int objY = (int) Math.round((double) (texY / GRID_BLOCK_SIZE))
				+ grid_offset;

		// check if object has moved out of grid block
		/*if (obj.getCapacity() == 0)
			hasMoved = true;
		else if (((objX + objectWidth / 2) < (objX_prev + objectWidth / 2))
				|| ((objX - objectWidth / 2) > (objX_prev - objectWidth / 2))) {
			hasMoved = false;
		} else if (((objY + objectWidth / 2) < (objY_prev + objectWidth / 2))
				|| ((objY + objectWidth / 2) > (objY_prev + objectWidth / 2))) {
			hasMoved = false;
		} else
			hasMoved = true;
*/
		hasMoved = true;
		if (hasMoved) {
			// remove object from grid first, if its already registered
			deregisterObject(obj);

			// register object in grid blocks
			for (int x = 0; x < objectWidth; x++)
				for (int y = 0; y < objectWidth; y++) {
					// convert objects coords to grid coords
					objX = (int) Math.round((double) ((texX - x
							* GRID_BLOCK_SIZE) / GRID_BLOCK_SIZE))
							+ grid_offset;
					objY = (int) Math.round((double) ((texY - y
							* GRID_BLOCK_SIZE) / GRID_BLOCK_SIZE))
							+ grid_offset;

					// check that the object is still within the confines of the
					// grid
					if ((objX >= 0) && (objX < grid.length) && (objY >= 0)
							&& (objY < grid[1].length)) {
						grid[objX][objY].add(obj);
						//obj.setNewLocation(new Vector2(objX, objY));
					}
				}
		}
	}

	// clear pointers to grid and remove object from grid
	public void deregisterObject(GameObject obj) {
		//for (int i = 0; i < obj.getCapacity(); i++) {
			//Vector2 gridBlock = obj.getLocation(i);
			//grid[(int) Math.round(gridBlock.x)][(int) Math.round(gridBlock.x)].remove(obj);
		//}
		//obj.removeAllLocations();
	}

	// find list of objects near/adjacent to current object
	public ArrayList<GameObject> checkNeighbouringBlocks(GameObject obj) {
		ArrayList<GameObject> neighbours = new ArrayList<GameObject>();
		/*Vector2 gridBlock;
		int gridX, gridY;

		// loop though adjacent blocks containing objects to test for potential
		// collisions
		for (int i = 0; i < obj.getCapacity(); i++) {
			//gridBlock = obj.getLocation(i);
			gridX = (int) (gridBlock.x);
			gridY = (int) (gridBlock.y);

			// check all 8 blocks surrounding object (as well as block object is
			// in) for nearby objects
			for (int x = -1; x < 2; x++)
				for (int y = -1; y < 2; y++)
					checkForDuplicates(neighbours, obj, gridX + x, gridY + y);
		}*/
		return neighbours;
	}

	// find list of objects near/adjacent to current point in space
	public ArrayList<GameObject> checkNeighbouringBlocks(Vector2 pointInSpace) {
		ArrayList<GameObject> neighbours = new ArrayList<GameObject>();
		int gridX, gridY;

		// convert objects coords to grid coords
		gridX = (int) Math
				.round((double) (Math.round(pointInSpace.x) / GRID_BLOCK_SIZE))
				+ grid_offset;
		gridY = (int) Math
				.round((double) (Math.round(pointInSpace.y) / GRID_BLOCK_SIZE))
				+ grid_offset;

		// check all 8 blocks surrounding object (as well as block object is in)
		// for nearby objects
		for (int x = -1; x < 2; x++)
			for (int y = -1; y < 2; y++)
				checkForDuplicates(neighbours, gridX + x, gridY + y);

		return neighbours;
	}

	// find list of objects within supplied radius to current point in space NEW
	public ArrayList<GameObject> checkWithInRadius(Vector2 pointInSpace,
			int radius) {
		ArrayList<GameObject> neighbours = new ArrayList<GameObject>();
		int gridX, gridY;

		// convert objects coords to grid coords
		gridX = (int) Math
				.round((double) (Math.round(pointInSpace.x) / GRID_BLOCK_SIZE))
				+ grid_offset;
		gridY = (int) Math
				.round((double) (Math.round(pointInSpace.y) / GRID_BLOCK_SIZE))
				+ grid_offset;

		// check all 8 blocks surrounding object (as well as block object is in)
		// for nearby objects
		int dist = (int) Math.ceil((double) radius / GRID_BLOCK_SIZE);
		for (int x = (gridX - dist); x < (gridX + dist); x++)
			for (int y = (gridY - dist); y < (gridY + dist); y++)
				checkForDuplicates(neighbours, gridX + x, gridY + y);

		return neighbours;
	}

	// check if supplied object is within the grid NEW
	public boolean isInGrid(GameObject obj) {
		int blockX = (int) Math.round(obj.getX());
		int blockY = (int) Math.round(obj.getY());

		// convert objects coords to grid coords
		int gridX = (int) Math.round((double) (blockX / GRID_BLOCK_SIZE))
				+ grid_offset;
		int gridY = (int) Math.round((double) (blockY / GRID_BLOCK_SIZE))
				+ grid_offset;

		if ((gridX < 0) && (gridX >= grid.length) && (gridY < 0)
				&& (gridY >= grid[1].length))
			return false;
		else
			return true;
	}

	// check list of neighbours to current object for duplicate entries
	private void checkForDuplicates(ArrayList<GameObject> nearByObjs,
			GameObject obj, int xcoord, int ycoord) {
		if ((xcoord >= 0) && (xcoord < grid.length) && (ycoord >= 0)
				&& (ycoord < grid[1].length))
			for (int j = 0; j < grid[xcoord][ycoord].size(); j++)
				if (!nearByObjs.contains(grid[xcoord][ycoord].get(j))
						&& (obj != grid[xcoord][ycoord].get(j)))
					nearByObjs.add(grid[xcoord][ycoord].get(j));
	}

	// check list of neighbours to point in space for duplicate entries
	private void checkForDuplicates(ArrayList<GameObject> nearByObjs,
			int xcoord, int ycoord) {
		if ((xcoord >= 0) && (xcoord < grid.length) && (ycoord >= 0)
				&& (ycoord < grid[1].length))
			for (int j = 0; j < grid[xcoord][ycoord].size(); j++)
				if (!nearByObjs.contains(grid[xcoord][ycoord].get(j)))
					nearByObjs.add(grid[xcoord][ycoord].get(j));
	}
}
