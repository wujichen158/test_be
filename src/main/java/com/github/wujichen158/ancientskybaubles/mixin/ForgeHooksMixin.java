package com.github.wujichen158.ancientskybaubles.mixin;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin {
    @Inject(method = "onBlockBreakEvent",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;")
    )
    private static void injectBreakBlockEntity(Level level, GameType gameType, ServerPlayer entityPlayer, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        handler$onBreakRegenerableBECallback$injectBreakBlockEntity(entityPlayer, level, pos);
    }

    @Unique
    private static void handler$onBreakRegenerableBECallback$injectBreakBlockEntity(ServerPlayer player, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof RegenerableBlockEntity regenerableBlockEntity) {
            Optional.ofNullable(regenerableBlockEntity.getUpdatePacket(player))
                    .ifPresent(packet -> AncientSkyBaublesNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(player)));
        }
    }
}
