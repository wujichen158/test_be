package com.github.wujichen158.ancientskybaubles.block;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public abstract class RegenerableBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    public static final BooleanProperty HARVESTED = BooleanProperty.create("harvested");
//    protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D,
//            16.0D, 16.0D, 16.0D);

    public RegenerableBlock(Properties properties) {
        // 基岩强度，无法被推动，无碰撞，光照影响不覆盖该方块，不会进战利品表，其上不会生成生物
        super(properties.strength(-1.0F, 3600000.0F)
                .pushReaction(PushReaction.BLOCK)
                .noCollission()
                .noOcclusion()
                .noLootTable()
                .isValidSpawn((blockState, level, blockPos, entityType) -> false));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
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
        builder.add(FACING);
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
}
