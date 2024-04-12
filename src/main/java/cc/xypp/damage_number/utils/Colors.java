package cc.xypp.damage_number.utils;

public class Colors {
    public static long toColor(String color){
        if(color.startsWith("#")){
            return Long.parseLong(color.substring(1),16);
        }else{
            return Long.parseLong(color);
        }
    }
}
