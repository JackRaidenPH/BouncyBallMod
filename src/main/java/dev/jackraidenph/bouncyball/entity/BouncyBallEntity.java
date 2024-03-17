package dev.jackraidenph.bouncyball.entity;

import dev.jackraidenph.bouncyball.registry.EntityTypeRegistry;
import dev.jackraidenph.bouncyball.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BouncyBallEntity extends ThrowableItemProjectile {

    public BouncyBallEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public BouncyBallEntity(Level level, LivingEntity livingEntity) {
        super(EntityTypeRegistry.BOUNCY_BALL.get(), livingEntity, level);
    }

    public BouncyBallEntity(Level level, double x, double y, double z) {
        super(EntityTypeRegistry.BOUNCY_BALL.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.BOUNCY_BALL_ITEM.get();
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        Vec3 hitLoc = hitResult.getLocation();
        Vec3 entityLoc = this.getPosition(0);
        Vec3 diff = hitLoc.subtract(entityLoc);

        BlockHitResult blockHitResult = this.level.clip(
                new ClipContext(
                        entityLoc,
                        hitLoc.add(diff),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this)
        );

        Vec3i normal = blockHitResult.getDirection().getNormal();

        if (this.level.isClientSide) {
            ParticleOptions particleoptions =
                    new BlockParticleOption(
                            ParticleTypes.BLOCK,
                            this.level.getBlockState(blockHitResult.getBlockPos())
                    );
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            Vec3 vec3normal = new Vec3(normal.getX(), normal.getY(), normal.getZ());

            Vec3 deltaVec = this.getDeltaMovement();

            Vec3 reflected = deltaVec
                    .subtract(deltaVec
                            .scale(2)
                            .multiply(vec3normal)
                            .multiply(vec3normal)
                            .scale(1 / (vec3normal.length() * vec3normal.length()))
                    ).scale(0.75);

            this.setDeltaMovement(reflected);

            if (this.getDeltaMovement().length() < 0.125) {
                ItemEntity item = new ItemEntity(
                        this.level,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        new ItemStack(ItemRegistry.BOUNCY_BALL_ITEM.get())
                );
                this.level.addFreshEntity(item);
                this.discard();
            }
        }

    }

}
