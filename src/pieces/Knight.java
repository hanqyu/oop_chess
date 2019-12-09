package pieces;

import util.Move;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
        this.type = Type.KNIGHT;
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

}
