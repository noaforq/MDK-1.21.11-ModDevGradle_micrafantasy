from PIL import Image
import os, sys

OUT_BLOCK = r"C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy\src\main\resources\assets\micrafantasy\textures\block"

_ = (0,0,0,0)
GR1=(30,80,20,255); GR2=(50,120,30,255); GR3=(80,160,50,255)
GR4=(110,190,70,255); GR5=(150,220,100,255); GRY=(200,230,150,255)
GRF=(200,170,60,255); GRS=(160,130,40,255)

# 16行 × 16列 確認済み
HERB = [
    [_,  _,  _,  GRF,_,  _,  _,  _,  _,  _,  _,  GRF,_,  _,  _,  _ ],
    [_,  _,  GRF,GRF,GRS,_,  _,  _,  _,  _,  GRF,GRF,GRS,_,  _,  _ ],
    [_,  GR4,GR5,GRY,GR4,GR3,_,  _,  _,  GR3,GR4,GRY,GR5,GR4,_,  _ ],
    [_,  GR3,GR4,GR5,GR3,GR2,_,  _,  _,  GR2,GR3,GR5,GR4,GR3,_,  _ ],
    [_,  GR2,GR3,GR4,GR2,GR1,_,  _,  _,  GR1,GR2,GR4,GR3,GR2,_,  _ ],
    [_,  _,  GR2,GR3,GR1,_,  _,  _,  _,  _,  GR1,GR3,GR2,_,  _,  _ ],
    [_,  _,  _,  GR3,GR2,_,  GR4,GR5,GR4,_,  GR2,GR3,_,  _,  _,  _ ],
    [_,  _,  _,  _,  GR3,GR3,GR5,GRY,GR5,GR3,GR3,_,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  GR2,GR4,GRY,GR5,GRY,GR4,GR2,_,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  GR3,GR4,GR3,GR4,GR3,_,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  GR2,GR3,GR2,GR3,GR2,_,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  GR1,GR2,GR3,GR2,GR1,_,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  _,  GR2,GR3,GR2,_,  _,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  _,  GR1,GR2,GR1,_,  _,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  _,  _,  GR1,_,  _,  _,  _,  _,  _,  _,  _ ],
    [_,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _,  _ ],
]

# 行数・列数チェック
for i, row in enumerate(HERB):
    if len(row) != 16:
        print(f"ERROR row {i}: {len(row)} cols", file=sys.stderr)
        sys.exit(1)
if len(HERB) != 16:
    print(f"ERROR: {len(HERB)} rows", file=sys.stderr)
    sys.exit(1)

img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
px = img.load()
for y, row in enumerate(HERB):
    for x, c in enumerate(row):
        px[x, y] = c

out_path = os.path.join(OUT_BLOCK, "herb.png")
img.save(out_path, "PNG")
print(f"saved: {out_path} ({os.path.getsize(out_path)} bytes)")

# プレビュー
prev = img.resize((128, 128), Image.NEAREST)
prev.save(os.path.join(OUT_BLOCK, "herb_preview.png"), "PNG")
print("preview saved")

