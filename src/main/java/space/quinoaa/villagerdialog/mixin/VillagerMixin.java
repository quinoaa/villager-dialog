package space.quinoaa.villagerdialog.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.quinoaa.villagerdialog.init.CapabilityInit;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

    public VillagerMixin(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "mobInteract",
            at = @At("HEAD"),
            cancellable = true
    )
    public void interact(Player plr, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        var cap = getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return;

        if(cap.handleInteract(plr)) cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tickCapability(CallbackInfo ci){
        var cap = getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return;

        cap.tick();
    }
}
