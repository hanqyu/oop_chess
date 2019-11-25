package pieces;

import util.Move;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
        this.type = Type.BISHOP;
    }

    @Override
    public boolean validateMove(Move move) {
//        TODO Bishop ValidateMove
        if (moveValidateCondition(move)) {
            return Math.abs(move.getDestinationFile() - move.getOriginFile())
                    == Math.abs(move.getDestinationRank() - move.getOriginRank());
        }

        return false;
    }

}
