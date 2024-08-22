package space.quinoaa.villagerdialog.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.network.PacketDistributor;
import space.quinoaa.villagerdialog.data.DialogTypeListLoader;
import space.quinoaa.villagerdialog.data.NameListDataLoader;
import space.quinoaa.villagerdialog.dialog.DialogInstance;
import space.quinoaa.villagerdialog.init.NetworkInit;
import space.quinoaa.villagerdialog.menu.DialogMenu;
import space.quinoaa.villagerdialog.net.ClientboundSetHasDialog;

import java.util.Random;

@AutoRegisterCapability
public class DialogCapability {
    public String villagerName = NameListDataLoader.INSTANCE.getRandomGame(new Random());

    private final Villager villager;

    private int nextDialog = -1;
    public DialogInstance dialog;

    public boolean hasDialog = false;

    public DialogCapability(Villager villager) {
        this.villager = villager;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();

        tag.putString("name", villagerName);
        if(dialog != null) tag.put("dialog", dialog.serialize());

        return tag;
    }

    public void deserialize(CompoundTag nbt) {
        if(nbt.contains("name", CompoundTag.TAG_STRING)) villagerName = nbt.getString("name");
        if(nbt.contains("dialog", CompoundTag.TAG_COMPOUND)) dialog = DialogInstance.deserialize(nbt.getCompound("dialog"), this);
        hasDialog = dialog != null;

        sendTracking(PacketDistributor.TRACKING_ENTITY.with(()->villager));
    }

    public void sendTracking(PacketDistributor.PacketTarget target){
        NetworkInit.CHANNEL.send(target, new ClientboundSetHasDialog(villager.getId(), hasDialog));
    }

    public void tick() {
        if(villager.level().isClientSide) return;
        if(dialog == null){
            if(nextDialog == -1) nextDialog = (int) (20 * 10 * Math.random()) + 20 * 5;

            nextDialog--;
            if(nextDialog<=0){
                nextDialog = -1;
                var type = DialogTypeListLoader.INSTANCE.getRandom(villager.level().random);
                if(type != null){
                    hasDialog = true;
                    dialog = new DialogInstance(type, this);
                    sendTracking();
                }
            }
        }
    }

    public boolean handleInteract(Player plr) {
        if(!hasDialog) return false;

        if(!plr.level().isClientSide){
            DialogMenu.open((ServerPlayer) plr, villager);
        }

        return true;
    }

    public void sendTracking() {
        sendTracking(PacketDistributor.TRACKING_ENTITY.with(()->villager));
    }
}
