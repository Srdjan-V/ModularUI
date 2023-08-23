package com.cleanroommc.modularui.mixins.early.minecraft;

import com.cleanroommc.modularui.screen.GuiScreenWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {

    @Shadow
    private Slot theSlot;

    @Inject(method = "getSlotAtPosition", at = @At("HEAD"), cancellable = true)
    public void modularui$injectGetSlotAtPosition(int x, int y, CallbackInfoReturnable<Slot> cir) {
        //noinspection ConstantValue
        if (((Object) this).getClass() == GuiScreenWrapper.class) {
            cir.setReturnValue(this.theSlot);
        }
    }
}
