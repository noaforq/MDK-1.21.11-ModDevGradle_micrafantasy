import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenTextures {
    static final int T = 0x00000000;
    // クリスタル
    static final int CC0=c(30,60,140), CC1=c(55,100,200), CC2=c(90,150,235), CC3=c(150,200,255), CCW=c(230,245,255);
    // ポーション
    static final int PO0=c(20,10,30),  PO1=c(60,40,80),   PO2=c(80,55,100);
    static final int PO3=c(140,60,60), PO4=c(200,80,80),  PO5=c(240,120,120);
    static final int POW=c(255,200,200), POG=c(120,200,100), POB=c(180,220,180);
    // 薬草
    static final int GR1=c(30,80,20),  GR2=c(50,120,30),  GR3=c(80,160,50);
    static final int GR4=c(110,190,70),GR5=c(150,220,100),GRY=c(200,230,150);
    static final int GRF=c(200,170,60),GRS=c(160,130,40);

    static int c(int r, int g, int b) { return (255<<24)|(r<<16)|(g<<8)|b; }

    static void save(int[][] pixels, String path) throws Exception {
        BufferedImage img = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
        for(int y=0;y<16;y++) for(int x=0;x<16;x++) img.setRGB(x,y,pixels[y][x]);
        File f = new File(path); f.getParentFile().mkdirs();
        ImageIO.write(img,"PNG",f);
        System.out.println("saved: " + f.getName());
    }

    public static void main(String[] args) {
        String base = "C:\\Users\\noafo\\IdeaProjects\\MDK-1.21.11-ModDevGradle_micrafantasy\\src\\main\\resources\\assets\\micrafantasy\\textures\\";
        String item  = base + "item\\";
        String block = base + "block\\";

        try {
            System.err.println("=== START crystal_shard ===");
            int[][] cs = new int[16][16];
            setDot(cs, 7,1,CCW);
            setDot(cs, 6,2,CC3); setDot(cs, 7,2,CC3); setDot(cs, 8,2,CC2);
            setDot(cs, 5,3,CC2); setDot(cs, 6,3,CC3); setDot(cs, 7,3,CCW); setDot(cs, 8,3,CC3); setDot(cs, 9,3,CC1);
            setDot(cs, 4,4,CC1); setDot(cs, 5,4,CC2); setDot(cs, 6,4,CC3); setDot(cs, 7,4,CC3); setDot(cs, 8,4,CC2); setDot(cs, 9,4,CC1); setDot(cs,10,4,CC0);
            setDot(cs, 3,5,CC0); setDot(cs, 4,5,CC1); setDot(cs, 5,5,CC2); setDot(cs, 6,5,CCW); setDot(cs, 7,5,CC3); setDot(cs, 8,5,CC2); setDot(cs, 9,5,CC1); setDot(cs,10,5,CC0); setDot(cs,11,5,CC0);
            setDot(cs, 2,6,CC0); setDot(cs, 3,6,CC1); setDot(cs, 4,6,CC2); setDot(cs, 5,6,CC3); setDot(cs, 6,6,CC3); setDot(cs, 7,6,CC2); setDot(cs, 8,6,CC1); setDot(cs, 9,6,CC1); setDot(cs,10,6,CC0); setDot(cs,11,6,CC0); setDot(cs,12,6,CC0);
            setDot(cs, 2,7,CC0); setDot(cs, 3,7,CC1); setDot(cs, 4,7,CC2); setDot(cs, 5,7,CC3); setDot(cs, 6,7,CCW); setDot(cs, 7,7,CC2); setDot(cs, 8,7,CC1); setDot(cs, 9,7,CC0); setDot(cs,10,7,CC0); setDot(cs,11,7,CC0);
            setDot(cs, 2,8,CC1); setDot(cs, 3,8,CC2); setDot(cs, 4,8,CC3); setDot(cs, 5,8,CC2); setDot(cs, 6,8,CC1); setDot(cs, 7,8,CC1); setDot(cs, 8,8,CC0); setDot(cs, 9,8,CC0); setDot(cs,10,8,CC0);
            setDot(cs, 2,9,CC0); setDot(cs, 3,9,CC1); setDot(cs, 4,9,CC2); setDot(cs, 5,9,CC1); setDot(cs, 6,9,CC0); setDot(cs, 7,9,CC0); setDot(cs, 8,9,CC0); setDot(cs, 9,9,CC0);
            setDot(cs, 3,10,CC0); setDot(cs, 4,10,CC1); setDot(cs, 5,10,CC0); setDot(cs, 6,10,CC0); setDot(cs, 7,10,CC0); setDot(cs, 8,10,CC0);
            setDot(cs, 4,11,CC0); setDot(cs, 5,11,CC0); setDot(cs, 6,11,CC0); setDot(cs, 7,11,CC0);
            setDot(cs, 5,12,CC0); setDot(cs, 6,12,CC0);
            setDot(cs, 6,13,CC0);
            save(cs, item+"crystal_shard.png");
            System.err.println("=== DONE crystal_shard ===");
        } catch(Throwable e) {
            System.err.println("ERROR crystal_shard: " + e);
            e.printStackTrace(System.err);
        }

        // ── 回復ポーション ──
        try {
            System.err.println("=== START healing_potion ===");
            int[][] po = new int[16][16];
            setDot(po, 6,1,PO0); setDot(po, 7,1,PO0); setDot(po, 8,1,PO0); setDot(po, 9,1,PO0);
            setDot(po, 5,2,PO0); setDot(po, 6,2,PO1); setDot(po, 7,2,PO2); setDot(po, 8,2,PO2); setDot(po, 9,2,PO1); setDot(po,10,2,PO0);
            setDot(po, 5,3,PO0); setDot(po, 6,3,PO1); setDot(po, 7,3,POB); setDot(po, 8,3,POG); setDot(po, 9,3,PO2); setDot(po,10,3,PO0);
            setDot(po, 4,4,PO0); setDot(po, 5,4,PO0); setDot(po, 6,4,PO0); setDot(po, 7,4,PO0); setDot(po, 8,4,PO0); setDot(po, 9,4,PO0); setDot(po,10,4,PO0); setDot(po,11,4,PO0);
            setDot(po, 3,5,PO0); setDot(po, 4,5,PO1); setDot(po, 5,5,PO2); setDot(po, 6,5,POB); setDot(po, 7,5,POG); setDot(po, 8,5,POG); setDot(po, 9,5,POB); setDot(po,10,5,PO2); setDot(po,11,5,PO1); setDot(po,12,5,PO0);
            setDot(po, 2,6,PO0); setDot(po, 3,6,PO1); setDot(po, 4,6,PO2); setDot(po, 5,6,PO3); setDot(po, 6,6,PO4); setDot(po, 7,6,PO5); setDot(po, 8,6,POW); setDot(po, 9,6,PO5); setDot(po,10,6,PO4); setDot(po,11,6,PO2); setDot(po,12,6,PO1); setDot(po,13,6,PO0);
            setDot(po, 2,7,PO0); setDot(po, 3,7,PO1); setDot(po, 4,7,PO3); setDot(po, 5,7,PO4); setDot(po, 6,7,PO5); setDot(po, 7,7,POW); setDot(po, 8,7,PO5); setDot(po, 9,7,PO4); setDot(po,10,7,PO3); setDot(po,11,7,PO3); setDot(po,12,7,PO1); setDot(po,13,7,PO0);
            setDot(po, 2,8,PO0); setDot(po, 3,8,PO2); setDot(po, 4,8,PO3); setDot(po, 5,8,PO5); setDot(po, 6,8,POW); setDot(po, 7,8,PO5); setDot(po, 8,8,PO4); setDot(po, 9,8,PO3); setDot(po,10,8,PO3); setDot(po,11,8,PO3); setDot(po,12,8,PO2); setDot(po,13,8,PO0);
            setDot(po, 2,9,PO0); setDot(po, 3,9,PO2); setDot(po, 4,9,PO4); setDot(po, 5,9,PO5); setDot(po, 6,9,PO5); setDot(po, 7,9,PO4); setDot(po, 8,9,PO3); setDot(po, 9,9,PO3); setDot(po,10,9,PO3); setDot(po,11,9,PO3); setDot(po,12,9,PO2); setDot(po,13,9,PO0);
            setDot(po, 2,10,PO0); setDot(po, 3,10,PO1); setDot(po, 4,10,PO3); setDot(po, 5,10,PO4); setDot(po, 6,10,PO4); setDot(po, 7,10,PO3); setDot(po, 8,10,PO3); setDot(po, 9,10,PO3); setDot(po,10,10,PO4); setDot(po,11,10,PO3); setDot(po,12,10,PO1); setDot(po,13,10,PO0);
            setDot(po, 2,11,PO0); setDot(po, 3,11,PO1); setDot(po, 4,11,PO2); setDot(po, 5,11,PO3); setDot(po, 6,11,PO4); setDot(po, 7,11,PO4); setDot(po, 8,11,PO4); setDot(po, 9,11,PO4); setDot(po,10,11,PO3); setDot(po,11,11,PO2); setDot(po,12,11,PO1); setDot(po,13,11,PO0);
            setDot(po, 2,12,PO0); setDot(po, 3,12,PO1); setDot(po, 4,12,PO1); setDot(po, 5,12,PO2); setDot(po, 6,12,PO3); setDot(po, 7,12,PO3); setDot(po, 8,12,PO3); setDot(po, 9,12,PO3); setDot(po,10,12,PO2); setDot(po,11,12,PO1); setDot(po,12,12,PO1); setDot(po,13,12,PO0);
            setDot(po, 3,13,PO0); setDot(po, 4,13,PO1); setDot(po, 5,13,PO2); setDot(po, 6,13,PO2); setDot(po, 7,13,PO2); setDot(po, 8,13,PO2); setDot(po, 9,13,PO2); setDot(po,10,13,PO2); setDot(po,11,13,PO1); setDot(po,12,13,PO0);
            setDot(po, 4,14,PO0); setDot(po, 5,14,PO1); setDot(po, 6,14,PO1); setDot(po, 7,14,PO1); setDot(po, 8,14,PO1); setDot(po, 9,14,PO1); setDot(po,10,14,PO1); setDot(po,11,14,PO0);
            setDot(po, 5,15,PO0); setDot(po, 6,15,PO0); setDot(po, 7,15,PO0); setDot(po, 8,15,PO0); setDot(po, 9,15,PO0); setDot(po,10,15,PO0);
            save(po, item+"healing_potion.png");
            System.err.println("=== DONE healing_potion ===");
        } catch(Throwable e) { System.err.println("ERROR healing_potion: " + e); e.printStackTrace(System.err); }

        // ── 薬草 ──
        try {
            System.err.println("=== START herb ===");
            int[][] hb = new int[16][16];
            setDot(hb, 4,1,GRF); setDot(hb,11,1,GRF);
            setDot(hb, 3,2,GRF); setDot(hb, 4,2,GRF); setDot(hb, 5,2,GRS); setDot(hb,10,2,GRF); setDot(hb,11,2,GRF); setDot(hb,12,2,GRS);
            setDot(hb, 2,3,GR4); setDot(hb, 3,3,GR5); setDot(hb, 4,3,GRY); setDot(hb, 5,3,GR4); setDot(hb, 6,3,GR3); setDot(hb, 9,3,GR3); setDot(hb,10,3,GR4); setDot(hb,11,3,GRY); setDot(hb,12,3,GR5); setDot(hb,13,3,GR4);
            setDot(hb, 2,4,GR3); setDot(hb, 3,4,GR4); setDot(hb, 4,4,GR5); setDot(hb, 5,4,GR3); setDot(hb, 6,4,GR2); setDot(hb, 9,4,GR2); setDot(hb,10,4,GR3); setDot(hb,11,4,GR5); setDot(hb,12,4,GR4); setDot(hb,13,4,GR3);
            setDot(hb, 2,5,GR2); setDot(hb, 3,5,GR3); setDot(hb, 4,5,GR4); setDot(hb, 5,5,GR2); setDot(hb, 6,5,GR1); setDot(hb, 9,5,GR1); setDot(hb,10,5,GR2); setDot(hb,11,5,GR4); setDot(hb,12,5,GR3); setDot(hb,13,5,GR2);
            setDot(hb, 3,6,GR2); setDot(hb, 4,6,GR3); setDot(hb, 5,6,GR1); setDot(hb,10,6,GR1); setDot(hb,11,6,GR3); setDot(hb,12,6,GR2);
            setDot(hb, 4,7,GR3); setDot(hb, 5,7,GR2); setDot(hb, 7,7,GR4); setDot(hb, 8,7,GR5); setDot(hb, 9,7,GR4); setDot(hb,10,7,GR2); setDot(hb,11,7,GR3);
            setDot(hb, 5,8,GR3); setDot(hb, 6,8,GR3); setDot(hb, 7,8,GR5); setDot(hb, 8,8,GRY); setDot(hb, 9,8,GR5); setDot(hb,10,8,GR3); setDot(hb,11,8,GR3);
            setDot(hb, 5,9,GR2); setDot(hb, 6,9,GR4); setDot(hb, 7,9,GRY); setDot(hb, 8,9,GR5); setDot(hb, 9,9,GRY); setDot(hb,10,9,GR4); setDot(hb,11,9,GR2);
            setDot(hb, 6,10,GR3); setDot(hb, 7,10,GR4); setDot(hb, 8,10,GR3); setDot(hb, 9,10,GR4); setDot(hb,10,10,GR3);
            setDot(hb, 6,11,GR2); setDot(hb, 7,11,GR3); setDot(hb, 8,11,GR2); setDot(hb, 9,11,GR3); setDot(hb,10,11,GR2);
            setDot(hb, 6,12,GR1); setDot(hb, 7,12,GR2); setDot(hb, 8,12,GR3); setDot(hb, 9,12,GR2); setDot(hb,10,12,GR1);
            setDot(hb, 7,13,GR2); setDot(hb, 8,13,GR3); setDot(hb, 9,13,GR2);
            setDot(hb, 7,14,GR1); setDot(hb, 8,14,GR2); setDot(hb, 9,14,GR1);
            setDot(hb, 8,15,GR1);
            save(hb, item+"herb.png");
            save(hb, block+"herb.png");
            System.err.println("=== DONE herb ===");
        } catch(Throwable e) { System.err.println("ERROR herb: " + e); e.printStackTrace(System.err); }

        System.err.println("=== All done ===");
    }

    static void setDot(int[][] pixels, int x, int y, int color) {
        pixels[y][x] = color;
    }
}
