package space.quinoaa.villagerdialog.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.dialog.DialogStep;
import space.quinoaa.villagerdialog.dialog.step.DefaultStep;
import space.quinoaa.villagerdialog.dialog.step.RequestStep;
import space.quinoaa.villagerdialog.dialog.step.RewardStep;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = VillagerDialog.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DialogStepInit {
    private static final ResourceLocation REGISTRY_ID = new ResourceLocation(VillagerDialog.MODID, "dialog_type");
    private static Supplier<IForgeRegistry<DialogStep.Type>> REGISTRY;

    private static final DeferredRegister<DialogStep.Type> REGISTER = DeferredRegister.create(REGISTRY_ID, VillagerDialog.MODID);

    public static final RegistryObject<DialogStep.Type> DEFAULT = REGISTER.register("default", ()->DefaultStep::new);
    public static final RegistryObject<DialogStep.Type> REQUEST = REGISTER.register("request", ()-> RequestStep::new);
    public static final RegistryObject<DialogStep.Type> REWARD = REGISTER.register("reward", ()-> RewardStep::new);


    public static void init(IEventBus bus){
        REGISTER.register(bus);
    }

    @SubscribeEvent
    public static void createRegistry(NewRegistryEvent event){
        REGISTRY = event.create(RegistryBuilder.<DialogStep.Type>of().setName(REGISTRY_ID));
    }

    public static IForgeRegistry<DialogStep.Type> getRegistry(){
        return REGISTRY.get();
    }
}
