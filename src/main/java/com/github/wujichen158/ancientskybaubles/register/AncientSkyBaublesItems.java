package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.item.MedalCuriosItem;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class AncientSkyBaublesItems {
    /**
     * 物品注册器
     */
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static class MedalItems {
        public static final ItemEntry<MedalCuriosItem> ROUND_1ST_MEDAL = ItemEntry.register("round_1st_medal",
                () -> new MedalCuriosItem(new Item.Properties()));
//        public static final ItemEntry<MedalCuriosItem> ROUND_2ND_MEDAL = ItemEntry.register("round_2nd_medal",
//                () -> new MedalCuriosItem(new Item.Properties()));

        public static void init() {
        }
    }


    private AncientSkyBaublesItems() {
    }

    public static void init(IEventBus bus) {
        REGISTER.register(bus);

        MedalItems.init();
    }

    /**
     * 用于自动化注册物品的类
     *
     * @param <T>
     */
    public static class ItemEntry<T extends Item> implements Supplier<T>, ItemLike {

        public static final List<ItemEntry<? extends Item>> ALL_ITEMS = Lists.newArrayList();

        private final RegistryObject<T> regObject;

        private ItemEntry(RegistryObject<T> regObject) {
            this.regObject = regObject;
            ALL_ITEMS.add(this);
        }

        public static <T extends Item> ItemEntry<T> register(String name, Supplier<? extends T> make) {
            return new ItemEntry<>(REGISTER.register(name, make));
        }

        @Override
        public T get() {
            return this.regObject.get();
        }

        @Override
        public Item asItem() {
            return this.regObject.get();
        }

        public ResourceLocation getId() {
            return this.regObject.getId();
        }
    }
}
