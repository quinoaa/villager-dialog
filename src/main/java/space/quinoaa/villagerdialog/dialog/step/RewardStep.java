package space.quinoaa.villagerdialog.dialog.step;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import space.quinoaa.villagerdialog.dialog.DialogInstance;
import space.quinoaa.villagerdialog.dialog.DialogStep;
import space.quinoaa.villagerdialog.dialog.DialogType;

import java.util.ArrayList;
import java.util.List;

public class RewardStep extends DialogStep {
    private final List<ItemStack> rewards = new ArrayList<>();

    private final String next;

    public RewardStep(String name, JsonObject json, Type type, DialogType parent) {
        super(name, json, type, parent);
        this.next = json.has("next") ? json.get("next").getAsString() : null;

        for (JsonElement el : json.getAsJsonArray("rewards")) {
            var itemJson = el.getAsJsonObject();
            rewards.add(ItemStack.CODEC.decode(JsonOps.INSTANCE, itemJson).get().left().get().getFirst());
        }

    }

    @Override
    public List<Choice> getChoices(ServerPlayer player) {
        return List.of(new Choice() {
            @Override
            public void onClick(ServerPlayer player, DialogInstance instance) {
                var inv = player.getInventory();
                int reqSpace = rewards.size();
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    if(inv.getItem(i).isEmpty()) reqSpace--;
                }
                if(reqSpace > 0) return;
                for (ItemStack reward : rewards) {
                    inv.add(reward.copy());
                }

                if(next == null) instance.terminate();
                else instance.switchStep(next);
            }

            @Override
            public Component text() {
                var c = Component.translatable("dialog.standard.receive");
                for (ItemStack itemStack : rewards) {
                    c = c.append("\n").append(itemStack.getCount() + " ").append(itemStack.getHoverName());
                }
                return c;
            }
        });
    }

    @Override
    public List<String> getPossiblePaths() {
        return next != null ? List.of(next) : List.of();
    }
}
