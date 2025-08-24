package cc.xypp.damage_number.data;

import cc.xypp.damage_number.api.decoration.DecorationSerializer;
import cc.xypp.damage_number.api.decoration.INumberDecoration;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record DamageRecord(float amount, long color, DamageTextFmt displayFormat,
                           List<INumberDecoration> decorations) {
    public static final DamageRecord EMPTY = new DamageRecord(0, 0, DamageTextFmt.DEFAULT_TEXT_FMT, new ArrayList<>());

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(amount);
        buf.writeLong(color);
        displayFormat.toNetwork(buf);
        buf.writeCollection(decorations, DecorationSerializer::toNetwork);
    }

    public static DamageRecord fromNetwork(FriendlyByteBuf buf) {
        return new DamageRecord(
                buf.readFloat(),
                buf.readLong(),
                DamageTextFmt.fromNetwork(buf),
                buf.readCollection(
                        ArrayList::new,
                        buf1 -> Objects.requireNonNull(DecorationSerializer.fromNetwork(buf1)))
        );
    }
}
