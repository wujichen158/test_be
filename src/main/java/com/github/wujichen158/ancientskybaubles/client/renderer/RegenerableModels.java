package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;

@OnlyIn(Dist.CLIENT)
public enum RegenerableModels {
    /**
     * 所有可再生资源的模型RL
     */

    shoal_salt,
    ;

    private final ResourceLocation unharvested;
    private final ResourceLocation harvested;

    RegenerableModels(String unharvestedModelName, String harvestedModelName) {
        this.unharvested = new ResourceLocation(Reference.MOD_ID, "block/" + unharvestedModelName);
        this.harvested = new ResourceLocation(Reference.MOD_ID, "block/" + harvestedModelName);
    }

    RegenerableModels() {
        String fieldName = this.name().toLowerCase();
        this.unharvested = new ResourceLocation(Reference.MOD_ID, String.format("block/%s", fieldName));
        this.harvested = new ResourceLocation(Reference.MOD_ID, String.format("block/%s_mined", fieldName));
    }

    public ResourceLocation getUnharvestedModel() {
        return this.unharvested;
    }

    public ResourceLocation getHarvestedModel() {
        return this.harvested;
    }

    public ResourceLocation getModel(boolean harvested) {
        return harvested ? this.harvested : this.unharvested;
    }

    public static void registerAllModels(ModelEvent.RegisterAdditional event) {
        for (RegenerableModels model : values()) {
            event.register(model.getUnharvestedModel());
            event.register(model.getHarvestedModel());
        }
    }
}
