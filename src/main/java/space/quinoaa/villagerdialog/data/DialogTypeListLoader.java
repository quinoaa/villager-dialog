package space.quinoaa.villagerdialog.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.dialog.DialogType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = VillagerDialog.MODID)
public class DialogTypeListLoader implements PreparableReloadListener {
    public static final DialogTypeListLoader INSTANCE = new DialogTypeListLoader();

    private transient Map<ResourceLocation, DialogType> dialogs = Map.of();
    private transient List<DialogType> randomList = List.of();


    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.supplyAsync(()->{
            var res = pResourceManager.listResources("dialogs", loc->loc.getPath().endsWith(".json"));
            Gson gson = new Gson();

            Map<ResourceLocation, DialogType> dialogs = new HashMap<>();
            for (ResourceLocation id : res.keySet()) {
                try{
                    var json = gson.fromJson(res.get(id).openAsReader(), JsonObject.class);
                    dialogs.put(id, new DialogType(json, id));
                }catch (Exception e){
                    throw new RuntimeException("Error when loading dialog " + id, e);
                }
            }
            VillagerDialog.LOGGER.info("Loaded {} dialog types", dialogs.size());

            return Map.copyOf(dialogs);
        }).thenCompose(pPreparationBarrier::wait).thenAcceptAsync((dialogs)->{
            this.dialogs = dialogs;
            this.randomList = List.copyOf(new ArrayList<>(dialogs.values()));
        }, pGameExecutor);
    }

    public DialogType getDialogType(ResourceLocation loc){
        return dialogs.get(loc);
    }

    public @Nullable DialogType getRandom(RandomSource random) {
        if(randomList.isEmpty()) return null;
        var list = this.randomList;
        return list.get(random.nextInt(list.size()));
    }

    @SubscribeEvent
    public static void register(AddReloadListenerEvent event){
        event.addListener(INSTANCE);
    }
}
