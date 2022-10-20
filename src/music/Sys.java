package music;

import reaction.Mass;

import java.awt.*;
import java.util.ArrayList;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;
    public Sys.Fmt fmt;

    public Sys(Page page, int iSys, Sys.Fmt fmt){
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        this.fmt = fmt;

    }

    int yTop() {return page.sysTop(iSys);}

    //------------Fnt---------------
    public static class Fmt extends ArrayList<Staff.Fmt>{
        public ArrayList<Integer> staffOffset = new ArrayList<>();

        public int height() {
            int last = size() - 1;
            return staffOffset.get(last) + get(last).height();
        }

        public void showAt(Graphics g, int y){
            for (int i = 0; i < size(); i++) {
                get(i).showAt(g, y + staffOffset.get(i));
            }
        }
    }
}
