package space.quinoaa.villagerdialog.init;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import space.quinoaa.villagerdialog.cap.DialogCapability;
import space.quinoaa.villagerdialog.cap.DialogCapabilityProvider;

@Mod.EventBusSubscriber
public class CapabilityInit {
    public static final Capability<DialogCapability> DIALOG = CapabilityManager.get(new CapabilityToken<>() {});

    public static void init() {

    }

    @SubscribeEvent
    public static void trackBegin(PlayerEvent.StartTracking event){
        var plr = event.getEntity();
        if(plr.level().isClientSide) return;
        var srvPlr = (ServerPlayer) plr;

        if(event.getTarget() instanceof Villager villager){
            var cap = villager.getCapability(CapabilityInit.DIALOG).orElse(null);

            cap.sendTracking(PacketDistributor.PLAYER.with(()->srvPlr));
        }
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Villager villager){
            event.addCapability(DialogCapabilityProvider.KEY, new DialogCapabilityProvider(villager));
        }
    }
}
