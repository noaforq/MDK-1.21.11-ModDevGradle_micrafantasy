package cloud.laboratory.n.micrafantasy.job;

import net.minecraft.nbt.CompoundTag;

public class JobData {
    // ジョブ情報
    private JobType jobType = JobType.NONE;
    private int level = 1;
    private int experience = 0;           // 現在レベル内の経験値（表示用）
    private int totalExperience = 0;      // 累計経験値（死亡ペナルティ等の基準）
    private int experienceToNextLevel = 100;

    // スタミナ（物理スキル消費）
    private float stamina = 100f;
    private float maxStamina = 100f;

    // マナ（魔法スキル消費）
    private float mana = 100f;
    private float maxMana = 100f;

    // リミットゲージ
    private float limitGauge = 0f;
    private float maxLimitGauge = 1000f;

    // ジョブスキルクールダウン（tick単位、1日=24000tick）
    private int jobSkillCooldown = 0;

    // スキルクールダウン（tick単位）
    private int[] skillCooldowns = new int[10];

    // 詠唱状態（-1 = 詠唱なし）
    private int castingSkillId = -1;
    private int castingTicksRemaining = 0;
    private int castingTicksTotal = 0;

    public JobData() {}

    // ---- Getters / Setters ----

    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public int getTotalExperience() { return totalExperience; }
    public void setTotalExperience(int totalExperience) { this.totalExperience = Math.max(0, totalExperience); }

    public int getExperienceToNextLevel() { return experienceToNextLevel; }
    public void setExperienceToNextLevel(int exp) { this.experienceToNextLevel = exp; }

    public float getStamina() { return stamina; }
    public void setStamina(float stamina) { this.stamina = Math.max(0, Math.min(stamina, maxStamina)); }

    public float getMaxStamina() { return maxStamina; }
    public void setMaxStamina(float maxStamina) { this.maxStamina = maxStamina; }

    public float getMana() { return mana; }
    public void setMana(float mana) { this.mana = Math.max(0, Math.min(mana, maxMana)); }

    public float getMaxMana() { return maxMana; }
    public void setMaxMana(float maxMana) { this.maxMana = maxMana; }

    public float getLimitGauge() { return limitGauge; }
    public void setLimitGauge(float limitGauge) { this.limitGauge = Math.max(0, Math.min(limitGauge, maxLimitGauge)); }

    public float getMaxLimitGauge() { return maxLimitGauge; }
    public void setMaxLimitGauge(float maxLimitGauge) { this.maxLimitGauge = maxLimitGauge; }

    public boolean isLimitBreakReady() { return limitGauge >= maxLimitGauge; }

    public int getJobSkillCooldown() { return jobSkillCooldown; }
    public void setJobSkillCooldown(int cooldown) { this.jobSkillCooldown = cooldown; }

    public int getSkillCooldown(int skillId) {
        if (skillId < 0 || skillId >= skillCooldowns.length) return 0;
        return skillCooldowns[skillId];
    }

    public void setSkillCooldown(int skillId, int cooldown) {
        if (skillId >= 0 && skillId < skillCooldowns.length) {
            skillCooldowns[skillId] = cooldown;
        }
    }

    /** EXP加算（累計に加算してレベルアップチェック） */
    public boolean addExperience(int amount) {
        totalExperience += amount;
        experience += amount;
        return tryLevelUp();
    }

    /** レベルアップ処理（cumulative EXP から何度もレベルアップ可能） */
    public boolean tryLevelUp() {
        if (jobType == JobType.NONE) return false;
        boolean leveled = false;
        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            level++;
            experienceToNextLevel = calculateNextLevelExp(level);
            maxStamina = 100f + (level - 1) * 20f;
            maxMana    = 100f + (level - 1) * 20f;
            stamina    = maxStamina;
            mana       = maxMana;
            leveled    = true;
        }
        return leveled;
    }

    /**
     * 死亡ペナルティ：累計EXPを半減してレベルを再計算する。
     * レベルを1に戻してから累計EXPを積み上げ直す。
     */
    public void onDeath() {
        totalExperience = totalExperience / 2;
        // レベル・EXPを初期化して累計から再計算
        level = 1;
        experience = totalExperience;
        experienceToNextLevel = calculateNextLevelExp(1);
        maxStamina = 100f;
        maxMana    = 100f;
        // 累計EXPを消費してレベルを積み上げ
        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            level++;
            experienceToNextLevel = calculateNextLevelExp(level);
            maxStamina = 100f + (level - 1) * 20f;
            maxMana    = 100f + (level - 1) * 20f;
        }
        stamina = maxStamina;
        mana    = maxMana;
    }

    private int calculateNextLevelExp(int level) {
        return 100 + (level - 1) * 50;
    }

    public int getCastingSkillId() { return castingSkillId; }
    public int getCastingTicksRemaining() { return castingTicksRemaining; }
    public int getCastingTicksTotal() { return castingTicksTotal; }

    public boolean isCasting() { return castingSkillId >= 0; }

    /** 詠唱開始 */
    public void startCasting(int skillId, int totalTicks) {
        this.castingSkillId = skillId;
        this.castingTicksRemaining = totalTicks;
        this.castingTicksTotal = totalTicks;
    }

    /** 詠唱キャンセル */
    public void cancelCasting() {
        this.castingSkillId = -1;
        this.castingTicksRemaining = 0;
        this.castingTicksTotal = 0;
    }

    /** tick毎クールダウン処理 */
    public void tickCooldowns() {
        if (jobSkillCooldown > 0) jobSkillCooldown--;
        for (int i = 0; i < skillCooldowns.length; i++) {
            if (skillCooldowns[i] > 0) skillCooldowns[i]--;
        }
        if (castingSkillId >= 0 && castingTicksRemaining > 0) {
            castingTicksRemaining--;
        }
    }

    /** 詠唱が完了したか（残りtick=0かつ詠唱中） */
    public boolean isCastComplete() {
        return castingSkillId >= 0 && castingTicksRemaining <= 0;
    }

    // ---- NBT シリアライズ ----

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("jobType", jobType.getId());
        tag.putInt("level", level);
        tag.putInt("experience", experience);
        tag.putInt("totalExperience", totalExperience);
        tag.putInt("experienceToNextLevel", experienceToNextLevel);
        tag.putFloat("stamina", stamina);
        tag.putFloat("maxStamina", maxStamina);
        tag.putFloat("mana", mana);
        tag.putFloat("maxMana", maxMana);
        tag.putFloat("limitGauge", limitGauge);
        tag.putFloat("maxLimitGauge", maxLimitGauge);
        tag.putInt("jobSkillCooldown", jobSkillCooldown);
        tag.putIntArray("skillCooldowns", skillCooldowns);
        tag.putInt("castingSkillId", castingSkillId);
        tag.putInt("castingTicksRemaining", castingTicksRemaining);
        tag.putInt("castingTicksTotal", castingTicksTotal);
        return tag;
    }

    public static JobData fromNbt(CompoundTag tag) {
        JobData data = new JobData();
        data.jobType = JobType.fromId(tag.getString("jobType").orElse("none"));
        data.level = tag.getInt("level").orElse(1);
        data.experience = tag.getInt("experience").orElse(0);
        data.totalExperience = tag.getInt("totalExperience").orElse(data.experience); // 旧セーブ互換
        data.experienceToNextLevel = tag.getInt("experienceToNextLevel").orElse(100);
        data.stamina = tag.getFloat("stamina").orElse(100f);
        data.maxStamina = tag.getFloat("maxStamina").orElse(100f);
        data.mana = tag.getFloat("mana").orElse(100f);
        data.maxMana = tag.getFloat("maxMana").orElse(100f);
        data.limitGauge = tag.getFloat("limitGauge").orElse(0f);
        data.maxLimitGauge = tag.getFloat("maxLimitGauge").orElse(1000f);
        data.jobSkillCooldown = tag.getInt("jobSkillCooldown").orElse(0);
        tag.getIntArray("skillCooldowns").ifPresent(arr -> {
            for (int i = 0; i < Math.min(arr.length, data.skillCooldowns.length); i++) {
                data.skillCooldowns[i] = arr[i];
            }
        });
        data.castingSkillId = tag.getInt("castingSkillId").orElse(-1);
        data.castingTicksRemaining = tag.getInt("castingTicksRemaining").orElse(0);
        data.castingTicksTotal = tag.getInt("castingTicksTotal").orElse(0);
        return data;
    }
}
