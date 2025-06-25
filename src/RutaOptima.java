import java.util.*;

class RutaOptima {
    public static List<Nodo> dijkstra(Grafo g, String inicio, String fin) {
        Map<Nodo, Double> dist = new HashMap<>();
        Map<Nodo, Nodo> prev = new HashMap<>();
        Set<Nodo> visitados = new HashSet<>();
        PriorityQueue<Nodo> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        Nodo start = g.nodos.get(inicio);
        Nodo end = g.nodos.get(fin);

        for (Nodo nodo : g.nodos.values()) {
            dist.put(nodo, Double.valueOf(Double.MAX_VALUE));
        }

        dist.put(start, Double.valueOf(0.0));
        pq.add(start);

        while (!pq.isEmpty()) {
            Nodo actual = pq.poll();
            if (!visitados.add(actual)) continue;

            // Obtener vecinos como si el grafo fuera no dirigido
            List<Arco> vecinos = new ArrayList<>(g.adyacencia.getOrDefault(actual, new ArrayList<>()));
            for (Map.Entry<Nodo, List<Arco>> entry : g.adyacencia.entrySet()) {
                for (Arco arco : entry.getValue()) {
                    if (arco.destino.equals(actual)) {
                        vecinos.add(new Arco(arco.destino, arco.origen, arco.peso)); // camino inverso
                    }
                }
            }

            for (Arco arco : vecinos) {
                double nuevoDist = dist.get(actual) + arco.peso;
                if (nuevoDist < dist.get(arco.destino)) {
                    dist.put(arco.destino, Double.valueOf(nuevoDist));
                    prev.put(arco.destino, actual);
                    pq.add(arco.destino);
                }
            }
        }

        List<Nodo> camino = new LinkedList<>();
        for (Nodo at = end; at != null; at = prev.get(at)) {
            camino.add(0, at);
        }

        return camino;
    }
}