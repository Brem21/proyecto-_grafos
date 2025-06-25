import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkTopologyAnalyzer extends JFrame {
    Grafo grafo = new Grafo();
    List<Nodo> ruta = new ArrayList<>();
    JPanel panelDibujo;

    public NetworkTopologyAnalyzer() {
        setTitle("Network Topology Analyzer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



        panelDibujo = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar nodos
                for (Nodo nodo : grafo.nodos.values()) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillOval(nodo.posicion.x - 15, nodo.posicion.y - 15, 30, 30);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(nodo.posicion.x - 15, nodo.posicion.y - 15, 30, 30);
                    g2.drawString(nodo.id, nodo.posicion.x - 10, nodo.posicion.y - 20);
                }

                // Dibujar arcos
                for (List<Arco> arcos : grafo.adyacencia.values()) {
                    for (Arco arco : arcos) {
                        drawArrow(g2, arco.origen.posicion, arco.destino.posicion, Color.BLACK, 1);
                        int mx = (arco.origen.posicion.x + arco.destino.posicion.x) / 2;
                        int my = (arco.origen.posicion.y + arco.destino.posicion.y) / 2;
                        g2.drawString(String.valueOf(arco.peso), mx, my);
                    }
                }

                // Dibujar ruta del ataque en rojo con flechas dirigidas
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3));
                for (int i = 0; i < ruta.size() - 1; i++) {
                    Point p1 = ruta.get(i).posicion;
                    Point p2 = ruta.get(i + 1).posicion;
                    drawArrow(g2, p1, p2, Color.RED, 3);
                }
                g2.setStroke(new BasicStroke(1));
            }

            private void drawArrow(Graphics2D g2, Point from, Point to, Color color, int thickness) {
                g2.setColor(color);
                g2.setStroke(new BasicStroke(thickness));
                g2.drawLine(from.x, from.y, to.x, to.y);

                double dx = to.x - from.x;
                double dy = to.y - from.y;
                double angle = Math.atan2(dy, dx);
                int arrowSize = 10;

                int x1 = (int) (to.x - arrowSize * Math.cos(angle - Math.PI / 6));
                int y1 = (int) (to.y - arrowSize * Math.sin(angle - Math.PI / 6));
                int x2 = (int) (to.x - arrowSize * Math.cos(angle + Math.PI / 6));
                int y2 = (int) (to.y - arrowSize * Math.sin(angle + Math.PI / 6));

                Polygon flecha = new Polygon();
                flecha.addPoint(to.x, to.y);
                flecha.addPoint(x1, y1);
                flecha.addPoint(x2, y2);
                g2.fill(flecha);
            }
        };

        panelDibujo.setBackground(Color.WHITE);
        add(panelDibujo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton addNodo = new JButton("Agregar Nodo");
        JButton addArco = new JButton("Agregar Arco");
        JButton calcRuta = new JButton("Calcular Ruta");

        addNodo.addActionListener(e -> agregarNodo());
        addArco.addActionListener(e -> agregarArco());
        calcRuta.addActionListener(e -> calcularRuta());

        panelBotones.add(addNodo);
        panelBotones.add(addArco);
        panelBotones.add(calcRuta);
        add(panelBotones, BorderLayout.SOUTH);
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
        panelDibujo.repaint();
    }

    private void agregarArco() {
        String origen = JOptionPane.showInputDialog("Nodo origen:");
        String destino = JOptionPane.showInputDialog("Nodo destino:");
        double peso = Double.parseDouble(JOptionPane.showInputDialog("Peso:"));
        grafo.agregarArco(origen, destino, peso);
        panelDibujo.repaint();
    }

    private void calcularRuta() {
        String origen = JOptionPane.showInputDialog("Nodo origen:");
        String destino = JOptionPane.showInputDialog("Nodo destino:");
        String[] ataques = {"DDOS", "BRUTE_FORCE", "MITM"};
        Ataque tipo = Ataque.valueOf((String) JOptionPane.showInputDialog(this, "Tipo Ataque:",
                "Ataque", JOptionPane.QUESTION_MESSAGE, null, ataques, ataques[0]));
        ruta = grafo.rutaOptima(origen, destino, tipo);
        panelDibujo.repaint();
        ruta = grafo.rutaOptima(origen, destino, tipo);
        panelDibujo.repaint();
        mostrarDetallesAtaque(); // Muestra la ventana automáticamente




    }
    private void mostrarDetallesAtaque() {
        Ataque[] ataques = Ataque.values();
        Random rand = new Random();


        StringBuilder detalles = new StringBuilder("Detalles del Ataque Simulado:\n\n");

        for (Ataque ataque : ataques) {
            int probabilidad = rand.nextInt(61) + 40;
            String impacto;
            if (probabilidad >= 85) {
                impacto = "Alto";
            } else if (probabilidad >= 60) {
                impacto = "Medio";
            } else {
                impacto = "Bajo";
            }
            detalles.append(String.format("- %s: Probabilist %d%%, Impacto: %s\n", ataque.name(), (Object) probabilidad, impacto));
        }

        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Detalles del Ataque Simulado", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkTopologyAnalyzer().setVisible(true));
    }
}

