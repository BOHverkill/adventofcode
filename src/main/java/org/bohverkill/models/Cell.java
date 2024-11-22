package org.bohverkill.models;

public record Cell(int row, int column) {
    public static Cell of(int row, int column) {
        return new Cell(row, column);
    }
}