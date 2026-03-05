package cloud.laboratory.n.micrafantasy.skill;
import cloud.laboratory.n.micrafantasy.job.JobData;
import cloud.laboratory.n.micrafantasy.job.JobType;
import cloud.laboratory.n.micrafantasy.network.ModNetwork;
import cloud.laboratory.n.micrafantasy.network.packet.SyncJobDataPayload;
import cloud.laboratory.n.micrafantasy.registry.ModAttachmentTypes;
import cloud.laboratory.n.micrafantasy.skill.paladin.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
public class SkillRegistry {

    /** ジョブ共通スキル（0: 通常攻撃、10: 通常防御）*/
    private static final Map<Integer, ISkill> COMMON_SKILLS = new HashMap<>();

    private static final Map<JobType, Map<Integer, ISkill>> JOB_SKILLS = new HashMap<>();

    static {
        // ── 共通スキル（ジョブ不問） ──
        COMMON_SKILLS.put(0,  new NormalAttackSkill());  // 通常攻撃
        COMMON_SKILLS.put(10, new NormalDefenseSkill()); // 通常防御

        // ── ナイト固有スキル ──
        Map<Integer, ISkill> paladinSkills = new HashMap<>();
        paladinSkills.put(1, new ProvokeSkill());        // 挑発
        paladinSkills.put(2, new ShieldBashSkill());     // シールド・バッシュ
        paladinSkills.put(3, new ClemencySkill());       // クレメンシー
        paladinSkills.put(4, new FastBladeSkill());      // ファスト・ブレード
        paladinSkills.put(5, new RiotSwordSkill());      // ライオットソード
        paladinSkills.put(6, new RageOfHaloneSkill());   // レイジ・オブ・ハルオーネ
        paladinSkills.put(7, new SentinelSkill());       // センチネル
        paladinSkills.put(8, new InvincibleSkill());     // インビンシブル
        paladinSkills.put(9, new LastBastionSkill());    // ラスト・バスティオン（LB）
        JOB_SKILLS.put(JobType.PALADIN, paladinSkills);
    }

    /**
     * キー入力時に呼ばれる。
     * スキル0・10は共通スキルとしてジョブ不問で発動する。
     */
    public static void executeSkill(ServerPlayer player, int skillId) {
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        JobType jobType = data.getJobType();

        // 共通スキル（0: 通常攻撃、10: 通常防御）はジョブ不問で発動
        ISkill skill = COMMON_SKILLS.get(skillId);
        if (skill == null) {
            // ジョブ固有スキル
            if (jobType == JobType.NONE) {
                player.displayClientMessage(Component.translatable("message.micrafantasy.no_job"), true);
                return;
            }
            Map<Integer, ISkill> skills = JOB_SKILLS.get(jobType);
            if (skills == null) return;
            skill = skills.get(skillId);
            if (skill == null) return;
        }

        // アンロックレベルチェック（共通スキルはLv1固定なので実質スキップ）
        if (data.getLevel() < skill.getUnlockLevel()) {
            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.skill_locked",
                            skill.getName(), skill.getUnlockLevel()), true);
            return;
        }

        // 追加使用条件チェック（装備など）
        if (!skill.canUse(player)) return;

        // 詠唱中に別スキルを押したらキャンセル
        if (data.isCasting()) {
            data.cancelCasting();
            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.cast_cancelled"), true);
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
            return;
        }

        // クールダウンチェック
        if (data.getSkillCooldown(skillId) > 0) {
            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.skill_cooldown"), true);
            return;
        }

        // リソース消費チェック（詠唱スキルは詠唱開始時に消費）
        if (!checkAndConsumeResource(player, data, skill)) return;

        // 詠唱時間がある場合は詠唱開始、ない場合は即時発動
        if (skill.getCastTimeTicks() > 0) {
            data.startCasting(skillId, skill.getCastTimeTicks());
            player.displayClientMessage(
                    Component.translatable("message.micrafantasy.casting_start", skill.getName()), true);
            ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
        } else {
            fireSkill(player, data, skill, skillId);
        }
    }

    /**
     * 詠唱完了時（ResourceTickHandlerから呼ばれる）に実際にスキルを発動する。
     */
    public static void fireCastedSkill(ServerPlayer player) {
        JobData data = player.getData(ModAttachmentTypes.JOB_DATA.get());
        if (!data.isCastComplete()) return;

        int skillId = data.getCastingSkillId();
        JobType jobType = data.getJobType();
        Map<Integer, ISkill> skills = JOB_SKILLS.get(jobType);
        if (skills == null) { data.cancelCasting(); return; }
        ISkill skill = skills.get(skillId);
        if (skill == null) { data.cancelCasting(); return; }

        data.cancelCasting();
        fireSkill(player, data, skill, skillId);
    }

    /** スキルを実際に発動してクールダウンをセットし同期する */
    private static void fireSkill(ServerPlayer player, JobData data, ISkill skill, int skillId) {
        skill.execute(player);
        data.setSkillCooldown(skillId, skill.getCooldownTicks());
        ModNetwork.sendToPlayer(player, new SyncJobDataPayload(data));
    }

    /** リソース消費チェック＆実際に消費する。失敗時はfalseを返す。 */
    private static boolean checkAndConsumeResource(ServerPlayer player, JobData data, ISkill skill) {
        switch (skill.getType()) {
            case PHYSICAL -> {
                if (data.getStamina() < skill.getStaminaCost()) {
                    player.displayClientMessage(
                            Component.translatable("message.micrafantasy.not_enough_stamina"), true);
                    return false;
                }
                data.setStamina(data.getStamina() - skill.getStaminaCost());
            }
            case MAGIC -> {
                if (data.getMana() < skill.getManaCost()) {
                    player.displayClientMessage(
                            Component.translatable("message.micrafantasy.not_enough_mana"), true);
                    return false;
                }
                data.setMana(data.getMana() - skill.getManaCost());
            }
            case LIMIT_BREAK -> {
                if (!data.isLimitBreakReady()) {
                    player.displayClientMessage(
                            Component.translatable("message.micrafantasy.limit_not_ready"), true);
                    return false;
                }
                data.setLimitGauge(0);
            }
            case JOB -> {
                if (data.getJobSkillCooldown() > 0) {
                    player.displayClientMessage(
                            Component.translatable("message.micrafantasy.job_skill_cooldown"), true);
                    return false;
                }
                data.setJobSkillCooldown(24000);
            }
        }
        return true;
    }

    public static ISkill getSkill(JobType jobType, int skillId) {
        // 共通スキルを優先
        ISkill common = COMMON_SKILLS.get(skillId);
        if (common != null) return common;
        // ジョブ固有スキル
        Map<Integer, ISkill> skills = JOB_SKILLS.get(jobType);
        if (skills == null) return null;
        return skills.get(skillId);
    }
}
