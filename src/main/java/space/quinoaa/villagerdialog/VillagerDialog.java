package space.quinoaa.villagerdialog;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import space.quinoaa.villagerdialog.init.CapabilityInit;
import space.quinoaa.villagerdialog.init.DialogStepInit;
import space.quinoaa.villagerdialog.init.MenuInit;
import space.quinoaa.villagerdialog.init.NetworkInit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VillagerDialog.MODID)
public class VillagerDialog {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "villagerdialog";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();



    public VillagerDialog() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MenuInit.init(modEventBus);
        CapabilityInit.init();
        DialogStepInit.init(modEventBus);
        NetworkInit.init();
    }

}
