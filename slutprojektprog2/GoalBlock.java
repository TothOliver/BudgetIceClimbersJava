/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slutprojektprog2;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Innehåller funktioner och variabler som angår alla målblock.
 * @author Totheman
 */
public class GoalBlock extends Block{

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     * @param width målblockets bred
     * @param height målblockets höjd
     * @param x målblockets x-koordinat
     * @param y målblockets y-koordinat
     * @param color målblockets färg
     */
    public GoalBlock(int width, int height, int x, int y, Color color) {
        super(width, height, x, y, color);
    }

    @Override
    public Rectangle boundArea(){
        return new Rectangle(getX(), getY(), width, height);
    }
    
}
