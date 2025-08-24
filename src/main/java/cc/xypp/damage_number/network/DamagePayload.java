package cc.xypp.damage_number.network;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.client.Data;
import cc.xypp.damage_number.data.DamageRecord;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record DamagePayload(String t, float amount, int combo,
                            DamageRecord data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamagePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DamageNumber.MODID, "damage"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DamagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            DamagePayload::t,
            ByteBufCodecs.FLOAT,
            DamagePayload::amount,
            ByteBufCodecs.INT,
            DamagePayload::combo,
            StreamCodec.of(
                    (RegistryFriendlyByteBuf t, DamageRecord c) -> c.toNetwork(t),
                    DamageRecord::fromNetwork
            ),
            DamagePayload::data,
            DamagePayload::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
