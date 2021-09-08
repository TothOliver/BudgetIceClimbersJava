/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slutprojektprog2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Innehåller funktioner och variabler som angår alla block.
 * @author Totheman
 */
public class Block {
    protected final int width , height;
    private int x, y;
    private Color color;
    
    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     * @param width blockets bred
     * @param height blockets höjd
     * @param x blockets x-koordinat
     * @param y blockets y-koordiant
     * @param color blockets färg
     */
    public Block(int width, int height, int x, int y,  Color color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void render(Graphics g){
        g.setColor(getColor());
        g.fillRect(getX(), getY(), width, height);

    }
    
    /**
     * @return Rectanglens x-koordinat, y-koordinat, bred och jhöjd
     */
    public Rectangle boundArea(){
        return new Rectangle(getX(), getY(), width, height);
    }

    
}
