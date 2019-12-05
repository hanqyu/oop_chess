package pieces;

import util.Move;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
        this.doubleMoveRank = this.color.equals(Color.BLACK) ? 7 : 2;  // 초기위치의 rank 지정
        this.rankDifferenceForPawn = this.color.equals(Color.BLACK) ? -1 : 1;  // 움직일 수 있는 rank
        this.hasSpecialMove = true;  // able to go 2 rank at first
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Pawn validateMove
        int difference = move.getDestinationRank() - move.getOriginRank();

        if (moveValidateCondition(move)) {
            if (move.getCapturedPiece() == null) {  // 잡지 않고 움직이는 경우
                if ((move.getDestinationFile() != move.getOriginFile()))
                    return false;

                if (difference == rankDifferenceForPawn)
                    return true;  // 한칸만 움직임

                if (difference == rankDifferenceForPawn * 2 && move.getOriginRank() == doubleMoveRank)
                    return true;  // 두칸까지 움직임. 단, 시작지점의 rank 인 경우
            } else {  // 잡으면서 움직이는 경우. 전진방향 대각선
                if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1
                        && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn) {
                    return true;
                }
            }
        }
        return false;
    }
}
