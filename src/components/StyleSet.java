package components;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class StyleSet extends SimpleAttributeSet {

    public StyleSet setFontFamily(String font) {
        StyleConstants.setFontFamily(this, font);
        return this;
    }

    public StyleSet setFontSize(int size) {
        StyleConstants.setFontSize(this, size);
        return this;
    }

    public StyleSet setForeground(Color color) {
        StyleConstants.setForeground(this, color);
        return this;
    }

    public StyleSet setBackground(Color color) {
        StyleConstants.setBackground(this, color);
        return this;
    }

    public StyleSet setUnderline(boolean underline) {
        StyleConstants.setUnderline(this, underline);
        return this;
    }

    public StyleSet setBold(boolean bold) {
        StyleConstants.setBold(this, bold);
        return this;
    }

    public StyleSet setClickable(String key, Clickable clickable) {
        this.addAttribute(key, clickable);
        return this;
    }

    public StyleSet setLineSpacing(float lineSpacing) {
        StyleConstants.setLineSpacing(this, lineSpacing);
        return this;
    }
}
