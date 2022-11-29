package sandbox;

import graphics.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Collapse extends Window implements ActionListener {
    public static final int NC = 13, NR = 15, W = 50, H = 30; //num of col, num of row
    public static int xM = 100, yM = 100;
    public static Random random = new Random();
    public static int rnd(int max) {return random.nextInt(max);}
    public static Color[] colors =
            {Color.lightGray, Color.cyan, Color.green, Color.yellow, Color.red, Color.pink};
    public static Timer timer; //swing timer
    public int [][] grid = new int[NC][NR];
    public int bricksRemaining;
    public Collapse() {
        super("Collapse", 1000, 750);
        rndColor(3);
        timer = new Timer(20, this); //30 Milliseconds = 30 times/second
        timer.start();
    }
    public void paintComponent(Graphics g) {
        g.setColor(colors[0]);
        g.fillRect(0, 0, 5000, 5000);
        if (slideCol()) {xM += W / 2;};
        bubbleAll();
        showGrid(g);
        showRemaining(g);
    }
    public static void main(String[] args) {
        (PANEL = new Collapse()).launch();
    }

    public void rndColor(int k) {
        for (int c = 0; c < NC; c++) {
            for (int r = 0; r < NR; r++) {
                grid[c][r] = rnd(k) + 1; // +1 to never generate ligth gray
            }
        }
        bricksRemaining = NC * NR;
    }
    public void showRemaining(Graphics g) {
        String str = "bricks remaining: " + bricksRemaining;
        g.setColor(Color.black);
        g.drawString(str, 70, 70);
    }

    public void showGrid(Graphics g) {
        for (int c = 0; c < NC; c++) {
            for (int r = 0; r < NR; r++) {
                g.setColor(colors[grid[c][r]]);
                g.fillRect(x(c), y(r), W, H);
            }
        }
    }

    public int x(int c) {return xM + c * W;}
    public int y(int r) {return yM + r * H;}
    public int c(int x) {return (x - xM) / W;}
    public int r(int y) {return (y - yM) / H;}
    public void mouseClicked(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        if (x < xM || y < yM) {return;} // outside margin
        int r = r(y), c = c(x);
        if (r < NR && c < NC) {
            cellClicked(c, r);
        }
    }
    private void cellClicked(int c, int r) {
//        grid[c][r] = 0;
        if (infectable(c, r)) {
            int v = grid[c][r];
            infect(c, r, v);
        }
        repaint();
    }
    public void infect(int c, int r, int v) { //v is the value
        if(grid[c][r] != v) {return;}
        grid[c][r] = 0;
        if(r > 0) {infect(c, r-1, v);}
        if(c > 0) {infect(c - 1, r, v);}
        if(r < NR - 1) {infect(c , r + 1, v);}
        if(c < NC - 1) {infect(c + 1, r, v);}  //search four direction color anf infect
        bricksRemaining--;
    }
    public boolean infectable(int c, int r) {
        int v = grid[c][r];
        if (v == 0) {return false;}
        if (r > 0) {if (grid[c][r-1] == v) {return true;}}
        if (c > 0) {if (grid[c-1][r] == v) {return true;}}
        if (r < NR - 1) {if (grid[c][r+1] == v) {return true;}}
        if (c < NC - 1) {if (grid[c+1][r] == v) {return true;}}
        return false;
    }
    public boolean bubble(int c) {
        boolean res = false;
        for (int r = NR - 1; r > 0; r--) {
            if (grid[c][r] == 0 && grid[c][r-1] != 0) {
                grid[c][r] = grid[c][r-1];
                grid[c][r-1] = 0;
                res = true;
                return res;
            }
        }
        return res;
    }
    public void bubbleAll() {
        for (int c = 0; c < NC; c++) {
//            while (bubble(c)) {
//            }
            bubble(c);
        }
    }
    public boolean colIsEmpty(int c) {
        for(int r = 0; r < NR; r++) {
            if (grid[c][r] != 0) {
                return false;
            }
        }
        return true;
    }
    public void swapCol(int c) {    //c is non-empty , c-1 is empty (copy c to c-1)
        for (int r = 0; r < NR; r++) {
            grid[c-1][r] = grid[c][r];
            grid[c][r] = 0;
        }
    }
    public boolean slideCol() {
        boolean res = false;
        for (int c = 1; c < NC; c++) {
            if (colIsEmpty(c-1) && !colIsEmpty(c)) {
                swapCol(c);
                res = true;
            }
        }
        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
