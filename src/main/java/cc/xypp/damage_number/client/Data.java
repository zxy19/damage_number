package cc.xypp.damage_number.client;


import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.data.DamageRecord;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Data {
    public static float amount = 0.0f;
    public static boolean show = false;
    public static int shakes = 3;
    public static boolean confirm = true;
    public static List<Pair<Long, DamageRecord>> latest = new ArrayList<>();
    public static Queue<Pair<Long, DamageRecord>> waitToAdd = new ConcurrentLinkedDeque<>();
    public static int combo = 0;

    public static void add(Pair<Long, DamageRecord> pair) {
        waitToAdd.add(pair);
    }

    public static void update() {
        if (!waitToAdd.isEmpty()) {
            Data.confirm = false;
            Data.show = true;
        }
        while (!waitToAdd.isEmpty()) {
            latest.add(waitToAdd.poll());
        }
        while (latest.size() != 0 && latest.size() > Config.damageListMaxSize) {
            latest.remove(0);
        }

        long currentTime = new Date().getTime();
        while (latest.size() > 0 && latest.get(0).getA() < currentTime - Config.damageListClearTime) {
            latest.remove(0);
        }
    }

    public static void clear() {
        latest.clear();
        confirm = false;
        show = false;
    }
}
