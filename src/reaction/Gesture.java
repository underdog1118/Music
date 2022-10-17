package reaction;

import graphics.G;
import music.I;

public class Gesture {
    public static I.Area AREA = new I.Area() {
        public boolean hit(int x, int y){return true;}
        public void dn(int x , int y) {Ink.BUFFER.dn(x, y);}
        public void drag(int x, int y){Ink.BUFFER.drag(x, y);}
        public void up(int x, int y){
            Ink.BUFFER.add(x,y);
            Ink ink = new Ink();
            Gesture gest = Gesture.getNew(ink);    //can fail if unrecognized
            Ink.BUFFER.clear();
            if (gest != null){
                Reaction r = Reaction.best(gest);  //can fail
                if (r != null){
                    r.act(gest);
                }
            }
        }
    };
    public Shape shape;
    public G.VS vs;

    public Gesture(Shape shape, G.VS vs){
        this.shape = shape;
        this.vs = vs;
    }
    public static Gesture getNew(Ink ink){  //can return null
        Shape s = Shape.recognize(ink);
        return (s == null) ? null : new Gesture(s, ink.vs);
    }

}