//map panel where user can draw game objects onto map
//Nathan Floor
//FLRNAT001

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.badlogic.gdx.utils.XmlWriter;


public class MapOutline extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Dimension mapSize;
	private String shape;
	private Point2D.Float shapePosition;
	private MainFrame parent;
	private ArrayList<ShapeDimensions> gameObjects;
	
	public MapOutline(MainFrame p,int width,int height){
		super();
		
		parent = p;
//		mapSize = new Dimension(width,height);
		shape = null;
		shapePosition = null;
		gameObjects = new ArrayList<ShapeDimensions>();
		this.setBackground(Color.white);
		this.updateUI();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int x = 0;
		int y = 0;
				
		if(shape != null){
			x = (int)shapePosition.x-parent.getSelectedObjWidth()/2;
			y = (int)shapePosition.y-parent.getSelectedObjHeight()/2;			
		}		

		if(shape == null){
			//do nothing
		}
		else if(shape.equals("circle")){
			g.setColor(Color.blue);
			g.fillOval(x, y, parent.getSelectedObjWidth(), parent.getSelectedObjHeight());
		}
		else if(shape.equals("square")){
			g.setColor(Color.red);
			g.fillRect(x, y, parent.getSelectedObjWidth(), parent.getSelectedObjHeight());
		}
		else if(shape.equals("diamond")){
			g.setColor(Color.green);
			int[] xpoints = {x,x+parent.getSelectedObjWidth()/2,x+parent.getSelectedObjWidth()};
			int[] ypoints = {y+parent.getSelectedObjHeight()/2,y,y+parent.getSelectedObjHeight()/2};
			g.fillPolygon(xpoints, ypoints, 3);	
			int[] xpoints1 = {x,x+parent.getSelectedObjWidth()/2,x+parent.getSelectedObjWidth()};
			int[] ypoints1 = {y+parent.getSelectedObjHeight()/2,y+parent.getSelectedObjHeight(),y+parent.getSelectedObjHeight()/2};
			g.fillPolygon(xpoints1, ypoints1, 3);	
		}
		else if(shape.equals("triangle")){
			g.setColor(Color.yellow);
			int[] xpoints = {x,x+parent.getSelectedObjWidth()/2,x+parent.getSelectedObjWidth()};
			int[] ypoints = {y+parent.getSelectedObjHeight(),y,y+parent.getSelectedObjHeight()};
			g.fillPolygon(xpoints, ypoints, 3);	
		}
		else if(shape.equals("bench")){
			g.setColor(Color.black);
			x = x - parent.getDispersion()/2;
			y = y + 20;
			g.fillRect(x, y, parent.getSelectedObjWidth()+parent.getDispersion(), 10);
			g.fillRect(x, y-10, 5, 30);
			g.fillRect(x+parent.getSelectedObjWidth()+parent.getDispersion(), y-10, 5, 30);
		}
		else if(shape.equals("star")){
			g.setColor(Color.black);
			x = x + 10;
			y = y + 20;
			
			g.fillRect(x, y, 25, 5);
			g.fillRect(x+10, y-10, 5, 25);			
		}
		
		//draw placed shapes
		for(int i=0;i<gameObjects.size();i++){
			if(gameObjects.get(i).object.equals("frog")){
				g.setColor(Color.blue);
				g.fillOval(gameObjects.get(i).X, gameObjects.get(i).Y, gameObjects.get(i).shapeSize.width, gameObjects.get(i).shapeSize.height);
			}
			else if(gameObjects.get(i).object.equals("spider")){
				g.setColor(Color.red);
				g.fillRect(gameObjects.get(i).X, gameObjects.get(i).Y, gameObjects.get(i).shapeSize.width, gameObjects.get(i).shapeSize.height);
			}
			else if(gameObjects.get(i).object.equals("flytrap")){
				g.setColor(Color.green);
				int[] xpoints = {gameObjects.get(i).X,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width/2,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width};
				int[] ypoints = {gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height/2,gameObjects.get(i).Y,gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height/2};
				g.fillPolygon(xpoints, ypoints, 3);	
				int[] xpoints1 = {gameObjects.get(i).X,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width/2,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width};
				int[] ypoints1 = {gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height/2,gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height,gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height/2};
				g.fillPolygon(xpoints1, ypoints1, 3);
			}
			else if(gameObjects.get(i).object.equals("bird")){
				g.setColor(Color.yellow);
				int[] xpoints = {gameObjects.get(i).X,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width/2,gameObjects.get(i).X+gameObjects.get(i).shapeSize.width};
				int[] ypoints = {gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height,gameObjects.get(i).Y,gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height};
				g.fillPolygon(xpoints, ypoints, 3);	
			} 
			else if(gameObjects.get(i).object.equals("mosquitoes")){
				g.setColor(Color.black);
				g.fillRect(gameObjects.get(i).X, gameObjects.get(i).Y, gameObjects.get(i).shapeSize.width, 10);
				g.fillRect(gameObjects.get(i).X, gameObjects.get(i).Y-10, 5, 30);
				g.fillRect(gameObjects.get(i).X+ gameObjects.get(i).shapeSize.width, gameObjects.get(i).Y-10, 5, 30);
			}
			else if(gameObjects.get(i).object.equals("tutorial")){
				g.setColor(Color.black);
				
//				g.drawLine( gameObjects.get(i).X, gameObjects.get(i).Y, gameObjects.get(i).X+20, gameObjects.get(i).Y);
//				g.drawLine( gameObjects.get(i).X+10, gameObjects.get(i).Y-10, gameObjects.get(i).X+10, gameObjects.get(i).Y+10);
			
				g.fillRect(gameObjects.get(i).X, gameObjects.get(i).Y, 25, 5);
				g.fillRect(gameObjects.get(i).X+10, gameObjects.get(i).Y-10, 5, 25);
			}
		}
	}
	
	public void stopDrawing(){
		shape = null;
		repaint();
	}
	
	public void drawShapes(String s, int x, int y){
		shape = s;
		shapePosition = new Point2D.Float(x, y);
		repaint();
	}
	
	public XmlWriter compileXMLFile(XmlWriter xmlBuilder){
		try{
			for(int i=0;i<gameObjects.size();i++){
				if(gameObjects.get(i).object.equals("frog") 
						|| gameObjects.get(i).object.equals("spider") 
						|| gameObjects.get(i).object.equals("flytrap") 
						|| gameObjects.get(i).object.equals("bird")){
					xmlBuilder.element("obj")
						.attribute("type",gameObjects.get(i).object)
						.element("x")
							.text(gameObjects.get(i).X)
						.pop()
						.element("y")
							.text(gameObjects.get(i).Y)
						.pop()
						.element("width")
							.text(gameObjects.get(i).shapeSize.width)
						.pop()
						.element("height")
							.text(gameObjects.get(i).shapeSize.height)
						.pop()
						.element("rotation")
							.text(gameObjects.get(i).rotation)
						.pop()						
					.pop();
				}
				else if(shape.equals("bench")){
					xmlBuilder.element("moziSpawner")
						.attribute("type",gameObjects.get(i).object)
						.element("x")
							.text(gameObjects.get(i).X)
						.pop()
						.element("y")
							.text(gameObjects.get(i).Y)
						.pop()
						.element("numberOfBoids")
							.text(gameObjects.get(i).numberOfBoids)
						.pop()	
						.element("dispersion")
							.text(gameObjects.get(i).dispersion)
						.pop()										
					.pop();
				}
				else if(shape.equals("star")){
					xmlBuilder.element("tutorialScreen")
						.element("id")
							.text(gameObjects.get(i).id)
						.pop()
						.element("y")
							.text(gameObjects.get(i).Y)
						.pop()			
					.pop();
				}
				
			}	
		}catch(Exception e){e.printStackTrace();}
		return xmlBuilder;
	}
	
	//will check through all saved objects and grab the one which the mouse has clicked
	public ShapeDimensions grabObject(int x,int y){
		for(int i=0;i<gameObjects.size();i++){			
			if((x >= gameObjects.get(i).X) && (x <= gameObjects.get(i).X+gameObjects.get(i).shapeSize.width)){
				if((y >= gameObjects.get(i).Y) && (y <= gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height)){
					ShapeDimensions temp = gameObjects.get(i);
					System.out.println(x+":"+y+", "+gameObjects.get(i).X+":"+gameObjects.get(i).Y);
					gameObjects.remove(i);
					return temp;
				}
			}			
		}			
		return null;
	}
	
	//will check through all saved objects and grab the one which the mouse has clicked
	public void deleteObject(int x,int y){
		for(int i=0;i<gameObjects.size();i++){			
			if((x >= gameObjects.get(i).X) && (x <= gameObjects.get(i).X+gameObjects.get(i).shapeSize.width)){
				if((y >= gameObjects.get(i).Y) && (y <= gameObjects.get(i).Y+gameObjects.get(i).shapeSize.height)){
					System.out.println(x+":"+y+", "+gameObjects.get(i).X+":"+gameObjects.get(i).Y);
					gameObjects.remove(i);
					repaint();
				}
			}			
		}			
	}
	
	public void addObjectToMap(String s,float r,int disp,int id,int numB,Dimension size,int x, int y){
		gameObjects.add(new ShapeDimensions(s,r,disp,id,numB,size, x, y));
	}
	
	public void addObjectToMap(ShapeDimensions obj){
		gameObjects.add(obj);
	}
}
