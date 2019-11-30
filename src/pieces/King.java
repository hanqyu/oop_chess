package pieces;

import util.Move;

public class King extends Piece {

    public King(Color color) {
        super(color);
        this.type = Type.KING;
        this.everMoved = false;
        this.hasSpecialMove = true;  // castling
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO King validateMove
        if (moveValidateCondition(move)) {  // TODO-ValidateCondition을 사용하지 않아야 castling이 가능할 듯
            return Math.abs(move.getDestinationRank() - move.getOriginRank())
                    * Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1;
        }

        return false;
    }

    public boolean setEverMoved(boolean bool) {
        return everMoved = bool;
    }

    @Override
    public void evokedSpecialMove() {
        setEverMoved(true);
    }
}
