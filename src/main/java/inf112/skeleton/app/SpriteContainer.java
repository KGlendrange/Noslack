package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.cards.AbilityCard;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.RRCard;
import inf112.skeleton.app.gameobjects.Flag;
import inf112.skeleton.app.gameobjects.GameObject;
import inf112.skeleton.app.gameobjects.GameObjectType;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class SpriteContainer {

    private Sprite dealtCardsBackgroundSprite;
    private Sprite selectedCardsBackgroundSprite;
    private Sprite cardTestSprite;
    private Sprite currentSprite;
    private ProgramCard currentCard;
    private String abilityText;
    private Sprite goButton;
    private SpriteBatch batch;
    private int drawPositionX;
    private int drawPositionY;
    private final int TILE_SIZE = 32;
    private AbilityCard currentAbility;
    private AbilityCard emptyAbility;
    private BitmapFont font;
    private final int GRID_ROWS = 12;
    private final int GRID_COLUMNS = 12;






    public SpriteContainer(SpriteBatch batch){

        this.batch = batch;
        this.dealtCardsBackgroundSprite = setSprite("./assets/cards/dealtCardsBackground.png");
        this.selectedCardsBackgroundSprite = setSprite("./assets/cards/KortBakgrunn2.png");
        this.cardTestSprite = setSprite("./assets/cards/back-up.png");
        this.goButton = setSprite("./assets/cards/dontpress.png");
        this.goButton.setPosition(33, 220);
        this.emptyAbility = new AbilityCard(" ");
        this.currentAbility = emptyAbility;
        this.abilityText = "";
        this.font = new BitmapFont();
        this.font.setColor(0,0,0,1);


    }



    public void renderDealtCards(ArrayList<ProgramCard> programHand) {
        this.drawPositionX = 0;
        this.drawPositionY = 40 + TILE_SIZE * 4;

        // Draw background for dealt cards.

        this.dealtCardsBackgroundSprite.setPosition(this.drawPositionX, Gdx.graphics.getHeight()-dealtCardsBackgroundSprite.getHeight()-1);
        this.dealtCardsBackgroundSprite.draw(this.batch);

        this.selectedCardsBackgroundSprite.draw(this.batch);


        this.currentAbility.getSprite().draw(this.batch);



        for (ProgramCard card : programHand) {
            card.getSprite().draw(this.batch);
            font.draw(this.batch,""+card.getPriority(),card.getSprite().getX()+7,card.getSprite().getY()+100);
            font.draw(this.batch,""+card.getMove(),card.getSprite().getX()+7,card.getSprite().getY()+30);
        }

    }

    public void getCardSprite(AbilityCard abilityCard){
        this.currentAbility=abilityCard;

    }

    private Sprite setSprite(String texturePath) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        return new Sprite(texture);
    }


    public void renderGrid(TileGrid tileGrid) {

        /*
         * Todo:
         * Render the grid and all the objects residing
         * on it.
         */

        // Work in progress

        // Start draw position after the dealt cards.
        this.drawPositionX = TILE_SIZE * 4;
        this.drawPositionY = 40 + TILE_SIZE * 4;
        for (int row = 0; row < this.GRID_ROWS; row++) {
            for (int column = 0; column < this.GRID_COLUMNS; column++) {

                // Retrieve current tile from grid
                Tile tileBeingDrawn = tileGrid.getTile(row, column);
                // Retrieve PriorityQueue of GameObjects on current tile
                PriorityQueue<GameObject> objectsOnTile = tileBeingDrawn.getObjectsOnTile();

                // Draw the tile
                Sprite spriteOfTile = tileBeingDrawn.getSprite();
                spriteOfTile.setPosition(this.drawPositionX, this.drawPositionY);
                spriteOfTile.draw(this.batch);

                // Draw GameObjects on tile
                for (GameObject gameObject : objectsOnTile) {
                    Sprite spriteOfGameObject = gameObject.getSprite();
                    spriteOfGameObject.setPosition(this.drawPositionX, this.drawPositionY);
                    spriteOfGameObject.draw(this.batch);
                    if (gameObject.getGameObjectType() == GameObjectType.FLAG){
                        this.font.draw(this.batch,((Flag)gameObject).getFlagNumber()+"",drawPositionX+spriteOfGameObject.getWidth()/2,drawPositionY+spriteOfGameObject.getHeight()/2);
                    }
                }

                this.drawPositionX += this.TILE_SIZE;    // Moving the horizontal drawPosition, one tile over.
            }
            this.drawPositionX = this.TILE_SIZE * 4; // Resetting the horizontal drawPosition.
            this.drawPositionY += this.TILE_SIZE;    // Moving the vertical drawPosition, one tile up.
        }
        // Resetting the vertical drawPosition.
        this.drawPositionY = 0;

    }

}
