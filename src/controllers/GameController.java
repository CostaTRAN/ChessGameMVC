package controllers;

import views.GameView;

import models.Color;
import models.Game;
import models.Piece;
import models.PieceType;
import models.Position;

public class GameController {
    private GameView view;

    public GameController(GameView view) {
        Game.getGameInstance();
        this.view = view;
    }

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
                updateGameStatus();
            } else {
                this.view.showError("Invalid move!");
            }
        } catch (IllegalArgumentException e) {
            this.view.showError("Invalid position format! Use 'e2 e4' format.");
        }
    }

    private Position parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position format");
        }
        int column = pos.charAt(0) - 'a';
        int row = Character.getNumericValue(pos.charAt(1)) - 1;
        return new Position(row, column);
    }

    private void updateGameStatus() {
        switch (Game.getGameInstance().getStatus()) {
            case CHECK -> this.view.showMessage("Check!");
            case CHECKMATE -> this.view.showMessage("\nGame Over!\nCheckmate! " + 
                (Game.getGameInstance().getCurrentTurn() == Color.WHITE ? "Black" : "White") + " wins!");
            case STALEMATE -> this.view.showMessage("Stalemate! Game is drawn.");
            case ACTIVE -> {} // No message needed
        }
    }
}