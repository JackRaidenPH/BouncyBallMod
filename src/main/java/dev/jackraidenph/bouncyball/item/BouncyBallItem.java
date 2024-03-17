package dev.jackraidenph.bouncyball.item;

import dev.jackraidenph.bouncyball.entity.BouncyBallEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BouncyBallItem extends Item {

    private static final int maxUseTicks = 40;

    public BouncyBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int untilUseEnd) {

        int duration = this.getUseDuration(itemStack) - untilUseEnd;
        float strength = (duration / (float) maxUseTicks) + 0.1f;

        float strNormalized = Math.min(strength, 1f);

        level.playSound(null,
                livingEntity.getX(),
                livingEntity.getY(),
                livingEntity.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            BouncyBallEntity snowball = new BouncyBallEntity(level, livingEntity);
            snowball.setItem(itemStack);
            snowball.shootFromRotation(
                    livingEntity,
                    livingEntity.getXRot(),
                    livingEntity.getYRot(),
                    0.0F,
                    1.5F * strNormalized,
                    1.0f);
            level.addFreshEntity(snowball);
        }

        if (livingEntity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        } else {
            itemStack.shrink(1);
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

}
