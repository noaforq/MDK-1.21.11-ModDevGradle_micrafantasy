"""
FF14クリスタルをイメージしたジョブクリスタルブロックテクスチャ生成スクリプト
Minecraft用 16x16 PNG（ピクセルアート風・マイクラの四角い世界観に合わせたデザイン）

デザイン方針:
  - 16x16 ピクセルを1ドット単位で直接ペイント（スムージングなし）
  - アメジスト鉱石 + FF14クリスタルの青白い色味
  - 石ベース背景に、菱形（ひし形）クリスタルを複数配置
  - クリスタルは明→暗のピクセルグラデーションで立体感
  - 発光効果はドット単位の白いハイライト1点で表現
"""
from PIL import Image
import os

BASE = r"C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy"
OUT  = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "block")
os.makedirs(OUT, exist_ok=True)

# -----------------------------------------------------------------
# カラーパレット（FF14クリスタル × マイクラピクセルアート）
# -----------------------------------------------------------------
# 石ベース
S0 = (20,  22,  52, 255)   # 最暗・輪郭
S1 = (30,  32,  72, 255)   # 暗部
S2 = (42,  46,  95, 255)   # 中暗
S3 = (58,  65, 120, 255)   # 中間
S4 = (75,  85, 145, 255)   # 中明

# クリスタル（青白）
C0 = (25,  45, 110, 255)   # 最暗・影
C1 = (45,  80, 170, 255)   # 暗部
C2 = (75, 125, 215, 255)   # 中間
C3 = (120, 175, 245, 255)  # 明部
C4 = (175, 215, 255, 255)  # 明るい
C5 = (220, 240, 255, 255)  # 最明・ハイライト
CW = (245, 252, 255, 255)  # 輝点（白）

# サブクリスタル（少し紫味）
P1 = (55,  60, 155, 255)
P2 = (90, 100, 195, 255)
P3 = (135, 150, 230, 255)

# 透明（背景）
__ = (0, 0, 0, 0)

# -----------------------------------------------------------------
# 16x16 ピクセルマップ（行 y=0..15、列 x=0..15）
# -----------------------------------------------------------------
# 設計:
#   背景 = 深い宇宙ブルーの石（S0〜S4）
#   メインクリスタル = 中央やや左下に縦長菱形（C0〜CW）
#   サブクリスタル左上 = 小さい菱形（P1〜P3）
#   サブクリスタル右下 = 小さい菱形（P1〜P3）
#   ノイズ = 石テクスチャをドットで表現
# -----------------------------------------------------------------
PIXEL_MAP = [
    # 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
    [S0,  S1,  S2,  S1,  S0,  S1,  S2,  S1,  S0,  S1,  S2,  S1,  S0,  S1,  S2,  S0 ],# 0
    [S1,  S3,  S3,  S2,  S1,  S3,  S4,  S2,  S1,  S2,  S3,  S2,  P2,  S1,  S3,  S1 ],# 1
    [S2,  S3,  P2,  P3,  S2,  S3,  S3,  S1,  S2,  S3,  S2,  P1,  P3,  P2,  S2,  S2 ],# 2
    [S1,  S2,  P1,  P3,  P2,  S2,  S3,  S2,  S3,  S2,  S1,  P1,  P2,  P1,  S1,  S1 ],# 3
    [S0,  S2,  S1,  P2,  C0,  C1,  S2,  S3,  S2,  S3,  S2,  S1,  S2,  S1,  S2,  S0 ],# 4
    [S1,  S3,  S2,  S1,  C1,  C3,  C1,  S2,  S3,  S2,  S3,  S2,  S3,  S2,  S1,  S1 ],# 5
    [S2,  S2,  S3,  S2,  C0,  C2,  C4,  C2,  C0,  S3,  S2,  S3,  S2,  S3,  S2,  S2 ],# 6
    [S1,  S3,  S2,  S3,  S1,  C1,  C3,  C5,  C3,  C1,  S2,  S2,  S3,  S2,  S1,  S1 ],# 7
    [S0,  S2,  S3,  S2,  S2,  C0,  C2,  C4,  CW,  C3,  C1,  S3,  S2,  S1,  S2,  S0 ],# 8
    [S1,  S3,  S2,  S3,  S2,  S1,  C1,  C3,  C4,  C2,  C0,  S2,  S3,  S2,  S1,  S1 ],# 9
    [S2,  S2,  S3,  S2,  S3,  S2,  C0,  C2,  C3,  C1,  S3,  S2,  S2,  S3,  S2,  S2 ],#10
    [S1,  S3,  S2,  S3,  S2,  S3,  S1,  C1,  C2,  S2,  S3,  S2,  S1,  S2,  S3,  S1 ],#11
    [S0,  S2,  S3,  S2,  S3,  S2,  S3,  S1,  C0,  S3,  S2,  S3,  P2,  P3,  S2,  S0 ],#12
    [S1,  S3,  S2,  S1,  S2,  S3,  S2,  S3,  S1,  S2,  S3,  P1,  P3,  P2,  S3,  S1 ],#13
    [S2,  S2,  S3,  S2,  S3,  S2,  S3,  S2,  S2,  S3,  S1,  P1,  P2,  P1,  S2,  S2 ],#14
    [S0,  S1,  S2,  S3,  S2,  S1,  S2,  S3,  S2,  S1,  S2,  S1,  S2,  S1,  S2,  S0 ],#15
]

def make_pixel_crystal():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 255))
    pixels = img.load()
    for y, row in enumerate(PIXEL_MAP):
        for x, color in enumerate(row):
            pixels[x, y] = color
    return img

if __name__ == "__main__":
    print("=== クリスタルブロックテクスチャ生成（ピクセルアート版）===")

    img = make_pixel_crystal()

    out_path = os.path.join(OUT, "job_crystal_block.png")
    img.save(out_path, "PNG")
    print(f"  OK: {out_path}")

    # 確認用 x8 拡大版（128x128）
    preview = img.resize((128, 128), Image.NEAREST)
    preview_path = os.path.join(OUT, "job_crystal_block_preview.png")
    preview.save(preview_path, "PNG")
    print(f"  OK (preview x8): {preview_path}")

    print("=== 完了 ===")

