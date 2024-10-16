package com.github.wujichen158.ancientskybaubles.register;

import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AncientSkyBaublesCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ANCIENTSKY_BAUBLES = register(
            "ancientskybaubles", Component.translatable("itemGroup.ancientskybaubles"), () -> new ItemStack(AncientSkyBaublesItems.MedalItems.ROUND_1ST_MEDAL),
            (parameters, output) -> AncientSkyBaublesItems.ItemEntry.ALL_ITEMS.forEach(output::accept)
    );

    public static void init(IEventBus bus) {
        REGISTER.register(bus);
    }

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<CreativeModeTab> register(String name, Component title, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator generator) {
        return REGISTER.register(name, () -> CreativeModeTab.builder().title(title).icon(icon).displayItems(generator).build());
    }

    private AncientSkyBaublesCreativeTabs() {

    }
}
