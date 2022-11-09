package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass {
    public Staff staff;
    public int line;
    public Time time;
    public Glyph forceGlyph = null;
    public Stem stem = null;
    public boolean wrongSide = false;
    public Head(Staff staff, int x, int y) {
        super("Note");
        this.staff = staff;
        time = staff.sys.getTime(x);
        time.heads.add(this);
        int H = staff.fmt.H;
//        int top = staff.yTop() - H;
//        line = ((y - top + H/2) / H) - 1;
        this.line = staff.lineOfY(y);
//        System.out.println("line" + line);

        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(), y1 = gesture.vs.yL(), y2 = gesture.vs.yM();
                int w = Head.this.w(), hY = Head.this.y();
                if (y1 >  y || y2 < y) {return UC.noBid;}
                int hLeft = Head.this.time.x, hRight = hLeft + w;
                if (x < hLeft - w || x > hRight + w) {return UC.noBid;}
                if (x < hLeft + w/2) {return hLeft - x;}
                if (x > hRight - w/2) {return x - hRight;}
                return UC.noBid;
            }
            @Override
            public void act(Gesture gesture) {
                int x = gesture.vs.xM(), y1 = gesture.vs.yL(), y2 = gesture.vs.yM();
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean up = x > (t.x + w/2);
                if (Head.this.stem == null) {
                    t.stemHeads(up, y1, y2);
                }else {
                    t.unStemHeads(y1, y2);
                }
            }
        });
    }

    public int w() {return 24 * staff.fmt.H / 10;}
    public void show(Graphics g) {
        int H = staff.fmt.H;
        g.setColor(stem == null ? Color.red : Color.black);
        (forceGlyph != null ? forceGlyph : normalGlyph()).showAt(g, H, x(), y());
    }

    public int x() {
        // Stub
        return time.x;
    }
    public int y() { return staff.yLine(line);}
    public Glyph normalGlyph() {
        // stub
        return Glyph.HEAD_Q;
    }
    public void deleteHead() {
        time.heads.remove(this);
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this);
            if (stem.heads.size() == 0) {stem.deleteStem();}
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null) {unStem();}
        s.heads.add(this);
        stem = s;
    }


    //------------------List--------------------
    public static class  List extends ArrayList<Head>{}
}
