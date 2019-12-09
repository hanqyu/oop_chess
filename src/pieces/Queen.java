package pieces;

import util.Move;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
        this.type = Type.QUEEN;
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Queen validateMove
        if (moveValidateCondition(move)) {
            // along file
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                return true;
            }
            // along rank
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                return true;
            }
            if (moveValidateCondition(move)) {
                return Math.abs(move.getDestinationFile() - move.getOriginFile())
                        == Math.abs(move.getDestinationRank() - move.getOriginRank());
            }
        }
        return false;
    }

}
