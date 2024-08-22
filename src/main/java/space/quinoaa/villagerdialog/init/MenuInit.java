package space.quinoaa.villagerdialog.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.menu.DialogMenu;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, VillagerDialog.MODID);

    public static final RegistryObject<MenuType<DialogMenu>> DIALOG = REGISTRY.register("dialog", ()->IForgeMenuType.<DialogMenu>create(DialogMenu::new));

    public static void init(IEventBus bus){
        REGISTRY.register(bus);
    }
}
