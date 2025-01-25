package controllers;

import views.ChessView;
import views.GameView;

import java.io.IOException;

import models.Color;
import models.Game;
import models.Piece;
import models.PieceType;
import models.Position;

public class GameController {
    private ChessView view;

    public GameController(ChessView view) {
        Game.getGameInstance();
        this.view = view;
    }

    public void handleCommand(String command) {
        if (command.equals("undo")) {
            Game.getGameInstance().undoMove();
            return;
        }

        if (command.equals("save")) {
            try {
                Game.getGameInstance().saveGame("chess_save.dat");
                view.showMessage("Game saved successfully!");
            } catch (IOException e) {
                view.showError("Error saving game: " + e.getMessage());
            }
            return;
        }

        if (command.equals("load")) {
            try {
                Game.getGameInstance().loadGame("chess_save.dat");
                // Update game reference and refresh view
                view.update();
                view.showMessage("Game loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                view.showError("Error loading game: " + e.getMessage());
            }
            return;
        }

        handleMove(command);
    }

    public void handleMove(String moveCommand) {
        String[] parts = moveCommand.split(" ");
        if (parts.length != 2) {
            view.showError("Invalid command format! Use 'e2 e4' format.");
            return;
        }

        try {
            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);

            if (!from.isValid() || !to.isValid()) {
                view.showError("Invalid position!");
                return;
            }

            if (Game.getGameInstance().makeMove(from, to)) {
                Piece piece = Game.getGameInstance().getBoard().getPiece(to);
                if (piece.getType() == PieceType.PAWN && (to.getRow() == 0 || to.getRow() == 7)) {
                    PieceType promotionType = ((GameView) view).askPromotionPawn();
                    Game.getGameInstance().promotePawn(to, promotionType);
                }
                updateGameStatus();
            } else {
                view.showError("Invalid move!");
            }
        } catch (IllegalArgumentException e) {
            view.showError("Invalid position format! Use 'e2 e4' format.");
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
            case CHECK -> view.showMessage("Check!");
            case CHECKMATE -> view.showMessage("Checkmate! " + 
                (Game.getGameInstance().getCurrentTurn() == Color.WHITE ? "Black" : "White") + " wins!");
            case STALEMATE -> view.showMessage("Stalemate! Game is drawn.");
            case ACTIVE -> {} // No message needed
        }
    }
}