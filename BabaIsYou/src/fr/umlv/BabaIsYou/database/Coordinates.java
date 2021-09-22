package fr.umlv.BabaIsYou.database;

import java.util.Objects;
import fr.umlv.BabaIsYou.enums.Directions;

/**
 * Class that allows every entity to locate themselves in the game.
 * @author Diagne Ben, Kamdom Omairt
 *
 */
public class Coordinates {
	private int x;
	private int y;
	
	/**
	 * Initializes the coordinates object.
	 * @param x X-axis of the object.
	 * @param y Y-axis of the object.
	 */
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y; 
	}
	/**
	 * Changes the coordinates' values depending on the direction value. Returns 1 if values have been modified, 0 otherwise.
	 * @param direction The direction the coordinates are moved to.
	 * @param width Width of the level. Used to set a boundary.
	 * @param height Height of the level. Used to set a boundary.
	 * @return 1 if the process was successful, 0 otherwise.
	 */
	public int move(Directions direction, int width, int height) {
		switch(direction) { 
			case Up:
				if(y > 0)
					this.y -= 1;
				return 1; 
			case Down:
				if(y < height)
					this.y += 1;
				return 1;
			case Left:
				if(x > 0)
					this.x -= 1;
				return 1;
			case Right:
				if(x < width)
					this.x += 1;
				return 1;
		}
		
		return 0;
		
	}
	
	/**
	 * X-axis of the object.
	 * @return the X-axis.
	 */
	public int x() {
		return x;
	}

	/**
	 * Y-axis of the object.
	 * @return the Y-axis.
	 */
	public int y() {
		return y;
	}
	
	@Override
	public boolean equals(Object e) {
		if(!(e instanceof Coordinates))
			return false;
		Coordinates coordinates_test = (Coordinates) e;
		return (x == coordinates_test.x() && y == coordinates_test.y());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	/**
	 * Returns the nearest coordinates placed in the parameterized direction.
	 * @param direction Direction which the nearby coordinates are located to.
	 * @return the nearest coordinates located in the parameterized direction.
	 */
	public Coordinates find_coordinates_near(Directions direction) {
		switch(direction) {
			case Up:
				return new Coordinates(this.x, this.y - 1);
			case Down:
				return new Coordinates(this.x, this.y + 1);
			case Left:
				return new Coordinates(this.x - 1, this.y);
			case Right:
				return new Coordinates(this.x + 1, this.y);
			default:
				return null;
		}
	}
}