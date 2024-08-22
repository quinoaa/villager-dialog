package space.quinoaa.villagerdialog.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.villagerdialog.init.CapabilityInit;
import space.quinoaa.villagerdialog.init.MenuInit;
import space.quinoaa.villagerdialog.init.NetworkInit;
import space.quinoaa.villagerdialog.net.ClientboundSetDialogMenuData;

import java.util.List;

public class DialogMenu extends AbstractContainerMenu {
    private final Player player;
    private final Villager villager;

    public String part;
    public Component message;
    public List<Component> choices;
    private boolean dataUpdated = false;

    public DialogMenu(int pContainerId, Inventory inventory, int entityId) {
        super(MenuInit.DIALOG.get(), pContainerId);

        player = inventory.player;
        if(player.level().getEntity(entityId) instanceof Villager villager) this.villager = villager;
        else this.villager = null;

    }

    public DialogMenu(int i, Inventory inventory, FriendlyByteBuf b) {
        this(i, inventory, b.readInt());
        var p = ClientboundSetDialogMenuData.decode(b);
        part = p.part();
        message = p.msg();
        choices = p.choices();
        dataUpdated = true;
    }



    public static void open(ServerPlayer player, Villager villager){
        var cap = villager.getCapability(CapabilityInit.DIALOG).orElse(null);
        var p = getUpdatePacket(villager, player);
        if(cap == null || p == null) return;

        NetworkHooks.openScreen(player, new SimpleMenuProvider(
                (id, inv, plr)->new DialogMenu(id, inv, villager.getId()),
                Component.literal(cap.villagerName)
        ), b-> {
            b.writeInt(villager.getId());
            p.encode(b);
        });
    }

    public void handleDialogSwitch(String part, int choice) {
        if(villager.level().isClientSide) throw new IllegalStateException("Server method");

        var cap = villager.getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return;

        var dialog = cap.dialog;
        if(dialog == null) return;

        if(dialog.currentStep.equals(part)) {
            dialog.getStep().getChoices((ServerPlayer) player).get(choice).onClick((ServerPlayer) player, dialog);
            syncClientData();
        }
    }

    public boolean pollDataUpdate(){
        if(!dataUpdated) return false;
        dataUpdated = false;
        return true;
    }

    public void syncClientData(){
        var p = getUpdatePacket(villager, (ServerPlayer) player);
        if(p == null) return;
        NetworkInit.CHANNEL.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player), p);
    }

    public static @Nullable ClientboundSetDialogMenuData getUpdatePacket(Villager villager, ServerPlayer player){
        if(villager.level().isClientSide) throw new IllegalStateException("Server method");
        var cap = villager.getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return null;
        var dial = cap.dialog;
        if(dial == null) return null;
        var step = dial.getStep();

        return new ClientboundSetDialogMenuData(dial.currentStep, step.dialog, step.getChoicesAsComponents(player));
    }







    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if(villager == null || villager.isRemoved() || villager.distanceToSqr(player) > 20 * 20) return false;

        var cap = villager.getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return false;

        return cap.hasDialog;
    }

    public void updateDatas(String part, Component msg, List<Component> components) {
        this.part = part;
        this.message = msg;
        this.choices = components;
        dataUpdated = true;
    }
}
