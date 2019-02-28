package inf112.skeleton.app.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class AbilityDeck implements IDeck {
    private Stack<RRCard> deck;
    private ArrayList<RRCard> deckList;
    private File file;

    /**
     * Creates a deck of cards
     * @param fileName File needs to be placed in cardDocs directory
     */
    public AbilityDeck(String fileName){

        fileName = "./assets/cardDocs/"+fileName;
        this.file = new File(fileName);
        //file =new File(fileName);
        deck  = new Stack<>();
        deckList = new ArrayList<>();
        createDeck();
    }

    @Override
    public void reset() {
        deck.clear();
        createDeck();
    }

    @Override
    public void createDeck() {
        try{
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                deckList.add(new AbilityCard(reader.nextLine()));
            }
            reader.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        shuffle();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deckList);
        deck.addAll(deckList);
        deckList.clear();
    }


    /**
     *
     * @param health Player health.
     * @return ArrayList of AbilityCards
     */
    @Override
    public ArrayList<RRCard> deal(int health) {
        ArrayList<RRCard> playerDeck = new ArrayList<>();
        playerDeck.add(deck.pop());
        return playerDeck;
    }

    @Override
    public RRCard dealOne() {
        return deck.pop();
    }

    @Override
    public void returnCard(RRCard c) {
        this.deck.insertElementAt(c,(this.deck.size()-1));
    }

    @Override
    public int getSize() {
        return this.deck.size();
    }

    @Override
    public boolean contains(RRCard card) {
        for(RRCard c : deck)
            if(c.compareTo(card)==0)
                return true;
        return false;
    }

}
