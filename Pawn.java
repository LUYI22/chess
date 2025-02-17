package chess;

public class Pawn extends Piece {
    public Pawn(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, Piece[][] board) {
        int direction = (player == Player.white) ? 1 : -1;
        int startRow = (player == Player.white) ? 1 : 6;

        // Basic move
        if (fromX == toX && toY == fromY + direction && board[toY][toX] == null) {
            return true;
        }

        // Double move from starting position
        if (fromX == toX && toY == fromY + 2 * direction && fromY == startRow && board[toY][toX] == null) {
            return true;
        }

        // Capture move
        if (Math.abs(toX - fromX) == 1 && toY == fromY + direction && board[toY][toX] != null && board[toY][toX].getPlayer() != player) {
            return true;
        }

        return false;
    }

    @Override
    public ReturnPiece.PieceType getType() {
        return ReturnPiece.PieceType.P;
    }
}