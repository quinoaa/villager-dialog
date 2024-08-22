package space.quinoaa.villagerdialog.cap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.init.CapabilityInit;

public class DialogCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(VillagerDialog.MODID, "dialog");
    public final Villager villager;
    private LazyOptional<DialogCapability> value;

    public DialogCapabilityProvider(Villager villager) {
        this.villager = villager;
        this.value = LazyOptional.of(()-> new DialogCapability(villager));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityInit.DIALOG){
            return (LazyOptional<T>) value;
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return value.resolve().get().serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        value.resolve().get().deserialize(nbt);
    }
}
