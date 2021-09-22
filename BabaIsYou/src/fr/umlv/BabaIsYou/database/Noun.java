package fr.umlv.BabaIsYou.database;

import fr.umlv.BabaIsYou.enums.Name;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Sub-class of Word that represents nouns.
 * @author Diagne Ben, Kamdom Omairt
 *
 */
public class Noun extends Word {
	/**
	 * Method that initializes the noun.
	 * @param name Enumerated type that represents the noun.
	 */
	public Noun(Name name) {
		super(name);
	}
	
	@Override
	public void draw(int x, int y, Graphics2D graphics, int width, int height, int arena_width, int arena_height) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("../BabaIsYou/src/Images/" + name() + "/Text_"  + name() + "_0.gif");
		graphics.drawImage(image, x * (width / arena_width), y * (height / arena_height), (width / arena_width) + 4, (height / arena_height) - 9, null);		
	}	
}