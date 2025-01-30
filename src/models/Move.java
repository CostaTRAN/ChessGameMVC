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

    public Piece getPiece() { return this.piece; }
    public Position getFrom() { return this.from; }
    public Position getTo() { return this.to; }
    public Piece getCapturedPiece() { return this.capturedPiece; }
    public boolean isFirstMove() { return this.isFirstMove; }

    @Override
    public String toString() {
        return String.format("%s%s-%s%s", 
            this.piece.toString(), this.from.toString(), 
            this.capturedPiece != null ? "x" : "", this.to.toString());
    }
}