package com.github.wujichen158.ancientskybaubles.block.entity;

import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.SyncHarvestDataPacket;
import com.github.wujichen158.ancientskybaubles.util.DayDateUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class RegenerableBlockEntity extends BlockEntity {

    // 客户端可见的BE状态，用于渲染
    @OnlyIn(Dist.CLIENT)
    public boolean harvestStatus = false;

    private LocalDate regenerateDate;


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

        ListTag listTag = tag.getList("harvested_players", 8);
        harvestedPlayers.clear();
        listTag.forEach(uuidTag -> harvestedPlayers.add(NbtUtils.loadUUID(uuidTag)));

        regenerateDate = DayDateUtil.daysToDate(tag.getInt("regenerate_time"));
    }

    @Nullable
    public SyncHarvestDataPacket getUpdatePacket(ServerPlayer player) {
        return new SyncHarvestDataPacket(hasHarvested(player), this.getBlockPos());
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
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate currentDate = currentTime.toLocalDate();
        //TODO: 改成使用配置
        LocalDateTime regenerateTime = LocalDateTime.now().withMinute(30);

        // 判断是否同一天。仅日期严格大于regenerateTime才继续
        if (!blockEntity.regenerateDate.isEqual(currentDate) && currentDate.isAfter(blockEntity.regenerateDate)) {
            if (currentTime.isAfter(regenerateTime)) {
                blockEntity.regenerate();
                blockEntity.regenerateDate = currentDate;
                System.out.println("已重置！");
            }
        }

    }

    public InteractionResult use(Player player) {
        if (harvestedPlayers.contains(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("regenerable_res.msg.already_harvested", ""));
        } else {
            onHarvest(player);
            // TODO: 填写资源名
            player.sendSystemMessage(Component.translatable("regenerable_res.msg.harvest", ""));
        }
        return InteractionResult.SUCCESS;
    }

    public void onHarvest(Player player) {
        harvestedPlayers.add(player.getUUID());
        setChanged();
    }

    public void regenerate() {
        this.harvestedPlayers.clear();
    }

    public boolean hasHarvested(Player player) {
        return harvestedPlayers.contains(player.getUUID());
    }
}
