package com.github.wujichen158.ancientskybaubles.block;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class RegenerableBlock extends BaseEntityBlock {

    public RegenerableBlock(Properties properties) {
        // 基岩硬度，无法被推动，不会进战利品表，其上不会生成生物
        super(properties.strength(-1.0F, 3600000.0F)
                .pushReaction(PushReaction.BLOCK)
                .noLootTable()
                .isValidSpawn((blockState, level, blockPos, entityType) -> false));
//        this.registerDefaultState(this.stateDefinition.any().setValue(HARVESTED, false));
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    // 处理方块被点击的交互逻辑
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof RegenerableBlockEntity blockEntity) {
                return blockEntity.use(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(HARVESTED);
//    }

    // 留给子类实现再生逻辑
//    public abstract void regenerate(Level world, BlockPos pos, BlockState state);
}
