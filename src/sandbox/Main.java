package sandbox;

import graphics.Window;

public class Main {
    public static void main(String[] args){
        System.out.print("hello music");
        Window.PANEL = new PaintInk();//只需要看window调用的是paint还是squares
//      Window.PANEL = new shapeTrainer();  //先用trainer进行训练存储S-S/E-E, 再调用PaintInk画图识别


        Window.launch() ;

    }
}
