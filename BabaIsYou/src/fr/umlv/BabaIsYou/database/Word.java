package fr.umlv.BabaIsYou.database;

import java.awt.Graphics2D;
import fr.umlv.BabaIsYou.enums.Name;
import java.util.Objects;
/**
 * Class representing every word the game can have
 * @author Diagne Ben, Kamdom Omairt
 *
 */
public class Word {
	private Name name; 

	/**
	 * Initializes the word
	 * @param name Enumerated type that represents the word.
	 */
	public Word(Name name) {
		this.name = name;
	}
	
	/**
	 * Name of the word.
	 * @return the Name.
	 */
	public Name name() {
		return name;
	}
	
	@Override
	public boolean equals(Object e) {
		if(!(e instanceof Word)) {
			return false;
		}
		Word word_test = (Word) e;
		return name.equals(word_test.name());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	/**
	 * Method that graphically draws the word.
	 * @param x X-axis the word is getting drawn to.
	 * @param y Y-axis the word is getting drawn to.
	 * @param graphics The drawing zone which the word is drawn at.
	 * @param width Width of the screen.
	 * @param height Height of the screen.
	 * @param arena_width Width of the level.
	 * @param arena_height Height of the level.
	 */
	public void draw(int x, int y, Graphics2D graphics, int width, int height, int arena_width, int arena_height) {
		;
	}
	
}