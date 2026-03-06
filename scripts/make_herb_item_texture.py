"""
アイテム用 herb.png 生成スクリプト
"""
from PIL import Image
import os

OUT = r'C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy\src\main\resources\assets\micrafantasy\textures\item'
os.makedirs(OUT, exist_ok=True)

_ = (0,0,0,0)
GR1=(30,80,20,255); GR2=(50,120,30,255); GR3=(80,160,50,255)
GR4=(110,190,70,255); GR5=(150,220,100,255); GRY=(200,230,150,255)
GRF=(200,170,60,255); GRS=(160,130,40,255)
ST=(80,55,30,255)

HERB_ITEM = [
    [_,  _,  _,  _,  _,  _,  GRF,GRS,_,  _,  _,  _,  _,  _,  _,  _  ],
    [_,  _,  _,  _,  _,  GRF,GRF,GRS,GRS,_,  _,  _,  _,  _,  _,  _  ],
    [_,  GR5,GRY,_,  _,  GR4,GRF,GRF,GRS,_,  _,  GR5,GRY,_,  _,  _  ],
    [GR4,GR5,GRY,GR5,_,  GR3,GR4,GR5,GR3,_,  GR4,GR5,GRY,GR4,_,  _  ],
    [GR3,GR4,GR5,GR4,GR3,GR2,GR3,GR4,GR2,GR3,GR4,GR4,GR5,GR3,_,  _  ],
    [GR2,GR3,GR4,GR3,GR2,GR1,GR3,GR4,GR1,GR2,GR3,GR3,GR4,GR2,_,  _  ],
    [_,  GR2,GR3,GR2,_,  _,  GR3,GR3,_,  _,  GR2,GR3,GR2,_,  _,  _  ],
    [_,  _,  _,  _,  _,  _,  GR3,GR2,_,  _,  _,  _,  _,  _,  _,  _  ],
    [_,  _,  _,  _,  _,  GR2,GR3,GR2,GR2,_,  _,  _,  _,  _,  _,  _  ],
    [_,  _,  GR4,GR3,GR2,GR3,GR4,GR3,GR2,GR3,GR2,_,  _,  _,  _,  _  ],
    [_,  GR3,GR4,GR5,GR3,GR2,GR3,GR2,GR1,GR2,GR3,GR2,_,  _,  _,  _  ],
    [_,  GR2,GR3,GR4,GR2,GR1,GR2,GR1,GR2,GR3,GR2,GR1,_,  _,  _,  _  ],
    [_,  _,  GR2,GR3,GR1,GR2,ST, ST, GR2,GR1,GR2,_,  _,  _,  _,  _  ],
    [_,  _,  _,  GR2,GR1,ST, ST, ST, ST, GR1,_,  _,  _,  _,  _,  _  ],
    [_,  _,  _,  _,  ST, ST, ST, ST, ST, _,  _,  _,  _,  _,  _,  _  ],
    [_,  _,  _,  _,  _,  ST, ST, ST, _,  _,  _,  _,  _,  _,  _,  _  ],
]

img = Image.new('RGBA', (16,16), (0,0,0,0))
px = img.load()
for y, row in enumerate(HERB_ITEM):
    for x, c in enumerate(row):
        px[x, y] = c

img.save(os.path.join(OUT, 'herb.png'))
print('herb.png saved to', OUT)
img.resize((128,128), Image.NEAREST).save(os.path.join(OUT, 'herb_preview.png'))
print('herb_preview.png saved')

