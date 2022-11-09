package sandbox;

import graphics.G;
import graphics.Window;
import music.UC;
import reaction.*;

import java.awt.*;
import java.awt.event.MouseEvent;
public class reactionTest extends Window {
    static {
        new Layer("BACK");
        new Layer("FORE");
    }
    public reactionTest() {
        super("ReactionTest", UC.initialWindowHeight, UC.initialWindowWidth);
        //先用shapeTrainer进行训练SW-SW，关闭后运行reactionTest, 画出类似训练的图形，得到方块
        Reaction.initialReactions.addReaction(new Reaction("SW-SW") {
            public int bid(Gesture g) {return 0;}
            public void act(Gesture g) {new Box(g.vs);}
        });
    }

    public static void main(String[] args) {
        (PANEL = new reactionTest()).launch();
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        g.setColor(Color.blue);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }

    public void mousePressed(MouseEvent me){Gesture.AREA.dn(me.getX(), me.getY());repaint();}
    public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(), me.getY());repaint();}
    public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(), me.getY());repaint();}

    //------------------Box--------------------
    public static class Box extends Mass{
        public G.VS vs;
        public Color c = G.rndColor();
        public Box(G.VS vs){
            super("BACK");
            this.vs = vs;

            //-----------Delete--------
            //在方块上画出S-S的图形，删除功能
            addReaction(new Reaction("S-S"){
                public int bid(Gesture g) {
                    int x = g.vs.xM(), y = g.vs.yL();
                    if(Box.this.vs.hit(x,y)){
                        return Math.abs(x - Box.this.vs.xM());
                    }else {
                        return UC.noBid;
                    }
                }
                public void act(Gesture g) {
                    Box.this.deleteMass();
                }
            });

            //---------DOT---------
            //点击方块，改变颜色
            addReaction(new Reaction("DOT"){
                public int bid(Gesture g) {
                    int x = g.vs.xM(), y = g.vs.yL();
                    if(Box.this.vs.hit(x,y)){
                        return Math.abs(x - Box.this.vs.xM());
                    }else {
                        return UC.noBid;
                    }
                }
                public void act(Gesture g) {
                    Box.this.c = G.rndColor();
                }
            });
        }
        public void show(Graphics g){
            vs.fill(g, c);

        }
    }
}

