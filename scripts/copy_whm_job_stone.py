#!/usr/bin/env python3
"""白魔道士ジョブストーンのテクスチャをitemフォルダへリサイズコピー"""
from PIL import Image
import os

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
# JobIconsフォルダにWhiteMage.pngがあればそれを使う、なければConjurer.pngを使う
HEALER_JOB_DIR = os.path.join(BASE, "FFXIVIcons JobIcons", "02_HEALER")
ITEM = os.path.join(BASE, "src", "main", "resources", "assets", "micrafantasy", "textures", "item")

# 候補ファイルを探す
candidates = []
for root, dirs, files in os.walk(HEALER_JOB_DIR):
    for f in files:
        if f.endswith(".png"):
            candidates.append(os.path.join(root, f))

print("Found healer icons:")
for c in candidates:
    print(" ", c)

# WhiteMage.png 優先、なければ最初の候補
src = None
for c in candidates:
    if "WhiteMage" in c:
        src = c
        break
if src is None and candidates:
    src = candidates[0]

if src is None:
    print("ERROR: No healer icon found!")
    exit(1)

dst = os.path.join(ITEM, "white_mage_job_stone.png")
img = Image.open(src)
img = img.resize((16, 16), Image.LANCZOS)
img.save(dst)
print(f"Saved: white_mage_job_stone.png (from {os.path.basename(src)})")
