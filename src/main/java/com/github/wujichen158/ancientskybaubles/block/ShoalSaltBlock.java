package com.github.wujichen158.ancientskybaubles.block;

import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ShoalSaltBlock extends RegenerableBlock {

    public ShoalSaltBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShoalSaltBlockEntity(pos, state);
    }
}
