package models;

import java.util.ArrayList;

import views.GameModeSelectionView;
import views.Observer;

public class Game implements Subject {
    private Board board;
    private Color currentTurn;
    private GameStatus status;
    private ArrayList<String> moveNotation;
    private ArrayList<Observer> observers;
    private static Game gameInstance;

    private Game() {
        this.board = new Board();
        this.currentTurn = Color.WHITE;
        this.status = GameStatus.ACTIVE;
        this.moveNotation = new ArrayList<String>();
        this.observers = new ArrayList<Observer>();
    }

    public static Game getGameInstance() {
        if(Game.gameInstance == null) {
            Game.gameInstance = new Game();
        }
        return Game.gameInstance;
    }

    public boolean makeMove(Position from, Position to) {
        Piece piece = this.board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        if (!piece.isValidMove(to)) {
            return false;
        }

        // Make the move temporarily
        this.board.movePiece(from, to);
        
        // Check if the move puts or leaves own king in check
        Position kingPos = this.board.findKing(currentTurn);
        if (this.board.isUnderAttack(kingPos, getOppositeColor(currentTurn))) {
            this.board.undoLastMove();
            return false;
        }

        // Record the move in notation
        recordMove(this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1));
        
        // Switch turns and update game status
        switchTurn();
        updateGameStatus();
        notifyObservers();

        return true;
    }

    private Color getOppositeColor(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void switchTurn() {
        currentTurn = this.getOppositeColor(currentTurn);
    }

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

    private boolean isCheckmate() {
        return !hasLegalMoves() && this.status == GameStatus.CHECK;
    }

    private boolean isStalemate() {
        return !hasLegalMoves() && this.status != GameStatus.CHECK;
    }

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
                                // Try move
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

    private void recordMove(Move move) {
        String notation = String.format("%d. %s", 
            this.moveNotation.size() / 2 + 1, move.toString());
        this.moveNotation.add(notation);
    }

    public void undoMove() {
        this.board.undoLastMove();
        this.switchTurn();
        this.updateGameStatus();
        this.notifyObservers();
        if (!this.moveNotation.isEmpty()) {
            this.moveNotation.remove(this.moveNotation.size() - 1);
        }
    }

    public void promotePawn(Position position, PieceType newType) {
        Piece pawn = this.board.getPiece(position);
        if (pawn.getType() == PieceType.PAWN) {
            Piece promotedPiece = new Piece(newType, pawn.getColor(), position);
            this.board.setPiece(position, promotedPiece);
        }
    }

    public void stopGame(Observer observer) {
        this.removeObserver(observer);
        this.addObserver(new GameModeSelectionView());
        Game.resetGameInstance();
        this.notifyObservers();
    }

    public static void resetGameInstance() {
        Game.gameInstance = null;
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    // Getters
    public Board getBoard() { return this.board; }
    public Color getCurrentTurn() { return this.currentTurn; }
    public GameStatus getStatus() { return this.status; }
    public ArrayList<String> getMoveNotation() { return this.moveNotation; }
}