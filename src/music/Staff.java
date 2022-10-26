package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;

import static sandbox.Music.PAGE;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff;
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff, Staff.Fmt fmt){
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.fmt = fmt;

        addReaction(new Reaction("S-S") { //BarLine
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(), y1 =gesture.vs.yL(), y2 = gesture.vs.yM();
                if (x <PAGE.margins.left || x > PAGE.margins.right){
                    return UC.noBid;
                }
                System.out.println("Top" + y1 + " " + Staff.this.yTop()); //test
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 50) ? d + UC.Bar2MarginSnap: UC.noBid;
            }
            public void act(Gesture gesture) {
                int x = gesture.vs.xM();
                if (Math.abs(x - PAGE.margins.right) < UC.Bar2MarginSnap){ //close the right boundary of staff
                    x = PAGE.margins.right;
                }
                new Bar(Staff.this.sys, x);
            }
        });
    }

    public int sysOff(){return sys.fmt.staffOffset.get(iStaff);}
    public int yTop(){return sys.yTop() + sysOff();}
    public int yBot(){return yTop() + fmt.height();}


    //-------------Fmt----------------
    public static class Fmt {
        public int nLines = 5;
        public int H = UC.defaultStaffSpace;

        public int height() {
            return 2 * H * (nLines - 1);
        }

        public void showAt(Graphics g, int y){
            int LEFT = PAGE.margins.left, RIGHT = PAGE.margins.right;
            for(int i = 0; i < nLines; i++){g.drawLine(LEFT, y + 2 * H * i, RIGHT, y + 2 * H * i);}
        }
    }
}
