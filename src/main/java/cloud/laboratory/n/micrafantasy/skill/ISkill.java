package cloud.laboratory.n.micrafantasy.skill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface ISkill {
    int getId();
    String getName();
    SkillType getType();
    float getStaminaCost();
    float getManaCost();
    int getCooldownTicks();
    default int getCastTimeTicks() { return 0; }
    default int getUnlockLevel() { return 1; }
    @Nullable
    default InteractionHand swingHand() {
        return getType() == SkillType.PHYSICAL ? InteractionHand.MAIN_HAND : null;
    }
    /** サーバー側の追加条件チェック。 */
    default boolean canUse(ServerPlayer player) { return true; }
    /**
     * クライアント側の表示用条件チェック（HUDグレーアウト判定）。
     * 装備状態などを見るだけの軽量チェック。デフォルトは常に true。
     */
    default boolean canUseClient(Player player) { return true; }
    void execute(ServerPlayer player);
}

