package models;

public class Piece {
    private PieceType type;
    private Color color;
    private Position position;
    private boolean hasMoved;

    public Piece(PieceType type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    public boolean isValidMove(Position newPosition, Board board) {
        if (!newPosition.isValid() || position.equals(newPosition)) {
            return false;
        }

        Piece destinationPiece = board.getPiece(newPosition);
        if (destinationPiece != null && destinationPiece.getColor() == color) {
            return false;
        }

        return switch (type) {
            case PAWN -> isValidPawnMove(newPosition, board);
            case ROOK -> isValidRookMove(newPosition, board);
            case KNIGHT -> isValidKnightMove(newPosition, board);
            case BISHOP -> isValidBishopMove(newPosition, board);
            case QUEEN -> isValidQueenMove(newPosition, board);
            case KING -> isValidKingMove(newPosition, board);
        };
    }

    private boolean isValidPawnMove(Position newPosition, Board board) {
        int direction = (color == Color.WHITE) ? 1 : -1;
        int rowDiff = newPosition.getRow() - position.getRow();
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());
        
        // Forward movement
        if (colDiff == 0) {
            // Single square forward
            if (rowDiff == direction && board.getPiece(newPosition) == null) {
                return true;
            }
            // Initial two square move
            if (!hasMoved && rowDiff == 2 * direction) {
                Position intermediate = new Position(position.getRow() + direction, position.getColumn());
                return board.getPiece(intermediate) == null && board.getPiece(newPosition) == null;
            }
        }
        // Capture
        else if (colDiff == 1 && rowDiff == direction) {
            Piece targetPiece = board.getPiece(newPosition);
            return targetPiece != null && targetPiece.getColor() != color;
            // Note: En passant could be implemented here
        }
        return false;
    }

    private boolean isValidRookMove(Position newPosition, Board board) {
        return isLinearMove(newPosition, board, true, false);
    }

    private boolean isValidKnightMove(Position newPosition, Board board) {
        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private boolean isValidBishopMove(Position newPosition, Board board) {
        return isLinearMove(newPosition, board, false, true);
    }

    private boolean isValidQueenMove(Position newPosition, Board board) {
        return isLinearMove(newPosition, board, true, true);
    }

    private boolean isValidKingMove(Position newPosition, Board board) {
        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());
        
        // Normal move
        if (rowDiff <= 1 && colDiff <= 1) {
            return true;
        }
        
        // Castling
        if (!hasMoved && rowDiff == 0 && colDiff == 2) {
            int rookColumn = (newPosition.getColumn() > position.getColumn()) ? 7 : 0;
            Position rookPos = new Position(position.getRow(), rookColumn);
            Piece rook = board.getPiece(rookPos);
            
            if (rook != null && rook.getType() == PieceType.ROOK && !rook.hasMoved()) {
                // Check if path is clear
                int step = (rookColumn == 7) ? 1 : -1;
                for (int col = position.getColumn() + step; col != rookColumn; col += step) {
                    if (board.getPiece(new Position(position.getRow(), col)) != null) {
                        return false;
                    }
                }
                // Note: Should also check if king passes through check
                return true;
            }
        }
        return false;
    }

    private boolean isLinearMove(Position newPosition, Board board, boolean straight, boolean diagonal) {
        int rowDiff = newPosition.getRow() - position.getRow();
        int colDiff = newPosition.getColumn() - position.getColumn();
        
        boolean isStraightMove = rowDiff == 0 || colDiff == 0;
        boolean isDiagonalMove = Math.abs(rowDiff) == Math.abs(colDiff);
        
        if (!(straight && isStraightMove) && !(diagonal && isDiagonalMove)) {
            return false;
        }
        
        // Check path for obstacles
        int rowStep = Integer.compare(rowDiff, 0);
        int colStep = Integer.compare(colDiff, 0);
        
        Position current = new Position(position.getRow() + rowStep, position.getColumn() + colStep);
        while (!current.equals(newPosition)) {
            if (board.getPiece(current) != null) {
                return false;
            }
            current = new Position(current.getRow() + rowStep, current.getColumn() + colStep);
        }
        return true;
    }

    // Getters and Setters
    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public PieceType getType() { return type; }
    public boolean hasMoved() { return hasMoved; }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public void setMoved() {
        this.hasMoved = true;
    }

    @Override
    public String toString() {
        String colorPrefix = color == Color.WHITE ? "W" : "B";
        String symbol = switch (type) {
            case PAWN -> "P";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        return colorPrefix + symbol;
    }
}