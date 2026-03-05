package cloud.laboratory.n.micrafantasy.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

/**
 * Micrafantasy キーバインド定義
 *
 * デフォルト値は options.txt の設定値をそのまま使用。
 *
 * ---- キー割り当て ----
 *  スキル 0〜9      : テンキー 0〜9  (KP_0=320 〜 KP_9=329)
 *  スキル .         : テンキー .     (KP_DECIMAL=330)
 *  カメラ ズームイン : テンキー +     (KP_ADD=334)
 *  カメラ ズームアウト: テンキー -    (KP_SUBTRACT=333)
 *  カメラ 視点切替   : テンキー *     (KP_MULTIPLY=332)
 *  カメラ ズームリセット: テンキー /  (KP_DIVIDE=331)
 *  カメラ 上（ピッチ）: ↓キー        (DOWN=264)
 *  カメラ 下（ピッチ）: ↑キー        (UP=265)
 *  カメラ 左（ヨー） : Qキー          (Q=81)
 *  カメラ 右（ヨー） : Eキー          (E=69)
 */
public class ModKeyBindings {

    /** キーバインドカテゴリ */
    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath("micrafantasy", "micrafantasy"));

    // ================================================================
    // スキルキー  (slot 0〜9 = SKILL_KEYS[0〜9], slot「.」= SKILL_KEYS[10])
    // ================================================================
    public static final KeyMapping[] SKILL_KEYS = new KeyMapping[11];

    static {
        // スキル 0〜9: テンキー 0〜9 (KP_0=320, KP_1=321, ..., KP_9=329)
        for (int i = 0; i <= 9; i++) {
            SKILL_KEYS[i] = new KeyMapping(
                    "key.micrafantasy.skill_" + i,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    320 + i, CATEGORY);
        }
        // スキル「.」: テンキー . (KP_DECIMAL=330)
        SKILL_KEYS[10] = new KeyMapping(
                "key.micrafantasy.skill_dot",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                330, CATEGORY);
    }

    // ================================================================
    // カメラ操作キー
    // ================================================================

    /** テンキー＋ (KP_ADD=334): ズームイン */
    public static final KeyMapping CAMERA_ZOOM_IN = new KeyMapping(
            "key.micrafantasy.camera_zoom_in",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            334, CATEGORY);

    /** テンキー－ (KP_SUBTRACT=333): ズームアウト */
    public static final KeyMapping CAMERA_ZOOM_OUT = new KeyMapping(
            "key.micrafantasy.camera_zoom_out",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            333, CATEGORY);

    /** テンキー＊ (KP_MULTIPLY=332): 視点切替 */
    public static final KeyMapping CAMERA_VIEW = new KeyMapping(
            "key.micrafantasy.camera_view",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            332, CATEGORY);

    /** テンキー／ (KP_DIVIDE=331): ズームリセット */
    public static final KeyMapping CAMERA_RESET = new KeyMapping(
            "key.micrafantasy.camera_reset",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            331, CATEGORY);

    /** ↓キー (DOWN=264): カメラ上（ピッチ上向き） */
    public static final KeyMapping CAMERA_UP = new KeyMapping(
            "key.micrafantasy.camera_up",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            264, CATEGORY);

    /** ↑キー (UP=265): カメラ下（ピッチ下向き） */
    public static final KeyMapping CAMERA_DOWN = new KeyMapping(
            "key.micrafantasy.camera_down",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            265, CATEGORY);

    /** Qキー (Q=81): カメラ左（ヨー左） */
    public static final KeyMapping CAMERA_LEFT = new KeyMapping(
            "key.micrafantasy.camera_left",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            81, CATEGORY);

    /** Eキー (E=69): カメラ右（ヨー右） */
    public static final KeyMapping CAMERA_RIGHT = new KeyMapping(
            "key.micrafantasy.camera_right",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            69, CATEGORY);
}
