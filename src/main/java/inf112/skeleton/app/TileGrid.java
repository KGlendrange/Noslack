package inf112.skeleton.app;

import inf112.skeleton.app.cards.Program;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.gameobjects.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TileGrid{
    private Tile[][] tileGrid;
    private int rows;
    private int columns;
    private final String fileName = "./assets/maplayout.txt";
    private Player[] players;
    private int playersInitiated; // How many players have been initiated so far.

    /**
     * Default constructor.
     */
    public TileGrid(){
        this.rows = 12;
        this.columns = 12;
        tileGrid = new Tile[rows][columns];
        this.players = new Player[1];
        this.playersInitiated = 0;

        initiateTiles(tileGrid, fileName);
    }

    /**
     * Constructor with specifications.
     * @param rows The amount of rows in the grid.
     * @param columns The amount of columns in the grid.
     * @param players The amount of players in the game.
     */
    public TileGrid(int rows, int columns, int players){
        this.rows = rows;
        this.columns = columns;
        tileGrid = new Tile[rows][columns];
        this.players = new Player[players];
        this.playersInitiated = 0;

        initiateTiles(tileGrid, fileName);
    }

    public TileGrid(int rows, int columns, int players, String file) {
        this.rows = rows;
        this.columns = columns;
        tileGrid = new Tile[rows][columns];
        this.players = new Player[players];
        this.playersInitiated = 0;

        initiateTiles(tileGrid, file);
    }

    /**
     *
     * @param row: row of the requested tile
     * @param column: column of the requested tile
     * @return Tile at specified coordinate
     */
    public Tile getTile(int row, int column){
        return tileGrid[row][column];
    }

    public Tile getTile(Coordinate coordinate){
        return tileGrid[coordinate.getRow()][coordinate.getColumn()];
    }

    /**
     * Initiates every tile in the grid
     * @param tileGrid
     *
     * Todo:
     * Implement reading tile-layout from file, or randomisation.
     */
    private void initiateTiles(Tile[][] tileGrid, String fileName){

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            for(int row = rows-1; row>=0; row--){
                String nextTileTypeLine = bufferedReader.readLine();
                String[] nextTileTypeLineArray = nextTileTypeLine.split(" ");
                for(int column = columns-1; column>=0; column--){

                    String nextTileTypesOfColumn = nextTileTypeLineArray[column];
                    tileGrid[row][column] = new Tile(GameObjectType.STANDARD_TILE);
                    String[] typesOnTile = nextTileTypesOfColumn.split(",");

                    for(int index = 0; index<typesOnTile.length; index++) {


                        GameObjectType nextTileType = stringToGameObjectType(typesOnTile[index]);

                        // Adding objects on top of tile
                        if (typesOnTile[index] != " ") { // If tile type is not standardTile
                            switch (nextTileType) {
                                case FLAG:
                                    tileGrid[row][column].addObjectOnTile(new Flag());
                                    break;
                                case CONVEYOR_NORTH:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor());
                                    break;
                                case CONVEYOR_EAST:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_EAST));
                                    break;
                                case CONVEYOR_WEST:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_WEST));
                                    break;
                                case CONVEYOR_SOUTH:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_SOUTH));
                                    break;
                                case FAST_CONVEYOR_NORTH:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(true));
                                    break;
                                case FAST_CONVEYOR_EAST:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_EAST, true));
                                    break;
                                case FAST_CONVEYOR_WEST:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_WEST,true));
                                    break;
                                case FAST_CONVEYOR_SOUTH:
                                    tileGrid[row][column].addObjectOnTile(new Conveyor(Orientation.FACING_SOUTH,true));
                                    break;
                                case PLAYER_NORTH:
                                case NORTH_WALL:
                                    tileGrid[row][column].addObjectOnTile(new Wall(Orientation.FACING_NORTH));
                                    break;
                                case WEST_WALL:
                                    tileGrid[row][column].addObjectOnTile(new Wall(Orientation.FACING_WEST));
                                    break;
                                case SOUTH_WALL:
                                    tileGrid[row][column].addObjectOnTile(new Wall(Orientation.FACING_SOUTH));
                                    break;
                                case EAST_WALL:
                                    tileGrid[row][column].addObjectOnTile(new Wall(Orientation.FACING_EAST));
                                    break;
                                case PLAYER:
                                    Player newPlayer = new Player(playersInitiated);
                                    tileGrid[row][column].addObjectOnTile(newPlayer);
                                    players[playersInitiated] = newPlayer; // Add new player to list of players.
                                    newPlayer.setPosition(new Coordinate(row, column));
                                    newPlayer.setBackUp(new Coordinate(row, column));
                                    playersInitiated++; // One more player has been initiated, move the index 1 up.
                                    break;
                            }

                        }
                    }

                }
            }

            bufferedReader.close();
        }catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    private GameObjectType charToGameObjectType(char nextTileType){
        switch(nextTileType){
            case '1': return GameObjectType.STANDARD_TILE;
            case '2': return GameObjectType.CONVEYOR_NORTH;
            case '3': return GameObjectType.PLAYER;
            case '4': return GameObjectType.FLAG;

            case 'w': return GameObjectType.NORTH_WALL;
            case 'a': return GameObjectType.WEST_WALL;
            case 's': return GameObjectType.SOUTH_WALL;
            case 'd': return GameObjectType.EAST_WALL;

            default: return GameObjectType.STANDARD_TILE;
        }
    }

    public void activateTiles(){
        for(Tile[] tileRow : tileGrid){
            for(Tile tile : tileRow){
                for (Player player : players) {
                    if (tile.hasPlayer(player)) {
                        if(tile.hasConveyor()) {
                            Conveyor conveyor = tile.getConveyor();
                            moveInDirectionOfConveyor(conveyor, player.getPlayerNumber());
                        }
                        if(tile.hasRepairStation()){
                            player.repair();
                            player.setBackUp(player.getPosition());
                        }
                        if(tile.hasFlag()){
                            if(player.isFinished()){
                                player.win();
                            }
                        }
                    }
                }
            }
        }
    }

    public void moveInDirectionOfConveyor(Conveyor conveyor, int playerNumber){

        Player playerToMove = players[playerNumber];
        if(playerToMove.getCurrentMove() == Program.NONE) {
            if(conveyor.isFast()){
                switch (conveyor.getOrientation()) {
                    case FACING_NORTH:
                        movePlayer(playerNumber, 2, 0);
                        break;
                    case FACING_WEST:
                        movePlayer(playerNumber, 0, -2);
                        break;
                    case FACING_SOUTH:
                        movePlayer(playerNumber, -2, 0);
                        break;
                    case FACING_EAST:
                        movePlayer(playerNumber, 0, 2);
                        break;
                    default:
                        break;
                }
            }
            switch (conveyor.getOrientation()) {
                case FACING_NORTH:
                    movePlayer(playerNumber, 1, 0);
                    break;
                case FACING_WEST:
                    movePlayer(playerNumber, 0, -1);
                    break;
                case FACING_SOUTH:
                    movePlayer(playerNumber, -1, 0);
                    break;
                case FACING_EAST:
                    movePlayer(playerNumber, 0, 1);
                    break;
                default:
                    break;
            }
        }
    }

    public void applyNextProgram(int playerNumber){
        Player player = players[playerNumber];
        ProgramCard nextProgramCard = player.getNextProgram();
        Program move = nextProgramCard.getMove();

        player.setCurrentMove(move);
    }

    /**
     *
     * @param move the rotation to be applied.
     * @param playerNumber the identifier of the player whose move should be continued.
     */
    public void applyRotation(Program move, int playerNumber){
        Player player = players[playerNumber];
        player.updateOrientation(move);

    }

    /**
     * Method that makes a player perform a move.
     * @param move the move to apply
     * @param playerNumber the number of the player that the move should be applied to
     */
    public void applyMove(Program move, int playerNumber){
        Player player = players[playerNumber];

        int rowsToMove = 0;
        int columnsToMove = 0;

        switch (player.getOrientation()) {
            case FACING_NORTH:
                rowsToMove = 1;
                break;
            case FACING_SOUTH:
                rowsToMove = -1;
                break;
            case FACING_EAST:
                columnsToMove = 1;
                break;
            case FACING_WEST:
                columnsToMove = -1;
                break;

        }
        if(move==Program.BACK){
            rowsToMove *= -1;
            columnsToMove *= -1;
        }

        movePlayer(playerNumber, rowsToMove, columnsToMove);
    }

    /**
     * Method that continues the move a player has in progress.
     * @param playerNumber the identifier of the player whose move should be continued.
     */
    public void continueMove(int playerNumber){
        Player player = players[playerNumber];
        Program currentMove = player.getCurrentMove();
        int moveProgression = player.getMoveProgression();
        int totalMoves = currentMove.totalMoves();
        if(moveProgression == totalMoves){
            player.setCurrentMove(Program.NONE);
            player.resetMoveProgress();
        }else{
            boolean moveIsRotation = (currentMove==Program.LEFT) || (currentMove==Program.RIGHT) || (currentMove==Program.U);
            if(moveIsRotation){
                applyRotation(currentMove, playerNumber);
            }else{
                applyMove(currentMove, playerNumber);
            }
            player.progressMove();
        }
    }

    public void movePlayer(int playerNumber, int rowsToMove, int columnsToMove){

        Player player = players[playerNumber];
        Coordinate coordinatesOfPlayer = player.getPosition();
        int rowOfPlayer = coordinatesOfPlayer.getRow();
        int columnOfPlayer = coordinatesOfPlayer.getColumn();

        if(!canMovePlayer(playerNumber, rowsToMove, columnsToMove, rowOfPlayer, columnOfPlayer)){
            return;
        }

        tileGrid[rowOfPlayer][columnOfPlayer].removeObjectFromTile(player);
        tileGrid[rowOfPlayer+rowsToMove][columnOfPlayer+columnsToMove].addObjectOnTile(player);
        player.setPosition(new Coordinate(rowOfPlayer+rowsToMove, columnOfPlayer+columnsToMove));
    }

    private boolean canMovePlayer(int playerNumber, int rowsToMove, int columnsToMove, int rowOfPlayer, int columnOfPlayer){

        if((rowOfPlayer+rowsToMove > rows-1) || (rowOfPlayer+rowsToMove < 0)){
            playerOutOfBounds(playerNumber);
            return false;
        }
        if((columnOfPlayer+columnsToMove > columns-1) || (columnOfPlayer+columnsToMove < 0)){
            playerOutOfBounds(playerNumber);
            return false;
        }

        if(playerFacingWall(playerNumber, rowOfPlayer, columnOfPlayer)){
            return false;
        }

        return true;
    }

    private boolean playerFacingWall(int playerNumber, int rowOfPlayer, int columnOfPlayer){
        Player player = players[playerNumber];
        Orientation orientationOfPlayer = player.getOrientation();

        switch(orientationOfPlayer){
            case FACING_NORTH:
                Tile tileNorthOfPlayer = tileGrid[rowOfPlayer-1][columnOfPlayer];
                if(tileNorthOfPlayer.hasWallWithOrientation(Orientation.FACING_SOUTH)){
                    return true;
                }
                break;
            case FACING_WEST:
                Tile tileWestOfPlayer = tileGrid[rowOfPlayer][columnOfPlayer-1];
                if(tileWestOfPlayer.hasWallWithOrientation(Orientation.FACING_EAST)){
                    return true;
                }
                break;
            case FACING_SOUTH:
                Tile tileSouthOfPlayer = tileGrid[rowOfPlayer+1][columnOfPlayer];
                if(tileSouthOfPlayer.hasWallWithOrientation(Orientation.FACING_NORTH)){
                    return true;
                }
                break;
            case FACING_EAST:
                Tile tileEastOfPlayer = tileGrid[rowOfPlayer][columnOfPlayer+1];
                if(tileEastOfPlayer.hasWallWithOrientation(Orientation.FACING_WEST)){
                    return true;
                }
                break;
        }

        return false;
    }

    private void playerOutOfBounds(int playerNumber){
        // Respawn player
        respawnPlayer(playerNumber);
    }

    private void respawnPlayer(int playerNumber){
        Player player = players[playerNumber];

        int rowOfPlayer = player.getPosition().getRow();
        int columnOfPlayer = player.getPosition().getColumn();
        tileGrid[rowOfPlayer][columnOfPlayer].removeObjectFromTile(player);

        int respawnRow = player.getBackUp().getRow();
        int respawnColumn = player.getBackUp().getColumn();

        tileGrid[respawnRow][respawnColumn].addObjectOnTile(player);
        player.setPosition(new Coordinate(respawnRow, respawnColumn));
        player.recieveDamage(3);

        //players[playerNumber].getSprite().translate(respawnRow, respawnColumn);
    }

    public Player getPlayer(int playerNumber){
        return players[playerNumber];
    }

    public Coordinate getCoordinatesOfPlayer(int playerNumber){
        return players[playerNumber].getPosition();
    }

    private GameObjectType stringToGameObjectType(String nextTileType){
        switch(nextTileType){
            case "1": return GameObjectType.STANDARD_TILE;
            case "C1": return GameObjectType.CONVEYOR_NORTH;
            case "C2": return GameObjectType.CONVEYOR_EAST;
            case "C3": return GameObjectType.CONVEYOR_SOUTH;
            case "C4": return GameObjectType.CONVEYOR_WEST;
            case "CC1": return GameObjectType.FAST_CONVEYOR_NORTH;
            case "CC2": return GameObjectType.FAST_CONVEYOR_EAST;
            case "CC3": return GameObjectType.FAST_CONVEYOR_SOUTH;
            case "CC4": return GameObjectType.FAST_CONVEYOR_WEST;

            //Player
            case "P1": return GameObjectType.PLAYER_NORTH;
            case "P2": return GameObjectType.PLAYER_EAST;
            case "P3": return GameObjectType.PLAYER_SOUTH;
            case "P4": return GameObjectType.PLAYER_WEST;


            //Default(non orientation spesific) objects needs to be down here so they don't trigger before
            case "CC": return GameObjectType.FAST_CONVEYOR_NORTH;
            case "C": return GameObjectType.CONVEYOR_NORTH;
            case "F": return GameObjectType.FLAG;
            case "P": return GameObjectType.PLAYER_NORTH;

            default: return GameObjectType.STANDARD_TILE;
        }
    }

}
