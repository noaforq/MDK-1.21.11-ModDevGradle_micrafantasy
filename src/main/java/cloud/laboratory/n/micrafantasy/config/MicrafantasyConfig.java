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

    /** デバッグモード時にログイン時にセットするジョブレベル */
    private static int debugJobLevel = 1;

    /** チャンクロード時にクリスタルブロック置換を行うか */
    private static boolean crystalReplaceEnabled = true;

    /** 1ブロックをクリスタルに置き換える確率（0.0〜1.0） */
    private static float crystalReplaceChance = 0.002f;

    /** 置換対象Y範囲 最小（絶対値） */
    private static int crystalReplaceMinY = -37;

    /** 置換対象Y範囲 最大（絶対値） */
    private static int crystalReplaceMaxY = 163;

    /** 1チャンク当たりの最大置換ブロック数（0=無制限） */
    private static int crystalReplaceMaxPerChunk = 3;

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

            crystalReplaceEnabled     = Boolean.parseBoolean(props.getProperty("crystal_replace_enabled", "true"));
            crystalReplaceChance      = parseFloatSafe(props.getProperty("crystal_replace_chance",   "0.002"), 0.002f);
            crystalReplaceMinY        = parseIntSafe(props.getProperty("crystal_replace_min_y",      "-37"),  -37);
            crystalReplaceMaxY        = parseIntSafe(props.getProperty("crystal_replace_max_y",      "163"),  163);
            crystalReplaceMaxPerChunk = parseIntSafe(props.getProperty("crystal_replace_max_per_chunk", "3"),  3);

            MicrafantasyMod.LOGGER.info("[Micrafantasy] Config loaded. debug_mode={}, crystal_replace_enabled={}",
                    debugMode, crystalReplaceEnabled);


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
            props.setProperty("crystal_replace_enabled",       String.valueOf(crystalReplaceEnabled));
            props.setProperty("crystal_replace_chance",        String.valueOf(crystalReplaceChance));
            props.setProperty("crystal_replace_min_y",         String.valueOf(crystalReplaceMinY));
            props.setProperty("crystal_replace_max_y",         String.valueOf(crystalReplaceMaxY));
            props.setProperty("crystal_replace_max_per_chunk", String.valueOf(crystalReplaceMaxPerChunk));

            try (OutputStream out = Files.newOutputStream(CONFIG_FILE)) {
                props.store(out,
                        "Micrafantasy MOD Configuration\n" +
                        "# debug_mode                  : true でデバッグモードを有効にします\n" +
                        "# debug_job_level             : デバッグ時に設定するジョブレベル\n" +
                        "# crystal_replace_enabled     : true でチャンクロード時に既存ブロックをクリスタルに置換します\n" +
                        "# crystal_replace_chance      : 1ブロックを置換する確率（0.0〜1.0）\n" +
                        "# crystal_replace_min_y       : 置換対象Y範囲 最小\n" +
                        "# crystal_replace_max_y       : 置換対象Y範囲 最大\n" +
                        "# crystal_replace_max_per_chunk : 1チャンク当たりの最大置換数（0=無制限）"
                );
            }
        } catch (IOException e) {
            MicrafantasyMod.LOGGER.error("[Micrafantasy] Failed to save config: {}", e.getMessage());
        }
    }

    // ---- Getters ----

    public static boolean isDebugMode()     { return debugMode; }
    public static int getDebugJobLevel()    { return debugJobLevel; }

    public static boolean isCrystalReplaceEnabled()  { return crystalReplaceEnabled; }
    public static float getCrystalReplaceChance()     { return crystalReplaceChance; }
    public static int getCrystalReplaceMinY()         { return crystalReplaceMinY; }
    public static int getCrystalReplaceMaxY()         { return crystalReplaceMaxY; }
    public static int getCrystalReplaceMaxPerChunk()  { return crystalReplaceMaxPerChunk; }

    // ---- Helpers ----

    private static int parseIntSafe(String value, int defaultValue) {
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private static float parseFloatSafe(String value, float defaultValue) {
        try { return Float.parseFloat(value.trim()); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
