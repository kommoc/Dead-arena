package net.kommocgame.src.editor.nodes.params;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.OrderedMap;

public enum EColor {
	
	CLEAR(Color.CLEAR),
	BLACK(Color.BLACK),
	WHITE(Color.WHITE),
	LIGHT_GRAY(Color.LIGHT_GRAY),
	GRAY(Color.GRAY),
	DARK_GRAY(Color.DARK_GRAY),
	BLUE(Color.BLUE),
	NAVY(Color.NAVY),
	ROYAL(Color.ROYAL),
	SLATE(Color.SLATE),
	SKY(Color.SKY),
	CYAN(Color.CYAN),
	TEAL(Color.TEAL),
	GREEN(Color.GREEN),
	CHARTREUSE(Color.CHARTREUSE),
	LIME(Color.LIME),
	FOREST(Color.FOREST),
	OLIVE(Color.OLIVE),
	YELLOW(Color.YELLOW),
	GOLD(Color.GOLD),
	GOLDENROD(Color.GOLDENROD),
	ORANGE(Color.ORANGE),
	BROWN(Color.BROWN),
	TAN(Color.TAN),
	FIREBRICK(Color.FIREBRICK),
	RED(Color.RED),
	SCARLET(Color.SCARLET),
	CORAL(Color.CORAL),
	SALMON(Color.SALMON),
	PINK(Color.PINK),
	MAGENTA(Color.MAGENTA),
	PURPLE(Color.PURPLE),
	VIOLET(Color.VIOLET),
	MAROON(Color.MAROON);
	
	private final Color color;

	EColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
    
    public static EColor getEColor(Color color) {
    	
        return Constants.COLORS.keys().toArray().get(Constants.COLORS.values().toArray().indexOf(color, false));
    }

    private static final class Constants {
        private static final OrderedMap<EColor, Color> COLORS;

        static {
        	COLORS = new OrderedMap<EColor, Color>(EColor.values().length);
            for (final EColor ecolor : values()) {
            	COLORS.put(ecolor, ecolor.getColor());
            }
        }
    }
}
