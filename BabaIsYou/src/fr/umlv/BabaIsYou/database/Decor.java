package fr.umlv.BabaIsYou.database;
import java.awt.Graphics2D;
import fr.umlv.BabaIsYou.enums.Name;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Objects;

/**
 * Class that represents the decor.
 * @author Diagne Ben, Kamdom Omairt
 *
 */
public class Decor{
	private Name representation; 
	private boolean passable;
	private boolean pushable; 
	private boolean victory;
	private boolean isYou;
	private boolean sink;
	private boolean defeat;
	private boolean melt;
	private boolean hot;
	private Name name_prov;	// Nom qui va être utilisé pour son affichage dans le cas où le décor se transforme en une autre à cause d'une règle de jeu appliqué
	
	/**
	 * Initializes the decor by giving the parameterized representation value to the object.
	 * @param representation Enumerated type the decor represents.
	 */
	public Decor(Name representation) {
		this.representation = representation;
		this.name_prov = representation;
		this.isYou = false;
		this.passable = true;
		this.victory = false;
		this.pushable = false; 
		this.sink = false;
		this.defeat = false;
		this.melt = false;
		this.hot = false;
	}
	
	/**
	 * Representation of the decor.
	 * @return The representation
	 */
	public Name representation() {
		return representation;
	}
	
	/**
	 * Representation the decor becomes in the case it transforms into another decor thanks to an applied im-game rule.
	 * @return The concerned representation
	 */
	public Name name_prov() {
		return name_prov;
	}
	
	/**
	 * Checks if the decor can be controlled by the player or not
	 * @return True if it's the case, False otherwise
	 */
	public boolean isYou() {
		return isYou;
	}
	/**
	 * Checks if the decor can be gone through by any object.
	 * @return True if it's the case, False otherwise
	 */
	public boolean passable() {
		return passable;
	}
	
	/**
	 * Checks if the decor can be pushed.
	 * @return True if it's the case, False otherwise
	 */
	public boolean pushable() {
		return pushable;
	}
	/**
	 * Checks if the decor is the goal of the player.
	 * @return True if it's the case, False otherwise
	 */
	public boolean victory() {
		return victory;
	}
	/**
	 * Checks if the decor makes any object that collides with it sink.
	 * @return True if it's the case, False otherwise
	 */
	public boolean sink() {
		return sink;
	}
	/**
	 * Checks if the decor is hot.
	 * @return True if it's the case, False otherwise
	 */
	public boolean hot() {
		return hot;
	}
	/**
	 * Checks if the decor gets destroyed if it collides with a hot decor.
	 * @return True if it's the case, False otherwise
	 */
	public boolean melt() {
		return melt;
	}
	
	/**
	 * Checks if the decor can kill any decor controlled by the player.
	 * @return True if it's the case, False otherwise
	 */
	public boolean defeat() {
		return defeat;
	}
	
	/**
	 * Sets the parameterized representation to the decor that calls this method.
	 * @param representation The parameterized representation.
	 */
	public void setRepresentation(Name representation) {
		this.representation = representation;
		
	}
	/**
	 * Sets the parameterized isYou value to the decor that calls this method.
	 * @param isYou The parameterized isYou value
	 */
	public void setIsYou(boolean isYou) {
		this.isYou = isYou;
	}
	/**
	 * Sets the parameterized pushable value to the decor that calls this method.
	 * @param pushable The parameterized pushable value
	 */
	public void setPushable(boolean pushable) {
		this.pushable = pushable;
	}
	/**
	 * Sets the parameterized passable value to the decor that calls this method.
	 * @param passable The parameterized passable value
	 */
	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	/**
	 * Sets the parameterized victory value to the decor that calls this method.
	 * @param victory The parameterized victory value
	 */
	public void setVictory(boolean victory) {
		this.victory = victory;
	}
	/**
	 * Sets the parameterized name_prov value to the decor that calls this method.
	 * @param name_prov The parameterized name_prov value
	 */
	public void setNameProv(Name name_prov) {
		this.name_prov = name_prov;
	}
	/**
	 * Sets the parameterized sink value to the decor that calls this method.
	 * @param sink The parameterized sink value
	 */
	public void setSink(boolean sink) {
		this.sink = sink;
	}
	/**
	 * Sets the parameterized defeat value to the decor that calls this method.
	 * @param defeat The parameterized defeat value
	 */
	public void setDefeat(boolean defeat) {
		this.defeat = defeat;
	}
	/**
	 * Sets the parameterized melt value to the decor that calls this method.
	 * @param melt The parameterized melt value
	 */
	public void setMelt(boolean melt) {
		this.melt = melt;
	}
	/**
	 * Sets the parameterized hot value to the decor that calls this method.
	 * @param hot The parameterized hot value
	 */
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	
	@Override
	public boolean equals(Object e) {
		if(!(e instanceof Decor)) {
			return false;
		}
		Decor decor_test = (Decor) e;
		return representation.equals(decor_test.representation());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(representation);
	}
	
	/**
	 * Method that applies the same rules respected by the parameter decor_voisin to the decor that calls this method.
	 * @param decor_voisin The concerned parameterized decor.
	 */
	public void same_rules(Decor decor_voisin) {
		this.setIsYou(decor_voisin.isYou());
		this.setPassable(decor_voisin.passable());
		this.setVictory(decor_voisin.victory());
		this.setPushable(decor_voisin.pushable()); 
		this.setSink(decor_voisin.sink()); 
		this.setDefeat(decor_voisin.defeat()); 
		this.setMelt(decor_voisin.melt()); 
		this.setHot(decor_voisin.hot()); 
	}
	
	/**
	 * Method that graphically draws the decor.
	 * @param x X-axis the decor is getting drawn to.
	 * @param y Y-axis the decor is getting drawn to.
	 * @param graphics The drawing zone which the decor is drawn at.
	 * @param width Width of the screen.
	 * @param height Height of the screen.
	 * @param arena_width Width of the level.
	 * @param arena_height Height of the level.
	 */
	public void draw(int x, int y, Graphics2D graphics, int width, int height, int arena_width, int arena_height) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("../BabaIsYou/src/Images/" + name_prov() + "/" + name_prov() + "_0.gif");
		graphics.drawImage(image, x * (width / arena_width), y * (height / arena_height), (width / arena_width) + 4, (height / arena_height) - 9, null);		
	}
	
}