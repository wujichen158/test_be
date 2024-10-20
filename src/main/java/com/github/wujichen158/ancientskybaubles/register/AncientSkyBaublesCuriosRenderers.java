package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.client.renderer.MedalCuriosRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class AncientSkyBaublesCuriosRenderers {
    public static void registerCuriosRenderers() {
        CuriosRendererRegistry.register(AncientSkyBaublesItems.MedalItems.ROUND_1ST_MEDAL.get(), MedalCuriosRenderer::new);
    }
}
