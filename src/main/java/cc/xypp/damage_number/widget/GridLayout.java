package cc.xypp.damage_number.widget;

import net.minecraft.client.gui.components.AbstractWidget;

import java.util.List;


/**
 * Mojang haven't added this file. Use our self to replace.
 */
public class GridLayout {
    public int x;
    public int y;
    public int width;
    public int height;
    public int cols;
    public List<AbstractWidget> childreds;

    public GridLayout(int w, int h) {
        this.width = w;
        this.height = h;
        this.childreds = new java.util.ArrayList<>();
    }

    public RowHelper createRowHelper(int cols) {
        this.cols = cols;
        return new RowHelper(cols);
    }

    public void arrangeElements() {
        int rows = (this.childreds.size() + cols - 1)/cols;
        int[] ws = new int[cols];
        int[] hs = new int[rows];
        for (int i = 0; i < childreds.size(); i++) {
            AbstractWidget child = childreds.get(i);
            int col = i % cols;
            int row = i / cols;
            ws[col] = Math.max(ws[col], child.getWidth());
            hs[row] = Math.max(hs[row], child.getHeight());
        }
        int curX = this.x;
        int curY = this.y;
        for (int i = 0; i < childreds.size(); i++) {
            AbstractWidget child = childreds.get(i);
            int col = i % cols;
            int row = i / cols;
            child.x=curX;
            child.y=curY;
            curX += ws[col];
            if(col==cols-1){
                curX = 0;
                curY += hs[row];
            }
        }
    }

    public void setX(int i) {
        this.x = i;
    }

    public void setY(int y) {
        this.y = y;
    }


    public class RowHelper {
        int cols;

        RowHelper(int cols) {
            this.cols = cols;
        }

        public <T extends AbstractWidget> T addChild(T widget) {
            GridLayout.this.childreds.add(widget);
            return widget;
        }
    }
}
