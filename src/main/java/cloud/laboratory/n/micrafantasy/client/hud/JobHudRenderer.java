package cloud.laboratory.n.micrafantasy.client.hud;
import cloud.laboratory.n.micrafantasy.MicrafantasyMod;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.skill.ISkill;
import cloud.laboratory.n.micrafantasy.skill.SkillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = MicrafantasyMod.MODID, value = Dist.CLIENT)
public class JobHudRenderer {

    private static final int SLOT_SIZE   = 16;   // スロット1個のピクセルサイズ（小型化）
    private static final int ICON_SIZE   = 12;   // アイコン画像サイズ
    private static final int ICON_OFFSET = (SLOT_SIZE - ICON_SIZE) / 2;
    private static final int SLOT_GAP    = 2;
    private static final int TOTAL_SLOTS = 9;    // スロット1〜9（0は使用しない）
    private static final int TOTAL_WIDTH = TOTAL_SLOTS * (SLOT_SIZE + SLOT_GAP) - SLOT_GAP;

    private static final int BAR_WIDTH  = 80;    // バー幅（小型化）
    private static final int BAR_HEIGHT = 4;     // バー高さ（小型化）
    private static final int BAR_GAP    = 2;

    // 左中央バー表示位置
    private static final int BARS_LEFT_MARGIN = 8;   // 画面左端からの余白

    // カラー定義
    private static final int COL_SLOT_BG       = 0xCC111118;
    private static final int COL_SLOT_BORDER   = 0xFF666677;
    private static final int COL_SLOT_LB_READY = 0xFFFFEE44;
    private static final int COL_COOLDOWN      = 0xBB000011;
    private static final int COL_STAMINA_BG    = 0xFF0A2010;
    private static final int COL_STAMINA       = 0xFF33EE55;
    private static final int COL_MANA_BG       = 0xFF080A22;
    private static final int COL_MANA          = 0xFF3366FF;
    private static final int COL_LIMIT_BG      = 0xFF1A1000;
    private static final int COL_LIMIT         = 0xFFDDAA00;
    private static final int COL_LIMIT_READY   = 0xFFFFEE44;
    private static final int COL_LABEL         = 0xFFBBBBCC;
    private static final int COL_EXP_BG        = 0xFF100800;
    private static final int COL_EXP           = 0xFFFF8800;
    private static final int COL_JOB_NAME      = 0xFFFFDD66;
    private static final int COL_CAST_BG       = 0xFF0A0A20;
    private static final int COL_CAST          = 0xFF88AAFF;
    private static final int COL_CAST_BORDER   = 0xFF4466CC;

    // 未解放スキル用
    private static final int COL_LOCKED_OVERLAY = 0xCC000000; // 暗いオーバーレイ
    private static final int COL_LOCKED_BORDER  = 0xFF443333; // 暗い枠

    // スキルアイコンテクスチャ (gui/skill_N_xxx.png) ※インデックス0は未使用ダミー
    private static final Identifier[] SKILL_ICONS = {
        null, // 0: 未使用
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_1_provoke.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_2_shield_bash.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_3_clemency.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_4_fast_blade.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_5_riot_sword.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_6_rage_of_halone.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_7_sentinel.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_8_invincible.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_9_last_bastion.png"),
    };

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        JobData data = mc.player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() == JobType.NONE) return;

        GuiGraphics g = event.getGuiGraphics();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        // スキルスロット（画面上部中央、スロット1〜9）
        int slotX = (sw - TOTAL_WIDTH) / 2;
        int slotY = 4;
        renderSkillSlots(g, mc, data, slotX, slotY);

        // リソースバー＋ジョブ情報（画面左中央）
        int barsX = BARS_LEFT_MARGIN + 14; // ラベル幅分オフセット
        int barsY = sh / 2 - 30;          // 画面縦中央より少し上
        renderResourceBars(g, mc, data, barsX, barsY);

        // ジョブ名・レベル・EXPバー（バー群の下）
        int jobInfoY = barsY + (BAR_HEIGHT + BAR_GAP) * 3 + 4;
        renderJobInfo(g, mc, data, barsX, jobInfoY);

        // 詠唱バー（左中央付近）
        if (data.isCasting()) {
            renderCastBar(g, mc, data, sw, sh);
        }
    }

    private static void renderSkillSlots(GuiGraphics g, Minecraft mc, JobData data, int startX, int y) {
        int playerLevel = data.getLevel();

        // スロット1〜9のみ描画（0は使用しない）
        for (int slot = 1; slot <= 9; slot++) {
            int drawIndex = slot - 1; // 描画位置インデックス（0始まり）
            int x = startX + drawIndex * (SLOT_SIZE + SLOT_GAP);

            ISkill skill = SkillRegistry.getSkill(data.getJobType(), slot);

            // スロット背景
            g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, COL_SLOT_BG);

            // スキルが未登録（空きスロット）
            if (skill == null) {
                g.renderOutline(x, y, SLOT_SIZE, SLOT_SIZE, 0xFF333333);
                g.drawString(mc.font, String.valueOf(slot), x + 1, y + SLOT_SIZE - 8, 0xFF333333, false);
                continue;
            }

            boolean locked = playerLevel < skill.getUnlockLevel();

            // アイコン描画
            if (SKILL_ICONS[slot] != null) {
                if (locked) {
                    g.blit(RenderPipelines.GUI_TEXTURED,
                           SKILL_ICONS[slot],
                           x + ICON_OFFSET, y + ICON_OFFSET,
                           0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE,
                           0x44FFFFFF);
                } else {
                    g.blit(RenderPipelines.GUI_TEXTURED,
                           SKILL_ICONS[slot],
                           x + ICON_OFFSET, y + ICON_OFFSET,
                           0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
                }
            }

            // スロット枠
            g.renderOutline(x, y, SLOT_SIZE, SLOT_SIZE,
                    locked ? COL_LOCKED_BORDER : COL_SLOT_BORDER);

            // ロック時オーバーレイ
            if (locked) {
                g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, COL_LOCKED_OVERLAY);
            }

            // クールダウンオーバーレイ（LBスロット除く）
            if (!locked && slot != 9) {
                int cd = data.getSkillCooldown(slot);
                if (cd > 0) {
                    int maxCd = skill.getCooldownTicks();
                    float ratio = (float) cd / Math.max(1, maxCd);
                    int overlayH = (int)(SLOT_SIZE * ratio);
                    g.fill(x, y + SLOT_SIZE - overlayH, x + SLOT_SIZE, y + SLOT_SIZE, COL_COOLDOWN);
                    int secs = (cd + 19) / 20;
                    String cdStr = secs + "s";
                    int tw = mc.font.width(cdStr);
                    g.drawString(mc.font, cdStr, x + (SLOT_SIZE - tw) / 2, y + SLOT_SIZE / 2 - 3, 0xFFFFFFFF, true);
                }
            }

            // LB準備完了強調枠
            if (slot == 9 && data.isLimitBreakReady()) {
                g.renderOutline(x - 1, y - 1, SLOT_SIZE + 2, SLOT_SIZE + 2, COL_SLOT_LB_READY);
                g.renderOutline(x - 2, y - 2, SLOT_SIZE + 4, SLOT_SIZE + 4, COL_SLOT_LB_READY & 0x88FFFFFF);
            }

            // キー番号
            g.drawString(mc.font, String.valueOf(slot), x + 1, y + SLOT_SIZE - 8,
                    locked ? 0xFF555555 : 0xFFAAAAAA, false);
        }
    }

    private static void renderResourceBars(GuiGraphics g, Minecraft mc, JobData data, int x, int y) {
        renderBar(g, mc, x, y, data.getStamina(), data.getMaxStamina(),
                COL_STAMINA_BG, COL_STAMINA, "ST");
        y += BAR_HEIGHT + BAR_GAP;
        renderBar(g, mc, x, y, data.getMana(), data.getMaxMana(),
                COL_MANA_BG, COL_MANA, "MP");
        y += BAR_HEIGHT + BAR_GAP;
        int limitCol = data.isLimitBreakReady() ? COL_LIMIT_READY : COL_LIMIT;
        renderBar(g, mc, x, y, data.getLimitGauge(), data.getMaxLimitGauge(),
                COL_LIMIT_BG, limitCol, "LB");
    }

    private static void renderBar(GuiGraphics g, Minecraft mc, int x, int y,
                                   float current, float max, int bgCol, int fgCol, String label) {
        g.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, bgCol);
        if (max > 0) {
            int fill = (int)(BAR_WIDTH * Math.min(1f, current / max));
            if (fill > 0) g.fill(x, y, x + fill, y + BAR_HEIGHT, fgCol);
        }
        g.drawString(mc.font, label, x - 13, y - 1, fgCol, true);
        String val = (int)current + "/" + (int)max;
        int tw = mc.font.width(val);
        g.drawString(mc.font, val, x + BAR_WIDTH - tw, y - 1, COL_LABEL, false);
    }

    private static void renderJobInfo(GuiGraphics g, Minecraft mc, JobData data, int x, int y) {
        String jobName = data.getJobType().name().charAt(0)
                + data.getJobType().name().substring(1).toLowerCase();
        String levelStr = "Lv." + data.getLevel();
        String header = jobName + "  " + levelStr;
        int hw = mc.font.width(header);
        g.drawString(mc.font, jobName, x + (BAR_WIDTH - hw) / 2, y, COL_JOB_NAME, true);
        int jobNameW = mc.font.width(jobName + "  ");
        g.drawString(mc.font, levelStr, x + (BAR_WIDTH - hw) / 2 + jobNameW, y, 0xFFFFFFFF, true);

        int expBarY = y + 10;
        renderBar(g, mc, x, expBarY,
                data.getExperience(), data.getExperienceToNextLevel(),
                COL_EXP_BG, COL_EXP, "EX");
    }

    private static void renderCastBar(GuiGraphics g, Minecraft mc, JobData data, int sw, int sh) {
        final int CAST_BAR_W = 100;  // 小型化
        final int CAST_BAR_H = 5;
        // 画面左中央付近
        int cx = BARS_LEFT_MARGIN + 14;
        int cy = sh / 2 + 10;

        int total     = data.getCastingTicksTotal();
        int remaining = data.getCastingTicksRemaining();
        float progress = (total > 0) ? (float)(total - remaining) / total : 1f;

        g.fill(cx - 1, cy - 1, cx + CAST_BAR_W + 1, cy + CAST_BAR_H + 1, COL_CAST_BORDER);
        g.fill(cx, cy, cx + CAST_BAR_W, cy + CAST_BAR_H, COL_CAST_BG);
        int fill = (int)(CAST_BAR_W * progress);
        if (fill > 0) g.fill(cx, cy, cx + fill, cy + CAST_BAR_H, COL_CAST);

        ISkill skill = SkillRegistry.getSkill(data.getJobType(), data.getCastingSkillId());
        String label = (skill != null) ? skill.getName() : "Casting...";
        int lw = mc.font.width(label);
        g.drawString(mc.font, label, cx + (CAST_BAR_W - lw) / 2, cy - 10, COL_CAST, true);

        float secsLeft = remaining / 20f;
        String timeStr = String.format("%.1fs", secsLeft);
        int tw = mc.font.width(timeStr);
        g.drawString(mc.font, timeStr, cx + CAST_BAR_W - tw, cy + CAST_BAR_H + 2, 0xFFCCCCFF, false);
    }
}
