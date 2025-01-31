package models;

import java.util.ArrayList;

public class Board {
    private Piece[][] squares;
    private ArrayList<Move> moveHistory;

    public Board() {
        this.squares = new Piece[8][8];
        this.moveHistory = new ArrayList<Move>();
        initializeBoard();
    }

    public void initializeBoard() {
        // Place pawns
        for (int col = 0; col < 8; col++) {
            this.squares[1][col] = new Piece(PieceType.PAWN, Color.WHITE, new Position(1, col));
            this.squares[6][col] = new Piece(PieceType.PAWN, Color.BLACK, new Position(6, col));
        }

        // Place major pieces
        // White pieces
        placePiece(0, 0, PieceType.ROOK, Color.WHITE);
        placePiece(0, 1, PieceType.KNIGHT, Color.WHITE);
        placePiece(0, 2, PieceType.BISHOP, Color.WHITE);
        placePiece(0, 3, PieceType.QUEEN, Color.WHITE);
        placePiece(0, 4, PieceType.KING, Color.WHITE);
        placePiece(0, 5, PieceType.BISHOP, Color.WHITE);
        placePiece(0, 6, PieceType.KNIGHT, Color.WHITE);
        placePiece(0, 7, PieceType.ROOK, Color.WHITE);

        // Black pieces
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

    private void placePiece(int row, int col, PieceType type, Color color) {
        Position pos = new Position(row, col);
        this.squares[row][col] = new Piece(type, color, pos);
    }

    public Piece getPiece(Position position) {
        return this.squares[position.getRow()][position.getColumn()];
    }

    public void setPiece(Position position, Piece piece) {
        this.squares[position.getRow()][position.getColumn()] = piece;
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        Piece capturedPiece = getPiece(to);
        
        this.moveHistory.add(new Move(piece, from, to, capturedPiece));
        
        this.squares[to.getRow()][to.getColumn()] = piece;
        this.squares[from.getRow()][from.getColumn()] = null;
        piece.setPosition(to);
        piece.setMoved();

        // Handle castling
        if (piece.getType() == PieceType.KING && Math.abs(to.getColumn() - from.getColumn()) == 2) {
            this.handleCastling(from, to);
        }
    }

    private void handleCastling(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        // Kingside castling
        if (kingTo.getColumn() == 6) {
            Position rookFrom = new Position(row, 7);
            Position rookTo = new Position(row, 5);
            Piece rook = getPiece(rookFrom);
            this.squares[rookTo.getRow()][rookTo.getColumn()] = rook;
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = null;
            rook.setPosition(rookTo);
            rook.setMoved();
        }
        // Queenside castling
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

    public void undoLastMove() {
        if (this.moveHistory.isEmpty())
            return;
        
        Move lastMove = this.moveHistory.remove(moveHistory.size() - 1);
        
        // Restore pieces to their original positions
        this.squares[lastMove.getFrom().getRow()][lastMove.getFrom().getColumn()] = lastMove.getPiece();
        this.squares[lastMove.getTo().getRow()][lastMove.getTo().getColumn()] = lastMove.getCapturedPiece();
        
        // Update piece position
        lastMove.getPiece().setPosition(lastMove.getFrom());
        
        // Handle castling undo
        if (lastMove.getPiece().getType() == PieceType.KING && 
            Math.abs(lastMove.getTo().getColumn() - lastMove.getFrom().getColumn()) == 2) {
                this.undoCastling(lastMove.getFrom(), lastMove.getTo());
        }
        
        // Reset moved status if it was the piece's first move
        if (lastMove.isFirstMove()) {
            lastMove.getPiece().setMoved(false);
        }
    }

    private void undoCastling(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        // Undo kingside castling
        if (kingTo.getColumn() == 6) {
            Position rookFrom = new Position(row, 7);
            Position rookTo = new Position(row, 5);
            Piece rook = getPiece(rookTo);
            this.squares[rookFrom.getRow()][rookFrom.getColumn()] = rook;
            this.squares[rookTo.getRow()][rookTo.getColumn()] = null;
            rook.setPosition(rookFrom);
            rook.setMoved(false);
        }
        // Undo queenside castling
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

    public ArrayList<Move> getMoveHistory() {
        return this.moveHistory;
    }

    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < 8 && 
               position.getColumn() >= 0 && position.getColumn() < 8;
    }

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