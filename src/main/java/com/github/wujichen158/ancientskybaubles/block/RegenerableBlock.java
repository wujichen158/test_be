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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class RegenerableBlock<R extends RegenerableBlockEntity> extends BaseEntityBlock {

    public static final BooleanProperty HARVESTED = BooleanProperty.create("harvested");
//    protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D,
//            16.0D, 16.0D, 16.0D);

    private final BlockEntityType<R> blockEntityType;

    public RegenerableBlock(BlockEntityType<R> blockEntityType, Properties properties) {
        // 基岩强度，无法被推动，无碰撞，不会进战利品表，其上不会生成生物
        super(properties.strength(-1.0F, 3600000.0F)
                .pushReaction(PushReaction.BLOCK)
                .noCollission()
                .noLootTable()
                .isValidSpawn((blockState, level, blockPos, entityType) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(HARVESTED, false));
        this.blockEntityType = blockEntityType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RegenerableBlockEntity(this.blockEntityType, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, this.blockEntityType, RegenerableBlockEntity::tick);
    }

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

    // BaseEntityBlock 已将 RenderShape 调整为 INVISIBLE，
    // 此处无需额外的处理


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HARVESTED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

//    @NotNull
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
//        return BASE_SHAPE;
//    }
//
//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return this.defaultBlockState().setValue(HARVESTED, false);
//    }
}
