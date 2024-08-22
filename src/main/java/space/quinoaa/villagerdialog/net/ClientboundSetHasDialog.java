package space.quinoaa.villagerdialog.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.villagerdialog.init.CapabilityInit;

import java.util.function.Supplier;

public class ClientboundSetHasDialog {
    public final int entityId;
    public final boolean hasDialog;

    public ClientboundSetHasDialog(int entityId, boolean hasDialog) {
        this.entityId = entityId;
        this.hasDialog = hasDialog;
    }

    public void encode(FriendlyByteBuf b) {
        b.writeInt(entityId);
        b.writeBoolean(hasDialog);
    }

    public static ClientboundSetHasDialog decode(FriendlyByteBuf b) {
        return new ClientboundSetHasDialog(b.readInt(), b.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isClient()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var level = Minecraft.getInstance().level;
            if(level == null) return;

            var entity = level.getEntity(entityId);
            if(!(entity instanceof Villager villager)) return;

            var dialog = villager.getCapability(CapabilityInit.DIALOG).orElse(null);
            if(dialog == null) return;

            dialog.hasDialog = hasDialog;
        });
    }
}
