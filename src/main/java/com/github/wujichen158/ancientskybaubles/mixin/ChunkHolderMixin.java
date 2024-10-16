package com.github.wujichen158.ancientskybaubles.mixin;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin {

    @Inject(method = "broadcastBlockEntity",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"),
            cancellable = true)
    private void injectBroadcastBlockEntity(List<ServerPlayer> p_288988_, Level p_289005_, BlockPos p_288981_, CallbackInfo ci) {
        this.handler$broadcastRegenerableBECallback$injectBroadcastBlockEntity(p_288988_, p_289005_, p_288981_, ci);
    }

    @Unique
    private void handler$broadcastRegenerableBECallback$injectBroadcastBlockEntity(List<ServerPlayer> players, Level level, BlockPos blockPos, CallbackInfo ci) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof RegenerableBlockEntity regenerableBlockEntity) {
            players.forEach(player -> Optional.ofNullable(regenerableBlockEntity.getUpdatePacket(player))
                    .ifPresent(packet -> AncientSkyBaublesNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(player))));
            ci.cancel();
        }
    }
}
