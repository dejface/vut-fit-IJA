/*
 * @author xorave05
 */
package vut.fit.ija.homework1.myMaps;

import vut.fit.ija.homework1.maps.Coordinate;

public class MyCoordinate implements Coordinate{
	private int x = 0;
	private int y = 0;
	
	public static Coordinate create(int x, int y) {
		MyCoordinate myCoord = new MyCoordinate();
		if (x >= 0 && y >= 0) {
			myCoord.x = x;
			myCoord.y = y;
		} else {
			return null;
		}
		return myCoord;
	}
	
	@Override
	public int getX() {
		return this.x;
	}
	
	@Override 
	public int getY(){
		return this.y;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this) {
			return true;
		}
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}

		Coordinate c = (Coordinate) o;

		return getX() == c.getX() && getY() == c.getY();
	}
}