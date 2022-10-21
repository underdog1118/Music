package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

import static sandbox.Music.PAGE;

public class Page extends Mass {
    public Margins margins = new Margins();
    public Sys.Fmt sysFmt;
    public int sysGap;
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(Sys.Fmt sysFmt) {
        super("BACK");
        this.sysFmt = sysFmt;

        //E-E画新五线谱，E-W在现有五线谱中链接新的，N-N取消某一步骤

        addReaction(new Reaction("E-W"){ //add new staff
            public int bid(Gesture gesture){
                int y = gesture.vs.yM();
                if(y <= PAGE.margins.top + sysFmt.height() + 30){
                    return UC.noBid;
                }
                    return 50;
            }
            public void act(Gesture gesture) {
                int y = gesture.vs.yM();
                PAGE.addNewStaff(y - PAGE.margins.top);
            }
        });
        addReaction(new Reaction("E-E") { //add a new system

            public int bid(Gesture gesture) {
                int y = gesture.vs.yM();
                int yBot = PAGE.sysTop(PAGE.sysList.size() - 1);
                if (y < yBot){
                    return UC.noBid;
                }return 50;
            }
            public void act(Gesture gesture) {
                int y = gesture.vs.yM();
                if(PAGE.sysList.size() == 1){
                    PAGE.sysGap = y - PAGE.margins.top - sysFmt.height(); // *
                }
                PAGE.addNewsys();
            }
        });
    }

    public void addNewsys(){
        sysList.add(new Sys(this, sysList.size(), sysFmt));
    }

    public void addNewStaff(int yOffset){
        Staff.Fmt fmt = new Staff.Fmt();
        int n = sysFmt.size();
        sysFmt.add(fmt);
        sysFmt.staffOffset.add(yOffset);
        for (int i = 0; i < sysList.size(); i++) {
            Sys sys = sysList.get(i);
            sysList.get(i).staffs.add(new Staff(sys, n, fmt));

        }
    }

    int sysTop(int iSys) {return margins.top + iSys * (sysFmt.height() + sysGap);}

    public void show(Graphics g){
        for (int i = 0; i < sysList.size(); i++) {
            sysFmt.showAt(g, sysTop(i));
        }
    }

    //-----------Margins---------------
    public static class Margins {
        private static int M = 50;
        public int top = M, left = M, by = UC.initialWindowWidth - M, right = UC.initialWindowHeight - M;

    }
}
