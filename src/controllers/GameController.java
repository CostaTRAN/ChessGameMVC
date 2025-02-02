package controllers;

import views.GameView;

import java.util.ArrayList;
import java.util.Random;

import models.Board;
import models.Color;
import models.Game;
import models.Move;
import models.Piece;
import models.PieceType;
import models.Position;

/**
 * La classe GameController gère les commandes et les mouvements dans un jeu d'échecs.
 * Elle interagit avec la vue du jeu (GameView) et le modèle du jeu (Game) pour traiter les commandes
 * et mettre à jour l'état du jeu.
 */
public class GameController implements ChessController {
    private GameView view;

    /**
     * Constructeur de la classe GameController.
     *
     * @param view la vue du jeu à associer à ce contrôleur.
     */
    public GameController(GameView view) {
        Game.getGameInstance();
        this.view = view;
    }

    /**
     * Gère une commande dans le jeu d'échecs.
     *
     * @param command la commande à gérer, représentée sous forme de chaîne de caractères.
     */
    @Override
    public void handleCommand(String command) {
        switch (command) {
            case "quit", "exit":
                System.out.println("Game ended by player.");
                Game.getGameInstance().stopGame(this.view);
                return;
            case "help":
                this.view.showHelp();
                break;
            case "undo":
                if(Game.getGameInstance().getBoard().getMoveHistory().size() == 0) {
                    this.view.showError("No moves to undo!");
                    return;
                }
                Game.getGameInstance().undoMove();
                break;
            default:
                handleMove(command);
                break;
        }
    }

    /**
     * Gère un mouvement dans le jeu d'échecs.
     *
     * @param moveCommand la commande de mouvement à gérer, représentée sous forme de chaîne de caractères.
     */
    public void handleMove(String moveCommand) {
        String[] parts = moveCommand.split(" ");
        if (parts.length != 2) {
            this.view.showError("Invalid command format! Use 'e2 e4' format.");
            return;
        }

        try {
            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);

            if (!from.isValid() || !to.isValid()) {
                this.view.showError("Invalid position!");
                return;
            }

            if (Game.getGameInstance().makeMove(from, to)) {
                Piece piece = Game.getGameInstance().getBoard().getPiece(to);
                if (piece.getType() == PieceType.PAWN && (to.getRow() == 0 || to.getRow() == 7)) {
                    PieceType promotionType = (this.view).askPromotionPawn();
                    Game.getGameInstance().promotePawn(to, promotionType);
                }
                this.updateGameStatus();
            } else {
                this.view.showError("Invalid move!");
            }
        } catch (IllegalArgumentException e) {
            this.view.showError("Invalid position format! Use 'e2 e4' format.");
        }
    }

    /**
     * Parse une position à partir d'une chaîne de caractères.
     *
     * @param pos la position à parser, représentée sous forme de chaîne de caractères.
     * @return la position parsée.
     * @throws IllegalArgumentException si le format de la position est invalide.
     */
    private Position parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position format");
        }
        int column = pos.charAt(0) - 'a';
        int row = Character.getNumericValue(pos.charAt(1)) - 1;
        return new Position(row, column);
    }

    /**
     * Met à jour l'état du jeu et affiche les messages appropriés.
     */
    private void updateGameStatus() {
        switch (Game.getStatus()) {
            case CHECK:
                this.view.showMessage("Check!");
                break;
            case CHECKMATE:
                this.view.showMessage("\nGame Over!\nCheckmate! " +
                (Game.getCurrentTurn() == Color.WHITE ? "Black" : "White") + " wins!");
                break;
            case STALEMATE:
                this.view.showMessage("Stalemate! Game is drawn.");
                break;
            case ACTIVE:
                break;
            default:
                break;
        }
    }

    /**
     * Joue un coup aléatoire pour l'IA.
     */
    public void playRandomMove() {
        // Obtient l'instance unique du jeu
        Game game = Game.getGameInstance();
        // Obtient l'échiquier du jeu
        Board board = game.getBoard();
        // Liste pour stocker les coups légaux
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        // Détermine la couleur de l'IA en fonction de la couleur du joueur
        Color aiColor = (Game.getPlayerColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        // Parcourt toutes les positions de l'échiquier pour trouver les pièces de l'IA
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Position from = new Position(fromRow, fromCol);
                Piece piece = board.getPiece(from);
                // Vérifie si la pièce appartient à l'IA
                if (piece != null && piece.getColor() == aiColor) {
                    // Parcourt toutes les positions possibles pour déplacer la pièce
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Position to = new Position(toRow, toCol);
                            // Vérifie si le mouvement est valide
                            if (piece.isValidMove(to)) {
                                // Effectue le mouvement temporairement
                                board.movePiece(from, to);
                                // Trouve la position du roi de l'IA
                                Position kingPos = board.findKing(aiColor);
                                // Vérifie si le roi est en échec après le mouvement
                                boolean inCheck = board.isUnderAttack(kingPos, getOppositeColor(aiColor));
                                // Annule le mouvement temporaire
                                board.undoLastMove();

                                // Si le roi n'est pas en échec, ajoute le mouvement à la liste des coups légaux
                                if (!inCheck) {
                                    legalMoves.add(new Move(piece, from, to, board.getPiece(to)));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Si aucun coup légal n'est trouvé, affiche un message d'erreur
        if (legalMoves.isEmpty()) {
            this.view.showError("AI couldn't find any legal moves!");
            return;
        }

        // Sélectionne un coup aléatoire parmi les coups légaux
        var random = new Random();
        Move selectedMove = legalMoves.get(random.nextInt(legalMoves.size()));

        // Effectue le coup sélectionné
        boolean moveSuccess = game.makeMove(selectedMove.getFrom(), selectedMove.getTo());
        if (moveSuccess) {
            // Affiche un message indiquant le coup joué par l'IA
            this.view.showMessage("AI (" + aiColor + ") has moved " +
                selectedMove.getPiece() + " from " + selectedMove.getFrom() + " to " + selectedMove.getTo());

            // Vérifie si la pièce déplacée est un pion et si elle doit être promue
            Piece movedPiece = board.getPiece(selectedMove.getTo());
            if (movedPiece.getType() == PieceType.PAWN && (selectedMove.getTo().getRow() == 0 || selectedMove.getTo().getRow() == 7)) {
                game.promotePawn(selectedMove.getTo(), PieceType.QUEEN);
            }

            // Met à jour le statut du jeu
            updateGameStatus();
        } else {
            // Affiche un message d'erreur si le coup n'a pas pu être joué
            this.view.showError("AI couldn't play the move!");
        }
    }


    /**
     * Retourne la couleur opposée à la couleur donnée.
     *
     * @param color la couleur donnée.
     * @return la couleur opposée à la couleur donnée.
     */
    private Color getOppositeColor(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
}
