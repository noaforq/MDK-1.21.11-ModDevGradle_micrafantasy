package cloud.laboratory.n.micrafantasy.config;

import cloud.laboratory.n.micrafantasy.MicrafantasyMod;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

/**
 * MOD設定ファイル管理
 * 設定ファイル: config/micrafantasy/config.properties
 */
public class MicrafantasyConfig {

    private static final Path CONFIG_DIR  = Paths.get("config", "micrafantasy");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("config.properties");

    // ---- 設定値 ----
    /** デバッグモード有効フラグ */
    private static boolean debugMode = false;

    /**
     * デバッグモード時にログイン時にセットするジョブレベル。
     * 0 以下の場合はレベルを変更しない。
     */
    private static int debugJobLevel = 1;

    /** 設定を読み込む（存在しない場合はデフォルト値でファイルを生成） */
    public static void load() {
        try {
            Files.createDirectories(CONFIG_DIR);

            if (!Files.exists(CONFIG_FILE)) {
                save();
                MicrafantasyMod.LOGGER.info("[Micrafantasy] Config created: {}", CONFIG_FILE.toAbsolutePath());
                return;
            }

            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(CONFIG_FILE)) {
                props.load(in);
            }

            debugMode     = Boolean.parseBoolean(props.getProperty("debug_mode",      "false"));
            debugJobLevel = parseIntSafe(props.getProperty("debug_job_level", "1"), 1);

            MicrafantasyMod.LOGGER.info("[Micrafantasy] Config loaded. debug_mode={}, debug_job_level={}",
                    debugMode, debugJobLevel);


        } catch (IOException e) {
            MicrafantasyMod.LOGGER.error("[Micrafantasy] Failed to load config: {}", e.getMessage());
        }
    }

    /** 現在の設定値をファイルに書き出す */
    public static void save() {
        try {
            Files.createDirectories(CONFIG_DIR);

            Properties props = new Properties();
            props.setProperty("debug_mode",      String.valueOf(debugMode));
            props.setProperty("debug_job_level", String.valueOf(debugJobLevel));

            try (OutputStream out = Files.newOutputStream(CONFIG_FILE)) {
                props.store(out,
                        "Micrafantasy MOD Configuration\n" +
                        "# debug_mode      : true でデバッグモードを有効にします\n" +
                        "#   - ログイン時にすべてのジョブストーンを配布します\n" +
                        "#   - ログイン時にすべてのジョブのレベルを debug_job_level に設定します\n" +
                        "#   - ログイン時にすべてのジョブのカレント経験値を 0 にします\n" +
                        "# debug_job_level : デバッグ時に設定するジョブレベル（1以上の整数）"
                );
            }
        } catch (IOException e) {
            MicrafantasyMod.LOGGER.error("[Micrafantasy] Failed to save config: {}", e.getMessage());
        }
    }

    // ---- Getters ----

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static int getDebugJobLevel() {
        return debugJobLevel;
    }

    // ---- Helpers ----

    private static int parseIntSafe(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
