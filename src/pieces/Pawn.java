package pieces;

import util.Move;

public class Pawn extends Piece {

    private boolean everMoved;

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
        this.everMoved = false;
        this.hasSpecialMove = true;  // able to go 2 rank at first
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Pawn validateMove
        if (moveValidateCondition(move)) {
            if (isValidForwardRank(move)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidForwardRank(Move move) {
//        TODO - has to be changed if reverse board
        int difference = 1;
        if (color == Color.BLACK) {
            difference = -1;
        }

        if (!everMoved()) {
            return move.getDestinationRank() == move.getOriginRank() + difference
                    || move.getDestinationRank() == move.getOriginRank() + difference * 2;
        }
        return move.getDestinationRank() == move.getOriginRank() + difference;
    }

    public boolean everMoved() {
        return everMoved;
    }

    public boolean setEverMoved(boolean condition) {
        return everMoved = condition;
    }

    @Override
    public void evokedSpecialMove() {
        setEverMoved(true);
    }
}
