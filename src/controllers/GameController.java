package controllers;

import views.GameView;
import models.Color;
import models.Game;
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
        switch (Game.getGameInstance().getStatus()) {
            case CHECK:
                this.view.showMessage("Check!");
                break;
            case CHECKMATE:
                this.view.showMessage("\nGame Over!\nCheckmate! " +
                (Game.getGameInstance().getCurrentTurn() == Color.WHITE ? "Black" : "White") + " wins!");
                break;
            case STALEMATE:
                this.view.showMessage("Stalemate! Game is drawn.");
                break;
            case ACTIVE:
                break;
        }
    }
}
