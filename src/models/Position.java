package models;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean isValid() {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.column == other.column;
    }

    @Override
    public String toString() {
        return String.format("%c%d", (char)('a' + this.column), this.row + 1);
    }
}
