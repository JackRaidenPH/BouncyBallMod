package dev.jackraidenph.bouncyball.client.renderer;

import dev.jackraidenph.bouncyball.BouncyBallMod;
import dev.jackraidenph.bouncyball.registry.EntityTypeRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BouncyBallMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void setupEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeRegistry.BOUNCY_BALL.get(), BouncyBallEntityRenderer::new);
    }

}
