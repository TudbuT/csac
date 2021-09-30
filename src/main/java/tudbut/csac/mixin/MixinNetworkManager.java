package tudbut.csac.mixin;

import io.netty.channel.ChannelHandlerContext;
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

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"))
    private void dispatchPacket(final Packet<?> packet, CallbackInfo info) {
        EventHandler.onPacket(packet);
    }
    @Inject(method = "channelRead0", at = @At("TAIL"))
    private void dispatchPacket(ChannelHandlerContext context, final Packet<?> packet, CallbackInfo info) {
        EventHandler.onPacket(packet);
    }
}
