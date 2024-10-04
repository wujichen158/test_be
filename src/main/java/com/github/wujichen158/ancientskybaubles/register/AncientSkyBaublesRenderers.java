package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.renderer.MedalCuriosRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class AncientSkyBaublesRenderers {
    public static void registerRenderers() {
        CuriosRendererRegistry.register(AncientSkyBaublesItems.MedalItems.ROUND_1ST_MEDAL.get(), MedalCuriosRenderer::new);
    }
}
