"""
アイテムテクスチャ再生成 - 中央配置版
"""
from PIL import Image
import os

BASE      = r"C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy"
OUT_ITEM  = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "item")
OUT_BLOCK = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "block")

_ = (0,0,0,0)

# クリスタル
CC0=(30,60,140,255); CC1=(55,100,200,255); CC2=(90,150,235,255)
CC3=(150,200,255,255); CCW=(230,245,255,255)
# ポーション
PO0=(20,10,30,255);  PO1=(60,40,80,255);   PO2=(80,55,100,255)
PO3=(140,60,60,255); PO4=(200,80,80,255);  PO5=(240,120,120,255)
POW=(255,200,200,255); POG=(120,200,100,255); POB=(180,220,180,255)
# 薬草
GR1=(30,80,20,255);  GR2=(50,120,30,255);  GR3=(80,160,50,255)
GR4=(110,190,70,255); GR5=(150,220,100,255); GRY=(200,230,150,255)
GRF=(200,170,60,255); GRS=(160,130,40,255)

def save_png(pixel_map, path):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px  = img.load()
    for y, row in enumerate(pixel_map):
        for x, color in enumerate(row):
            px[x, y] = color
    xs = [x for x in range(16) for y in range(16) if px[x,y][3]>0]
    ys = [y for x in range(16) for y in range(16) if px[x,y][3]>0]
    img.save(path, "PNG")
    if xs:
        print(f"  saved: {os.path.basename(path)}  x={min(xs)}-{max(xs)} y={min(ys)}-{max(ys)} center=({(min(xs)+max(xs))/2:.1f},{(min(ys)+max(ys))/2:.1f})")
    else:
        print(f"  saved: {os.path.basename(path)} (empty)")

# クリスタルの欠片: bboxがx=2-12, y=0-12 → center=(7.0,6.0) → y+2シフトで中央寄り
cs = [
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  CCW,_,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  CC3,CC3,CC2,_,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  CC2,CC3,CCW,CC3,CC1,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  CC1,CC2,CC3,CC3,CC2,CC1,CC0,_,  _,  _,  _,  _],
    [_,  _,  _,  CC0,CC1,CC2,CCW,CC3,CC2,CC1,CC0,CC0,_,  _,  _,  _],
    [_,  _,  CC0,CC1,CC2,CC3,CC3,CC2,CC1,CC1,CC0,CC0,CC0,_,  _,  _],
    [_,  _,  CC0,CC1,CC2,CC3,CCW,CC2,CC1,CC0,CC0,CC0,_,  _,  _,  _],
    [_,  _,  CC1,CC2,CC3,CC2,CC1,CC1,CC0,CC0,CC0,_,  _,  _,  _,  _],
    [_,  _,  CC0,CC1,CC2,CC1,CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  CC0,CC1,CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  CC0,CC0,_,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  CC0,_,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
]

# 回復ポーション: bboxがx=2-13, y=0-14 → center=(7.5,7.0) → y+1シフトで中央寄り
po = [
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  PO0,PO0,PO0,PO0,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  PO0,PO1,PO2,PO2,PO1,PO0,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  PO0,PO1,POB,POG,PO2,PO0,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  PO0,PO0,PO0,PO0,PO0,PO0,PO0,PO0,_,  _,  _,  _],
    [_,  _,  _,  PO0,PO1,PO2,POB,POG,POG,POB,PO2,PO1,PO0,_,  _,  _],
    [_,  _,  PO0,PO1,PO2,PO3,PO4,PO5,POW,PO5,PO4,PO2,PO1,PO0,_,  _],
    [_,  _,  PO0,PO1,PO3,PO4,PO5,POW,PO5,PO4,PO3,PO3,PO1,PO0,_,  _],
    [_,  _,  PO0,PO2,PO3,PO5,POW,PO5,PO4,PO3,PO3,PO3,PO2,PO0,_,  _],
    [_,  _,  PO0,PO2,PO4,PO5,PO5,PO4,PO3,PO3,PO3,PO3,PO2,PO0,_,  _],
    [_,  _,  PO0,PO1,PO3,PO4,PO4,PO3,PO3,PO3,PO4,PO3,PO1,PO0,_,  _],
    [_,  _,  PO0,PO1,PO2,PO3,PO4,PO4,PO4,PO4,PO3,PO2,PO1,PO0,_,  _],
    [_,  _,  PO0,PO1,PO1,PO2,PO3,PO3,PO3,PO3,PO2,PO1,PO1,PO0,_,  _],
    [_,  _,  _,  PO0,PO1,PO2,PO2,PO2,PO2,PO2,PO2,PO1,PO0,_,  _,  _],
    [_,  _,  _,  _,  PO0,PO1,PO1,PO1,PO1,PO1,PO1,PO0,_,  _,  _,  _],
    [_,  _,  _,  _,  _,  PO0,PO0,PO0,PO0,PO0,PO0,_,  _,  _,  _,  _],
]

# 薬草: bboxがx=2-13, y=0-14 → center=(7.5,7.0) → y+1シフトで中央寄り
herb = [
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  GRF,_,  _,  _,  _,  _,  _,  GRF,_,  _,  _,  _],
    [_,  _,  _,  GRF,GRF,GRS,_,  _,  _,  _,  GRF,GRF,GRS,_,  _,  _],
    [_,  _,  GR4,GR5,GRY,GR4,GR3,_,  _,  GR3,GR4,GRY,GR5,GR4,_,  _],
    [_,  _,  GR3,GR4,GR5,GR3,GR2,_,  _,  GR2,GR3,GR5,GR4,GR3,_,  _],
    [_,  _,  GR2,GR3,GR4,GR2,GR1,_,  _,  GR1,GR2,GR4,GR3,GR2,_,  _],
    [_,  _,  _,  GR2,GR3,GR1,_,  _,  _,  _,  GR1,GR3,GR2,_,  _,  _],
    [_,  _,  _,  _,  GR3,GR2,_,  GR4,GR5,GR4,GR2,GR3,_,  _,  _,  _],
    [_,  _,  _,  _,  _,  GR3,GR3,GR5,GRY,GR5,GR3,GR3,_,  _,  _,  _],
    [_,  _,  _,  _,  _,  GR2,GR4,GRY,GR5,GRY,GR4,GR2,_,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  GR3,GR4,GR3,GR4,GR3,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  GR2,GR3,GR2,GR3,GR2,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  GR1,GR2,GR3,GR2,GR1,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  GR2,GR3,GR2,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  GR1,GR2,GR1,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  GR1,_,  _,  _,  _,  _,  _,  _],
]

print("=== テクスチャ再生成 ===")
save_png(cs,   os.path.join(OUT_ITEM,  "crystal_shard.png"))
save_png(po,   os.path.join(OUT_ITEM,  "healing_potion.png"))
save_png(herb, os.path.join(OUT_ITEM,  "herb.png"))
save_png(herb, os.path.join(OUT_BLOCK, "herb.png"))
print("=== 完了 ===")
