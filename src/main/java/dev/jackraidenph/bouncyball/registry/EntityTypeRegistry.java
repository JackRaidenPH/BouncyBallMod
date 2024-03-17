package dev.jackraidenph.bouncyball.registry;

import dev.jackraidenph.bouncyball.BouncyBallMod;
import dev.jackraidenph.bouncyball.entity.BouncyBallEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BouncyBallMod.MODID);

    public static final RegistryObject<EntityType<BouncyBallEntity>> BOUNCY_BALL = ENTITY_TYPES.register("bouncy_ball",
            () -> EntityType.Builder.<BouncyBallEntity>of(
                    BouncyBallEntity::new,
                    MobCategory.MISC)
            .sized(0.25F, 0.25F)
            //.setShouldReceiveVelocityUpdates(true)
            .clientTrackingRange(4)
            .updateInterval(1)
            .build(BouncyBallMod.MODID + ":bouncy_ball")
    );

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
