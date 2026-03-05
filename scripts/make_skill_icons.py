"""
FFXIVのパラディンアイコンをMinecraftのスキルアイコン(64x64 PNG)に変換するスクリプト
"""
from PIL import Image, ImageDraw, ImageFilter
import os, shutil

BASE   = r"C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy"
PLD    = os.path.join(BASE, "FFXIVIcons Battle(PvE)", "01_PLD")
TANK   = os.path.join(PLD, "TankRollAction")
OUT    = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "gui")
SIZE   = 64  # 出力サイズ

os.makedirs(OUT, exist_ok=True)

# スキルID → (FFXIVアイコンパス, 枠色 RGBA)
# 枠色: 物理=金, 魔法=青, ジョブ=紫, LB=オレンジ, 防御=シアン
GOLD   = (220, 180,  40, 255)
BLUE   = ( 60, 130, 220, 255)
PURPLE = (160,  60, 220, 255)
ORANGE = (255, 140,   0, 255)
CYAN   = ( 40, 200, 200, 255)
WHITE  = (220, 220, 220, 255)

SKILLS = [
    # (output_name,              source_path,                                   border_color)
    ("skill_0_normal_attack",   os.path.join(BASE, "FFXIVIcons JobIcons", "00_ROLE", "DPSRole.png"), GOLD),
    ("skill_1_provoke",         os.path.join(TANK, "provoke.png"),              GOLD),
    ("skill_2_shield_bash",     os.path.join(PLD,  "shield_bash.png"),          GOLD),
    ("skill_3_clemency",        os.path.join(PLD,  "clemency.png"),             BLUE),
    ("skill_4_fast_blade",      os.path.join(PLD,  "fast_blade.png"),           GOLD),
    ("skill_5_riot_sword",      os.path.join(PLD,  "riot_blade.png"),           GOLD),
    ("skill_6_rage_of_halone",  os.path.join(PLD,  "rage_of_halone.png"),       GOLD),
    ("skill_7_sentinel",        os.path.join(PLD,  "sentinel.png"),             BLUE),
    ("skill_8_invincible",      os.path.join(PLD,  "hallowed_ground.png"),      PURPLE),
    ("skill_9_last_bastion",    os.path.join(BASE, "FFXIVIcons MainCommand (Others)", "02_General", "limit_break.png"), ORANGE),
    ("skill_10_normal_defense", os.path.join(BASE, "FFXIVIcons JobIcons", "00_ROLE", "TankRole.png"), CYAN),
]

def make_icon(src_path, dst_path, border_color):
    """
    1. ソースをリサイズ
    2. 角丸+枠線をオーバーレイ
    3. 保存
    """
    img = Image.open(src_path).convert("RGBA")
    # 中央クロップしてから正方形にする
    w, h = img.size
    side = min(w, h)
    left = (w - side) // 2
    top  = (h - side) // 2
    img = img.crop((left, top, left + side, top + side))
    img = img.resize((SIZE, SIZE), Image.LANCZOS)

    # 角丸マスク
    mask = Image.new("L", (SIZE, SIZE), 0)
    draw_m = ImageDraw.Draw(mask)
    radius = 8
    draw_m.rounded_rectangle([0, 0, SIZE - 1, SIZE - 1], radius=radius, fill=255)
    rounded = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    rounded.paste(img, mask=mask)

    # 枠線（2px）
    draw = ImageDraw.Draw(rounded)
    bw = 2
    draw.rounded_rectangle([bw//2, bw//2, SIZE - bw//2 - 1, SIZE - bw//2 - 1],
                            radius=radius, outline=border_color, width=bw)

    rounded.save(dst_path, "PNG")
    print(f"  OK: {os.path.basename(dst_path)}")

print("=== スキルアイコン変換開始 ===")
for name, src, col in SKILLS:
    dst = os.path.join(OUT, name + ".png")
    if not os.path.exists(src):
        print(f"  SKIP (src not found): {src}")
        continue
    make_icon(src, dst, col)
print("=== 完了 ===")

# ---- ジョブストーン ----
print("=== ジョブストーン変換開始 ===")
ITEM_OUT = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "item")
os.makedirs(ITEM_OUT, exist_ok=True)

JOB_STONES = [
    # (output_name,           source_path,                                              border_color)
    # ソース: https://cdn.eriones.com/img/icon/ls_nq/67fd81c209e.png（ナイトの証）
    ("paladin_job_stone", os.path.join(BASE, "scripts", "paladin_job_stone_src.png"), (255, 215, 0, 255)),
]

for name, src, col in JOB_STONES:
    dst = os.path.join(ITEM_OUT, name + ".png")
    if not os.path.exists(src):
        print(f"  SKIP (src not found): {src}")
        continue
    make_icon(src, dst, col)
print("=== 完了 ===")

