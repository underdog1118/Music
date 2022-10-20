package music;

import reaction.Mass;

import java.awt.*;
import java.util.ArrayList;

public class Page extends Mass {
    public Margins margins = new Margins();
    public Sys.Fmt sysFmt;
    public int sysGap;
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(Sys.Fmt sysFmt) {
        super("BACK");
        this.sysFmt = sysFmt;
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
