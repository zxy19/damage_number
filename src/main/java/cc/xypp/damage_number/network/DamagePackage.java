package cc.xypp.damage_number.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class DamagePackage {
    public String type="emit";
    public float amount=0.0f;
    public int combo=0;
    public float instant=0.0f;
    public DamagePackage(String type, float amount, int combo,float instant) {
        this.type = type;
        this.amount = amount;
        this.combo = combo;
        this.instant =  instant;
    }
    public DamagePackage(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        if (tag != null) {
            type = tag.getString("type");
            amount = tag.getFloat("amount");
            combo = tag.getInt("combo");
            instant = tag.getFloat("instant");
        }
    }

    public void toBytes(FriendlyByteBuf buffer) {
        CompoundTag toSend = new CompoundTag();
        toSend.putFloat("amount", amount);
        toSend.putString("type", type);
        toSend.putInt("combo", combo);
        toSend.putFloat("instant", instant);
        buffer.writeNbt(toSend);
    }
}
