package space.quinoaa.villagerdialog.dialog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import space.quinoaa.villagerdialog.init.DialogStepInit;

import java.util.List;

public abstract class DialogStep {
    public final String name;
    public final Component dialog;
    public final Type type;
    public final DialogType parent;

    public DialogStep(String name, JsonObject json, Type type, DialogType parent) {
        this.name = name;
        this.dialog = Component.Serializer.fromJson(json.get("dialog"));
        this.type = type;
        this.parent = parent;
    }

    public abstract List<Choice> getChoices(ServerPlayer player);

    public abstract List<String> getPossiblePaths();

    public static DialogStep load(JsonElement stepJson, String name, DialogType parent) {
        if(!stepJson.isJsonObject()) return null;
        var obj = stepJson.getAsJsonObject();
        return (obj.has("type")
                ? DialogStepInit.getRegistry().getValue(ResourceLocation.tryParse(obj.get("type").getAsString()))
                : DialogStepInit.DEFAULT.get()).build(name, obj, parent);
    }

    public List<Component> getChoicesAsComponents(ServerPlayer player) {
        return getChoices(player).stream().map(Choice::text).toList();
    }

    public interface Choice {

        void onClick(ServerPlayer player, DialogInstance instance);

        Component text();

    }

    public interface Type {
        DialogStep build(String name, JsonObject json, Type type, DialogType parent);

        default DialogStep build(String name, JsonObject json, DialogType parent){
            return build(name, json, this, parent);
        }
    }
}
