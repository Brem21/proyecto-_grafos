import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class CanvasPanel extends JPanel {
    Grafo grafo;
    List<Nodo> ruta = new ArrayList<>();

    public CanvasPanel(Grafo grafo) {
        this.grafo = grafo;
        setBackground(Color.WHITE);
    }

    public void setRuta(List<Nodo> ruta) {
        this.ruta = ruta;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Nodo nodo : grafo.nodos.values()) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(nodo.posicion.x - 15, nodo.posicion.y - 15, 30, 30);
            g2.setColor(Color.BLACK);
            g2.drawString(nodo.id, nodo.posicion.x - 10, nodo.posicion.y);
        }
        for (List<Arco> arcos : grafo.adyacencia.values()) {
            for (Arco arco : arcos) {
                g2.setColor(Color.BLACK);
                g2.drawLine(arco.origen.posicion.x, arco.origen.posicion.y,
                        arco.destino.posicion.x, arco.destino.posicion.y);
                int mx = (arco.origen.posicion.x + arco.destino.posicion.x) / 2;
                int my = (arco.origen.posicion.y + arco.destino.posicion.y) / 2;
                g2.drawString(String.valueOf(arco.peso), mx, my);
            }
        }
        g2.setColor(Color.RED);
        for (int i = 0; i < ruta.size() - 1; i++) {
            Point p1 = ruta.get(i).posicion;
            Point p2 = ruta.get(i + 1).posicion;
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}

// ==================== MainFrame.java ====================
public class NetworkTopologyAnalyzer extends JFrame {
    Grafo grafo = new Grafo();
    CanvasPanel canvas = new CanvasPanel(grafo);




    public NetworkTopologyAnalyzer() {
        setTitle("Network Topology Analyzer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);

        grafo.inicializarNodos1a20();


        JPanel panel = new JPanel();
        JButton addNodo = new JButton("Agregar Nodo");
        JButton addArco = new JButton("Agregar Arco");
        JButton calcRuta = new JButton("Calcular Ruta");

        addNodo.addActionListener(e -> agregarNodo());
        addArco.addActionListener(e -> agregarArco());
        calcRuta.addActionListener(e -> calcularRuta());

        panel.add(addNodo);
        panel.add(addArco);
        panel.add(calcRuta);
        add(panel, BorderLayout.SOUTH);
    }

    private void agregarNodo() {
        String id = JOptionPane.showInputDialog("ID Nodo:");
        String[] tipos = {"ROUTER", "SWITCH", "PC"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Tipo Nodo:",
                "Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        int x = Integer.parseInt(JOptionPane.showInputDialog("Pos X:"));
        int y = Integer.parseInt(JOptionPane.showInputDialog("Pos Y:"));
        Nodo nodo = new Nodo(id, Nodo.Tipo.valueOf(tipo), new Point(x, y));
        grafo.agregarNodo(nodo);
        canvas.repaint();
    }

    private void agregarArco() {
        String origen = JOptionPane.showInputDialog("Nodo origen:");
        String destino = JOptionPane.showInputDialog("Nodo destino:");
        double peso = Double.parseDouble(JOptionPane.showInputDialog("Peso:"));
        grafo.agregarArco(origen, destino, peso);
        canvas.repaint();
    }

    private void calcularRuta() {
        String origen = JOptionPane.showInputDialog("Nodo origen:");
        String destino = JOptionPane.showInputDialog("Nodo destino:");
        String[] ataques = {"DDOS", "BRUTE_FORCE", "MITM"};
        Ataque tipo = Ataque.valueOf((String) JOptionPane.showInputDialog(this, "Tipo Ataque:",
                "Ataque", JOptionPane.QUESTION_MESSAGE, null, ataques, ataques[0]));
        List<Nodo> ruta = grafo.rutaOptima(origen, destino, tipo);
        canvas.setRuta(ruta);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NetworkTopologyAnalyzer().setVisible(true);
        });
    }
}
