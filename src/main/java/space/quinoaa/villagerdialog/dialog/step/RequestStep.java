package space.quinoaa.villagerdialog.dialog.step;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import space.quinoaa.villagerdialog.dialog.DialogInstance;
import space.quinoaa.villagerdialog.dialog.DialogStep;
import space.quinoaa.villagerdialog.dialog.DialogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestStep extends DialogStep {
    private final List<ItemStack> required = new ArrayList<>();

    private final String accept, decline;

    public RequestStep(String name, JsonObject json, Type type, DialogType parent) {
        super(name, json, type, parent);

        for (JsonElement el : json.getAsJsonArray("request")) {
            var itemJson = el.getAsJsonObject();
            required.add(ItemStack.CODEC.decode(JsonOps.INSTANCE, itemJson).get().left().get().getFirst());
        }

        accept = json.get("accept").getAsString();
        decline = json.get("decline").getAsString();
    }

    public boolean hasRequired(ServerPlayer player){
        var menu = player.inventoryMenu;

        for (ItemStack req : required) {
            int count = 0;

            for (Slot slot : menu.slots) {
                var item = slot.getItem();
                if(ItemStack.isSameItemSameTags(req, item)) count += item.getCount();
            }
            if(count < req.getCount()) return false;
        }
        return true;
    }

    public void takeRequired(ServerPlayer player){
        var menu = player.inventoryMenu;

        for (ItemStack req : required) {
            int take = req.getCount();

            for (Slot slot : menu.slots) {
                var item = slot.getItem();
                var itemCount = item.getCount();
                if(ItemStack.isSameItemSameTags(req, item)) {
                    if(itemCount <= take) slot.set(ItemStack.EMPTY);
                    else item.shrink(take);
                    take -= itemCount;
                }

                if(take <= 0) break;
            }
        }
    }

    @Override
    public List<Choice> getChoices(ServerPlayer player) {
        return List.of(
                new Choice() {
                    @Override
                    public void onClick(ServerPlayer player, DialogInstance instance) {
                        if(!hasRequired(player)) return;
                        takeRequired(player);

                        instance.switchStep(accept);
                    }

                    @Override
                    public Component text() {
                        var c = Component.translatable("dialog.standard.give");
                        for (ItemStack itemStack : required) {
                            c = c.append("\n").append(itemStack.getCount() + " ").append(itemStack.getHoverName());
                        }
                        return c.withStyle(hasRequired(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
                    }
                },
                new Choice() {
                    @Override
                    public void onClick(ServerPlayer player, DialogInstance instance) {
                        instance.switchStep(decline);
                    }

                    @Override
                    public Component text() {
                        return Component.translatable("dialog.standard.decline");
                    }
                }
        );
    }


    @Override
    public List<String> getPossiblePaths() {
        return List.of(accept, decline);
    }

}
