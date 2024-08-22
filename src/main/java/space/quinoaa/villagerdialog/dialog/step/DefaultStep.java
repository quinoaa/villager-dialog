package space.quinoaa.villagerdialog.dialog.step;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import space.quinoaa.villagerdialog.dialog.DialogInstance;
import space.quinoaa.villagerdialog.dialog.DialogStep;
import space.quinoaa.villagerdialog.dialog.DialogType;

import java.util.ArrayList;
import java.util.List;

public class DefaultStep extends DialogStep {
    public final List<Choice> choices;
    private final List<String> possiblePaths;

    public DefaultStep(String name, JsonObject json, Type type, DialogType parent) {
        super(name, json, type, parent);

        List<Choice> choices = new ArrayList<>();
        List<String> possiblePaths = new ArrayList<>();
        if(json.has("choices")){
            for (JsonElement entry : json.getAsJsonArray("choices")) {
                var choice = entry.getAsJsonObject();

                var ins = new DefaultChoice(Component.Serializer.fromJson(choice.get("dialog")), choice.get("goto").getAsString());
                choices.add(ins);
                possiblePaths.add(ins.nextStep);
            }
        } else choices.add(new FinishChoice(Component.translatable("dialog.standard.finish")));
        this.choices = List.copyOf(choices);
        this.possiblePaths = List.copyOf(possiblePaths);
    }

    @Override
    public List<Choice> getChoices(ServerPlayer player) {
        return choices;
    }

    @Override
    public List<String> getPossiblePaths() {
        return possiblePaths;
    }

    public record DefaultChoice(Component text, String nextStep) implements Choice {

        @Override
        public void onClick(ServerPlayer player, DialogInstance instance) {
            instance.switchStep(nextStep);
        }

    }

    public record FinishChoice(Component text) implements Choice {

        @Override
        public void onClick(ServerPlayer player, DialogInstance instance) {
            instance.terminate();
        }

    }
}
