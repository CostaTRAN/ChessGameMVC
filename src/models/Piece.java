package models;

import strategies.*;

/**
 * La classe Piece représente une pièce dans un jeu d'échecs.
 * Elle contient des informations sur le type de la pièce, sa couleur, sa position,
 * si elle a déjà été déplacée, et la stratégie de mouvement associée.
 */
public class Piece {
    private PieceType type;
    private Color color;
    private Position position;
    private boolean hasMoved;
    private MoveStrategy moveStrategy;

    /**
     * Constructeur de la classe Piece.
     *
     * @param type le type de la pièce.
     * @param color la couleur de la pièce.
     * @param position la position initiale de la pièce.
     */
    public Piece(PieceType type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
        this.hasMoved = false;
        this.moveStrategy = this.createMoveStrategy();
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
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

    /**
     * Crée et retourne la stratégie de mouvement appropriée pour le type de la pièce.
     *
     * @return la stratégie de mouvement appropriée.
     */
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

    /**
     * Retourne la couleur de la pièce.
     *
     * @return la couleur de la pièce.
     */
    public Color getColor() { return this.color; }

    /**
     * Retourne la position actuelle de la pièce.
     *
     * @return la position actuelle de la pièce.
     */
    public Position getPosition() { return this.position; }

    /**
     * Retourne le type de la pièce.
     *
     * @return le type de la pièce.
     */
    public PieceType getType() { return this.type; }

    /**
     * Indique si la pièce a déjà été déplacée.
     *
     * @return true si la pièce a déjà été déplacée, false sinon.
     */
    public boolean hasMoved() { return this.hasMoved; }

    /**
     * Retourne la stratégie de mouvement de la pièce.
     *
     * @return la stratégie de mouvement de la pièce.
     */
    public MoveStrategy getMoveStrategy() { return this.moveStrategy; }

    /**
     * Définit la position de la pièce.
     *
     * @param position la nouvelle position de la pièce.
     */
    public void setPosition(Position position) {
        this.position = position;
        this.moveStrategy.setPosition(position);
    }

    /**
     * Définit si la pièce a déjà été déplacée.
     *
     * @param moved true si la pièce a déjà été déplacée, false sinon.
     */
    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    /**
     * Marque la pièce comme ayant été déplacée.
     */
    public void setMoved() {
        this.hasMoved = true;
    }

    /**
     * Définit la stratégie de mouvement de la pièce.
     *
     * @param moveStrategy la nouvelle stratégie de mouvement.
     */
    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la pièce.
     *
     * @return une représentation sous forme de chaîne de caractères de la pièce.
     */
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
