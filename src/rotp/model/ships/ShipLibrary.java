/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.ships;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import rotp.ui.BasePanel;
import rotp.util.Base;

public class ShipLibrary implements Base {
    static ShipLibrary instance = new ShipLibrary();
    public static ShipLibrary current()   { return instance; }

    public static final int sizes = 4;
    public static final int designsPerSize = 6;
    public static final String imageDir = "images/ships/";
    public static final String setFilename = "listing.txt";

    public ImageIcon stargate;
    public List<String> styles = new ArrayList<>();
    public List<String> unchosenStyles = new ArrayList<>();
    public List<ShipStyle> shipStyles  = new ArrayList<>();
    public List<Integer> missileDesign = new ArrayList<>();
    public List<Integer> scatterDesign = new ArrayList<>();
    public List<Integer> torpedoDesign = new ArrayList<>();

    private static final String[] sizeKey = { "A", "B", "C", "D" };
    private static final String[] designKey = { "01", "02", "03", "04", "05", "06" };
    private static final String[] frameKey = { "a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String shipImageExtension = ".png";
    private static final String shipImageExtensionWebP = ".webp";

    static {
        current().loadData();
    }
    public int selectRandomUnchosenSet() {
        if (unchosenStyles.isEmpty())
            resetUnchosenStyles();

        String setName = random(unchosenStyles);
        unchosenStyles.remove(setName);
        return styles.indexOf(setName);
    }
    public BufferedImage scoutImage(Integer colorId) {
        int destH = BasePanel.s10;
        int destW = BasePanel.s17;
        int[] pX = new int[3];
        int[] pY = new int[3];
        
        pX[0] = BasePanel.s4;
        pX[1] = BasePanel.s4;
        pX[2] = BasePanel.s15;
        pY[0] = BasePanel.s1;
        pY[1] = BasePanel.s9;
        pY[2] = BasePanel.s5;
        
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);
        g.setColor(c0);
        g.fillPolygon(pX, pY, 3);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.black);
        g.drawPolygon(pX, pY, 3);
        g.dispose();
        return destImg;
    }
    public BufferedImage shipImage(Integer colorId) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        int[] pX = new int[3];
        int[] pY = new int[3];
        
        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;
        int s3 = BasePanel.s3;
        int s6 = BasePanel.s6;
        int s9 = BasePanel.s9;
        
        pX[0] = BasePanel.s4;
        pX[1] = BasePanel.s4;
        pX[2] = BasePanel.s18;
        pY[0] = BasePanel.s1;
        pY[1] = BasePanel.s11;
        pY[2] = BasePanel.s6;
        
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);
        g.setColor(c0);
        g.fillPolygon(pX, pY, 3);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect( 0, s6, s3, s1);
        g.setColor(Color.orange);
        g.fillRect(s1, s3, s2, s1);
        g.fillRect(s1, s9, s2, s1);
        g.setColor(Color.black);
        g.drawPolygon(pX, pY, 3);
        g.dispose();
        return destImg;
    }
    public BufferedImage shipImageLarge(Integer colorId) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        int[] pX = new int[3];
        int[] pY = new int[3];
        
        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;
        int s3 = BasePanel.s3;
        int s8 = BasePanel.s8;
        int s13 = BasePanel.s13;
        
        pX[0] = BasePanel.s4;
        pX[1] = BasePanel.s4;
        pX[2] = BasePanel.s23;
        pY[0] = BasePanel.s1;
        pY[1] = BasePanel.s15;
        pY[2] = BasePanel.s8;
        
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);
        g.setColor(c0);
        g.fillPolygon(pX, pY, 3);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect( 0, s8, s3, s1);
        g.setColor(Color.orange);
        g.fillRect(s1, s3, s2, s1);
        g.fillRect(s1, s13, s2, s1);
        g.setColor(Color.black);
        g.drawPolygon(pX, pY, 3);
        g.dispose();
        return destImg;
    }
    public BufferedImage shipImageHuge(Integer colorId) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        int[] pX = new int[3];
        int[] pY = new int[3];
        
        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;
        int s3 = BasePanel.s3;
        int s6 = BasePanel.s6;
        int s10 = BasePanel.s10;
        int s14 = BasePanel.s14;
        int s17 = BasePanel.s17;
        
        pX[0] = BasePanel.s4;
        pX[1] = BasePanel.s4;
        pX[2] = BasePanel.s28;
        pY[0] = BasePanel.s1;
        pY[1] = BasePanel.s19;
        pY[2] = BasePanel.s10;
        
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);
        g.setColor(c0);
        g.fillPolygon(pX, pY, 3);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect( 0, s10, s3, s1);
        g.setColor(Color.orange);
        g.fillRect(s1, s3, s2, s1);
        g.fillRect(s1, s6, s2, s1);
        g.fillRect(s1, s14, s2, s1);
        g.fillRect(s1, s17, s2, s1);
        g.setColor(Color.black);
        g.drawPolygon(pX, pY, 3);
        g.dispose();
        return destImg;
    }
    // --- Mission-specific fleet icons: Scout ---
    public BufferedImage scoutShipImage(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        int[] pX = { BasePanel.s2, BasePanel.s1, BasePanel.s6, BasePanel.s18, BasePanel.s6, BasePanel.s1 };
        int[] pY = { BasePanel.s6, BasePanel.s4, BasePanel.s3, BasePanel.s6, BasePanel.s9, BasePanel.s8 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 6);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s5, BasePanel.s2, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 6);
        g.dispose();
        return destImg;
    }
    public BufferedImage scoutShipImageLarge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        int[] pX = { BasePanel.s3, BasePanel.s1, BasePanel.s8, BasePanel.s23, BasePanel.s8, BasePanel.s1 };
        int[] pY = { BasePanel.s8, BasePanel.s5, BasePanel.s4, BasePanel.s8, BasePanel.s12, BasePanel.s11 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 6);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s7, BasePanel.s3, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 6);
        g.dispose();
        return destImg;
    }
    public BufferedImage scoutShipImageHuge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        int[] pX = { BasePanel.s3, BasePanel.s2, BasePanel.s9, BasePanel.s27, BasePanel.s9, BasePanel.s2 };
        int[] pY = { BasePanel.s10, BasePanel.s7, BasePanel.s5, BasePanel.s10, BasePanel.s15, BasePanel.s13 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 6);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s9, BasePanel.s3, BasePanel.s3);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 6);
        g.dispose();
        return destImg;
    }

    // --- Mission-specific fleet icons: Colony (dome/ark shape) ---
    public BufferedImage colonyShipImage(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);

        // Hull base - wide flat bottom
        int[] hX = { BasePanel.s2, BasePanel.s2, BasePanel.s16, BasePanel.s16 };
        int[] hY = { BasePanel.s7, BasePanel.s10, BasePanel.s10, BasePanel.s7 };
        g.setColor(c0);
        g.fillPolygon(hX, hY, 4);

        // Dome on top
        g.fillArc(BasePanel.s3, BasePanel.s1, BasePanel.s12, BasePanel.s12, 0, 180);

        // Dome highlight
        g.setColor(new Color(255, 255, 255, 80));
        g.fillArc(BasePanel.s5, BasePanel.s2, BasePanel.s6, BasePanel.s6, 30, 120);

        // Engine glow
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s8, BasePanel.s2, BasePanel.s2);

        // Forward light
        g.setColor(Color.cyan);
        g.fillRect(BasePanel.s15, BasePanel.s8, BasePanel.s2, BasePanel.s1);

        // Outline - draw sides and bottom only (no top line), then dome arc
        g.setStroke(BasePanel.stroke2);
        g.setColor(outlineColor);
        g.drawLine(BasePanel.s2, BasePanel.s7, BasePanel.s2, BasePanel.s10);
        g.drawLine(BasePanel.s2, BasePanel.s10, BasePanel.s16, BasePanel.s10);
        g.drawLine(BasePanel.s16, BasePanel.s10, BasePanel.s16, BasePanel.s7);
        g.drawArc(BasePanel.s3, BasePanel.s1, BasePanel.s12, BasePanel.s12, 0, 180);

        g.dispose();
        return destImg;
    }
    public BufferedImage colonyShipImageLarge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);

        // Hull base - wide flat bottom
        int[] hX = { BasePanel.s3, BasePanel.s3, BasePanel.s20, BasePanel.s20 };
        int[] hY = { BasePanel.s9, BasePanel.s13, BasePanel.s13, BasePanel.s9 };
        g.setColor(c0);
        g.fillPolygon(hX, hY, 4);

        // Dome on top
        g.fillArc(BasePanel.s4, BasePanel.s1, BasePanel.s15, BasePanel.s15, 0, 180);

        // Dome highlight
        g.setColor(new Color(255, 255, 255, 80));
        g.fillArc(BasePanel.s7, BasePanel.s3, BasePanel.s8, BasePanel.s8, 30, 120);

        // Engine glow
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s10, BasePanel.s3, BasePanel.s3);

        // Forward light
        g.setColor(Color.cyan);
        g.fillRect(BasePanel.s19, BasePanel.s10, BasePanel.s3, BasePanel.s2);

        // Outline - sides and bottom only, then dome arc
        g.setStroke(BasePanel.stroke2);
        g.setColor(outlineColor);
        g.drawLine(BasePanel.s3, BasePanel.s9, BasePanel.s3, BasePanel.s13);
        g.drawLine(BasePanel.s3, BasePanel.s13, BasePanel.s20, BasePanel.s13);
        g.drawLine(BasePanel.s20, BasePanel.s13, BasePanel.s20, BasePanel.s9);
        g.drawArc(BasePanel.s4, BasePanel.s1, BasePanel.s15, BasePanel.s15, 0, 180);

        g.dispose();
        return destImg;
    }
    public BufferedImage colonyShipImageHuge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);

        // Hull base - wide flat bottom
        int[] hX = { BasePanel.s4, BasePanel.s4, BasePanel.s24, BasePanel.s24 };
        int[] hY = { BasePanel.s12, BasePanel.s17, BasePanel.s17, BasePanel.s12 };
        g.setColor(c0);
        g.fillPolygon(hX, hY, 4);

        // Dome on top
        g.fillArc(BasePanel.s5, BasePanel.s1, BasePanel.s18, BasePanel.s18, 0, 180);

        // Dome highlight
        g.setColor(new Color(255, 255, 255, 80));
        g.fillArc(BasePanel.s9, BasePanel.s3, BasePanel.s10, BasePanel.s10, 30, 120);

        // Engine glow
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s13, BasePanel.s4, BasePanel.s3);

        // Forward light
        g.setColor(Color.cyan);
        g.fillRect(BasePanel.s23, BasePanel.s13, BasePanel.s4, BasePanel.s2);

        // Outline - sides and bottom only, then dome arc
        g.setStroke(BasePanel.stroke2);
        g.setColor(outlineColor);
        g.drawLine(BasePanel.s4, BasePanel.s12, BasePanel.s4, BasePanel.s17);
        g.drawLine(BasePanel.s4, BasePanel.s17, BasePanel.s24, BasePanel.s17);
        g.drawLine(BasePanel.s24, BasePanel.s17, BasePanel.s24, BasePanel.s12);
        g.drawArc(BasePanel.s5, BasePanel.s1, BasePanel.s18, BasePanel.s18, 0, 180);

        g.dispose();
        return destImg;
    }

    // --- Mission-specific fleet icons: Fighter ---
    public BufferedImage fighterShipImage(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        int[] pX = { BasePanel.s3, BasePanel.s1, BasePanel.s5, BasePanel.s8, BasePanel.s18, BasePanel.s8, BasePanel.s5, BasePanel.s1 };
        int[] pY = { BasePanel.s6, BasePanel.s1, BasePanel.s3, BasePanel.s4, BasePanel.s6, BasePanel.s8, BasePanel.s9, BasePanel.s11 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(BasePanel.s1, BasePanel.s5, BasePanel.s2, BasePanel.s2);
        g.setColor(Color.orange);
        g.fillRect(0, BasePanel.s1, BasePanel.s2, BasePanel.s1);
        g.fillRect(0, BasePanel.s10, BasePanel.s2, BasePanel.s1);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage fighterShipImageLarge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        int[] pX = { BasePanel.s4, BasePanel.s1, BasePanel.s6, BasePanel.s10, BasePanel.s23, BasePanel.s10, BasePanel.s6, BasePanel.s1 };
        int[] pY = { BasePanel.s8, BasePanel.s1, BasePanel.s4, BasePanel.s5, BasePanel.s8, BasePanel.s11, BasePanel.s12, BasePanel.s15 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(BasePanel.s1, BasePanel.s7, BasePanel.s3, BasePanel.s2);
        g.setColor(Color.orange);
        g.fillRect(0, BasePanel.s1, BasePanel.s2, BasePanel.s2);
        g.fillRect(0, BasePanel.s13, BasePanel.s2, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage fighterShipImageHuge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        int[] pX = { BasePanel.s5, BasePanel.s2, BasePanel.s8, BasePanel.s12, BasePanel.s27, BasePanel.s12, BasePanel.s8, BasePanel.s2 };
        int[] pY = { BasePanel.s10, BasePanel.s2, BasePanel.s5, BasePanel.s7, BasePanel.s10, BasePanel.s13, BasePanel.s15, BasePanel.s18 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(BasePanel.s1, BasePanel.s9, BasePanel.s3, BasePanel.s3);
        g.setColor(Color.orange);
        g.fillRect(0, BasePanel.s2, BasePanel.s3, BasePanel.s2);
        g.fillRect(0, BasePanel.s17, BasePanel.s3, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }

    // --- Mission-specific fleet icons: Bomber ---
    public BufferedImage bomberShipImage(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        int[] pX = { BasePanel.s3, BasePanel.s1, BasePanel.s3, BasePanel.s15, BasePanel.s18, BasePanel.s15, BasePanel.s3, BasePanel.s1 };
        int[] pY = { BasePanel.s6, BasePanel.s2, BasePanel.s2, BasePanel.s3, BasePanel.s6, BasePanel.s9, BasePanel.s10, BasePanel.s10 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s3, BasePanel.s2, BasePanel.s1);
        g.fillRect(0, BasePanel.s8, BasePanel.s2, BasePanel.s1);
        g.setColor(Color.red);
        g.fillRect(BasePanel.s7, BasePanel.s5, BasePanel.s5, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage bomberShipImageLarge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        int[] pX = { BasePanel.s4, BasePanel.s1, BasePanel.s4, BasePanel.s19, BasePanel.s23, BasePanel.s19, BasePanel.s4, BasePanel.s1 };
        int[] pY = { BasePanel.s8, BasePanel.s3, BasePanel.s3, BasePanel.s4, BasePanel.s8, BasePanel.s12, BasePanel.s13, BasePanel.s13 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s4, BasePanel.s2, BasePanel.s2);
        g.fillRect(0, BasePanel.s11, BasePanel.s2, BasePanel.s2);
        g.setColor(Color.red);
        g.fillRect(BasePanel.s9, BasePanel.s7, BasePanel.s6, BasePanel.s3);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage bomberShipImageHuge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        int[] pX = { BasePanel.s5, BasePanel.s2, BasePanel.s5, BasePanel.s23, BasePanel.s27, BasePanel.s23, BasePanel.s5, BasePanel.s2 };
        int[] pY = { BasePanel.s10, BasePanel.s3, BasePanel.s3, BasePanel.s5, BasePanel.s10, BasePanel.s15, BasePanel.s17, BasePanel.s17 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s5, BasePanel.s3, BasePanel.s2);
        g.fillRect(0, BasePanel.s14, BasePanel.s3, BasePanel.s2);
        g.setColor(Color.red);
        g.fillRect(BasePanel.s11, BasePanel.s8, BasePanel.s8, BasePanel.s4);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }

    // --- Mission-specific fleet icons: Destroyer ---
    public BufferedImage destroyerShipImage(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s12;
        int destW = BasePanel.s20;
        int[] pX = { BasePanel.s3, BasePanel.s2, BasePanel.s5, BasePanel.s10, BasePanel.s18, BasePanel.s10, BasePanel.s5, BasePanel.s2 };
        int[] pY = { BasePanel.s6, BasePanel.s3, BasePanel.s1, BasePanel.s3, BasePanel.s6, BasePanel.s9, BasePanel.s11, BasePanel.s9 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s5, BasePanel.s3, BasePanel.s2);
        g.setColor(Color.orange);
        g.fillRect(BasePanel.s3, BasePanel.s2, BasePanel.s2, BasePanel.s1);
        g.fillRect(BasePanel.s3, BasePanel.s9, BasePanel.s2, BasePanel.s1);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage destroyerShipImageLarge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s16;
        int destW = BasePanel.s25;
        int[] pX = { BasePanel.s4, BasePanel.s3, BasePanel.s6, BasePanel.s13, BasePanel.s23, BasePanel.s13, BasePanel.s6, BasePanel.s3 };
        int[] pY = { BasePanel.s8, BasePanel.s4, BasePanel.s1, BasePanel.s4, BasePanel.s8, BasePanel.s12, BasePanel.s15, BasePanel.s12 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s7, BasePanel.s3, BasePanel.s3);
        g.setColor(Color.orange);
        g.fillRect(BasePanel.s4, BasePanel.s3, BasePanel.s2, BasePanel.s1);
        g.fillRect(BasePanel.s4, BasePanel.s12, BasePanel.s2, BasePanel.s1);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }
    public BufferedImage destroyerShipImageHuge(Integer colorId, Color outlineColor) {
        int destH = BasePanel.s20;
        int destW = BasePanel.s30;
        int[] pX = { BasePanel.s5, BasePanel.s3, BasePanel.s8, BasePanel.s15, BasePanel.s27, BasePanel.s15, BasePanel.s8, BasePanel.s3 };
        int[] pY = { BasePanel.s10, BasePanel.s5, BasePanel.s2, BasePanel.s5, BasePanel.s10, BasePanel.s15, BasePanel.s18, BasePanel.s15 };

        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        g.setColor(options().color(colorId));
        g.fillPolygon(pX, pY, 8);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.yellow);
        g.fillRect(0, BasePanel.s9, BasePanel.s4, BasePanel.s3);
        g.setColor(Color.orange);
        g.fillRect(BasePanel.s5, BasePanel.s3, BasePanel.s3, BasePanel.s2);
        g.fillRect(BasePanel.s5, BasePanel.s15, BasePanel.s3, BasePanel.s2);
        g.setColor(outlineColor);
        g.drawPolygon(pX, pY, 8);
        g.dispose();
        return destImg;
    }

    public BufferedImage transportImage(Integer colorId) {
        int destH = BasePanel.s7;
        int destW = BasePanel.s16;
        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;
        int crv = BasePanel.s4;
        BufferedImage destImg = newBufferedImage(destW, destH);
        Graphics2D g = (Graphics2D) destImg.getGraphics();
        setRenderingHints(g);
        Color c0 = options().color(colorId);
        g.setColor(c0);
        g.fillRoundRect(s1,s1,destW-s2,destH-s2,crv,crv);
        g.setStroke(BasePanel.stroke2);
        g.setColor(Color.black);
        g.drawRoundRect(s1,s1,destW-s2,destH-s2,crv,crv);
        g.dispose();
        return destImg;
    }
    public ShipImage shipImage(int styleNum, int size, int num) {
        int shipSeq = (size * designsPerSize) + num;
        styleNum = bounds(0, styleNum, shipStyles.size()-1);
        List<ShipImage> images = shipStyles.get(styleNum).images;
        return images.get(shipSeq);
    }
    public String shipKey(int i, int size, int num) {
        int shipSeq = (size * designsPerSize) + num;
        List<ShipImage> images = shipStyles.get(i).images;
        return images.get(shipSeq).nextIcon();
    }

    public List<String> validIconKeys(int labNum, int size) {
        List<String> validIconKeys = new ArrayList<>();

        List<ShipImage> images = shipStyles.get(labNum).images;
        for (int i=0;i<designsPerSize;i++) {
            int index = (size*designsPerSize)+i;
            if (index >= images.size()) {
                err("ERROR: icon index:"+index+"  iconSize:"+images.size()+"   labnum:"+labNum+"  size:"+size);
            }
            ShipImage image = images.get((size*designsPerSize)+i);
            validIconKeys.add(image.currentIcon());
        }
        return validIconKeys;
    }
    private void loadData() {
        log("Loading Ship Sets...");
        styles.clear();
        unchosenStyles.clear();
        shipStyles.clear();

        stargate = icon("images/ships/stargate_icon.png");

        loadSetFile();
        resetUnchosenStyles();

        for (int i=0;i<styles.size();i++) {
            ShipStyle style = new ShipStyle(styles.get(i));
            shipStyles.add(style);
            for (int j=0;j<sizes;j++) {
                for (int k=0;k<designsPerSize;k++) {
                    ShipImage styleImage = new ShipImage();
                    style.images.add(styleImage);
                    // Only check if it exists. Do NOT add WebP key anywhere in internal structures
                    // as it would break save game compatibility
                    String shipIconKeyWebP = fileName(i,j,k, true);
                    String shipIconKey = fileName(i,j,k, true);

                    if (url(shipIconKeyWebP) != null || url(shipIconKey) != null) {
                        // add original name to the data structures, not webp.
                        styleImage.iconKeys.add(shipIconKey);
                    } else {
                        for (String f: frameKey) {
                            shipIconKeyWebP = fileName(i,j,k,f, true);
                            shipIconKey = fileName(i,j,k,f, false);
                            if (url(shipIconKeyWebP) != null || url(shipIconKey) != null) {
                                // add original name to the data structures, not webp.
                                styleImage.iconKeys.add(shipIconKey);
                            }
                        }
                    }
                }
            }
        }
    }
    private void resetUnchosenStyles() {
        unchosenStyles.clear();
        for (int i=0;i<styles.size();i++)
            unchosenStyles.add(styles.get(i));
    }
    private String fileName(int i, int j, int k, boolean webp) {
        String setName = styles.get(i);
        if (webp) {
            return imageDir+setName+"/"+sizeKey[j]+designKey[k]+shipImageExtensionWebP;
        } else {
            return imageDir+setName+"/"+sizeKey[j]+designKey[k]+shipImageExtension;
        }
    }
    private String fileName(int i, int j, int k, String f, boolean webp) {
        String setName = styles.get(i);
        if (webp) {
            return imageDir+setName+"/"+sizeKey[j]+designKey[k]+f+shipImageExtensionWebP;
        } else {
            return imageDir+setName+"/"+sizeKey[j]+designKey[k]+f+shipImageExtension;
        }
    }
    private void loadSetFile() {
        BufferedReader in = reader(imageDir+setFilename);
        if (in == null)
                return;

        try {
            String input;
            while ((input = in.readLine()) != null)
                loadSetLine(input);
            in.close();
        }
        catch (IOException e) {
            System.err.println("ShipLibrary.loadSetFile -- IOException: " + e);
        }
    }
    private void loadSetLine(String input) {
        if (isComment(input))
            return;

        String[] entries = input.split(",");
        String setName = entries[0].trim();
        styles.add(setName);
        Integer design = 1;
        int id = 1;
        if (entries.length > id) {
        	Integer des = getInteger(entries[id].trim());
        	if (des!=null && des!=0 && des<=6 && des>=-6)
        		design = des;
        }
    	missileDesign.add(design);

    	id++;
        if (entries.length > id) {
        	Integer des = getInteger(entries[id].trim());
           	if (des!=null && des!=0 && des<=6 && des>=-6)
        		design = des;
        }
        scatterDesign.add(design);
        
    	id++;
        if (entries.length > id) {
        	Integer des = getInteger(entries[id].trim());
           	if (des!=null && des!=0 && des<=6 && des>=-6)
        		design = des;
        }
        torpedoDesign.add(design);

    	// int mark = input.indexOf(',', 0);
        // String setName = input.substring(0, mark).trim();
        // styles.add(setName);
    }
}
