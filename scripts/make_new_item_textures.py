"""
新規アイテム/ブロック用テクスチャ生成スクリプト（ピクセルアート・16x16）
- crystal_ore.png   : クリスタル鉱石ブロック
- herb.png          : 薬草ブロック（cross形状・RGBA透過あり）
- crystal_shard.png : クリスタルの欠片
- healing_potion.png: 回復ポーション
"""
from PIL import Image
import os

BASE      = r"C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy"
OUT_BLOCK = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "block")
OUT_ITEM  = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "item")
os.makedirs(OUT_BLOCK, exist_ok=True)
os.makedirs(OUT_ITEM,  exist_ok=True)

_ = (0,0,0,0)

ST0=(40,40,40,255);ST1=(55,55,55,255);ST2=(72,72,72,255);ST3=(90,90,90,255);ST4=(108,108,108,255)
CC0=(30,60,140,255);CC1=(55,100,200,255);CC2=(90,150,235,255);CC3=(150,200,255,255);CCW=(230,245,255,255)
GR1=(30,80,20,255);GR2=(50,120,30,255);GR3=(80,160,50,255)
GR4=(110,190,70,255);GR5=(150,220,100,255);GRY=(200,230,150,255)
GRF=(200,170,60,255);GRS=(160,130,40,255)
PO0=(20,10,30,255);PO1=(60,40,80,255);PO2=(80,55,100,255)
PO3=(140,60,60,255);PO4=(200,80,80,255);PO5=(240,120,120,255)
POW=(255,200,200,255);POG=(120,200,100,255);POB=(180,220,180,255)

CRYSTAL_ORE = [
    [ST1,ST2,ST3,ST2,ST1,ST2,ST3,ST1,ST2,ST3,ST2,ST1,ST2,ST3,ST2,ST1],
    [ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST2,CC0,CC1,ST3,ST2,ST3,ST4,ST3,ST2],
    [ST3,ST4,ST2,ST4,ST3,CC0,CC2,CC1,CC2,CC3,CC1,ST3,ST4,ST2,ST4,ST3],
    [ST2,ST3,ST4,ST3,CC1,CC2,CC3,CCW,CC3,CC2,CC0,ST2,ST3,ST4,ST3,ST2],
    [ST1,ST2,ST3,ST2,CC0,CC1,CC2,CC3,CC2,CC0,ST3,ST2,ST3,ST2,ST1,ST1],
    [ST2,ST3,ST4,ST2,ST1,CC0,CC1,CC2,CC0,ST3,ST4,ST3,ST2,ST3,ST2,ST2],
    [ST3,ST4,ST3,ST4,ST3,ST2,ST1,ST2,ST3,ST4,ST3,ST4,ST3,ST4,ST3,ST3],
    [ST2,ST3,ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,ST2],
    [ST1,ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,CC0,CC1,ST3,ST2,ST1,ST1],
    [ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,CC1,CC2,CC3,CC1,ST3,ST2,ST2],
    [ST3,ST2,ST3,ST2,ST3,ST4,ST3,ST2,CC0,CC2,CC3,CCW,CC2,CC1,ST3,ST3],
    [ST4,ST3,ST2,ST3,ST4,ST3,ST2,ST3,CC1,CC1,CC2,CC1,CC0,ST4,ST4,ST4],
    [ST3,ST4,ST3,ST4,ST3,ST2,ST3,ST2,ST3,CC0,ST3,ST4,ST3,ST2,ST3,ST3],
    [ST2,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,ST2],
    [ST1,ST2,ST3,ST2,ST3,ST4,ST3,ST4,ST3,ST2,ST3,ST4,ST3,ST2,ST1,ST1],
    [ST0,ST1,ST2,ST3,ST2,ST1,ST2,ST3,ST2,ST1,ST2,ST3,ST2,ST1,ST0,ST0],
]

HERB = [
    [_,  _,  _,  GRF,_,  _,  _,  _,  _,  _,  _,  GRF,_,  _,  _,  _],
    [_,  _,  GRF,GRF,GRS,_,  _,  _,  _,  _,  GRF,GRF,GRS,_,  _,  _],
    [_,  GR4,GR5,GRY,GR4,GR3,_,  _,  _,  GR3,GR4,GRY,GR5,GR4,_,  _],
    [_,  GR3,GR4,GR5,GR3,GR2,_,  _,  _,  GR2,GR3,GR5,GR4,GR3,_,  _],
    [_,  GR2,GR3,GR4,GR2,GR1,_,  _,  _,  GR1,GR2,GR4,GR3,GR2,_,  _],
    [_,  _,  GR2,GR3,GR1,_,  _,  _,  _,  _,  GR1,GR3,GR2,_,  _,  _],
    [_,  _,  _,  GR3,GR2,_,  GR4,GR5,GR4,_,  GR2,GR3,_,  _,  _,  _],
    [_,  _,  _,  _,  GR3,GR3,GR5,GRY,GR5,GR3,GR3,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  GR2,GR4,GRY,GR5,GRY,GR4,GR2,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  GR3,GR4,GR3,GR4,GR3,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  GR2,GR3,GR2,GR3,GR2,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  GR1,GR2,GR3,GR2,GR1,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  GR2,GR3,GR2,_,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  GR1,GR2,GR1,_,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  GR1,_,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
]

CRYSTAL_SHARD = [
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  CCW,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  CC3,CC3,CC2,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  CC2,CC3,CCW,CC3,CC1,_,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  CC1,CC2,CC3,CC3,CC2,CC1,CC0,_,  _,  _,  _],
    [_,  _,  _,  _,  CC0,CC1,CC2,CCW,CC3,CC2,CC1,CC0,CC0,_,  _,  _],
    [_,  _,  _,  CC0,CC1,CC2,CC3,CC3,CC2,CC1,CC1,CC0,CC0,CC0,_,  _],
    [_,  _,  CC0,CC1,CC2,CC3,CCW,CC2,CC1,CC0,CC0,CC0,CC0,_,  _,  _],
    [_,  _,  CC1,CC2,CC3,CC2,CC1,CC1,CC0,CC0,CC0,CC0,_,  _,  _,  _],
    [_,  _,  CC0,CC1,CC2,CC1,CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _],
    [_,  _,  _,  CC0,CC1,CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  CC0,CC0,CC0,CC0,_,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  CC0,CC0,_,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  CC0,_,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
]

HEALING_POTION = [
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
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _],
]

def save_pixel(pixel_map, path):
    assert len(pixel_map) == 16, f"{path}: {len(pixel_map)} rows"
    for i, row in enumerate(pixel_map):
        assert len(row) == 16, f"{path} row {i}: {len(row)} cols"
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px  = img.load()
    for y, row in enumerate(pixel_map):
        for x, color in enumerate(row):
            px[x, y] = color
    img.save(path, "PNG")
    print(f"  OK: {os.path.basename(path)} ({os.path.getsize(path)} bytes)")

print("=== テクスチャ生成開始 ===")
save_pixel(CRYSTAL_ORE,    os.path.join(OUT_BLOCK, "crystal_ore.png"))
save_pixel(HERB,           os.path.join(OUT_BLOCK, "herb.png"))
save_pixel(CRYSTAL_SHARD,  os.path.join(OUT_ITEM,  "crystal_shard.png"))
save_pixel(HEALING_POTION, os.path.join(OUT_ITEM,  "healing_potion.png"))

for name, pmap, folder in [
    ("crystal_ore",    CRYSTAL_ORE,    OUT_BLOCK),
    ("herb",           HERB,           OUT_BLOCK),
    ("crystal_shard",  CRYSTAL_SHARD,  OUT_ITEM),
    ("healing_potion", HEALING_POTION, OUT_ITEM),
]:
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px  = img.load()
    for y, row in enumerate(pmap):
        for x, color in enumerate(row):
            px[x, y] = color
    img.resize((128, 128), Image.NEAREST).save(
        os.path.join(folder, f"{name}_preview.png"), "PNG")
    print(f"  preview: {name}_preview.png")

print("=== 完了 ===")
