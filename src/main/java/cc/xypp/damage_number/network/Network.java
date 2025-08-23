package cc.xypp.damage_number.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.logging.Logger;

public class Network {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("damage_number", "damaged"),
            () -> PROTOCOL_VERSION,
            (v)->true,
            (v)->true
    );
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                DamagePayload.TYPE,
                DamagePayload.STREAM_CODEC,
                Network::onClientMessage
        );
    }
    public static void send(ServerPlayer player, String type, float amount, int combo, float instant,long color){
        PacketDistributor.sendToPlayer(player,new DamagePayload(type,amount,combo,instant,color));
    }
    protected  static  void  onClientMessage(DamagePayload payload, IPayloadContext context){
        context.enqueueWork(()->{
            if (Objects.equals(payload.t(), "emit")) {
                Data.amount = payload.amount();
                if(!Config.noShake) Data.shakes = 4;
                Data.combo = payload.combo();
                Data.latest.add(new MutablePair<>(payload.instant(), new MutablePair<>(payload.color(),new Date().getTime())));
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
