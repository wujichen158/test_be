package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.block.RegenerableBlock;
import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AncientSkyBaublesBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final class RegenerableBlocks {

        public static final BlockEntry<RegenerableBlock<RegenerableBlockEntity>> SHOAL_SALT_BLOCK = new BlockEntry<>(
                "shoal_salt", () -> BlockBehaviour.Properties.of()
                .noOcclusion()
                .mapColor(MapColor.SAND),
                AncientSkyBaublesBlockEntities.SHOAL_SALT_BLOCK_ENTITY.get(),
                RegenerableBlock::new
        );

        private RegenerableBlocks() {
        }

        private static void init() {
            AncientSkyBaublesItems.ItemEntry.register(SHOAL_SALT_BLOCK.getId().getPath(),
                    () -> new BlockItem(SHOAL_SALT_BLOCK.get(), new Item.Properties()));
        }
    }

    private AncientSkyBaublesBlocks() {
    }

    public static void init(IEventBus bus) {
        REGISTER.register(bus);

        RegenerableBlocks.init();
    }

    public static final class BlockEntry<T extends Block> implements Supplier<T>, ItemLike {
        private final RegistryObject<T> regObject;
        private final Supplier<BlockBehaviour.Properties> properties;

        public BlockEntry(String name, Supplier<BlockBehaviour.Properties> properties, Function<BlockBehaviour.Properties, T> make) {
            this.properties = properties;
            this.regObject = REGISTER.register(name, () -> make.apply(properties.get()));
        }

        public BlockEntry(String name, Supplier<BlockBehaviour.Properties> properties,
                          BlockEntityType<RegenerableBlockEntity> blockEntityType,
                          BiFunction<BlockEntityType<RegenerableBlockEntity>, BlockBehaviour.Properties, T> make) {
            this.properties = properties;
            this.regObject = REGISTER.register(name, () -> make.apply(blockEntityType, properties.get()));
        }

        @Override
        public T get() {
            return this.regObject.get();
        }

        public BlockState defaultBlockState() {
            return this.get().defaultBlockState();
        }

        public ResourceLocation getId() {
            return this.regObject.getId();
        }

        public BlockBehaviour.Properties getProperties() {
            return this.properties.get();
        }

        @Override
        public Item asItem() {
            return this.get().asItem();
        }
    }
}
