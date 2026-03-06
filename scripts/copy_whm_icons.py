#!/usr/bin/env python3
"""白魔道士スキルアイコンをguiフォルダへリサイズコピーするスクリプト"""
from PIL import Image
import os

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WHM = os.path.join(BASE, "FFXIVIcons Battle(PvE)", "16_WHM")
GUI = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "gui")

mapping = {
    "skill_whm_1_stone":          "stone.png",
    "skill_whm_2_cure":           "cure.png",
    "skill_whm_3_aero":           "aero.png",
    "skill_whm_4_stonra":         "stone_II.png",
    "skill_whm_5_cura":           "cure_II.png",
    "skill_whm_6_holy":           "holy.png",
    "skill_whm_7_tetragrammaton": "tetragrammaton.png",
    "skill_whm_8_benediction":    "benediction.png",
    "skill_whm_9_asylum":         "asylum.png",
}

os.makedirs(GUI, exist_ok=True)
for dst_name, src_name in mapping.items():
    src = os.path.join(WHM, src_name)
    dst = os.path.join(GUI, dst_name + ".png")
    img = Image.open(src)
    img = img.resize((16, 16), Image.LANCZOS)
    img.save(dst)
    print(f"Saved: {dst_name}.png")

print("Done!")
