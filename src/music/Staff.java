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
                int x = gesture.vs.xM(), y1 =gesture.vs.yL(), y2 = gesture.vs.yH();
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
        addReaction(new Reaction("S-S") { // set bar continue
            // TODO: 10/25/22
            public int bid(Gesture gesture) {
                if(Staff.this.sys.iSys != 0) { //Only change bar continues in fist sys
                    return UC.noBid;
                }
                int y1 = gesture.vs.yL(), y2 = gesture.vs.yM(), iStaff = Staff.this.iStaff;
                if (iStaff == PAGE.sysFmt.size() - 1) {return UC.noBid;} //last staff cant continue
                Staff nextStaff = Staff.this.sys.staffs.get(iStaff + 1);
                if(Math.abs(y2 - nextStaff.yTop()) > 20){return UC.noBid;}
                return 10;
                }
            public void act(Gesture gesture) {
                Staff.this.fmt.toggleBarContinues();}
            });

        addReaction(new Reaction("SW-SW") {
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(), y = gesture.vs.yM();
                if(x <PAGE.margins.left || x > PAGE.margins.right) {return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
                if (y < top || y > bot) {return UC.noBid;}
                return 10;
            }

            public void act(Gesture gesture) {
                new Head(Staff.this, gesture.vs.xM(), gesture.vs.yM());
            }
        });

        addReaction(new Reaction("W-S") {  //add a Q rest
            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y = gesture.vs.yL();
                if (x < PAGE.margins.left || x > PAGE.margins.right) {return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop(), bot = Staff.this.yBot();
                if (y < top || y > bot){return UC.noBid;} //null
                return 10;
            }
            public void act(Gesture gesture) {
                Time t = Staff.this.sys.getTime(gesture.vs.xL());
                new Rest(Staff.this, t);
            }
        });

        addReaction(new Reaction("E-S") {  //add an Eighth rest
            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y = gesture.vs.yL();
                if (x < PAGE.margins.left || x > PAGE.margins.right) {return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop(), bot = Staff.this.yBot();
                if (y < top || y > bot){return UC.noBid;} //null
                return 10;
            }
            public void act(Gesture gesture) {
                Time t = Staff.this.sys.getTime(gesture.vs.xL());
                (new Rest(Staff.this, t)).incFlag();
            }
        });
    }



    public int sysOff(){return sys.fmt.staffOffset.get(iStaff);}
    public int yTop(){return sys.yTop() + sysOff();}
    public int yBot(){return yTop() + fmt.height();}

    public int yLine(int n) {return yTop() + n * fmt.H;}
    public int lineOfY(int y) {
        int bias = 100;
        int top = yTop() - bias * fmt.H;
        return (y - top + fmt.H / 2) / fmt.H - bias; //
    }


    //-------------Fmt----------------
    public static class Fmt {
        public int nLines = 5;
        public int H = UC.defaultStaffSpace;
        public boolean barContinues = false;

        public int height() {
            return 2 * H * (nLines - 1);
        }
        public void toggleBarContinues() {
            barContinues = !barContinues;
        }

        public void showAt(Graphics g, int y){
            int LEFT = PAGE.margins.left, RIGHT = PAGE.margins.right;
            for(int i = 0; i < nLines; i++){g.drawLine(LEFT, y + 2 * H * i, RIGHT, y + 2 * H * i);}
        }
    }
}
