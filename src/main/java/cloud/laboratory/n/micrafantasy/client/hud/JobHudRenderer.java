package cloud.laboratory.n.micrafantasy.client.hud;
import cloud.laboratory.n.micrafantasy.client.event.KeyInputHandler;
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

    // ---- スロット ----
    private static final int SLOT_SIZE   = 11;
    private static final int ICON_SIZE   = 9;
    private static final int ICON_OFFSET = (SLOT_SIZE - ICON_SIZE) / 2;
    private static final int SLOT_GAP    = 0;   // 負値=スロット同士を重ねる
    private static final int TOTAL_SLOTS = 11;
    private static final int TOTAL_WIDTH = TOTAL_SLOTS * (SLOT_SIZE + SLOT_GAP) - SLOT_GAP;

    // ---- バー ----
    private static final int BAR_WIDTH  = 54;
    private static final int BAR_HEIGHT = 3;
    private static final int BAR_GAP    = 2;

    // ---- フォントスケール（基準×0.8） ----
    private static final float FONT_SCALE_SMALL = 0.5f  * 0.8f;  // 0.40
    private static final float FONT_SCALE_KEY   = 0.5f  * 0.8f;  // 0.40
    private static final float FONT_SCALE_JOB   = 0.6f  * 0.8f;  // 0.48
    private static final float FONT_SCALE_CD    = 0.45f * 0.8f;  // 0.36

    // ---- 画面左余白 ----
    private static final int BARS_LEFT_MARGIN = 8;

    // ---- カラー ----
    private static final int COL_SLOT_BG        = 0xCC111118;
    private static final int COL_SLOT_BORDER    = 0xFF666677;
    private static final int COL_SLOT_LB_READY  = 0xFFFFEE44;
    private static final int COL_COOLDOWN       = 0xBB000011;
    private static final int COL_STAMINA_BG     = 0xFF0A2010;
    private static final int COL_STAMINA        = 0xFF33EE55;
    private static final int COL_MANA_BG        = 0xFF080A22;
    private static final int COL_MANA           = 0xFF3366FF;
    private static final int COL_LIMIT_BG       = 0xFF1A1000;
    private static final int COL_LIMIT          = 0xFFDDAA00;
    private static final int COL_LIMIT_READY    = 0xFFFFEE44;
    private static final int COL_LABEL          = 0xFFBBBBCC;
    private static final int COL_EXP_BG         = 0xFF100800;
    private static final int COL_EXP            = 0xFFFF8800;
    private static final int COL_JOB_NAME       = 0xFFFFDD66;
    private static final int COL_CAST_BG        = 0xFF0A0A20;
    private static final int COL_CAST           = 0xFF88AAFF;
    private static final int COL_CAST_BORDER    = 0xFF4466CC;
    private static final int COL_LOCKED_OVERLAY = 0xCC000000;
    private static final int COL_LOCKED_BORDER  = 0xFF443333;

    // ---- スキルアイコン ----
    private static final Identifier[] SKILL_ICONS = {
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_0_normal_attack.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_1_provoke.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_2_shield_bash.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_3_clemency.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_4_fast_blade.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_5_riot_sword.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_6_rage_of_halone.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_7_sentinel.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_8_invincible.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_9_last_bastion.png"),
        Identifier.fromNamespaceAndPath(MicrafantasyMod.MODID, "textures/gui/skill_10_normal_defense.png"),
    };

    // 表示順: 0, .(=10), 1〜9
    private static final int[] SLOT_ORDER = { 0, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private static String keyLabel(int slotId) {
        return (slotId == 10) ? "." : String.valueOf(slotId);
    }

    // ================================================================
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        JobData data = mc.player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (data.getJobType() == JobType.NONE) return;

        GuiGraphics g = event.getGuiGraphics();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        // スキルスロット（画面上部中央）
        int slotX = (sw - TOTAL_WIDTH) / 2;
        renderSkillSlots(g, mc, data, slotX, 4);

        // 左中央のUI基点
        int baseX = BARS_LEFT_MARGIN + 14;
        int baseY = sh / 2 - 20;

        // 上：ジョブ名 + EXバー
        renderJobInfo(g, mc, data, baseX, baseY);

        // 下：ST / MP / LB バー
        int barsY = baseY + 14;
        renderResourceBars(g, mc, data, baseX, barsY);

        // 詠唱バー
        if (data.isCasting()) {
            renderCastBar(g, mc, data, sh);
        }
    }

    // ================================================================
    // スキルスロット描画
    // ================================================================
    private static void renderSkillSlots(GuiGraphics g, Minecraft mc, JobData data, int startX, int y) {
        int playerLevel = data.getLevel();

        for (int drawIndex = 0; drawIndex < SLOT_ORDER.length; drawIndex++) {
            int slotId = SLOT_ORDER[drawIndex];
            int x = startX + drawIndex * (SLOT_SIZE + SLOT_GAP);

            ISkill skill = SkillRegistry.getSkill(data.getJobType(), slotId);

            g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, COL_SLOT_BG);

            if (skill == null) {
                g.renderOutline(x, y, SLOT_SIZE, SLOT_SIZE, 0xFF333333);
                continue;
            }

            boolean locked = playerLevel < skill.getUnlockLevel();

            // アイコン
            g.blit(RenderPipelines.GUI_TEXTURED, SKILL_ICONS[slotId],
                   x + ICON_OFFSET, y + ICON_OFFSET,
                   0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE,
                   locked ? 0x44FFFFFF : 0xFFFFFFFF);

            // 枠
            g.renderOutline(x, y, SLOT_SIZE, SLOT_SIZE,
                    locked ? COL_LOCKED_BORDER : COL_SLOT_BORDER);

            // ロックオーバーレイ
            if (locked) {
                g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, COL_LOCKED_OVERLAY);
            }

            // ── LBスロット（9）専用の視覚フィードバック ──
            if (slotId == 9 && !locked) {
                if (data.isLimitBreakReady()) {
                    // 満タン：sin波で点滅する金色グロー
                    float pulse = (float)(Math.sin(System.currentTimeMillis() / 300.0) * 0.5 + 0.5); // 0.0〜1.0
                    int alpha   = (int)(0x44 + 0xBB * pulse); // 0x44〜0xFF
                    int glowCol = (alpha << 24) | 0xFFEE00;
                    g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, glowCol);
                    // 外枠点滅
                    int borderAlpha = (int)(0x88 + 0x77 * pulse);
                    int borderCol   = (borderAlpha << 24) | 0xFFEE44;
                    g.renderOutline(x - 1, y - 1, SLOT_SIZE + 2, SLOT_SIZE + 2, borderCol);
                    g.renderOutline(x - 2, y - 2, SLOT_SIZE + 4, SLOT_SIZE + 4, (borderCol & 0x44FFFFFF));
                } else {
                    // 未満タン：下から金色でゲージ割合分オーバーレイ（暗い部分が残量）
                    float lbRatio = data.getMaxLimitGauge() > 0
                            ? data.getLimitGauge() / data.getMaxLimitGauge() : 0f;
                    // 暗い部分（未充填）を上から描画
                    int emptyH = (int)(SLOT_SIZE * (1f - lbRatio));
                    if (emptyH > 0) {
                        g.fill(x, y, x + SLOT_SIZE, y + emptyH, 0xBB000011);
                    }
                    // パーセント表示
                    String pctStr = (int)(lbRatio * 100) + "%";
                    float pctPx = mc.font.width(pctStr) * FONT_SCALE_CD;
                    float pctX  = x + (SLOT_SIZE - pctPx) / 2f;
                    float pctY  = y + (SLOT_SIZE - mc.font.lineHeight * FONT_SCALE_CD) / 2f;
                    drawStringScaled(g, mc, pctStr, pctX, pctY, FONT_SCALE_CD, 0xFFDDAA00, true);
                }
            }

            // クールダウンオーバーレイ（LB除く）
            if (!locked && slotId != 9) {
                int cd = data.getSkillCooldown(slotId);
                if (cd > 0) {
                    int maxCd = skill.getCooldownTicks();
                    float ratio = (float) cd / Math.max(1, maxCd);
                    int overlayH = (int)(SLOT_SIZE * ratio);
                    g.fill(x, y + SLOT_SIZE - overlayH, x + SLOT_SIZE, y + SLOT_SIZE, COL_COOLDOWN);
                    String cdStr = (cd + 19) / 20 + "s";
                    float cdPx = mc.font.width(cdStr) * FONT_SCALE_CD;
                    float cdX  = x + (SLOT_SIZE - cdPx) / 2f;
                    float cdY  = y + (SLOT_SIZE - mc.font.lineHeight * FONT_SCALE_CD) / 2f;
                    drawStringScaled(g, mc, cdStr, cdX, cdY, FONT_SCALE_CD, 0xFFFFFFFF, true);
                } else {
                    // クールダウン外でもリソース不足・使用条件不満足なら赤オーバーレイ
                    boolean stInsufficient = skill.getStaminaCost() > 0 && data.getStamina() < skill.getStaminaCost();
                    boolean mpInsufficient = skill.getManaCost()    > 0 && data.getMana()    < skill.getManaCost();
                    boolean cannotUse      = mc.player != null && !skill.canUseClient(mc.player);
                    if (stInsufficient || mpInsufficient || cannotUse) {
                        // 半透明の赤オーバーレイ
                        g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x88FF2222);
                    }
                }
            }


            // 押下ハイライト（キーを押している間、スロットを明るく表示）
            if (slotId == KeyInputHandler.pressedSlot) {
                g.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x55FFFFFF);
                g.renderOutline(x, y, SLOT_SIZE, SLOT_SIZE, 0xFFFFFFFF);
            }

            // キー番号：スロット右下に中央揃えで重ねて表示
            String kl = keyLabel(slotId);
            float klPx = mc.font.width(kl) * FONT_SCALE_KEY;
            float klH  = mc.font.lineHeight * FONT_SCALE_KEY;
            // スロット下端に揃え、横方向は右端から内側に配置
            drawStringScaled(g, mc, kl,
                    x + SLOT_SIZE - klPx - 1,
                    y + SLOT_SIZE - klH,
                    FONT_SCALE_KEY,
                    locked ? 0xFF555555 : 0xFFCCCCCC, false);
        }
    }

    // ================================================================
    // ジョブ名 + EXバー（上段）
    // ================================================================
    private static void renderJobInfo(GuiGraphics g, Minecraft mc, JobData data, int x, int y) {
        String jobName = data.getJobType().name().charAt(0)
                + data.getJobType().name().substring(1).toLowerCase();
        String lvStr = " Lv." + data.getLevel();

        drawStringScaled(g, mc, jobName, x, y, FONT_SCALE_JOB, COL_JOB_NAME, true);
        float nameW = mc.font.width(jobName) * FONT_SCALE_JOB;
        drawStringScaled(g, mc, lvStr, x + nameW, y, FONT_SCALE_JOB, 0xFFFFFFFF, false);

        int expY = y + (int)(mc.font.lineHeight * FONT_SCALE_JOB) + 1;
        renderBar(g, mc, x, expY, data.getExperience(), data.getExperienceToNextLevel(),
                COL_EXP_BG, COL_EXP, "EX");
    }

    // ================================================================
    // ST / MP / LB バー（下段）
    // ================================================================
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

    // ================================================================
    // バー1本描画
    // ================================================================
    private static void renderBar(GuiGraphics g, Minecraft mc, int x, int y,
                                   float current, float max, int bgCol, int fgCol, String label) {
        g.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, bgCol);
        if (max > 0) {
            int fill = (int)(BAR_WIDTH * Math.min(1f, current / max));
            if (fill > 0) g.fill(x, y, x + fill, y + BAR_HEIGHT, fgCol);
        }
        drawStringScaled(g, mc, label, x - 13, y, FONT_SCALE_SMALL, fgCol, true);
        String val = (int)current + "/" + (int)max;
        float valW = mc.font.width(val) * FONT_SCALE_SMALL;
        drawStringScaled(g, mc, val, x + BAR_WIDTH - valW, y, FONT_SCALE_SMALL, COL_LABEL, false);
    }

    // ================================================================
    // 詠唱バー
    // ================================================================
    private static void renderCastBar(GuiGraphics g, Minecraft mc, JobData data, int sh) {
        final int CAST_BAR_W = 67;
        final int CAST_BAR_H = 3;
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
        float lw = mc.font.width(label) * FONT_SCALE_SMALL;
        drawStringScaled(g, mc, label, cx + (CAST_BAR_W - lw) / 2f, cy - 7, FONT_SCALE_SMALL, COL_CAST, true);

        String timeStr = String.format("%.1fs", remaining / 20f);
        float tw = mc.font.width(timeStr) * FONT_SCALE_SMALL;
        drawStringScaled(g, mc, timeStr, cx + CAST_BAR_W - tw, cy + CAST_BAR_H + 1, FONT_SCALE_SMALL, 0xFFCCCCFF, false);
    }

    // ================================================================
    // スケール付き文字描画（Matrix3x2fStack: pushMatrix/popMatrix/translate/scale）
    // ================================================================
    private static void drawStringScaled(GuiGraphics g, Minecraft mc,
                                          String text, float anchorX, float anchorY,
                                          float scale, int color, boolean shadow) {
        var pose = g.pose();
        pose.pushMatrix();
        pose.translate(anchorX, anchorY);
        pose.scale(scale, scale);
        g.drawString(mc.font, text, 0, 0, color, shadow);
        pose.popMatrix();
    }
}
