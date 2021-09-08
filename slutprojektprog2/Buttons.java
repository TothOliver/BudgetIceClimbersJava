/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slutprojektprog2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Innehåller funktioner och variabler som angår alla knappar i menyn.
 * @author Totheman
 */
public class Buttons {
    private int x, y, width = 100, height = 90;
    private final String BUTTONTEXT;
    private final Color COLOR;
    
    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     * @param x
     * @param y
     * @param buttonText
     * @param color 
     */
    public Buttons(int x, int y, String buttonText, Color color) {
        this.width = buttonText.length() * 10 + 100;
        this.x = x - this.width / 2;
        this.y = y;
        this.BUTTONTEXT = buttonText;
        this.COLOR = color;
    }
    
    /**
     * Sätter ut färg, bred, höjd, x-koordinat, y-koordinat och typsnitt 
     * på alla knappar.
     * @param g hämtar grafiska egenskaper
     */
    public void render(Graphics g){
        g.setColor(COLOR);
        g.fillRect(x, y, width, height);
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", 1, 20));
        g.drawString(BUTTONTEXT, x + 50 - (int) (BUTTONTEXT.length() * 1.5), y + 50);
    }
    
    /**
     * 
     * @return Rectanglens x-koordinat, y-koordinat, bred och jhöjd.
     */
    public Rectangle boundArea(){
        return new Rectangle(x, y, width, height);
    }
}
