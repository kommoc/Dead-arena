package net.kommocgame.src.entity.buffs;

public class ArmorBuff extends Buff {
	
	public ArmorBuff(float protection) {
		this(1000, protection, true);
	}
	
	public ArmorBuff(long timer, float protection) {
		this(timer, protection, false);
	}
	
	public ArmorBuff(long timer, float protection, boolean isInfinite) {
		super(timer);
		
		if(isInfinite) setInfinite();
		setProtection(protection);		
	}
}
