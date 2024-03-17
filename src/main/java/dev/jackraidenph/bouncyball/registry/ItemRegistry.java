package dev.jackraidenph.bouncyball.registry;

import dev.jackraidenph.bouncyball.BouncyBallMod;
import dev.jackraidenph.bouncyball.item.BouncyBallItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BouncyBallMod.MODID);

    public static final RegistryObject<BouncyBallItem> BOUNCY_BALL_ITEM = ITEMS.register("bouncy_ball",
            () -> new BouncyBallItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)
            )
    );

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
