/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author domtr
 */
public class Juego {

    private boolean tieneMina;
    private boolean revelada;
    private boolean marcada;
    private int minasCercanas;

    public Juego() {
        // Al inicio suponemos que no hay minas, que ningun espacio esta marcado
        // Que ningun espacio esta reveleado y que no hay minas cercas
        this.tieneMina = false;
        this.revelada = false;
        this.marcada = false;
        this.minasCercanas = 0;
    }
    
    // Método para saber si hay una mina en el espacio 
    public boolean tieneMina() {
        return this.tieneMina;
    }

    // Método para colocar una mina en el espacio
    public void colocarMina() {
        this.tieneMina = true;
    }

    // Método para verificar si el espacio está revelado
    public boolean estaRevelada() {
        return this.revelada;
    }

    // Método para revelar un espacio
    public void revelar() {
        this.revelada = true;
    }

    // Método para verificar si el espacio está marcado
    public boolean estaMarcada() {
        return this.marcada;
    }

    // Método para marcar un espacio
    public void marcar() {
        this.marcada = true;
    }

    // Método para desmarcar un espacio
    public void desmarcar() {
        this.marcada = false;
    }

    // Método para obetener el numero de minas cercas
    public int getMinasCercanas() {
        return this.minasCercanas;
    }

    // Método para establecer el número de minas cercas
    public void setMinasCercanas(int minasCercanas) {
        this.minasCercanas = minasCercanas;
    }
}
