package dev.jackraidenph.bouncyball;

import com.mojang.logging.LogUtils;
import dev.jackraidenph.bouncyball.client.renderer.ClientEvents;
import dev.jackraidenph.bouncyball.registry.EntityTypeRegistry;
import dev.jackraidenph.bouncyball.registry.ItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(BouncyBallMod.MODID)
public class BouncyBallMod {
    public static final String MODID = "bouncyball";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BouncyBallMod() {
        ItemRegistry.init();
        EntityTypeRegistry.init();

        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
}
