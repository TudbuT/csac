package tudbut.csac.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tudbut.csac.events.EventHandler;

import javax.annotation.Nullable;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "dispatchPacket", at = @At("TAIL"))
    private void dispatchPacket(final Packet<?> p_dispatchPacket_1_, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] p_dispatchPacket_2_, CallbackInfo info) {
        EventHandler.onPacket(p_dispatchPacket_1_);
    }
}
