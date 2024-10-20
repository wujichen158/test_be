package com.github.wujichen158.ancientskybaubles.block.entity;

import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShoalSaltBlockEntity extends RegenerableBlockEntity {
    public ShoalSaltBlockEntity(BlockPos pos, BlockState state) {
        super(AncientSkyBaublesBlockEntities.SHOAL_SALT_BLOCK_ENTITY.get(), pos, state);
    }
}
