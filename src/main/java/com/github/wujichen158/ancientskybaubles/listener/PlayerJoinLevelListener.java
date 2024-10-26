package com.github.wujichen158.ancientskybaubles.listener;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerJoinLevelListener {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
//        if (event.getLevel().isClientSide()) {
//            // TODO: 有点低效
//            Entity entity = event.getEntity();
//            // 拦一层以提高效率
//            if (entity instanceof Player player) {
//                if (Minecraft.getInstance().player.is(player)) {
//                    RegenerableBlockEntityCache.clear();
//                }
//            }
//        }
    }

}
