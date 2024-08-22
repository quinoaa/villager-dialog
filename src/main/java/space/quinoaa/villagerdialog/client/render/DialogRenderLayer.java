package space.quinoaa.villagerdialog.client.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.client.screen.DialogScreen;
import space.quinoaa.villagerdialog.init.CapabilityInit;

public class DialogRenderLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
    public static final ResourceLocation LOCATION = DialogScreen.LOCATION;
    private final EntityRenderDispatcher renderDispatcher;

    public DialogRenderLayer(RenderLayerParent<Villager, VillagerModel<Villager>> pRenderer) {
        super(pRenderer);
        Minecraft mc = Minecraft.getInstance();
        renderDispatcher = mc.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource pBuffer, int pPackedLight, Villager pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        var cap = pLivingEntity.getCapability(CapabilityInit.DIALOG).orElse(null);
        if(cap == null) return;
        if(!cap.hasDialog) return;

        pose.popPose();
        pose.pushPose();
        pose.translate(0, 2.5, 0);
        float scale = 1 / 32f;
        pose.scale(scale, scale, scale);


        pose.mulPose(renderDispatcher.cameraOrientation());

        var buf = pBuffer.getBuffer(RenderType.entityCutout(LOCATION));

        var pMatrix = pose.last().pose();
        buf.vertex(pMatrix, -2.5f, 0, 0.0F).color(255, 255, 255, 255).uv(0 / 256f, 15 / 256f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(0, 1, 0).endVertex();
        buf.vertex(pMatrix, -2.5f, 15, 0.0F).color(255, 255, 255, 255).uv(0 / 256f, 0 / 256f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(0, 1, 0).endVertex();
        buf.vertex(pMatrix, 2.5f, 15, 0.0F).color(255, 255, 255, 255).uv(5 / 256f, 0 / 256f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(0, 1, 0).endVertex();
        buf.vertex(pMatrix, 2.5f, 0, 0.0F).color(255, 255, 255, 255).uv(5 / 256f, 15 / 256f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(0, 1, 0).endVertex();

        pose.popPose();
        pose.pushPose();
    }


}
