package net.kommocgame.src.layer;

public enum EnumGuiLayer {
	
	BackGround(1), Default(2), ForeGround(3);
	
	int priority;
	
	EnumGuiLayer(int par1) {
		priority = par1;
	}
	
	public static EnumGuiLayer getEnum(int par1) {
		switch(par1) {
			case 1: return EnumGuiLayer.BackGround;
			case 2: return EnumGuiLayer.Default;
			case 3: return EnumGuiLayer.ForeGround;
			default: return null;
		}
	}
	
	public int getPriority() {
		return priority;
	}
}
