package fr.umlv.BabaIsYou.main;

import java.awt.Color;
import java.awt.Font;
import java.nio.file.*;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
import java.awt.Toolkit;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.*;
import fr.umlv.BabaIsYou.database.*;
import fr.umlv.BabaIsYou.enums.*;
import fr.umlv.BabaIsYou.arena.PlayGround;
/**
 * Class that executes every class of the source folder to display them graphically and make the game be playable.
 * @author Diagne Ben, Kamdom Omairt
 *
 */
public class Main {
	/**
	 * Main that executes the database.
	 * @param args the options to choose the level(s) the player wants to play.
	 */
	public static void main(String[] args) {
		if( ( (args[0].equals("--level") && args.length <= 2) || (args[0].equals("--levels") && args.length > 2) ) && !args[1].equals(null)) {
		    Application.run(Color.WHITE, context -> {
		      ScreenInfo screenInfo = context.getScreenInfo();
		      float width = screenInfo.getWidth();
		      float height = screenInfo.getHeight();
		      for(int i = 1; i < args.length; i++) {
				  PlayGround arena = new PlayGround(Path.of("../BabaIsYou/src/levels/level" + args[i]));

			      for(;;) {
			        Event event = context.pollOrWaitEvent(10);
					context.renderFrame(graphics -> {
						graphics.setColor(Color.BLACK);
						graphics.fill(new Rectangle2D.Float(0, 0, width, height));
						for(Decor decor: arena.decors().keySet()) {
							for(Coordinates coordinates: arena.decors().get(decor))
								decor.draw(coordinates.x(), coordinates.y(), graphics, Math.round(width), Math.round(height), arena.width(), arena.height());
						}
						for(Word word: arena.playGround().keySet()) {
							for(Coordinates coordinates: arena.playGround().get(word))
								word.draw(coordinates.x(), coordinates.y(), graphics, Math.round(width), Math.round(height), arena.width(), arena.height());
						}
						// Si on a gagné ou perdu, on affiche les écrans de victoire/défaite.
						if(arena.youWon() || arena.youLose()) {
				    		Toolkit toolkit = Toolkit.getDefaultToolkit();
							Image image = toolkit.getImage("../BabaIsYou/src/Images/PROPERTIES/Text_YOU_0.gif");
				    		graphics.drawImage(image, Math.round(width / 2) - 225, Math.round(height / 2) - 50, (Math.round(width) / arena.width()) + 60, (Math.round(height) / arena.height()) + 30, null);
				    		image = toolkit.getImage("../BabaIsYou/src/Images/OPERATORS/Text_IS_0.gif");
				    		graphics.drawImage(image, Math.round(width / 2) - 75, Math.round(height / 2) - 50, (Math.round(width) / arena.width()) + 60, (Math.round(height) / arena.height()) + 30, null);
				    		graphics.setFont(new Font("Calibri", Font.BOLD, 35));
				    		graphics.setColor(Color.white);
					        if(arena.youWon()) {
					    		image = toolkit.getImage("../BabaIsYou/src/Images/PROPERTIES/Text_WIN_0.gif");
					    		graphics.drawImage(image, Math.round(width / 2) + 75, Math.round(height / 2) - 50, (Math.round(width) / arena.width()) + 60, (Math.round(height) / arena.height()) + 30, null);
					    		graphics.drawString("Press N to continue or Q to quit the game.", Math.round(width / 2) - 400, Math.round(height / 2) + 110);
					        }
					        else if(arena.youLose()) {
					    		image = toolkit.getImage("../BabaIsYou/src/Images/PROPERTIES/Text_DEFEAT_0.gif");
					    		graphics.drawImage(image, Math.round(width / 2) + 75, Math.round(height / 2) - 50, (Math.round(width) / arena.width()) + 60, (Math.round(height) / arena.height()) + 30, null);
					    		graphics.drawString("Press Q to quit the game.", Math.round(width / 2) - 270, Math.round(height / 2) + 110);
					        }
						}
					});
			        if (event == null)
			        	continue;
			        
			        Action action = event.getAction();
			        KeyboardKey key = event.getKey();
			        if (action == Action.KEY_RELEASED) {
						if(key == KeyboardKey.Q) {
							context.exit(0);
							return;
						}
						if(key == KeyboardKey.N && arena.youWon()) {
							break;
						}
						if(key == KeyboardKey.UP && !arena.youWon())
							arena.move_player(Directions.Up);
						
						else if(key == KeyboardKey.DOWN && !arena.youWon())
							arena.move_player(Directions.Down);
						
						else if(key == KeyboardKey.LEFT && !arena.youWon())
							arena.move_player(Directions.Left);
						
						else if(key == KeyboardKey.RIGHT && !arena.youWon())
							arena.move_player(Directions.Right);
						
					}
			       }
		      }
		      
			context.exit(0);
		    });
		}
		else
			throw new RuntimeException("Erreur: Arguments incorrects.");
  }
}
