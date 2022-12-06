package craftguide.mixin;

import craftguide.CraftGuide_Vanilla;
import net.minecraft.src.GuiAchievement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiAchievement.class)
public class GuiAchievementMixin {
    CraftGuide_Vanilla instance;

    @Inject(method = "updateAchievementWindow()V", at = @At("HEAD"))
    private void updateAchievementWindow(CallbackInfo info) {
        if (instance == null) {
            instance = new CraftGuide_Vanilla();
            instance.load();
        }
        instance.checkKeybind();
    }
}
