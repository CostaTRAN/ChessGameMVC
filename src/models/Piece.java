package models;

import strategies.BishopMoveStrategy;
import strategies.KingMoveStrategy;
import strategies.KnightMoveStrategy;
import strategies.MoveStrategy;
import strategies.PawnMoveStrategy;
import strategies.QueenMoveStrategy;
import strategies.RookMoveStrategy;

public class Piece {
    private PieceType type;
    private Color color;
    private Position position;
    private boolean hasMoved;
    private MoveStrategy moveStrategy;

    public Piece(PieceType type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
        this.hasMoved = false;
        this.moveStrategy = this.createMoveStrategy();
    }

    public boolean isValidMove(Position newPosition) {
        if (!newPosition.isValid() || this.position.equals(newPosition)) {
            return false;
        }

        Piece destinationPiece = Game.getGameInstance().getBoard().getPiece(newPosition);
        if (destinationPiece != null && destinationPiece.getColor() == color) {
            return false;
        }

        return this.moveStrategy.isValidMove(newPosition);
    }

    public MoveStrategy createMoveStrategy() {
        return switch (this.type) {
            case PAWN -> new PawnMoveStrategy(this.color, this.position, this.hasMoved);
            case ROOK -> new RookMoveStrategy(this.position);
            case KNIGHT -> new KnightMoveStrategy(this.position);
            case BISHOP -> new BishopMoveStrategy(this.position);
            case QUEEN -> new QueenMoveStrategy(this.position);
            case KING -> new KingMoveStrategy(this.position, this.hasMoved);
        };
    }

    // Getters and Setters
    public Color getColor() { return this.color; }
    public Position getPosition() { return this.position; }
    public PieceType getType() { return this.type; }
    public boolean hasMoved() { return this.hasMoved; }
    public MoveStrategy getMoveStrategy() { return this.moveStrategy; }

    public void setPosition(Position position) {
        this.position = position;
        this.moveStrategy.setPosition(position);
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public void setMoved() {
        this.hasMoved = true;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public String toString() {
        String colorPrefix = color == Color.WHITE ? "W" : "B";
        String symbol = switch (type) {
            case PAWN -> "P";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        return colorPrefix + symbol;
    }
}