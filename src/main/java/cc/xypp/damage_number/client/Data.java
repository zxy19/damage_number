package cc.xypp.damage_number.client;


import cc.xypp.damage_number.data.DamageRecord;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static float amount = 0.0f;
    public static boolean show = false;
    public static int shakes = 3;
    public static boolean confirm = true;
    public static List<Pair<Long,DamageRecord>> latest = new ArrayList<>();
    public static int combo = 0;
}
