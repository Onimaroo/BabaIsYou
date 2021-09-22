package fr.umlv.BabaIsYou.arena;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import fr.umlv.BabaIsYou.database.*;
import fr.umlv.BabaIsYou.enums.*;

/**
 * Class that manages the whole playground.
 * @author Diagne Ben, Kamdom Omairt
 */
public class PlayGround {
	private LinkedHashMap<Word, ArrayList<Coordinates>> playGround;
	private LinkedHashMap<Decor, ArrayList<Coordinates>> decors;
	private int width;
	private int height;
	private boolean youWon;
	private boolean youLose;
	
	/**
	 * Method that initializes the playground by reading the text file (that represents our level structure) 
	 * located in the path taken as the method's parameter.
	 * @param path Location of the file.
	 */
	public PlayGround(Path path) {
		this.youWon = false;
		this.youLose = false;
		/* Lecture du fichier pris en paramètre */
    	try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        	String currentLine = reader.readLine();
    		var tokens = currentLine.split(" ");
    		if(Integer.parseInt(tokens[0]) < 0 || Integer.parseInt(tokens[1]) < 0)
    			throw new IllegalStateException("Erreur: Coordonnées d'une entitée sortent des bornes du jeu.");
    		this.width = Integer.parseInt(tokens[0]);
    		this.height = Integer.parseInt(tokens[1]);
    		
    		/* Initialization of playGround */
    		this.playGround = new LinkedHashMap<Word, ArrayList<Coordinates>>();
    		this.decors = new LinkedHashMap<Decor, ArrayList<Coordinates>>();
    		
		    while((currentLine = reader.readLine()) != null)
		    	createLevel(currentLine);
    	} catch(IOException c) {
    		this.playGround = null;
			System.err.println(c.getMessage());
			throw new RuntimeException(c);
		}
    	
    	update();
		
	}
	/**
	 * Method that adds to the map the parameterized word located in the parameterized coordinates.
	 * @param word Word to add to the map.
	 * @param coordinates Coordinates which the word is located to.
	 */
	public void add_word(Word word, Coordinates coordinates) {
		Objects.requireNonNull(word);
		if(!playGround.containsKey(word)) {
			this.playGround.put(word, new ArrayList<Coordinates>());
			this.playGround.get(word).add(coordinates);
		}
		else {
			this.playGround.get(word).add(coordinates);
		}
	}
	
	/**
	 * Method that adds to the map the parameterized decor located in the parameterized coordinates.
	 * @param decor Decor to add to the map.
	 * @param coordinates Coordinates which the decor is located to.
	 */
	
	public void add_decor(Decor decor, Coordinates coordinates) {
		Objects.requireNonNull(decor);
		if(!decors.containsKey(decor)) {
			this.decors.put(decor, new ArrayList<Coordinates>());
			this.decors.get(decor).add(coordinates);
		}
		else {
			this.decors.get(decor).add(coordinates);
		}
	}
	/**
	 * Method that creates the level from the parameter string "line".
	 * @param line String that represents the line currently read by the reader.
	 * @return 1 if the process was successful, 0 otherwise.
	 */
	public int createLevel(String line) {
		var tokens = line.split(" ");
		for(var token: tokens) {
			if(token.equals(""))
				return 0;
		}
		int x = Integer.parseInt(tokens[0]);
		int y = Integer.parseInt(tokens[1]);
		String type = tokens[2];
	    String nom = tokens[3].toUpperCase();
	    if(x < width && y < height) {
			switch(type) {
				case "N":
					this.add_word(new Noun(Name.valueOf(nom)), new Coordinates(x, y));
			        return 1;
				case "O":
					this.add_word(new Operator(Name.valueOf(nom)), new Coordinates(x, y));
			        return 1;
				case "P":
					this.add_word(new Property(Name.valueOf(nom)), new Coordinates(x, y));
			        return 1;
			    case "D":
					this.add_decor(new Decor(Name.valueOf(nom)), new Coordinates(x, y));
			        return 1;
			    default:
			    	return 1;
			}
	    }
	    return 0;
	}
	  
	/**
	 * Map that stores every word in the playground as keys (with their coordinates as values). Note: We choose LinkedHashMap so the map will not randomly sort the word we add to the map.
	 * @return The concerned Map.
	 */
	public LinkedHashMap<Word, ArrayList<Coordinates>> playGround() {
		return playGround;
	}
	
	/**
	 * Map that stores every object in the playground as keys (with their coordinates as values)
	 * @return The concerned Map.
	 */
	public LinkedHashMap<Decor, ArrayList<Coordinates>> decors() {
		return decors;
	}
	
	/**
	 * Returns the current state of the game: Returns true if the player won, false otherwise.
	 * @return The concerned boolean.
	 */
	public boolean youWon() {
		return youWon;
	}
	
	/**
	 * Returns the current state of the game: Returns true if the player lost, false otherwise.
	 * @return The concerned boolean.
	 */
	public boolean youLose() {
		return youLose;
	}
	
	/**
	 * Returns the level's width.
	 * @return The level's width.
	 */
	public int width() {
		return width;
	}
	
	/**
	 * Returns the level's height.
	 * @return The level's height.
	 */
	
	public int height() {
		return height;
	}
	
	
	/**
	 * Recurring method that actualizes the playground by applying the rules.
	 */
	/* Méthode qui met les règles de jeu à jour. */
	public void update() {
		/* Boucle pour "ré-initialiser" le jeu */
		for(Decor decor: decors.keySet()) {
			decor.setNameProv(decor.representation());
			decor.setIsYou(false);
			decor.setPassable(true);
			decor.setPushable(false);
			decor.setVictory(false);
			decor.setSink(false);
			decor.setDefeat(false);
			decor.setMelt(false);
			decor.setHot(false);
		}
		/* Appel de méthode pour mettre les règles en jeu */
		sentence();
		/* Appels des méthodes qui appliquent les propriétés des règles aux décors du jeu */
		verif_sink();
		verif_defeat();
		verif_melt();
		/* Appels des méthodes qui vérifient l'état du jeu */
		if(win())
			this.youWon = true;
		if(lose())
			this.youLose = true;
	}
	
	/**
	 * Method that moves the decors controlled by the player. Also pushes the nearby decors if they can be pushed.
	 * @param direction Direction the player takes to move.
	 */
	// Méthode qui fait bouger les décors controlées par le joueur. Engendre aussi le poussement des décors poussables adjacents s'il y en a.
	public void move_player(Directions direction) {
		// Pour tous les décors
		for(Decor decor: decors.keySet()) {
			// On vérifie si c'est un décor controlé par le joueur
			if(decor.isYou()) {
				// Pour tous les coordonnées du décor
				for(Coordinates coordinates: decors.get(decor)) {
					// On pousse ou bouge selon la direction
					switch(direction) {
						case Up:
							if(coordinates.y() > 0)
								push(coordinates, direction);
							break;
						case Down:
							if(coordinates.y() < this.height - 1)
								push(coordinates, direction);
							break;
						case Left:
							if(coordinates.x() > 0)
								push(coordinates, direction);
							break;
						case Right:
							if(coordinates.x() < this.width - 1)
								push(coordinates, direction);
							break;
					}
				}
			}
		}
		// On met à jour les règles une fois les noms/décors déplacés.
		update();
	}
	
	/**
	 * Establishes the playground's boundaries.
	 * @param direction Direction the moving object has taken.
	 * @param coordinates Coordinates of the moving object.
	 * @return True if the player hasn't reached the boundary, False otherwise.
	 */
	boolean verif_limite(Directions direction, Coordinates coordinates) {
		switch(direction) {
			case Up:
				return coordinates.y() > 1;
			case Down:
				return coordinates.y() < this.height - 2;
			case Left:
				return coordinates.x() > 1;
			case Right:
				return coordinates.x() < this.width - 2;
			default:
				return false;
		}
	}
	
	/**
	 * Checks if there's a decor located in the parameterized coordinates that the player can pass.
	 * @param direction Direction the method is looking at.
	 * @param coordinates Coordinates of the object.
	 * @return True if there's a decor located in the parameterized coordinates that the player can pass, False otherwise.
	 */
	boolean verif_passable(Directions direction, Coordinates coordinates) {
		for(Decor decor: decors.keySet()) {
			if(decors.get(decor).contains(coordinates.find_coordinates_near(direction)) && !decor.passable()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Recursive method that allows the words to be pushed by the player or by another pushable object.
	 * @param direction Direction the moving word is getting pushed to.
	 * @param coordinates Coordinates of the object who's pushing the moving word.
	 * @return 1 if the push was successful, 0 otherwise.
	 */
	int push_word(Directions direction, Coordinates coordinates) {
		boolean test_limite = verif_limite(direction, coordinates);
		for(Word word: playGround.keySet()) {
			if(playGround.get(word).contains(coordinates.find_coordinates_near(direction))) {
				if(test_limite && verif_passable(direction, coordinates.find_coordinates_near(direction))) {
					Coordinates coord_word_poussé = playGround.get(word).get(playGround.get(word).indexOf((coordinates.find_coordinates_near(direction))));
					if(push_decor(direction, coord_word_poussé) == 0)
						return 0; // On retourne 0 pour tous les appels récursifs dans le cas où on arrive pas à pousser le dernier bloc
					if(push_word(direction, coord_word_poussé) == 0)
						return 0;
					coord_word_poussé.move(direction, width, height);
				}
				else
					return 0;
			}
		}
		return 1;
	}
	
	/**
	 * Recursive method that allows the pushable decors to be pushed by the player or by another pushable object.
	 * @param direction Direction the moving decor is getting pushed to.
	 * @param coordinates Coordinates of the object who's pushing the moving decor.
	 * @return 1 if the push was successful, 0 otherwise.
	 */
	int push_decor(Directions direction, Coordinates coordinates) {
		boolean test_limite = verif_limite(direction, coordinates);
		for(Decor decor: decors.keySet()) {
			if(decors.get(decor).contains(coordinates.find_coordinates_near(direction)) && decor.pushable()) {
				if(test_limite && verif_passable(direction, coordinates.find_coordinates_near(direction))) {
					Coordinates coord_decor_poussé = decors.get(decor).get(decors.get(decor).indexOf((coordinates.find_coordinates_near(direction))));
					if(push_decor(direction, coord_decor_poussé) == 0)
						return 0;
					if(push_word(direction, coord_decor_poussé) == 0)
						return 0;
					coord_decor_poussé.move(direction, width, height);
				}
				else
					return 0;
			}
		}
		return 1;
	}
	
	/**
	 * Method that reunites every pushing method to allow the player to move and interact with pushable objects.
	 * @param coordinates Coordinates of the player who pushes.
	 * @param direction Direction the player is taking to move.
	 */
	void push(Coordinates coordinates, Directions direction) {
		// On vérifie d'abord s'il y a un mot en haut
		if(verif_passable(direction, coordinates)) {
			if(push_word(direction, coordinates) != 0 && push_decor(direction, coordinates) != 0)
				coordinates.move(direction, width, height);
		}
	}
	
	
	/**
	 * Checks in the whole playground if there are existing sentences. If it is the case, it calls the changeDecor method.
	 */
	public void sentence() {
		for(Word noun: playGround.keySet()) {
			if(noun instanceof Noun) {
				for(Coordinates coordinates: playGround.get(noun)) {
					for(Word operator: playGround.keySet()) {
						if(operator instanceof Operator && operator.name().equals(Name.IS)) {
							// Horizontal sentence
							if(playGround.get(operator).contains(coordinates.find_coordinates_near(Directions.Right))) {
								for(Word property: playGround.keySet()) {
									if((property instanceof Property || property instanceof Noun) && playGround.get(property).contains(coordinates.find_coordinates_near(Directions.Right).find_coordinates_near(Directions.Right)))
										changeDecor(noun, property);
								}
							}
							// Horizontal sentence
							if(playGround.get(operator).contains(coordinates.find_coordinates_near(Directions.Down))) {
								for(Word property: playGround.keySet()) {
									if((property instanceof Property || property instanceof Noun) && playGround.get(property).contains(coordinates.find_coordinates_near(Directions.Down).find_coordinates_near(Directions.Down)))
										changeDecor(noun, property);
								}
							}
						}
						
					}
				}
			}
		}
		// Pour actualiser les règles du jeu à un décor qui s'est transformé en un autre décor
		for(Decor decor: decors.keySet()) {
			if(!decor.representation().equals(decor.name_prov())) {
				for(Decor decor2: decors.keySet()) {
					if(decor2.representation().equals(decor.name_prov()))
						decor.same_rules(decor2);
				}
			}
		}
	}
	
	/**
	 * Method that applies the rules to every decor sitting in the playground.
	 * @param noun Name in the sentence
	 * @param property Property in the sentence
	 */
	public void changeDecor(Word noun, Word property) {
		for(Decor decor: decors.keySet()) {
			if(decor.representation().equals(noun.name())) {
				switch(property.name()) {
					case YOU:
						decor.setIsYou(true);
						break;
					case PUSH :
						decor.setPushable(true);
						break;
					case STOP :
						decor.setPassable(false);
						break;
					case WIN :
						decor.setVictory(true);
						break;
					case SINK :
						decor.setSink(true);
						break;
					case DEFEAT:
						decor.setDefeat(true);
						break;
					case MELT:
						decor.setMelt(true);
						break;
					case HOT:
						decor.setHot(true);
						break;
					default:
						decor.setNameProv(property.name());
						break;
				}
			}
		}
	}
	
	/**
	 * Checks in the playground if the player won.
	 * @return True if the player won, False otherwise.
	 */
	public boolean win() {
		for(Decor decor_you: decors.keySet()) {
			if(decor_you.isYou() && decor_you.victory())
				return true;
			for(Decor decor_win: decors.keySet()) {
				if(decor_you.isYou() && decor_win.victory()) {
					for(Coordinates coordinates: decors.get(decor_you)) {
						if(decors.get(decor_win).contains(coordinates)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	/**
	 * Checks in the playground if the player lost.
	 * @return True if the player lost, False otherwise.
	 */
	public boolean lose() {
		for(Decor decor: decors.keySet()) {
			if(decor.isYou() && !decors.get(decor).isEmpty())
				return false;
		}
		return true;
	}
	
	/**
	 * Applies the sink property.
	 */
	public void verif_sink() {
		for(Decor decor_sink: decors.keySet()) {
			for(Decor decor: decors.keySet()) {
				if(decor_sink.sink()) {
					for(Iterator<Coordinates> coord_iterator = decors.get(decor_sink).iterator(); coord_iterator.hasNext();) {
						Coordinates coordinates = coord_iterator.next();
						if(decors.get(decor).contains(coordinates) && !decor.equals(decor_sink)) {
							coord_iterator.remove();
							decors.get(decor).remove(decors.get(decor).get(decors.get(decor).indexOf((coordinates))));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Applies the defeat property.
	 */
	public void verif_defeat() {
		for(Decor decor_defeat: decors.keySet()) {
			for(Decor decor_you: decors.keySet()) {
				if(decor_defeat.defeat() && decor_you.isYou()) {
					for(Iterator<Coordinates> coord_iterator = decors.get(decor_you).iterator(); coord_iterator.hasNext();) {
						Coordinates coordinates = coord_iterator.next();
						if(decors.get(decor_defeat).contains(coordinates) && !decor_defeat.equals(decor_you))
							coord_iterator.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Applies the melt property.
	 */
	public void verif_melt() {
		for(Decor decor_hot: decors.keySet()) {
			for(Decor decor_melt: decors.keySet()) {
				if(decor_hot.hot() && decor_melt.melt()) {
					for(Iterator<Coordinates> coord_iterator = decors.get(decor_melt).iterator(); coord_iterator.hasNext();) {
						Coordinates coordinates = coord_iterator.next();
						if(decors.get(decor_hot).contains(coordinates) && !decor_hot.equals(decor_melt))
							coord_iterator.remove();
					}
				}
			}
		}
	}
}
