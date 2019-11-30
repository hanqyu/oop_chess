package pieces;

import util.Move;

public class Knight extends Piece {

    private boolean everMoved;

    public Knight(Color color) {
        super(color);
        this.type = Type.KNIGHT;
        this.everMoved = false;
        this.hasSpecialMove = true;  // castling
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Knight validateMove
        if (moveValidateCondition(move)) {

            if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1
                    && Math.abs(move.getDestinationRank() - move.getOriginRank()) == 2) {
                return true;
            }

            if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 2
                    && Math.abs(move.getDestinationRank() - move.getOriginRank()) == 1) {
                return true;
            }

        }
        return false;
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
