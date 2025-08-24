package cc.xypp.damage_number.client;


import cc.xypp.damage_number.data.DamageListItem;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public static float amount = 0.0f;
    public static boolean show = false;
    public static int shakes = 3;
    public static boolean confirm = true;
    public static List<Pair<Long,DamageRecord>> latest = new ArrayList<>();
    public static int combo = 0;
}
