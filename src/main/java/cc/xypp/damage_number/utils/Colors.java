package cc.xypp.damage_number.utils;

import java.awt.*;
import java.util.Random;

public class Colors {

    static Random random = new Random();
    public static long toColor(String color){
        if(color.startsWith("#")){
            return Long.parseLong(color.substring(1),16);
        }else{
            return Long.parseLong(color);
        }
    }

    public static long randomColor(){
        return randomColor(0.5f);
    }
    public static long randomColor(float brightness){
        float h = random.nextFloat();
        float s = random.nextFloat();
        float b = brightness + ((1f - brightness) * random.nextFloat());
        Color c = Color.getHSBColor(h, s, b);
        return c.getRGB();
    }
}
