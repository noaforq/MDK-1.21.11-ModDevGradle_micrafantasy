package cloud.laboratory.n.micrafantasy.client.keybind;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class ModKeyBindings {
    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath("micrafantasy", "micrafantasy"));

    public static final KeyMapping[] SKILL_KEYS = new KeyMapping[10];

    // ---- カメラ操作キー ----
    // テンキー+ (GLFW_KEY_KP_ADD=334)  : ズームイン（長押し）
    public static final KeyMapping CAMERA_ZOOM_IN = new KeyMapping(
            "key.micrafantasy.camera_zoom_in",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            334, CATEGORY);

    // テンキー- (GLFW_KEY_KP_SUBTRACT=333) : ズームアウト（長押し）
    public static final KeyMapping CAMERA_ZOOM_OUT = new KeyMapping(
            "key.micrafantasy.camera_zoom_out",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            333, CATEGORY);

    // テンキー* (GLFW_KEY_KP_MULTIPLY=332) : 視点切替
    public static final KeyMapping CAMERA_VIEW = new KeyMapping(
            "key.micrafantasy.camera_view",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            332, CATEGORY);

    // テンキー/ (GLFW_KEY_KP_DIVIDE=331) : ズームリセット
    public static final KeyMapping CAMERA_RESET = new KeyMapping(
            "key.micrafantasy.camera_reset",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            331, CATEGORY);

    // ---- カメラ上下左右キー（矢印キー）----
    // 上 (GLFW_KEY_UP=265)
    public static final KeyMapping CAMERA_UP = new KeyMapping(
            "key.micrafantasy.camera_up",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            265, CATEGORY);

    // 下 (GLFW_KEY_DOWN=264)
    public static final KeyMapping CAMERA_DOWN = new KeyMapping(
            "key.micrafantasy.camera_down",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            264, CATEGORY);

    // 左 (GLFW_KEY_LEFT=263)
    public static final KeyMapping CAMERA_LEFT = new KeyMapping(
            "key.micrafantasy.camera_left",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            263, CATEGORY);

    // 右 (GLFW_KEY_RIGHT=262)
    public static final KeyMapping CAMERA_RIGHT = new KeyMapping(
            "key.micrafantasy.camera_right",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            262, CATEGORY);

    static {
        // スキル0-9: テンキー0-9 (GLFW_KEY_KP_0=320, KP_1=321, ..., KP_9=329)
        for (int i = 0; i <= 9; i++) {
            SKILL_KEYS[i] = new KeyMapping("key.micrafantasy.skill_" + i,
                    KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
                    320 + i, CATEGORY);
        }
    }
}
