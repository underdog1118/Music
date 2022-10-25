package music;

import reaction.Mass;

import java.awt.*;

public class Bar extends Mass {
    private static final int FAT = 2, RIGHT = 4, LEFT = 8;
    public Sys sys;
    public int x, barType = 0;
    public Bar(Sys sys, int x) {
        super("Note");
        this.sys = sys;
        this.x = x;
    }

    public void show(Graphics g){
        int yTop = sys.yTop(), N = sys.fmt.size();
        for (int i = 0; i < N; i++) {
            Staff.Fmt sf = sys.fmt.get(i);
            int topLine = yTop + sys.fmt.staffOffset.get(i);
            g.drawLine(x, topLine, x, topLine + sf.height());
        }
    }
}
