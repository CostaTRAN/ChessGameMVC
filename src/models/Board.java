package models;

import java.util.ArrayList;

/**
 * La classe Board représente l'échiquier dans un jeu d'échecs.
 * Elle gère la disposition des pièces, l'historique des mouvements et les règles de déplacement.
 */
public class Board {
    private Piece[][] squares;
    private ArrayList<Move> moveHistory;

    /**
     * Constructeur de la classe Board.
     * Initialise l'échiquier avec les pièces à leurs positions de départ.
     */
    public Board() {
        this.squares = new Piece[8][8];
        this.moveHistory = new ArrayList<Move>();
        initializeBoard();
    }

    /**
     * Initialise l'échiquier avec les pièces à leurs positions de départ.
     */
    public void initializeBoard() {
        // Place les pions
        for (int col = 0; col < 8; col++) {
            this.squares[1][col] = new Piece(PieceType.PAWN, Color.WHITE, new Position(1, col));
            this.squares[6][col] = new Piece(PieceType.PAWN, Color.BLACK, new Position(6, col));
        }

        // Place les pièces majeures
        // Pièces blanches
        placePiece(0, 0, PieceType.ROOK, Color.WHITE);
        placePiece(0, 1, PieceType.KNIGHT, Color.WHITE);
        placePiece(0, 2, PieceType.BISHOP, Color.WHITE);
        placePiece(0, 3, PieceType.QUEEN, Color.WHITE);
        placePiece(0, 4, PieceType.KING, Color.WHITE);
        placePiece(0, 5, PieceType.BISHOP, Color.WHITE);
        placePiece(0, 6, PieceType.KNIGHT, Color.WHITE);
        placePiece(0, 7, PieceType.ROOK, Color.WHITE);

        // Pièces noires
        placePiece(7, 0, PieceType.ROOK, Color.BLACK);
        placePiece(7, 1, PieceType.KNIGHT, Color.BLACK);
        placePiece(7, 2, PieceType.BISHOP, Color.BLACK);
        placePiece(7, 3, PieceType.QUEEN, Color.BLACK);
        placePiece(7, 4, PieceType.KING, Color.BLACK);
        placePiece(7, 5, PieceType.BISHOP, Color.BLACK);
        placePiece(7, 6, PieceType.KNIGHT, Color.BLACK);
        placePiece(7, 7, PieceType.ROOK, Color.BLACK);

        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                this.squares[row][col] = null;
            }
        }
    }

    /**
     * Place une pièce à une position spécifique sur l'échiquier.
     *
     * @param row la ligne de la position.
     * @param col la colonne de la position.
     * @param type le type de la pièce.
     * @param color la couleur de la pièce.
     */
    private void placePiece(int row, int col, PieceType type, Color color) {
        Position pos = new Position(row, col);
        this.squares[row][col] = new Piece(type, color, pos);
    }

    /**
     * Récupère la pièce à une position spécifique sur l'échiquier.
     *
     * @param position la position de la pièce.
     * @return la pièce à la position spécifiée, ou null si aucune pièce n'est présente.
     */
    public Piece getPiece(Position position) {
        return this.squares[position.getRow()][position.getColumn()];
    }

    /**
     * Place une pièce à une position spécifique sur l'échiquier.
     *
     * @param position la position où placer la pièce.
     * @param piece la pièce à placer.
     */
    public void setPiece(Position position, Piece piece) {
        this.squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Déplace une pièce d'une position à une autre sur l'échiquier.
     *
     * @param from la position de départ.
     * @param to la position d'arrivée.
     */
    public void movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        Piece capturedPiece = getPiece(to);

        this.moveHistory.add(new Move(piece, from, to, capturedPiece));

        this.squares[to.getRow()][to.getColumn()] = piece;
        this.squares[from.getRow()][from.getColumn()] = null;
        piece.setPosition(to);
        piece.setMoved();

        // Gère le roque
        if (piece.getType() == PieceType.KING && Math.abs(to.getColumn() - from.getColumn()) == 2) {
            this.handleCastling(from, to);
        }
    }

    /**
     * Gère le roque en déplaçant la tour et le roi.
     *
     * @param kingFrom la position de départ du roi.
     * @param kingTo la position d'arrivée du roi.
     */
    private void handleCastling(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        // Roque côté roi
        if (kingTo.getColumn() == 6) {
            Position rookFrom = new Position(row, 7);
            Position rookTo = new Position(row, 5);
            Piece rook = getPiece(rookFrom);
            this.squares[rookTo.getRow()][rookTo.getColumn()] = rook;
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = null;
            rook.setPosition(rookTo);
            rook.setMoved();
        }
        // Roque côté dame
        else if (kingTo.getColumn() == 2) {
            Position rookFrom = new Position(row, 0);
            Position rookTo = new Position(row, 3);
            Piece rook = getPiece(rookFrom);
            this.squares[rookTo.getRow()][rookTo.getColumn()] = rook;
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = null;
            rook.setPosition(rookTo);
            rook.setMoved();
        }
    }

    /**
     * Annule le dernier mouvement effectué sur l'échiquier.
     */
    public void undoLastMove() {
        if (this.moveHistory.isEmpty())
            return;

        Move lastMove = this.moveHistory.remove(moveHistory.size() - 1);

        // Rétablit les pièces à leurs positions d'origine
        this.squares[lastMove.getFrom().getRow()][lastMove.getFrom().getColumn()] = lastMove.getPiece();
        this.squares[lastMove.getTo().getRow()][lastMove.getTo().getColumn()] = lastMove.getCapturedPiece();

        // Met à jour la position de la pièce
        lastMove.getPiece().setPosition(lastMove.getFrom());

        // Gère l'annulation du roque
        if (lastMove.getPiece().getType() == PieceType.KING &&
            Math.abs(lastMove.getTo().getColumn() - lastMove.getFrom().getColumn()) == 2) {
                this.undoCastling(lastMove.getFrom(), lastMove.getTo());
        }

        // Réinitialise le statut de déplacement si c'était le premier mouvement de la pièce
        if (lastMove.isFirstMove()) {
            lastMove.getPiece().setMoved(false);
        }
    }

    /**
     * Annule le roque en rétablissant les positions de la tour et du roi.
     *
     * @param kingFrom la position de départ du roi.
     * @param kingTo la position d'arrivée du roi.
     */
    private void undoCastling(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        // Annule le roque côté roi
        if (kingTo.getColumn() == 6) {
            Position rookFrom = new Position(row, 7);
            Position rookTo = new Position(row, 5);
            Piece rook = getPiece(rookTo);
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = rook;
            this.squares[rookTo.getRow()][rookTo.getColumn()] = null;
            rook.setPosition(rookFrom);
            rook.setMoved(false);
        }
        // Annule le roque côté dame
        else if (kingTo.getColumn() == 2) {
            Position rookFrom = new Position(row, 0);
            Position rookTo = new Position(row, 3);
            Piece rook = getPiece(rookTo);
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = rook;
            this.squares[rookTo.getRow()][rookTo.getColumn()] = null;
            rook.setPosition(rookFrom);
            rook.setMoved(false);
        }
    }

    /**
     * Trouve la position du roi d'une couleur spécifique sur l'échiquier.
     *
     * @param color la couleur du roi à trouver.
     * @return la position du roi.
     * @throws IllegalStateException si le roi n'est pas trouvé.
     */
    public Position findKing(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = this.squares[row][col];
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        throw new IllegalStateException("King not found for color: " + color);
    }

    /**
     * Vérifie si une position est attaquée par une pièce d'une couleur spécifique.
     *
     * @param position la position à vérifier.
     * @param attackingColor la couleur de la pièce attaquante.
     * @return true si la position est attaquée, false sinon.
     */
    public boolean isUnderAttack(Position position, Color attackingColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = this.squares[row][col];
                if (piece != null && piece.getColor() == attackingColor) {
                    if (piece.isValidMove(position)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Récupère l'historique des mouvements effectués sur l'échiquier.
     *
     * @return l'historique des mouvements.
     */
    public ArrayList<Move> getMoveHistory() {
        return this.moveHistory;
    }

    /**
     * Vérifie si une position est valide sur l'échiquier.
     *
     * @param position la position à vérifier.
     * @return true si la position est valide, false sinon.
     */
    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < 8 &&
               position.getColumn() >= 0 && position.getColumn() < 8;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'échiquier.
     *
     * @return une représentation sous forme de chaîne de caractères de l'échiquier.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 7; row >= 0; row--) {
            sb.append(row + 1).append(" ");
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                sb.append(piece == null ? "." : piece.toString()).append(" ");
            }
            sb.append("\n");
        }
        sb.append("  a b c d e f g h");
        return sb.toString();
    }
}
