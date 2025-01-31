package models;

/**
 * La classe Position représente une position sur un échiquier.
 * Elle contient des informations sur la ligne et la colonne de la position.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructeur de la classe Position.
     *
     * @param row la ligne de la position.
     * @param column la colonne de la position.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Vérifie si la position est valide sur un échiquier.
     *
     * @return true si la position est valide, false sinon.
     */
    public boolean isValid() {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    /**
     * Retourne la ligne de la position.
     *
     * @return la ligne de la position.
     */
    public int getRow() { return this.row; }

    /**
     * Retourne la colonne de la position.
     *
     * @return la colonne de la position.
     */
    public int getColumn() { return this.column; }

    /**
     * Vérifie si cette position est égale à un autre objet.
     *
     * @param obj l'objet à comparer.
     * @return true si les positions sont égales, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.column == other.column;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la position.
     *
     * @return une représentation sous forme de chaîne de caractères de la position.
     */
    @Override
    public String toString() {
        return String.format("%c%d", (char)('a' + this.column), this.row + 1);
    }
}
