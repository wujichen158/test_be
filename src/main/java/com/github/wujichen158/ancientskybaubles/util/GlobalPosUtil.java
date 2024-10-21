package com.github.wujichen158.ancientskybaubles.util;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GlobalPosUtil {
    public static GlobalPos constructGlobalPos(BlockEntity blockEntity) {
        return GlobalPos.of(blockEntity.getLevel().dimension(), blockEntity.getBlockPos());
    }
}
