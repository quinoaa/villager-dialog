package space.quinoaa.villagerdialog.dialog;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DialogType {
    public final Map<String, DialogStep> steps;
    public final String firstStep;
    public final ResourceLocation id;

    public DialogType(JsonObject json, ResourceLocation id) {
        this.firstStep = json.get("first").getAsString();
        this.id = id;

        Map<String, DialogStep> steps = new HashMap<>();
        json.getAsJsonObject("dialogs").asMap().forEach((name, stepJson)->{
            steps.put(name, DialogStep.load(stepJson, name, this));
        });
        for (DialogStep value : steps.values()) {
            for (String possiblePath : value.getPossiblePaths()) {
                if(!steps.containsKey(possiblePath)) throw new IllegalStateException("Can't find dialog " + possiblePath);
            }
        }

        this.steps = Map.copyOf(steps);
    }
}
