package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientSkyBaublesBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);


    public static final RegistryObject<BlockEntityType<ShoalSaltBlockEntity>> SHOAL_SALT_BLOCK_ENTITY = REGISTER.register(
            "shoal_salt", () -> BlockEntityType.Builder.of(
                    ShoalSaltBlockEntity::new,
                    AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK.get()).build(null)
    );

    public static void init(IEventBus bus) {
        REGISTER.register(bus);
    }

    private AncientSkyBaublesBlockEntities() {
    }
}
