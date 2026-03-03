package cloud.laboratory.n.micrafantasy.client.camera;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.client.keybind.ModKeyBindings;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

/**
 * カメラ操作ハンドラ（クライアント専用）
 *
 * キーバインド（デフォルト）：
 *   テンキー+  : ズームイン（長押し、離すと戻る）
 *   テンキー-  : ズームアウト（長押し、離すと戻る）
 *   テンキー*  : 視点切替（1人称 → 3人称後方 → 3人称前方 → 1人称）
 *   テンキー/  : ズームリセット
 *   矢印上     : カメラ上（ピッチ）
 *   矢印下     : カメラ下（ピッチ）
 *   矢印左     : カメラ左（ヨー）
 *   矢印右     : カメラ右（ヨー）
 */
@EventBusSubscriber(modid = MicrafantasyMod.MODID, value = Dist.CLIENT)
public class ClientCameraHandler {

    // ---- ズーム状態 ----
    private static float currentFovMultiplier = 1.0f;
    private static float targetFovMultiplier  = 1.0f;

    private static final float FOV_MIN    = 0.15f;
    private static final float FOV_MAX    = 2.0f;
    private static final float ZOOM_STEP  = 0.03f;
    private static final float LERP_SPEED = 0.25f;

    // ---- 上下左右回転速度（度/tick）----
    /** キー1tickあたりの回転量（度） */
    private static final float TURN_SPEED = 6.0f;
    /** ピッチの上限/下限（バニラ準拠） */
    private static final float PITCH_MAX  =  90.0f;
    private static final float PITCH_MIN  = -90.0f;

    // ----------------------------------------------------------------
    // ClientTick：毎tick キー状態を読んでカメラ値を更新
    // ----------------------------------------------------------------
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        // GUI表示中は操作しない
        if (mc.screen != null) return;

        // ---- ズームイン/アウト（長押し）----
        if (ModKeyBindings.CAMERA_ZOOM_IN.isDown()) {
            targetFovMultiplier = Math.max(FOV_MIN, targetFovMultiplier - ZOOM_STEP);
        } else if (ModKeyBindings.CAMERA_ZOOM_OUT.isDown()) {
            targetFovMultiplier = Math.min(FOV_MAX, targetFovMultiplier + ZOOM_STEP);
        } else {
            targetFovMultiplier = 1.0f;
        }
        currentFovMultiplier += (targetFovMultiplier - currentFovMultiplier) * LERP_SPEED;

        // ---- 視点切替（1回押し）----
        if (ModKeyBindings.CAMERA_VIEW.consumeClick()) {
            CameraType next = switch (mc.options.getCameraType()) {
                case FIRST_PERSON       -> CameraType.THIRD_PERSON_BACK;
                case THIRD_PERSON_BACK  -> CameraType.THIRD_PERSON_FRONT;
                case THIRD_PERSON_FRONT -> CameraType.FIRST_PERSON;
            };
            mc.options.setCameraType(next);
        }

        // ---- ズームリセット（1回押し）----
        if (ModKeyBindings.CAMERA_RESET.consumeClick()) {
            targetFovMultiplier  = 1.0f;
            currentFovMultiplier = 1.0f;
        }

        // ---- カメラ上下左右（長押し）: player の向きを直接変更 ----
        float yawDelta   = 0f;
        float pitchDelta = 0f;

        if (ModKeyBindings.CAMERA_LEFT.isDown())  yawDelta   -= TURN_SPEED;
        if (ModKeyBindings.CAMERA_RIGHT.isDown()) yawDelta   += TURN_SPEED;
        if (ModKeyBindings.CAMERA_UP.isDown())    pitchDelta -= TURN_SPEED;
        if (ModKeyBindings.CAMERA_DOWN.isDown())  pitchDelta += TURN_SPEED;

        if (yawDelta != 0f || pitchDelta != 0f) {
            float newYaw   = mc.player.getYRot() + yawDelta;
            float newPitch = Mth.clamp(mc.player.getXRot() + pitchDelta, PITCH_MIN, PITCH_MAX);
            mc.player.setYRot(newYaw);
            mc.player.setXRot(newPitch);
            // 前tick向きも同期（補間ちらつき防止）
            mc.player.yRotO   = newYaw;
            mc.player.xRotO   = newPitch;
        }
    }

    // ----------------------------------------------------------------
    // FOVイベント：補間済みFOVに乗算して適用
    // ----------------------------------------------------------------
    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        if (Math.abs(currentFovMultiplier - 1.0f) > 0.001f) {
            event.setNewFovModifier(event.getNewFovModifier() * currentFovMultiplier);
        }
    }

    // ----------------------------------------------------------------
    // カメラアングルイベント：ズーム中にわずかなロールでスコープ感を演出
    // ----------------------------------------------------------------
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        // ズームイン時に roll を微妙に揺らしてスコープ感を演出（任意）
        // 現在は無効（0）。必要なら有効化してください。
        // float rollEffect = (1.0f - currentFovMultiplier) * 0.5f;
        // event.setRoll(event.getRoll() + rollEffect);
    }

    // ----------------------------------------------------------------
    // マウススクロール補正：ズームイン中はスクロールをキャンセル
    // ----------------------------------------------------------------
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (isZooming()) {
            event.setCanceled(true);
        }
    }

    // ----------------------------------------------------------------
    // Getter
    // ----------------------------------------------------------------
    public static float getCurrentFovMultiplier() { return currentFovMultiplier; }
    public static boolean isZooming()             { return currentFovMultiplier < 0.95f; }
}
