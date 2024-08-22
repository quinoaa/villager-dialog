package space.quinoaa.villagerdialog.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.villagerdialog.menu.DialogMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ClientboundSetDialogMenuData(String part, Component msg, List<Component> choices) {

    public void encode(FriendlyByteBuf b) {
        b.writeInt(choices.size());
        for (Component component : choices) {
            b.writeComponent(component);
        }
        b.writeUtf(part);
        b.writeComponent(msg);
    }

    public static ClientboundSetDialogMenuData decode(FriendlyByteBuf b) {
        int c = b.readInt();
        List<Component> cs = new ArrayList<>();
        for (int i = 0; i < c; i++) cs.add(b.readComponent());

        return new ClientboundSetDialogMenuData(b.readUtf(), b.readComponent(), cs);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isClient()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = Minecraft.getInstance().player;
            if(plr == null) return;

            if(plr.containerMenu instanceof DialogMenu menu){
                menu.updateDatas(part, msg, choices);
            }
        });
    }
}
