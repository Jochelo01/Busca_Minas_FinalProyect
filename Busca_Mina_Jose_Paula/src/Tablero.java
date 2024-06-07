/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author domtr
 */
public class Tablero {

    private Juego[][] espacio;
    private int filas;
    private int columnas;
    private int minas;

    public Tablero(int filas, int columnas, int minas) {
        this.filas = filas;
        this.columnas = columnas;
        espacio = new Juego[filas][columnas];
        iniciarTablero(minas);
    }

    private void iniciarEspacios(int fila, int columna) {
        if (fila >= filas) {
            return;
        }//Si sobrepasa el numero de filas listo
        if (columna >= columnas) {
            iniciarEspacios(fila + 1, 0);//Columna mayor entonces vamos a la siguiente fila y nos devolvemos a la columna 0
            return;
        }
        espacio[fila][columna] = new Juego();
        iniciarEspacios(fila, columna + 1);
    }//Llamamos a la siguiente columna

    private void colocarMinas(int minasRestantes) {
        if (minasRestantes <= 0) {
            return;
        }
        int fila = (int) (Math.random() * filas);
        int columna = (int) (Math.random() * columnas);
        if (!espacio[fila][columna].tieneMina()) {
            espacio[fila][columna].colocarMina();
            colocarMinas(minasRestantes - 1);//Llamamos a el mismo y retsamos las minas que faltan
        } else {
            colocarMinas(minasRestantes);
        }
    }

    private void calcularMinasCercanas(int fila, int columna) {
        if (fila >= filas) {
            return;
        }
        if (columna >= columnas) {
            calcularMinasCercanas(fila + 1, 0);
            return;
        }
        if (!espacio[fila][columna].tieneMina()) {
            int minasCercanas = contarMinasCercanas(fila, columna);
            espacio[fila][columna].setMinasCercanas(minasCercanas);
        }
        calcularMinasCercanas(fila, columna + 1);
    }//Calculamos las minas en esa posición y vamos a la de al lado

    private int contarMinasCercanas(int fila, int columna) {
        int minasCercanas = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int filaCercana = fila + i;
                int columnaCercana = columna + j;
                if (filaCercana >= 0 && filaCercana < filas && columnaCercana >= 0 && columnaCercana < columnas) {
                    if (espacio[filaCercana][columnaCercana].tieneMina()) {
                        minasCercanas++;
                    }
                }
            }
        }
        return minasCercanas;
    }

    private void iniciarTablero(int minas) {
        iniciarEspacios(0, 0);
        colocarMinas(minas);
        calcularMinasCercanas(0, 0);
    }

    public void revelarCelda(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || espacio[fila][columna].estaRevelada()) {
            return;
        }

        espacio[fila][columna].revelar();

        // Si no tiene minas cerca, las muestro también
        if (espacio[fila][columna].getMinasCercanas() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revelarCelda(fila + i, columna + j);
                    }
                }
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    public int getMinas() {
        return minas;
    }

    public Juego getCelda(int fila, int columna) {
        return espacio[fila][columna];
    }

    public void marcarCelda(int fila, int columna) {
        espacio[fila][columna].marcar();
    }

    public void imprimirTablero() {
        System.out.print("  ");
        for (int j = 0; j < columnas; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < columnas; j++) {
                if (espacio[i][j].estaRevelada()) {
                    if (espacio[i][j].tieneMina()) {
                        System.out.print("**");
                    } else {
                        System.out.print(espacio[i][j].getMinasCercanas() + " ");
                    }
                } else if (espacio[i][j].estaMarcada()) {
                    System.out.print("M ");
                } else {
                    System.out.print("? ");
                }
            }
            System.out.println();
        }
    }

    public boolean estaCompleto() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!espacio[i][j].tieneMina() && !espacio[i][j].estaRevelada()) {
                    return false;
                }
            }
        }
        return true;
    }
}
