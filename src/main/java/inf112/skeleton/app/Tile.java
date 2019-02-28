package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import inf112.skeleton.app.gameobjects.*;

import java.util.PriorityQueue;

public class Tile implements GameObject{

    private GameObjectType gameObjectType;
    private Texture texture;
    private Sprite sprite;
    private PriorityQueue<GameObject> objectsOnTile;

    public Tile(GameObjectType gameObjectType){
        objectsOnTile = new PriorityQueue<>();
        this.gameObjectType = gameObjectType;
        evaluateSprite();
    }


    /**
     * Evalautes what sprite should be loaded for
     * this tile, based on what the tile's
     * GameObjectType is.
     */
    public void evaluateSprite(){
        switch(gameObjectType){
            case STANDARD_TILE:
                texture = new Texture(Gdx.files.internal("./assets/tiles/standardTile32x32.png"));
                sprite = new Sprite(texture);
                break;
            default:
                texture = new Texture(Gdx.files.internal("./assets/error.png"));
                sprite = new Sprite(texture);
                break;
        }
    }

    public void addObjectOnTile(GameObject newObjectOnTile){
        objectsOnTile.add(newObjectOnTile);
    }

    public void removeObjectFromTile(GameObject objectToRemove){
        objectsOnTile.remove(objectToRemove);
    }

    /**
     * Method that returns a PriorityQueue containing
     * the GameObjects on the tile.
     * @return  PriorityQueue of every GameObject on the tile
     */
    public PriorityQueue<GameObject> getObjectsOnTile(){
        return objectsOnTile;
    }

    public Boolean hasPlayer(Player player){
        return objectsOnTile.contains(player);
    }

    public Boolean hasFlag(){
        for(GameObject gameObject : objectsOnTile){
            if(gameObject.getGameObjectType() == GameObjectType.FLAG){
                return true;
            }
        }
        return false;
    }

    public Boolean hasWallWithOrientation(Orientation orientation){
        GameObjectType wallType;
        switch(orientation){
            case FACING_NORTH: wallType = GameObjectType.SOUTH_WALL; System.out.println("SouthWallAhead");break;
            case FACING_WEST: wallType = GameObjectType.EAST_WALL; break;
            case FACING_SOUTH: wallType = GameObjectType.NORTH_WALL; break;
            case FACING_EAST: wallType = GameObjectType.WEST_WALL; break;
            default: wallType = GameObjectType.NORTH_WALL; break;
        }

        for(GameObject gameObject : objectsOnTile){
            if(gameObject.getGameObjectType() == wallType){
                System.out.println("Hit Wall!");
                return true;
            }
        }
        return false;
    }

    public Boolean hasConveyor(){
        for(GameObject gameObject : objectsOnTile){
            if(gameObject.getGameObjectType() == GameObjectType.CONVEYOR_NORTH){
                return true;
            }
        }
        return false;
    }

    public Conveyor getConveyor(){
        for(GameObject gameObject : objectsOnTile){
            if(gameObject.getGameObjectType() == GameObjectType.CONVEYOR_NORTH){
                return (Conveyor) gameObject;
            }
        }
        return new Conveyor();
    }

    public Boolean hasRepairStation(){
        for(GameObject gameObject : objectsOnTile){
            if(gameObject.getGameObjectType() == GameObjectType.REPAIR_STATION){
                return true;
            }
        }
        return false;
    }

    @Override
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    @Override
    public Sprite getSprite(){
        return sprite;
    }

    @Override
    public int compareTo(Object other) {
        return -1;
    }
}
