package dev.jackraidenph.bouncyball.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.jackraidenph.bouncyball.BouncyBallMod;
import dev.jackraidenph.bouncyball.entity.BouncyBallEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BouncyBallEntityRenderer extends EntityRenderer<BouncyBallEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BouncyBallMod.MODID, "textures/entities/bouncy_ball.png");

    public BouncyBallEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BouncyBallEntity entity, float p_113840_, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        RenderSystem.enableDepthTest();

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(this.getTextureLocation(entity)));

        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
        RenderSystem.setShaderTexture(0, this.getTextureLocation(entity));

        float size = 0.25f;

        for (Direction dir : Direction.values()) {
            poseStack.pushPose();

            Quaternion rotY = Vector3f.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F);
            Quaternion rotZ = Vector3f.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()));

            poseStack.mulPose(rotY);
            poseStack.mulPose(rotZ);

            poseStack.translate(-size / 2f, -size / 2f, -size / 2f);

            poseStack.translate(size / 2f, size / 2f, size / 2f);
            poseStack.translate(0, size / 2, 0);
            poseStack.mulPose(dir.getRotation());
            poseStack.translate(size / -2f, size / -2f, size / -2f);

            float f = 0f, s = f + size, u0 = 0, u1 = 0.5f, v0 = 0, v1 = 0.5f;

            Vec3i dn = dir.getNormal();
            Vector3f n = new Vector3f(dn.getX(), dn.getY(), dn.getZ());
            n.transform(rotY);
            n.transform(rotZ);

            Matrix4f matrix = poseStack.last().pose();
            Matrix3f normalMatrix = poseStack.last().normal();

            vertexconsumer.vertex(matrix, s, 0, f).color(1f, 1f, 1f, 1f).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, n.x(), n.y(), n.z()).endVertex();
            vertexconsumer.vertex(matrix, s, 0, s).color(1f, 1f, 1f, 1f).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, n.x(), n.y(), n.z()).endVertex();
            vertexconsumer.vertex(matrix, f, 0, s).color(1f, 1f, 1f, 1f).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, n.x(), n.y(), n.z()).endVertex();
            vertexconsumer.vertex(matrix, f, 0, f).color(1f, 1f, 1f, 1f).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, n.x(), n.y(), n.z()).endVertex();

            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, p_113840_, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BouncyBallEntity entity) {
        return TEXTURE;
    }
}
