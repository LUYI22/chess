package chess;

public class Queen extends Piece {
    private Rook rookMovement;
    private Bishop bishopMovement;

    public Queen(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
        this.rookMovement = new Rook(piecetype, pieceFile, pieceRank, color, board);
        this.bishopMovement = new Bishop(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        // Update the position of helper pieces
        rookMovement.pieceFile = this.pieceFile;
        rookMovement.pieceRank = this.pieceRank;
        bishopMovement.pieceFile = this.pieceFile;
        bishopMovement.pieceRank = this.pieceRank;

        // Queen can move like a rook or a bishop
        return rookMovement.canMove(destination) || bishopMovement.canMove(destination);
    }
}
