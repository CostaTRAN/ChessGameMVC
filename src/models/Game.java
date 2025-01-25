package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import views.GameModeSelectionView;
import views.Observer;

public class Game implements Subject {
    private Board board;
    private Color currentTurn;
    private GameStatus status;
    private List<String> moveNotation;
    private static String DEFAULT_SAVE_FILE = "chess_save.dat";
    private List<Observer> observers;
    private static Game gameInstance;

    private Game() {
        this.board = Board.getBoardInstance();
        this.currentTurn = Color.WHITE;
        this.status = GameStatus.ACTIVE;
        this.moveNotation = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public static Game getGameInstance() {
        if(Game.gameInstance == null) {
            Game.gameInstance = new Game();
        }
        return Game.gameInstance;
    }

    public boolean makeMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        if (!piece.isValidMove(to, board)) {
            return false;
        }

        // Make the move temporarily
        board.movePiece(from, to);
        
        // Check if the move puts or leaves own king in check
        Position kingPos = board.findKing(currentTurn);
        if (board.isUnderAttack(kingPos, getOppositeColor(currentTurn))) {
            board.undoLastMove();
            return false;
        }

        // Record the move in notation
        recordMove(board.getMoveHistory().get(board.getMoveHistory().size() - 1));
        
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
        currentTurn = getOppositeColor(currentTurn);
    }

    private void updateGameStatus() {
        Color oppositeColor = getOppositeColor(currentTurn);
        Position kingPos = board.findKing(currentTurn);
        
        if (board.isUnderAttack(kingPos, oppositeColor)) {
            status = GameStatus.CHECK;
            
            if (isCheckmate()) {
                status = GameStatus.CHECKMATE;
            }
        } else if (isStalemate()) {
            status = GameStatus.STALEMATE;
        } else {
            status = GameStatus.ACTIVE;
        }
    }

    private boolean isCheckmate() {
        return !hasLegalMoves() && status == GameStatus.CHECK;
    }

    private boolean isStalemate() {
        return !hasLegalMoves() && status != GameStatus.CHECK;
    }

    private boolean hasLegalMoves() {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Position from = new Position(fromRow, fromCol);
                Piece piece = board.getPiece(from);
                
                if (piece != null && piece.getColor() == currentTurn) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Position to = new Position(toRow, toCol);
                            
                            if (piece.isValidMove(to, board)) {
                                // Try move
                                board.movePiece(from, to);
                                Position kingPos = board.findKing(currentTurn);
                                boolean inCheck = board.isUnderAttack(kingPos, getOppositeColor(currentTurn));
                                board.undoLastMove();
                                
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
            moveNotation.size() / 2 + 1, move.toString());
        moveNotation.add(notation);
    }

    public void saveGame() throws IOException {
        saveGame(DEFAULT_SAVE_FILE);
    }

    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    public static Game loadGame() throws IOException, ClassNotFoundException {
        return loadGame(DEFAULT_SAVE_FILE);
    }

    public static Game loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Game) in.readObject();
        }
    }

    public void undoMove() {
        board.undoLastMove();
        switchTurn();
        updateGameStatus();
        notifyObservers();
        if (!moveNotation.isEmpty()) {
            moveNotation.remove(moveNotation.size() - 1);
        }
    }

    public void promotePawn(Position position, PieceType newType) {
        Board board = Game.getGameInstance().getBoard();
        Piece pawn = board.getPiece(position);
        if (pawn.getType() == PieceType.PAWN) {
            Piece promotedPiece = new Piece(newType, pawn.getColor(), position);
            board.setPiece(position, promotedPiece);
        }
    }

    public void stopGame(Observer observer) {
        removeObserver(observer);
        addObserver(new GameModeSelectionView());
        Game.resetGameInstance();
        notifyObservers();
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
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
    public GameStatus getStatus() { return status; }
    public List<String> getMoveNotation() { return new ArrayList<>(moveNotation); }
}