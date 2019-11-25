package pieces;

import util.Move;

public class King extends Piece {

    public King(Color color) {
        super(color);
        this.type = Type.KING;
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO King validateMove
        if (moveValidateCondition(move)) {
            return Math.abs(move.getDestinationRank() - move.getOriginRank())
                    * Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1;
        }

        return false;
    }

}
