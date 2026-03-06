from PIL import Image
img = Image.open(r'C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy\src\main\resources\assets\micrafantasy\textures\block\herb.png')
print('mode:', img.mode, 'size:', img.size)
if img.mode != 'RGBA':
    print('RGBA変換が必要！')
    img = img.convert('RGBA')
    img.save(r'C:\Users\noafo\IdeaProjects\MDK-1.21.11-ModDevGradle_micrafantasy\src\main\resources\assets\micrafantasy\textures\block\herb.png')
    print('変換・保存完了')
else:
    print('既にRGBA OK')

