package space.quinoaa.villagerdialog.dialog;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.villagerdialog.cap.DialogCapability;
import space.quinoaa.villagerdialog.data.DialogTypeListLoader;

public class DialogInstance {
    public final DialogType dialog;
    public final DialogCapability capability;
    public String currentStep;

    public DialogInstance(DialogType dialog, DialogCapability capability) {
        this.dialog = dialog;
        this.capability = capability;
        currentStep = dialog.firstStep;
    }

    public void switchStep(String nextStep) {
        currentStep = nextStep;
    }

    public void terminate(){
        capability.dialog = null;
        capability.hasDialog = false;
        capability.sendTracking();
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();

        tag.putString("type", DialogTypeListLoader.INSTANCE.getDialogType(dialog.id).id.toString());
        tag.putString("step", currentStep);

        return tag;
    }

    public static @Nullable DialogInstance deserialize(CompoundTag dialog, DialogCapability cap) {
        var type = DialogTypeListLoader.INSTANCE.getDialogType(ResourceLocation.tryParse(dialog.getString("type")));
        if(type == null) return null;
        var step = dialog.getString("step");
        if(!type.steps.containsKey(step)) return null;

        var ins = new DialogInstance(type, cap);
        ins.currentStep = step;

        return ins;
    }

    public DialogStep getStep() {
        return dialog.steps.get(currentStep);
    }
}
