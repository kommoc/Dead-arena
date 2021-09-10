package net.kommocgame.src.layer;

public enum EnumGameLayer {
	
	World(1), Default(2), Props(3), Entity(4), EntityLiving(5), Player(6), HighLayer(7);
	
	int priority;
	
	EnumGameLayer(int par1) {
		priority = par1;
	}
	
	public static EnumGameLayer getEnum(int par1) {
		switch(par1) {
			case 1: return EnumGameLayer.World;
			case 2: return EnumGameLayer.Default;
			case 3: return EnumGameLayer.Props;
			case 4: return EnumGameLayer.Entity;
			case 5: return EnumGameLayer.EntityLiving;
			case 6: return EnumGameLayer.Player;
			case 7: return EnumGameLayer.HighLayer;
			default: return null;
		}
	}
	
	public int getPriority() {
		return priority;
	}

}
