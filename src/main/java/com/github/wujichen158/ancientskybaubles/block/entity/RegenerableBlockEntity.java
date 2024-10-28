package com.github.wujichen158.ancientskybaubles.block.entity;

import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusResponsePacket;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.RegeneratePacket;
import com.github.wujichen158.ancientskybaubles.util.DayDateUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public abstract class RegenerableBlockEntity extends BlockEntity {

    // 客户端可见的BE状态，用于渲染
    @OnlyIn(Dist.CLIENT)
    public Boolean harvestStatus;

    @OnlyIn(Dist.CLIENT)
    public boolean packetedFlag;

    private LocalDate regenDate;

    private byte regenHour = 4;
    private byte regenMinute = 0;

    /**
     * 记录方块是否已被开采
     */
    protected final Set<UUID> harvestedPlayers = new HashSet<>();

    protected List<Pair<ItemStack, Integer>> weightedOutputs = new ArrayList<>();
    protected List<Float> harvestProbs = new ArrayList<>(Arrays.asList(100f, 60f, 30f));
    protected transient int totalWeight;
    protected boolean allIn = false;

    public RegenerableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        // 需要将再生日期提前一天，否则若放置时间还未到再生时间的话，今日就无法再生了
        regenDate = LocalDate.now().minusDays(1);
        totalWeight = this.weightedOutputs.stream().mapToInt(Pair::getSecond).sum();
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
        this.harvestedPlayers.forEach(uuid -> uuidList.add(NbtUtils.createUUID(uuid)));
        tag.put("harvested_players", uuidList);

        ListTag outputsList = new ListTag();
        this.weightedOutputs.forEach(pair -> {
            CompoundTag outputTag = new CompoundTag();
            pair.getFirst().save(outputTag);
            outputTag.putInt("weight", pair.getSecond());
            outputsList.add(outputTag);
        });
        tag.put("weighted_outputs", outputsList);

        ListTag probList = new ListTag();
        this.harvestProbs.forEach(prob -> probList.add(FloatTag.valueOf(prob)));
        tag.put("harvest_probs", probList);

        tag.putBoolean("all_in", this.allIn);

        tag.putInt("regenerate_date", DayDateUtil.dateToDays(this.regenDate));
        tag.putByte("regenerate_hour", this.regenHour);
        tag.putByte("regenerate_minute", this.regenMinute);
    }

    /**
     * 从 NBT 读取数据
     *
     * @param tag
     */
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        ListTag uuidListTag = tag.getList("harvested_players", Tag.TAG_INT_ARRAY);
        this.harvestedPlayers.clear();
        uuidListTag.forEach(uuidTag -> this.harvestedPlayers.add(NbtUtils.loadUUID(uuidTag)));

        this.weightedOutputs.clear();
        this.totalWeight = 0;
        ListTag outputsListTag = tag.getList("weighted_outputs", Tag.TAG_INT_ARRAY);
        outputsListTag.forEach(outputTag -> {
            if (outputTag instanceof CompoundTag outputTagCompound) {
                int weight = outputTagCompound.getInt("weight");
                this.weightedOutputs.add(Pair.of(ItemStack.of(outputTagCompound), weight));
                this.totalWeight += weight;
            }
        });

        this.harvestProbs.clear();
        ListTag probListTag = tag.getList("harvest_probs", Tag.TAG_INT_ARRAY);
        probListTag.forEach(prob -> {
            if (prob instanceof FloatTag floatProb) {
                this.harvestProbs.add(floatProb.getAsFloat());
            }
        });

        this.allIn = tag.getBoolean("all_in");

        this.regenDate = DayDateUtil.daysToDate(tag.getInt("regenerate_date"));
        this.regenHour = tag.getByte("regenerate_hour");
        this.regenMinute = tag.getByte("regenerate_minute");
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

        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        LocalTime regenerateTime = LocalTime.of(blockEntity.regenHour, blockEntity.regenMinute);

        // 判断当前日期是否在再生日期之后
        if (currentDate.isAfter(blockEntity.regenDate)) {
            // 如果当前时间在再生时间之后或相等，则触发再生
            if (!currentTime.isBefore(regenerateTime)) {
                blockEntity.regenerate(currentDate, level.players());
                // TODO: 替换为更可靠的日志
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
        AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusResponsePacket(true, this.getBlockPos()),
                PacketDistributor.PLAYER.with((ServerPlayer) player));

        if (this.totalWeight > 0) {
            this.harvestProbs.forEach(prob -> {
                if (Math.random() * 100 < prob) {
                    // 比较以下两种方法：
//                    player.addItem(itemStack);
//                    ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                    if (this.allIn) {
                        this.weightedOutputs.stream().map(Pair::getFirst).toList()
                                .forEach(itemStack -> ItemHandlerHelper.giveItemToPlayer(player, itemStack));
                    } else {
                        Optional.ofNullable(calOutput())
                                .filter(ItemStack::isEmpty)
                                .ifPresent(itemStack -> ItemHandlerHelper.giveItemToPlayer(player, itemStack));
                    }
                }
            });
        }
    }

    /**
     * 按照权重随机选取收割产物中的一个并给予玩家
     *
     * @return 可能的产物。若无产物则返回 null
     */
    private ItemStack calOutput() {
        int randomWeight = (new Random()).nextInt(this.totalWeight);
        int currentWeight = 0;
        for (Pair<ItemStack, Integer> weightedOutput : this.weightedOutputs) {
            currentWeight += weightedOutput.getSecond();
            if (randomWeight < currentWeight) {
                return weightedOutput.getFirst();
            }
        }
        return null;
    }

    public void regenerate(LocalDate currentDate, List<? extends Player> players) {
        this.harvestedPlayers.clear();
        this.regenDate = currentDate;
        setChanged();
//        System.out.println("regenerate!!!");
        players.forEach(player ->
                AncientSkyBaublesNetwork.INSTANCE.send(new RegeneratePacket(false, this.getBlockPos()),
                        PacketDistributor.PLAYER.with((ServerPlayer) player)));
    }

    public boolean hasHarvested(Player player) {
        return harvestedPlayers.contains(player.getUUID());
    }
}
