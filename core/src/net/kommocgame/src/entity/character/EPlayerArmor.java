package net.kommocgame.src.entity.character;

public enum EPlayerArmor {
	
	CLEAR(0), LIGHT(1), MEDIUM(2), HEAVY(3);
	
	int armor_grade;
	
	EPlayerArmor(int par1) {
		armor_grade = par1;
	}
	
	public int get() {
		return armor_grade;
	}
}
