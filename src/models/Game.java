package models;

import java.util.ArrayList;
import views.GameModeSelectionView;
import views.Observer;

/**
 * La classe Game représente une partie d'échecs.
 * Elle gère l'état du jeu, les mouvements des pièces, et les interactions avec les observateurs.
 */
public class Game implements Subject {
    private Board board;
    private Color currentTurn;
    private GameStatus status;
    private ArrayList<String> moveNotation;
    private ArrayList<Observer> observers;
    private static Game gameInstance;

    /**
     * Constructeur privé de la classe Game.
     * Initialise l'échiquier, le tour actuel, le statut du jeu, la notation des mouvements et les observateurs.
     */
    private Game() {
        this.board = new Board();
        this.currentTurn = Color.WHITE;
        this.status = GameStatus.ACTIVE;
        this.moveNotation = new ArrayList<String>();
        this.observers = new ArrayList<Observer>();
    }

    /**
     * Retourne l'instance unique de la classe Game.
     * Si l'instance n'existe pas, elle est créée.
     *
     * @return l'instance unique de la classe Game.
     */
    public static Game getGameInstance() {
        if (Game.gameInstance == null) {
            Game.gameInstance = new Game();
        }
        return Game.gameInstance;
    }

    /**
     * Effectue un mouvement sur l'échiquier.
     *
     * @param from la position de départ.
     * @param to la position d'arrivée.
     * @return true si le mouvement est valide et effectué, false sinon.
     */
    public boolean makeMove(Position from, Position to) {
        Piece piece = this.board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        if (!piece.isValidMove(to)) {
            return false;
        }

        // Effectue le mouvement temporairement
        this.board.movePiece(from, to);

        // Vérifie si le mouvement met ou laisse le roi en échec
        Position kingPos = this.board.findKing(currentTurn);
        if (this.board.isUnderAttack(kingPos, getOppositeColor(currentTurn))) {
            this.board.undoLastMove();
            return false;
        }

        // Enregistre le mouvement en notation
        this.recordMove(this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1));

        // Change de tour et met à jour le statut du jeu
        this.switchTurn();
        this.updateGameStatus();
        this.notifyObservers();

        return true;
    }

    /**
     * Retourne la couleur opposée à celle donnée.
     *
     * @param color la couleur dont on veut obtenir l'opposé.
     * @return la couleur opposée.
     */
    private Color getOppositeColor(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Change le tour actuel.
     */
    private void switchTurn() {
        currentTurn = this.getOppositeColor(currentTurn);
    }

    /**
     * Met à jour le statut du jeu.
     */
    private void updateGameStatus() {
        Color oppositeColor = this.getOppositeColor(currentTurn);
        Position kingPos = this.board.findKing(currentTurn);

        if (this.board.isUnderAttack(kingPos, oppositeColor)) {
            this.status = GameStatus.CHECK;

            if (isCheckmate()) {
                this.status = GameStatus.CHECKMATE;
            }
        } else if (isStalemate()) {
            this.status = GameStatus.STALEMATE;
        } else {
            this.status = GameStatus.ACTIVE;
        }
    }

    /**
     * Vérifie si le joueur actuel est en échec et mat.
     *
     * @return true si le joueur est en échec et mat, false sinon.
     */
    private boolean isCheckmate() {
        return !hasLegalMoves() && this.status == GameStatus.CHECK;
    }

    /**
     * Vérifie si le joueur actuel est en pat.
     *
     * @return true si le joueur est en pat, false sinon.
     */
    private boolean isStalemate() {
        return !hasLegalMoves() && this.status != GameStatus.CHECK;
    }

    /**
     * Vérifie si le joueur actuel a des mouvements légaux.
     *
     * @return true si le joueur a des mouvements légaux, false sinon.
     */
    private boolean hasLegalMoves() {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Position from = new Position(fromRow, fromCol);
                Piece piece = this.board.getPiece(from);

                if (piece != null && piece.getColor() == currentTurn) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Position to = new Position(toRow, toCol);

                            if (piece.isValidMove(to)) {
                                // Essaie le mouvement
                                this.board.movePiece(from, to);
                                Position kingPos = this.board.findKing(currentTurn);
                                boolean inCheck = this.board.isUnderAttack(kingPos, getOppositeColor(currentTurn));
                                this.board.undoLastMove();

                                if (!inCheck) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Enregistre un mouvement en notation.
     *
     * @param move le mouvement à enregistrer.
     */
    private void recordMove(Move move) {
        String notation = String.format("%d. %s",
            this.moveNotation.size() / 2 + 1, move.toString());
        this.moveNotation.add(notation);
    }

    /**
     * Annule le dernier mouvement effectué.
     */
    public void undoMove() {
        this.board.undoLastMove();
        this.switchTurn();
        this.updateGameStatus();
        this.notifyObservers();
        if (!this.moveNotation.isEmpty()) {
            this.moveNotation.remove(this.moveNotation.size() - 1);
        }
    }

    /**
     * Promotion d'un pion en une autre pièce.
     *
     * @param position la position du pion à promouvoir.
     * @param newType le type de la nouvelle pièce.
     */
    public void promotePawn(Position position, PieceType newType) {
        Piece pawn = this.board.getPiece(position);
        if (pawn.getType() == PieceType.PAWN) {
            Piece promotedPiece = new Piece(newType, pawn.getColor(), position);
            this.board.setPiece(position, promotedPiece);
        }
    }

    /**
     * Arrête la partie et réinitialise l'instance du jeu.
     *
     * @param observer l'observateur à retirer.
     */
    public void stopGame(Observer observer) {
        this.removeObserver(observer);
        this.addObserver(new GameModeSelectionView());
        Game.resetGameInstance();
        this.notifyObservers();
    }

    /**
     * Réinitialise l'instance unique de la classe Game.
     */
    public static void resetGameInstance() {
        Game.gameInstance = null;
    }

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Retire un observateur de la liste des observateurs.
     *
     * @param observer l'observateur à retirer.
     */
    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notifie tous les observateurs de la mise à jour du jeu.
     */
    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    /**
     * Retourne l'échiquier.
     *
     * @return l'échiquier.
     */
    public Board getBoard() { return this.board; }

    /**
     * Retourne la couleur du joueur dont c'est le tour.
     *
     * @return la couleur du joueur dont c'est le tour.
     */
    public Color getCurrentTurn() { return this.currentTurn; }

    /**
     * Retourne le statut actuel du jeu.
     *
     * @return le statut actuel du jeu.
     */
    public GameStatus getStatus() { return this.status; }

    /**
     * Retourne la notation des mouvements.
     *
     * @return la notation des mouvements.
     */
    public ArrayList<String> getMoveNotation() { return this.moveNotation; }
}
