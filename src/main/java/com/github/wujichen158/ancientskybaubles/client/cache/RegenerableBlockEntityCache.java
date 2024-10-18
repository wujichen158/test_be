package com.github.wujichen158.ancientskybaubles.client.cache;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegenerableBlockEntityCache {

    @OnlyIn(Dist.CLIENT)
    private static final LRUMap<GlobalPos, Boolean> REGENERABLE_BE_CACHE = new LRUMap<>(100);

    /**
     * 通过 BE 构造 GlobalPos 并从缓存中进行查询
     *
     * @param blockEntity 待查询的 BE
     * @return 其当前是否位于缓存中
     */
    public static boolean hasHarvestStatus(BlockEntity blockEntity) {
        return REGENERABLE_BE_CACHE.containsKey(constructGlobalPos(blockEntity));
    }

    /**
     * 不额外创建 Optional 对象以提高效率
     *
     * @param blockEntity
     * @return
     */
    public static boolean queryHarvestStatus(BlockEntity blockEntity) {
        Boolean res = REGENERABLE_BE_CACHE.get(constructGlobalPos(blockEntity));
        return res != null ? res : false;
    }

    public static void addHarvestStatus(GlobalPos regenerableGlobalPos, boolean harvestStatus) {
        REGENERABLE_BE_CACHE.put(regenerableGlobalPos, harvestStatus);
    }

    public static void regenerate(GlobalPos regenerableGlobalPos, boolean harvestStatus) {
        REGENERABLE_BE_CACHE.computeIfPresent(regenerableGlobalPos, (key, oldValue) -> harvestStatus);
    }

    public static void onBreak(BlockEntity blockEntity) {
        onBreak(constructGlobalPos(blockEntity));
    }

    public static void onBreak(GlobalPos globalPos) {
        REGENERABLE_BE_CACHE.remove(globalPos);
    }

    public static void clear() {
        REGENERABLE_BE_CACHE.clear();
    }

    public static GlobalPos constructGlobalPos(BlockEntity blockEntity) {
        return GlobalPos.of(blockEntity.getLevel().dimension(), blockEntity.getBlockPos());
    }

    private static class LRUMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        // 通常初始容量会设定为期望大小的一半到三分之二
        public LRUMap(int maxSize) {
            super((int) (maxSize * 0.64), 0.75f, true);
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }
    }
}
