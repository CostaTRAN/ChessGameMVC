package models;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }

    public boolean isValid() {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return row == other.row && column == other.column;
    }

    @Override
    public String toString() {
        return String.format("%c%d", (char)('a' + column), row + 1);
    }
}
