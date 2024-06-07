/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author domtr
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bus extends JFrame {

    private Tablero tablero;
    private JButton[][] botones;
    private JPanel panelJuego;
    private boolean juegoTerminado;
    private Timer timer;
    private int segundosTranscurridos;
    private JLabel labelTiempo;

    public Bus() {
        Menu();
    }

    private void Menu() {
        String[] opciones = {"Fácil (10 minas)", "Medio (40 Minas)", "Difícil (99 Minas)"};
        int op = JOptionPane.showOptionDialog(
                this,
                "Seleccione un nivel:",
                "Buscaminas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (op) {
            case 0:
                iJuego(8, 8, 10);
                break;
            case 1:
                iJuego(16, 16, 40);
                break;
            case 2:
                iJuego(16, 30, 99);
                break;
            default:
                System.exit(0);
        }
    }

    private void iJuego(int filas, int columnas, int minas) {
        if (panelJuego != null) {
            getContentPane().remove(panelJuego);
        }

        // Eliminar como el panel 
        if (labelTiempo != null) {
            getContentPane().remove(labelTiempo);
        }

        tablero = new Tablero(filas, columnas, minas);
        botones = new JButton[filas][columnas];
        juegoTerminado = false;
        panelJuego = new JPanel(new GridLayout(filas, columnas));

        // Tiempo
        segundosTranscurridos = 0;
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                segundosTranscurridos++;
                labelTiempo.setText("Tiempo: " + segundosTranscurridos + " segundos");
            }
        });
        timer.start();

        // Panel de juego
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setFont(new Font("Arial", Font.PLAIN, 12));
                botones[i][j].setBackground(Color.LIGHT_GRAY);
                final int x = i;
                final int y = j;
                botones[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Celda(x, y);
                    }
                });
                botones[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            marcarCelda(x, y);
                        }
                    }
                });
                panelJuego.add(botones[i][j]);
            }
        }

        // Agregar todo al JFrame
        getContentPane().add(panelJuego, BorderLayout.CENTER);

        labelTiempo = new JLabel("Tiempo: 0 segundos", JLabel.CENTER);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        getContentPane().add(labelTiempo, BorderLayout.NORTH);

        setTitle("Buscaminas");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void Celda(int fila, int columna) {
        if (juegoTerminado) {
            MenuDespues();
            return;
        }
        Juego celda = tablero.getCelda(fila, columna);
        if (celda.estaMarcada()) {
            return;
        } else if (celda.tieneMina()) {
            botones[fila][columna].setText("*BOM*");
            botones[fila][columna].setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "¡BOOOOOM! Has perdido");
            revelarTablero();
            juegoTerminado = true;
            timer.stop(); 
        } else {
            revelarCelda(fila, columna);
            if (tablero.estaCompleto()) {
                JOptionPane.showMessageDialog(this, "Buena buena, GANASTE");
                revelarTablero();
                juegoTerminado = true;
                timer.stop(); 
            }
        }
    }

    private void MenuDespues() {
        String[] opciones = {"Volver al menú principal", "Salir"};
        int op1 = JOptionPane.showOptionDialog(
                this,
                "¿Qué desea hacer?",
                "Juego terminado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (op1) {
            case 0:
                getContentPane().remove(panelJuego);
                reiniciarTiempo();
                Menu();
                break;
            default:
                System.exit(0);
        }
    }

    private void reiniciarTiempo() {
        segundosTranscurridos = 0;
        labelTiempo.setText("Tiempo: " + segundosTranscurridos + " segundos");
    }

    private void revelarCelda(int fila, int columna) {
        if (fila < 0 || fila >= tablero.getFilas() || columna < 0 || columna >= tablero.getColumnas() || tablero.getCelda(fila, columna).estaRevelada()) {
            return;
        }
        Juego celda = tablero.getCelda(fila, columna);
        celda.revelar();
        botones[fila][columna].setText(String.valueOf(celda.getMinasCercanas()));
        botones[fila][columna].setBackground(Color.WHITE);

        if (celda.getMinasCercanas() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revelarCelda(fila + i, columna + j);
                    }
                }
            }
        }
    }

    private void marcarCelda(int fila, int columna) {
        if (juegoTerminado) {
            return;
        }
        Juego celda = tablero.getCelda(fila, columna);
        if (!celda.estaRevelada()) {
            if (!celda.estaMarcada()) {
                celda.marcar();
                botones[fila][columna].setText("M");
                botones[fila][columna].setBackground(Color.BLUE);
            } else {
                celda.desmarcar();
                botones[fila][columna].setText("");
                botones[fila][columna].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void revelarTablero() {
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Juego celda = tablero.getCelda(i, j);
                if (celda.tieneMina()) {
                    botones[i][j].setText("*");
                    botones[i][j].setBackground(Color.RED);
                } else {
                    botones[i][j].setText(String.valueOf(celda.getMinasCercanas()));
                    botones[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Bus();
            }
        });
    }
}
