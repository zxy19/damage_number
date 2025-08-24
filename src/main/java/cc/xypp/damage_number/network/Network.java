package cc.xypp.damage_number.network;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.client.Data;
import cc.xypp.damage_number.data.DamageRecord;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import oshi.util.tuples.Pair;

import java.util.Date;
import java.util.Objects;

public class Network {
    private static final String PROTOCOL_VERSION = "1";

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                DamagePayload.TYPE,
                DamagePayload.STREAM_CODEC,
                Network::onClientMessage
        );
    }

    public static void send(ServerPlayer player, String type, float amount, int combo, DamageRecord data) {
        PacketDistributor.sendToPlayer(player, new DamagePayload(type, amount, combo, data));
    }

    protected static void onClientMessage(DamagePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Objects.equals(payload.t(), "emit")) {
                Data.amount = payload.amount();
                if (!Config.noShake) Data.shakes = 4;
                Data.combo = payload.combo();
                Data.latest.add(new Pair<>(new Date().getTime(), payload.data()));
                while (Data.latest.size() != 0 && Data.latest.size() > Config.damageListMaxSize) {
                    Data.latest.remove(0);
                }
                Data.confirm = false;
                Data.show = true;
            } else if (Objects.equals(payload.t(), "total")) {
                Data.amount = payload.amount();
                Data.confirm = true;
                Data.show = true;
            }
        });
    }
}
