package space.quinoaa.villagerdialog.data;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import space.quinoaa.villagerdialog.VillagerDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = VillagerDialog.MODID)
public class NameListDataLoader implements PreparableReloadListener {
    public static final NameListDataLoader INSTANCE = new NameListDataLoader();

    private volatile List<String> names = new ArrayList<>();

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.supplyAsync(()->{
            List<String> names = new ArrayList<>();

            for (Resource res : manager.listResources("names", loc -> true).values()) {
                BufferedReader reader = null;
                try {
                    reader = res.openAsReader();
                    String line;
                    while((line = reader.readLine()) != null) names.add(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            VillagerDialog.LOGGER.info("Loaded {} villager names", names.size());
            return List.copyOf(names);
        }, pBackgroundExecutor).thenCompose(barrier::wait).thenAcceptAsync(list->{
            names = list;
        });
    }

    public String getRandomGame(Random random){
        if(names.isEmpty()) return "No name loaded";
        return names.get(random.nextInt(names.size()));
    }

    @SubscribeEvent
    public static void register(AddReloadListenerEvent event){
        event.addListener(INSTANCE);
    }
}
