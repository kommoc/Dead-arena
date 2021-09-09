package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.utils.ArrayMap;

public class Guild {
	
	public static Guild PLAYER = new Guild("Player");
	public static Guild ZOMBIE = new Guild("Zombie");
	
	public static Guild G_NEUTRAL = new Guild("G_Neutral");
	public static Guild G_ENEMY = new Guild("G_Enemy");
	public static Guild G_FRIEND = new Guild("G_Friend");
	public static Guild G_INDIFFERENCE = new Guild("G_Indifference");
	
	static {
		ZOMBIE.add(PLAYER, GuildType.ENEMY);
	}
	
	private String guild_name;
	private ArrayMap<Guild, GuildType> guild_list = new ArrayMap<Guild, Guild.GuildType>();
	
	public Guild(String name) {
		this.guild_name = name;
	}
	
	public void add(Guild guild, GuildType type) {
		if(guild_list.containsKey(guild)) {
			System.out.println("	GUILD: that guild [" + guild.guild_name + "] is added yet to [" + guild_list.get(guild) + 
					"].\n	Set to [" + type + "]");
			guild_list.setValue(guild_list.indexOfKey(guild), type);
		} else guild_list.put(guild, type);
	}
	
	public GuildType getRelationship(Guild guild) {
		if(guild == G_NEUTRAL)
			return GuildType.NEUTRAL;
		else if(guild == G_ENEMY)
			return GuildType.ENEMY;
		else if(guild == G_FRIEND) 
			return GuildType.FRIEND;
		else if(guild == G_INDIFFERENCE)
			return GuildType.INDIFFERENCE;
		
		if(guild_list.containsKey(guild)) {
			return guild_list.get(guild);
		} else {
			add(guild, GuildType.NEUTRAL);
			return GuildType.NEUTRAL;
		}
	}
	
	public String getName() {
		return guild_name;
	}
	
	public enum GuildType {
		FRIEND, NEUTRAL, ENEMY, INDIFFERENCE
	}
}
