import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		// Initialize 2 players for the game
		Player<Card> p1 = new Player<Card>(0, "Alice");
		Player<Card> p2 = new Player<Card>(1, "Bob");
		
		// Create an array of players
		Player<Card>[] players = new Player[2];
		players[0] = p1;
		players[1] = p2;
		
		// Deal cards to each player
		deal(players);
		
		// Start the game
		playNotchWar(players);
	}
	
	// Advances the game until there is a winner
	// What to print:
	// At the end of each round, the number of cards each player has should be shown
	public static void playNotchWar(Player<Card>[] players) {
		while(!winCondition(players)) {
			playRound(players);
			
			// Prints the number of cards each player has
			for (int i = 0; i < players.length - 1; i++) {
				System.out.print("Player " + players[i].getIndex() + " has " + players[i].size() + ", ");
			}
			System.out.println("Player " + players[players.length - 1].getIndex() + " has " + players[players.length - 1].size());
		}
	}
	
	// Deals the card to players
	// Supports as many people as needed
	public static void deal(Player<Card>[] players) {
		// If there are more than 52 players, we cannot play
		// If there are less than 2 players, we cannot play
		if (players.length > 52 || players.length < 2) return;
		
		Deck deck = new Deck();
		deck.shuffle();
		
		// Each player receives a card one by one until the deck is completely dealt
		int i = 0;
		while (!deck.isEmpty()) {
			players[i].put(deck.getCard());
			i = (i + 1) % players.length;
		}
	}
	
	// Plays out 1 round
	// What to print:
	// Each round, the cards being compared should be shown
	// If the result is a Notch, that should be included in the output as shown
	// If there is a war (i.e., a tie in a round), it should print "WAR!"
	public static int playRound(Player<Card>[] players) {
		// Idea: add an Index field to the Player class to keep track of its original position
		// // Then sort the array of players according to its cards
		// // This way we can determine the winner and 2nd place easily
		// // Update: make a copy of the original array
		// // And then sort the copy so that the order doesn't get messed up
		Player<Card>[] temp = players.clone();
		
		// Prints the card each player plays in this round
		for(int i = 0; i < players.length - 1; i++) {
			System.out.print(players[i].peek().toString() + " versus ");
		}
		System.out.print(players[players.length - 1].peek().toString());
		
		// Sort player objects in ascending order with respect to their top card
		Arrays.sort(temp);
		int minDiff = temp[temp.length - 1].peek().compareTo(temp[temp.length - 2].peek());
		
		switch (minDiff) {
		case 0:
			System.out.println("\nWAR!");
			return war(players);
		case 1:
			// Notch: the player with the 1 lower card wins
			// Print that the result was a notch
			System.out.println(" (Notched!)");
			// Player object stores its index so we can retrieve that
			return win(players, temp[temp.length - 2].getIndex());
		default:
			System.out.println();
			return win(players, temp[temp.length - 1].getIndex());
		}
	}
	
	// A war is when each player "puts down" 3 cards, then their next card is compared
	// The winner takes all the cards in the pile
	public static int war(Player<Card>[] players) {
		// Each player "puts down" 3 cards, then compares their 4th card
		// Make a pile to store the "pile"
		Queue<Card> pile = new Queue<Card>();
		
		// Add first 3 cards for all players
		// Null is added if the player runs out of cards
		for (int i = 0; i < players.length; i++) {
			// If the player has enough cards, put down 3 cards
			if (players[i].size() > 3) {
				for (int j = 0; j < 3; j++) {
					pile.put(players[i].get());
				}
			} else if (players[i].size() > 1) {
				// The player doesn't have enough cards
				// Add as many cards as possible while keeping 1 card in their hand
				while(players[i].size() > 1) {
					pile.put(players[i].get());
				}
			} else {
				// The player has no card left in their hand
				continue;
			}
		}
		
		// Play round like normal
		int winner = win(players, playRound(players));
		
		// Add all the cards in the pile to the winner's hand
		for (Card c : pile) {
			if (c != null) {
				players[winner].put(c);
			}
		}
		return winner;
	}
	
	// Non-war win
	// Each player gives 1 card to the winner
	public static int win(Player<Card>[] players, int winner) {
		// Collects all the cards in the pile and adds it to the back of the winner
		for (int i = 0; i < players.length; i++) {
			// Remove top card and add to the back of the winner
			if (players[i].size() > 0) {
				players[winner].add(players[i].get());
			}
		}
		return winner;
	}
	
	// Returns true if only 1 player has the cards
	// What to print:
	// At the end of the game, the winner should be reported
	public static boolean winCondition(Player<Card>[] players) {
		// Count the number of players with a valid hand (ie their top card isn't null)
		int numLost = 0;
		Player<Card> winner = null;
		for (Player<Card> p : players) {
			// If the size of the player's hand is less than 0 (no card), then they lost the game
			if (p.size() < 1) {
				numLost++;
			} else {
				winner = p;
			}
		}
		
		// The game ends if there are only 1 player remaining
		if (players.length - numLost == 1 && winner != null) {
			System.out.println("The winner is... Player " + winner.getIndex());
		}
		
		return players.length - numLost == 1;
	}

}
