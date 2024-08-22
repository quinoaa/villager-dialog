package space.quinoaa.villagerdialog.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.client.render.DialogRenderLayer;
import space.quinoaa.villagerdialog.client.screen.DialogScreen;
import space.quinoaa.villagerdialog.init.MenuInit;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = VillagerDialog.MODID)
public class ClientMod {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            MenuScreens.register(MenuInit.DIALOG.get(), DialogScreen::new);
        });
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event){
        LivingEntityRenderer<Villager, VillagerModel<Villager>> renderer = event.getRenderer(EntityType.VILLAGER);
        renderer.addLayer(new DialogRenderLayer(renderer));
    }



}
