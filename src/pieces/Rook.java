package pieces;

import util.Move;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
        this.type = Type.ROOK;
        this.everMoved = false;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
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
        }

        // all other cases
        return false;
    }

}
