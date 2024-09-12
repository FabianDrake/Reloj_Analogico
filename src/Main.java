import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.time.LocalTime;

public class Main extends JPanel implements Runnable {
    private Image fondo;
    private Thread hilo;

    public Main() {
        // Cargar imagen de fondo
        fondo = new ImageIcon("src/imagenes/fondo.jpg").getImage();
        setPreferredSize(new Dimension(800, 600));

        // Iniciar el hilo para las manecillas
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar imagen de fondo
        g2d.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

        // Dibujar el círculo con degradado o textura
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 200;

        GradientPaint gradient = new GradientPaint(centerX - radius, centerY - radius, Color.BLUE,
                centerX + radius, centerY + radius, Color.ORANGE);
        g2d.setPaint(gradient);
        g2d.fill(new Ellipse2D.Double(centerX - radius, centerY - radius, 2 * radius, 2 * radius));

        // Dibujar los números del reloj
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i - 3) * 30);  // Posición de los números
            int x = (int) (centerX + Math.cos(angle) * (radius - 30));
            int y = (int) (centerY + Math.sin(angle) * (radius - 30));
            g2d.drawString(String.valueOf(i), x - 10, y + 10);
        }

        // Obtener la hora actual
        LocalTime time = LocalTime.now();
        int second = time.getSecond();
        int minute = time.getMinute();
        int hour = time.getHour();

        // Dibujar las manecillas del reloj
        dibujarManecilla(g2d, centerX, centerY, second, 6, radius - 20, Color.RED);  // Segundos
        dibujarManecilla(g2d, centerX, centerY, minute, 6, radius - 50, Color.BLACK); // Minutos
        dibujarManecilla(g2d, centerX, centerY, (hour % 12) * 5 + minute / 12, 30, radius - 80, Color.BLACK); // Horas
    }

    // Método para dibujar cada manecilla
    private void dibujarManecilla(Graphics2D g2d, int cx, int cy, int value, int degreeUnit, int length, Color color) {
        double angle = Math.toRadians((value - 15) * degreeUnit);
        int x = (int) (cx + Math.cos(angle) * length);
        int y = (int) (cy + Math.sin(angle) * length);

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(color);
        g2d.drawLine(cx, cy, x, y);
    }

    @Override
    public void run() {
        while (true) {
            // Redibujar cada segundo
            repaint();
            try {
                Thread.sleep(1000);  // Espera 1 segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Practica 05 - Reloj Analogico - Arnoldo Fabian Talavera Felix");
        Main reloj = new Main();
        frame.add(reloj);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
