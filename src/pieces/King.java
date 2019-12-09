package pieces;

import util.Move;
import util.MoveValidator;

public class King extends Piece {

    public King(Color color) {
        super(color);
        this.type = Type.KING;
        this.everMoved = false;
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO King validateMove
        if (moveValidateCondition(move)) {
            if ((Math.abs(move.getDestinationRank() - move.getOriginRank()) < 2)
                && (Math.abs(move.getDestinationFile() - move.getOriginFile()) < 2)){
                return true;
            }
            if(MoveValidator.isValidCastling(move)){
                return true;
            }
        }
        return false;
    }

}
