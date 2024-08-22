package space.quinoaa.villagerdialog.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.net.ClientboundSetDialogMenuData;
import space.quinoaa.villagerdialog.net.ClientboundSetHasDialog;
import space.quinoaa.villagerdialog.net.ServerboundDialogChoice;

public class NetworkInit {
    public static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VillagerDialog.MODID, "main"),
            ()->PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );


    public static void init() {
        CHANNEL.registerMessage(0, ClientboundSetHasDialog.class, ClientboundSetHasDialog::encode, ClientboundSetHasDialog::decode, ClientboundSetHasDialog::handle);
        CHANNEL.registerMessage(1, ServerboundDialogChoice.class, ServerboundDialogChoice::encode, ServerboundDialogChoice::decode, ServerboundDialogChoice::handle);
        CHANNEL.registerMessage(2, ClientboundSetDialogMenuData.class, ClientboundSetDialogMenuData::encode, ClientboundSetDialogMenuData::decode, ClientboundSetDialogMenuData::handle);
    }
}
