import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class Nodo {
    enum Tipo { ROUTER, SWITCH, PC }
    String id;
    Tipo tipo;
    Point posicion;

    public Nodo(String id, Tipo tipo, Point posicion) {
        this.id = id;
        this.tipo = tipo;
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return id;
    }
}