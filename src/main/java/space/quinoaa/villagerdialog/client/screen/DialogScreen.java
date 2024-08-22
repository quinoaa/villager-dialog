package space.quinoaa.villagerdialog.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import space.quinoaa.villagerdialog.VillagerDialog;
import space.quinoaa.villagerdialog.init.NetworkInit;
import space.quinoaa.villagerdialog.menu.DialogMenu;
import space.quinoaa.villagerdialog.net.ServerboundDialogChoice;

import java.util.ArrayList;
import java.util.List;

public class DialogScreen extends AbstractContainerScreen<DialogMenu> {
    public static final ResourceLocation LOCATION = new ResourceLocation(VillagerDialog.MODID, "textures/gui/villager_dialog.png");

    public static final TextBoxTextureSpec MAIN_DIALOG = new TextBoxTextureSpec(LOCATION, 5, 0, 210, 27, 4, 300);
    public static final TextBoxTextureSpec CHOICE_DIALOG = new TextBoxTextureSpec(LOCATION, 0, 27, 155, 21, 3, 150);


    private TextBox message = null;
    private List<ButtonTextBox> choices = List.of();

    public DialogScreen(DialogMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        font = Minecraft.getInstance().font;

        imageWidth = 32;
        imageHeight = 32;
    }

    @Override
    protected void init() {
        super.init();

        repositionBoxes();
    }

    public void repositionBoxes(){
        int offY = height - 30;
        if(message != null){
            offY -= message.height + 10;
            message.x = (width - message.width) / 2;
            message.y = offY;
        }

        for (ButtonTextBox choice : choices) {
            offY -= choice.height + 10;
            choice.x = width - 10 - choice.width;
            choice.y = offY;
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        if(menu.pollDataUpdate()){
            message = new TextBox(MAIN_DIALOG, title, menu.message, font);
            List<ButtonTextBox> choices = new ArrayList<>();
            int i = 0;
            for (Component choice : menu.choices) {
                final int choiceId = i;
                choices.add(new ButtonTextBox(CHOICE_DIALOG, null, choice, font, ()->{
                    NetworkInit.CHANNEL.sendToServer(new ServerboundDialogChoice(menu.part, choiceId));
                }));
                i++;
            }
            this.choices = choices;
            repositionBoxes();
        }
    }

    @Override
    public void render(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.disableDepthTest();
        g.pose().pushPose();

        if(message != null) message.render(g);
        for (var choice : choices) {
            choice.setHovered(pMouseX, pMouseY);
            choice.render(g);
        }


        g.pose().popPose();
        g.flush();
        RenderSystem.enableDepthTest();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for (ButtonTextBox choice : choices) {
            if(choice.isMouseIn((int) pMouseX, (int) pMouseY)){
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
                choice.onClick();
            }
        }
        return true;
    }

    @Override
    protected void renderBg(GuiGraphics g, float v, int i, int i1) {

    }

}
