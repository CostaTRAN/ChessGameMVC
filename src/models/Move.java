package models;

/**
 * La classe Move représente un mouvement dans une partie d'échecs.
 * Elle contient des informations sur la pièce déplacée, les positions de départ et d'arrivée,
 * la pièce capturée (si applicable), et si c'est le premier mouvement de la pièce.
 */
public class Move {
    private Piece piece;
    private Piece capturedPiece;
    private Position from;
    private Position to;
    private boolean isFirstMove;

    /**
     * Constructeur de la classe Move.
     *
     * @param piece la pièce déplacée.
     * @param from la position de départ.
     * @param to la position d'arrivée.
     * @param capturedPiece la pièce capturée (si applicable).
     */
    public Move(Piece piece, Position from, Position to, Piece capturedPiece) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
        this.isFirstMove = !piece.hasMoved();
    }

    /**
     * Retourne la pièce déplacée.
     *
     * @return la pièce déplacée.
     */
    public Piece getPiece() { return this.piece; }

    /**
     * Retourne la pièce capturée (si applicable).
     *
     * @return la pièce capturée, ou null si aucune pièce n'a été capturée.
     */
    public Piece getCapturedPiece() { return this.capturedPiece; }


    /**
     * Retourne la position de départ.
     *
     * @return la position de départ.
     */
    public Position getFrom() { return this.from; }

    /**
     * Retourne la position d'arrivée.
     *
     * @return la position d'arrivée.
     */
    public Position getTo() { return this.to; }

    /**
     * Indique si c'est le premier mouvement de la pièce.
     *
     * @return true si c'est le premier mouvement de la pièce, false sinon.
     */
    public boolean isFirstMove() { return this.isFirstMove; }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du mouvement.
     *
     * @return une représentation sous forme de chaîne de caractères du mouvement.
     */
    @Override
    public String toString() {
        return String.format("%s%s-%s%s",
            this.piece.toString(), this.from.toString(),
            this.capturedPiece != null ? "x" : "", this.to.toString());
    }
}
