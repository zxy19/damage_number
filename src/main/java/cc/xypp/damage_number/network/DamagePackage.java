package cc.xypp.damage_number.network;

import cc.xypp.damage_number.data.DamageRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class DamagePackage {
    public String type = "emit";
    public float amount = 0.0f;
    public int combo = 0;
    public final DamageRecord data;

    public DamagePackage(String type, float amount, int combo, DamageRecord data) {
        this.type = type;
        this.amount = amount;
        this.combo = combo;
        this.data = data;
    }

    public DamagePackage(FriendlyByteBuf buffer) {
        type = buffer.readUtf();
        amount = buffer.readFloat();
        combo = buffer.readInt();
        data = DamageRecord.fromNetwork(buffer);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(type);
        buffer.writeFloat(amount);
        buffer.writeInt(combo);
        data.toNetwork(buffer);
    }

}
