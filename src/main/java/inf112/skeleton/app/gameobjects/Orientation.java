package inf112.skeleton.app.gameobjects;

import inf112.skeleton.app.cards.Program;
import inf112.skeleton.app.cards.ProgramCard;

public enum Orientation {
    FACING_NORTH,
    FACING_WEST,
    FACING_SOUTH,
    FACING_EAST,
    NONE;

    /**
     * Rotates the orientation from the rartionen given from ProgramCard.
     * @param r Program with orientation wanted.
     * @return New orientation after rotation
     */
    public Orientation rotate(Program r){
        if(r.equals(Program.MOVE3)||r.equals(Program.MOVE2)||r.equals(Program.MOVE1)||r.equals(Program.BACK))
            return this;
        switch (this) {
            case FACING_NORTH:
                switch (r) {
                    case RIGHT:
                        return FACING_EAST;
                    case LEFT:
                        return FACING_WEST;
                    case U:
                        return FACING_SOUTH;
                }
            case FACING_SOUTH:
                switch (r) {
                    case RIGHT:
                        return FACING_WEST;
                    case LEFT:
                        return FACING_EAST;
                    case U:
                        return FACING_NORTH;
                }
            case FACING_EAST:
                switch (r) {
                    case RIGHT:
                        return FACING_SOUTH;
                    case LEFT:
                        return FACING_NORTH;
                    case U:
                        return FACING_WEST;
                }
            case FACING_WEST:
                switch (r) {
                    case RIGHT:
                        return FACING_NORTH;
                    case LEFT:
                        return FACING_SOUTH;
                    case U:
                        return FACING_EAST;
                }
            default: return this;
        }

    }

}
