package chess;

public abstract class Piece {
    protected Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract boolean isValidMove(int fromX, int fromY, int toX, int toY, Piece[][] board);

    public abstract ReturnPiece.PieceType getType();
}