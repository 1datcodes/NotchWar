
public class Player<E> extends Queue<E> implements Comparable<Player<Card>>{
	// User can choose to index the player and/or name the player
	private int index = 0;
	private String name = "Default-player";
	
	// Default constructor
	public Player() {}
	
	// Constructor that only indexes the player
	public Player(int index) {
		super();
		this.index = index;
	}
	
	// Constructor that only names the player
	public Player(String name) {
		super();
		this.name = name;
	}
	
	// Constructor that indexes and names the player
	public Player(int index, String name) {
		super();
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Index " + index + ": " + super.toString();
	}

	public int compareTo(Player<Card> o) {
		Card c1 = (Card) this.peek();
		Card c2 = o.peek();
		return c1.compareTo(c2);
	}
}
