package com.github.wujichen158.ancientskybaubles.client.cache;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedHashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClientRegenerableBlockEntityCache {

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

    public static void queryHarvestStatus() {

    }

    public static void addHarvestStatus(GlobalPos regenerableGlobalPos, boolean harvestStatus) {
        REGENERABLE_BE_CACHE.put(regenerableGlobalPos, harvestStatus);
    }

    public static void harvest() {

    }

    public static void regenerate(BlockEntity blockEntity) {
    }

    public static void onBreak(BlockEntity blockEntity) {
        GlobalPos globalPos = constructGlobalPos(blockEntity);
        REGENERABLE_BE_CACHE.remove(globalPos);
    }

    public void addHarvestedStatus(GlobalPos globalPos) {
        if (REGENERABLE_BE_CACHE.containsKey(globalPos)) {

        } else {
            // TODO: 发包
        }
    }

    private static GlobalPos constructGlobalPos(BlockEntity blockEntity) {
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
