package board;

import pieces.*;
import util.Core;
import util.GameStatus;
import util.Move;

import java.util.Iterator;

public class Board {

    public static final int DIMENSION = 8;

    private static Square[][] grid = new Square[DIMENSION][DIMENSION];

    private static Board boardInstance = new Board();

    public static Board getInstance() {
        return boardInstance;
    }

    private Board() {
        initialize();
    }

    public static void initialize() {
        initializeSquares();
        initializePieces();
    }

    public static void initialize(GameStatus gameStatus) {
        initializeSquares();
        initializePieces(gameStatus);
    }

    public static Square getSquare(char file, int rank) {
        file = Character.toLowerCase(file);
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8) {
            return null;
        } else {
            return grid[file - 'a'][rank - 1];
        }
    }

    public static void executeMove(Move move) {
        Square originSquare = getSquare(move.getOriginFile(), move.getOriginRank());
        Square destinationSquare = getSquare(move.getDestinationFile(), move.getDestinationRank());
        if (destinationSquare.getCurrentPiece() != null) {
            destinationSquare.getCurrentPiece().setCapture(true);
            PieceSet.addCapturedPiece(destinationSquare.getCurrentPiece());
        }
        destinationSquare.setCurrentPiece(originSquare.getCurrentPiece());
        originSquare.setCurrentPiece(null);
    }

    private static void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square();
            }
        }
    }

    private static void initializePieces() {
        /*
        TODO-piece
            Initialize pieces on board
            Check following code to implement other pieces
            Highly recommended to use same template!
         */

        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        getSquare('a', 1).setCurrentPiece(whiteRooksIterator.next());
        getSquare('h', 1).setCurrentPiece(whiteRooksIterator.next());
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        getSquare('a', 8).setCurrentPiece(blackRooksIterator.next());
        getSquare('h', 8).setCurrentPiece(blackRooksIterator.next());

        Iterator<Piece> whiteKnightsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
        getSquare('b', 1).setCurrentPiece(whiteKnightsIterator.next());
        getSquare('g', 1).setCurrentPiece(whiteKnightsIterator.next());
        Iterator<Piece> blackKnightsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
        getSquare('b', 8).setCurrentPiece(blackKnightsIterator.next());
        getSquare('g', 8).setCurrentPiece(blackKnightsIterator.next());

        Iterator<Piece> whiteBishopsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
        getSquare('c', 1).setCurrentPiece(whiteBishopsIterator.next());
        getSquare('f', 1).setCurrentPiece(whiteBishopsIterator.next());
        Iterator<Piece> blackBishopsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
        getSquare('c', 8).setCurrentPiece(blackBishopsIterator.next());
        getSquare('f', 8).setCurrentPiece(blackBishopsIterator.next());

        Iterator<Piece> whiteKingIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
        getSquare('e', 1).setCurrentPiece(whiteKingIterator.next());
        Iterator<Piece> blackKingIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
        getSquare('e', 8).setCurrentPiece(blackKingIterator.next());

        Iterator<Piece> whiteQueenIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
        getSquare('d', 1).setCurrentPiece(whiteQueenIterator.next());
        Iterator<Piece> blackQueenIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
        getSquare('d', 8).setCurrentPiece(blackQueenIterator.next());


        Iterator<Piece> whitePawnsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
        Iterator<Piece> blackPawnsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();

        char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        for (int i = 0; i < 8; i++) {
            getSquare(files[i], 2).setCurrentPiece(whitePawnsIterator.next());
            getSquare(files[i], 7).setCurrentPiece(blackPawnsIterator.next());
        }
    }

    private static void initializePieces(GameStatus gameStatus) {
        if (gameStatus == null) {
            initializePieces();
        } else {
            for (GameStatus.Piece piece : gameStatus.getPieceObjs()) {
                getSquare(piece.getFile().toCharArray()[0], piece.getRank()).setCurrentPiece(piece.makePiece());
            }
        }
    }
}
