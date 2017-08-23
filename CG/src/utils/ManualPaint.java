/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Poligono;
import Model.Vertice;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

/**
 *
 * @author JFPS
 */
public class ManualPaint {
    private boolean map[][];
    private Poligono pol;
    
    public ManualPaint(int height, int width) {
        map = new boolean[height][width];
        
        for (int i=0; i<height; i++)
            for (int j=0; j<width; j++)
                map[i][j] = false;
    }
    
    private void paintPixel(Graphics g, int x, int y){
        g.fillRect(x, y, 1, 1);
        map[x][y] = true;
    }
    
    private boolean filled(int x, int y){
        return map[x][y];
    }
    
    //https://softwareengineering.stackexchange.com/questions/105580/what-is-the-best-bucket-fill-algorithm
    //https://stackoverflow.com/questions/23983465/is-there-a-fill-function-for-arbitrary-shapes-in-javafx
    //https://www.tutorialspoint.com/computer_graphics/polygon_filling_algorithm.htm
    public void floodFill(Graphics g, Poligono p){
        pol = p;
        Stack<Vertice> stack = new Stack<>();
        stack.push(p.getCentroideDaMedia());

        while (!stack.isEmpty()) {
            Vertice point = stack.pop();
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (filled(x, y)) {
                continue;
            }

            paintPixel(g, x, y);

            selectivePush(stack, x - 1, y - 1);
            selectivePush(stack, x - 1, y    );
            selectivePush(stack, x - 1, y + 1);
            selectivePush(stack, x    , y + 1);
            selectivePush(stack, x + 1, y + 1);
            selectivePush(stack, x + 1, y    );
            selectivePush(stack, x + 1, y - 1);
            selectivePush(stack, x,     y - 1);
        }
    }
        
    // tolerance for color matching (on a scale of 0 to 1);
    private final double E = 0.3;

    private void selectivePush(Stack<Vertice> stack, int x, int y) {
        if (x < 0 || x > map.length ||
            y < 0 || y > map[0].length) {
            return;
        }
        
        if (!pol.contains(new Vertice(x,y)))
            return;

        stack.push(new Vertice(x, y));
    }
}
