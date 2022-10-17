package reaction;

import music.I;
import music.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Reaction implements I.React {
    public Shape shape;
    public static Map byShape = new Map();
    public static List initialReactions = new List();
    public Reaction(String shapeName){
        shape = Shape.DB.get(shapeName);
        if(shape == null){
            System.out.println("wtf-shapeDB doesn't know" + shapeName);}
    }
    public void enable(){
        List list = byShape.getList(shape);
        if(!list.contains(this)){list.add(this);}
    }
    public void disable(){
        List list = byShape.getList(shape);
        list.remove(this);

    }
    public static Reaction best(Gesture g){
        return byShape.getList(g.shape).lowBid(g);
    }
    //---------------------List----------------------
    public static class List extends ArrayList<Reaction> {
        public void addReaction(Reaction r){
            add(r);
            r.enable();
        }

        public void removeReaction(Reaction r){
            remove(r);
            r.disable();
        }
        public void clearAll(){
            for (Reaction r : this){
                r.disable();
            }
            this.clear();
        }
        public Reaction lowBid(Gesture g){   //can return null
            Reaction res = null;
            int bestSoFar = UC.noBid;
            for (Reaction r : this){
                int b = r.bid(g);
                if (b < bestSoFar){
                    bestSoFar = b;
                    res = r;
                }
            }
            return res;
        }
    }

    //---------------------Map------------------------
    public static class Map extends HashMap<Shape, List>{
        public List getList(Shape s){        //always succeeds
            List res = get(s);
            if(res == null){res = new List(); put(s,res);}
            return res;
        }
    }
}
