package tudbut.csac.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @At("HEAD")
    private void dispatchPacket(final Packet<?> p_dispatchPacket_1_, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] p_dispatchPacket_2_) {
        
    }
}
