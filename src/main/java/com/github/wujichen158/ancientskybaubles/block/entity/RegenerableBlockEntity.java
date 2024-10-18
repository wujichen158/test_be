package com.github.wujichen158.ancientskybaubles.block.entity;

import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.GenerableOnBreakPacket;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusResponsePacket;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.RegeneratePacket;
import com.github.wujichen158.ancientskybaubles.util.DayDateUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class RegenerableBlockEntity extends BlockEntity {

    // 客户端可见的BE状态，用于渲染
//    @OnlyIn(Dist.CLIENT)
//    public boolean harvestStatus = false;

    private LocalDate regenerateDate;

    @Override
    public boolean hasCustomOutlineRendering(Player player) {
        return super.hasCustomOutlineRendering(player);
    }

    // 记录方块是否已被开采
    protected final Set<UUID> harvestedPlayers = new HashSet<>();

    public RegenerableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        regenerateDate = LocalDate.now();
    }

    /**
     * 存储数据到 NBT
     *
     * @param tag
     */
    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag uuidList = new ListTag();
        harvestedPlayers.forEach(uuid -> uuidList.add(NbtUtils.createUUID(uuid)));
        tag.put("harvested_players", uuidList);

//        System.out.println("save!");

        tag.putInt("regenerate_time", DayDateUtil.dateToDays(regenerateDate));
    }

    /**
     * 从 NBT 读取数据
     *
     * @param tag
     */
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        ListTag listTag = tag.getList("harvested_players", Tag.TAG_INT_ARRAY);
        harvestedPlayers.clear();
        listTag.forEach(uuidTag -> harvestedPlayers.add(NbtUtils.loadUUID(uuidTag)));

//        System.out.println("load!");

        regenerateDate = DayDateUtil.daysToDate(tag.getInt("regenerate_time"));
    }

//    @Nullable
//    public SyncHarvestDataPacket getUpdatePacket(ServerPlayer player) {
//        return new SyncHarvestDataPacket(hasHarvested(player), this.getBlockPos());
//    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        // 方案1：直接客户端层面删除
        if (getLevel().isClientSide()) {
            RegenerableBlockEntityCache.onBreak(this);
        }
        // 方案2（备选）：每次删除时广播删除包
//        if (getLevel().isClientSide()) {
//            return;
//        }
//        getLevel().players().forEach(player -> {
//            AncientSkyBaublesNetwork.INSTANCE.send(new GenerableOnBreakPacket(
//                            RegenerableBlockEntityCache.constructGlobalPos(this)),
//                    PacketDistributor.PLAYER.with((ServerPlayer) player));
//        });
    }

    /**
     * 方块实体tick时执行的方法
     *
     * @param level       所处维度
     * @param blockPos    方块位置
     * @param state       方块状态
     * @param blockEntity 方块实体对象
     */
    public static void tick(Level level, BlockPos blockPos, BlockState state, RegenerableBlockEntity blockEntity) {
//        LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());
        if (level.isClientSide()) {
            return;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate currentDate = currentTime.toLocalDate();
        //TODO: 改成使用配置
        LocalDateTime regenerateTime = LocalDateTime.now().withMinute(10);

        // 判断是否同一天。仅日期严格大于regenerateTime才继续
        if (!blockEntity.regenerateDate.isEqual(currentDate) && currentDate.isAfter(blockEntity.regenerateDate)) {
            if (currentTime.isAfter(regenerateTime) || currentTime.isEqual(regenerateTime)) {
                blockEntity.regenerate(currentDate, level.players());
                System.out.println("已重置！");
            }
        }

    }

    public InteractionResult use(Player player) {
        if (harvestedPlayers.contains(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("regenerable_res.msg.already_harvested", getBlockState().getBlock().getName().getString()));
        } else {
            onHarvest(player);
            player.sendSystemMessage(Component.translatable("regenerable_res.msg.harvest", getBlockState().getBlock().getName().getString()));
        }
        return InteractionResult.SUCCESS;
    }

    public void onHarvest(Player player) {
        harvestedPlayers.add(player.getUUID());
        setChanged();
//        System.out.println("harvested!!!");
        AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusResponsePacket(
                        true, RegenerableBlockEntityCache.constructGlobalPos(this)),
                PacketDistributor.PLAYER.with((ServerPlayer) player));
    }

    public void regenerate(LocalDate currentDate, List<? extends Player> players) {
        this.harvestedPlayers.clear();
        this.regenerateDate = currentDate;
        setChanged();
//        System.out.println("regenerate!!!");
        players.forEach(player ->
                AncientSkyBaublesNetwork.INSTANCE.send(new RegeneratePacket(
                                false, RegenerableBlockEntityCache.constructGlobalPos(this)),
                        PacketDistributor.PLAYER.with((ServerPlayer) player)));
    }

    public boolean hasHarvested(Player player) {
        return harvestedPlayers.contains(player.getUUID());
    }
}
