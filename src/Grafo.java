import java.awt.*;
import java.util.*;
import java.util.List;

class Grafo {
    Map<String, Nodo> nodos = new HashMap<>();
    Map<Nodo, List<Arco>> adyacencia = new HashMap<>();

    public void agregarNodo(Nodo nodo) {
        nodos.put(nodo.id, nodo);
        adyacencia.put(nodo, new ArrayList<>());
    }

    public void agregarArco(String origenId, String destinoId, double peso) {
        Nodo origen = nodos.get(origenId);
        Nodo destino = nodos.get(destinoId);
        if (origen != null && destino != null) {
            adyacencia.get(origen).add(new Arco(origen, destino, peso));
        }
    }

    public List<Nodo> rutaOptima(String src, String dst, Ataque tipo) {
        return RutaOptima.dijkstra(this, src, dst);
    }


    public void inicializarNodos1a20() {
        for (int i = 1; i <= 20; i++) {
            String id = String.valueOf(i);
            if (!nodos.containsKey(id)) {
                Nodo nodo = new Nodo(id, Nodo.Tipo.ROUTER, new Point(0, 0));
                agregarNodo(nodo);
            }
        }
    }

}