package space.quinoaa.villagerdialog.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.villagerdialog.menu.DialogMenu;

import java.util.function.Supplier;

public record ServerboundDialogChoice(String part, int choice) {


    public void encode(FriendlyByteBuf b) {
        b.writeUtf(part);
        b.writeInt(choice);
    }

    public static ServerboundDialogChoice decode(FriendlyByteBuf b) {
        return new ServerboundDialogChoice(b.readUtf(), b.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isServer()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = ctx.getSender();
            if(plr == null) return;

            if(plr.containerMenu instanceof DialogMenu menu){
                menu.handleDialogSwitch(part, choice);
            }
        });
    }
}
