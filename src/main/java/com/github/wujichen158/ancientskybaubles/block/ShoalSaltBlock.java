package com.github.wujichen158.ancientskybaubles.block;

import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType,
                AncientSkyBaublesBlockEntities.SHOAL_SALT_BLOCK_ENTITY.get(),
                ShoalSaltBlockEntity::tick);
    }
}
