package cloud.laboratory.n.micrafantasy.skill;
import net.minecraft.server.level.ServerPlayer;
public interface ISkill {
    int getId();
    String getName();
    SkillType getType();
    float getStaminaCost();
    float getManaCost();
    int getCooldownTicks();
    /** 詠唱時間（tick）。0なら即時発動。 */
    default int getCastTimeTicks() { return 0; }
    /** このスキルを使用するために必要なジョブレベル。 */
    default int getUnlockLevel() { return 1; }
     /**
     * スキル使用前の追加条件チェック。
     * 失敗時はプレイヤーにメッセージを表示して false を返す。
     * デフォルトは常に true。
     */
    default boolean canUse(ServerPlayer player) { return true; }
    void execute(ServerPlayer player);
}
