package models;

public class Move {
    private Piece piece;
    private Position from;
    private Position to;
    private Piece capturedPiece;
    private boolean isFirstMove;

    public Move(Piece piece, Position from, Position to, Piece capturedPiece) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
        this.isFirstMove = !piece.hasMoved();
    }

    public Piece getPiece() { return piece; }
    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public boolean isFirstMove() { return isFirstMove; }

    @Override
    public String toString() {
        return String.format("%s%s-%s%s", 
            piece.toString(), from.toString(), 
            capturedPiece != null ? "x" : "", to.toString());
    }
}