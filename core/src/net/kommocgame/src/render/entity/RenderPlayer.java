package net.kommocgame.src.render.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Curve;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Entity.ObjectType;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Mainline.Key;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Point;
import com.brashmonkey.spriter.Timeline;
import com.brashmonkey.spriter.gdx.Drawer;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.character.EPlayerArmor;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemType;
import net.kommocgame.src.render.GameSprite;
import net.kommocgame.src.render.RenderEngine;

public class RenderPlayer extends RenderEntityLiving implements IFirePoint {
	
	private EntityPlayer player;
	
	private Point fire_point;
	
	@Deprecated
	private float scl = 0.005f;
	
	/** Body SCML sprite. */
	Player sprite_player;
	/** Boots SCML sprite. */
	Player sprite_boots;
	
	@Deprecated
	private float pre_width;
	@Deprecated
	private float pre_height;
	
	private float motion_run = 4.6f;
	
	@Deprecated
	private ObjectMap<Item, Texture> sprite_map = new ObjectMap<Item, Texture>();
	
	@Deprecated
	Animation anim;
	@Deprecated
	Key main_start;
	@Deprecated
	Key main_end;
	@Deprecated
	Mainline line = new Mainline(2);
	
	public RenderPlayer(EntityPlayer par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		player = par1;
		
		sprite_map.put(Item.item_weapon_ak12, Loader.objectsUnits("worker_body_weapon_rifle.png"));
		sprite_map.put(Item.item_weapon_glock17, Loader.objectsUnits("worker_body_weapon_pistol.png"));
		sprite_map.put(Item.item_weapon_m134, Loader.objectsUnits("worker_body_weapon_m134.png"));
		sprite_map.put(Item.item_weapon_desertEagle, Loader.objectsUnits("worker_body_weapon_desertEagle.png"));
		
		sprite_boots = new Player(RenderEngine.getSCMLData("human_main_feet"));
		sprite_boots.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.75f);
		
		sprite_player = new Player(RenderEngine.getSCMLData("human_main"));
		sprite_player.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.75f);
		
		if(sprite_player.characterMaps == null)
			sprite_player.characterMaps = new Entity.CharacterMap[16];
		
		main_start = new Key(0, 0, new Curve(Curve.Type.Bezier), 12, 12);
		main_end = new Key(1, 1000, new Curve(Curve.Type.Bezier), 12, 12);
		line.addKey(main_start);
		line.addKey(main_end);
		
		if(Game.CORE.getInventory().getBodyarmor() == null) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_clear");
		} else {
			if(Game.CORE.getInventory().getBodyarmor().getItem() == Item.item_armor_light) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_light");
			} else if(Game.CORE.getInventory().getBodyarmor().getItem() == Item.item_armor_medium) {
				sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_medium");
			}
		}
		
		//anim = new Animation(line, 50, "cross", 1000, true, 10);
	}
	
	@Override 
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		float offset = 0f;
		
		if(tex_check()) {
			
			if(player.DA_getCurrentItem() != null) {
				if(player.DA_getCurrentItem().ID == Item.item_weapon_glock17.ID) {
					player.getSprite().setTexture(sprite_map.get(player.DA_getCurrentItem()));
					offset = 1f;
					player.spriteToCenter();
				} else if(player.DA_getCurrentItem().ID == Item.item_weapon_ak12.ID) {
					player.getSprite().setTexture(sprite_map.get(player.DA_getCurrentItem()));
					offset = 0f;
					player.spriteToCenter();
				} else if(player.DA_getCurrentItem().ID == Item.item_weapon_m134.ID) {
					player.getSprite().setTexture(sprite_map.get(player.DA_getCurrentItem()));
					offset = 0f;
					player.getSprite().setOrigin(player.getSprite().getWidth()/2.25f, player.getSprite().getHeight()/2.25f);
				} else if(player.DA_getCurrentItem().ID == Item.item_weapon_desertEagle.ID) {
					player.getSprite().setTexture(sprite_map.get(player.DA_getCurrentItem()));
					offset = 1f;
					player.spriteToCenter();
				}
			} else {
				player.getSprite().setTexture(Loader.objectsUnits("worker_body_idle_stay.png"));
				offset = 0f;
				player.spriteToCenter();
			}
		}
		
		player.getSprite().setRotation(player.getOrientation() * MathUtils.radDeg + MathUtils.PI * (offset));
		//player.getSprite().draw(renderBatch);
		
		//System.out.println("	RenderPlayer.render() ### have fire-point: " + sprite_player.getAnimation().getTimeline);
		
		drawer.draw(sprite_boots);
		drawer.draw(sprite_player);
		sprite_boots.update();
		sprite_player.update();
		
		sprite_player.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.6f);
		sprite_player.setPosition(player.getPosition().x, player.getPosition().y);
		sprite_player.setAngle(player.getRotation() - 90f);
		
		sprite_boots.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.6f);
		sprite_boots.setPosition(player.getPosition().x, player.getPosition().y);
		//sprite_boots.setAngle(player.getRotation() - 90f);
		
		//System.out.println("	RenderPlayer.render() ### player_vel_lenght: " + player.getLinearVelocity().len());
		
		if(player.DA_getCurrentItem() != null) {
			if(player.DA_getCurrentItem().getItemType() == ItemType.PISTOL) {
				sprite_player.setAnimation("idle_pistol_1");
				
				sprite_player.characterMaps[0] = sprite_player.getEntity().getCharacterMap(player.DA_getCurrentItem().getName());
			} else if(player.DA_getCurrentItem().getItemType() == ItemType.RIFLE) {
				sprite_player.setAnimation("idle_rifle_1");	//BAD CODE
				
				sprite_player.characterMaps[1] = sprite_player.getEntity().getCharacterMap(player.DA_getCurrentItem().getName());
			} else if(player.DA_getCurrentItem().getItemType() == ItemType.MACHINEGUN) {
				if(player.DA_getCurrentItem().ID == Item.item_weapon_m134.ID)
					sprite_player.setAnimation("idle_minigun_1");	//BAD CODE
				else if(player.DA_getCurrentItem().ID == Item.item_weapon_kpv.ID) {
					sprite_player.setAnimation("idle_kpw_1");
				}
			} else if(player.DA_getCurrentItem().getItemType() == ItemType.SHOTGUN) {
				sprite_player.setAnimation("idle_shotgun_1");	//BAD CODE
			} else if(player.DA_getCurrentItem().getItemType() == ItemType.SUBMACHINEGUN) {
				sprite_player.setAnimation("idle_pp_1");	//BAD CODE
			}
		} else {
			if(player.getLinearVelocity().len() > 1f && player.getLinearVelocity().len() <= motion_run) {
				boolean flag_setOffset = false;
				if(sprite_player.getAnimation() != sprite_player.getEntity().getAnimation("walking_forward_unarmed"))
					flag_setOffset = true;
				
				sprite_player.setAnimation("walking_forward_unarmed");
				if(flag_setOffset)
					sprite_player.setTime(285);
			} else if(player.getLinearVelocity().len() > motion_run) {
				boolean flag_setOffset = false;
				if(sprite_player.getAnimation() != sprite_player.getEntity().getAnimation("running_forward_unarmed"))
					flag_setOffset = true;
				
				sprite_player.setAnimation("running_forward_unarmed");
				
				if(flag_setOffset)
					sprite_player.setTime(140);
				
			} else {
				sprite_player.setAnimation("idle_stay");
			}
		}
		
		if(player.getLinearVelocity().len() > 1f) {
			float dif_angle = ArithmeticUtils.wrapAngleAroundZero(player.getRotation() * MathUtils.degRad
					- player.getLinearVelocity().angle() * MathUtils.degRad)  * MathUtils.radDeg;
			//System.out.println("	RenderPlayer.render() ### player.getLinearVelocity().angle(): " +
			//		Math.abs(ArithmeticUtils.wrapAngleAroundZero(player.getLinearVelocity().angle())) * MathUtils.radDeg);
			
			if(Math.abs(dif_angle) > 45f && Math.abs(dif_angle) < 135f) {
				sprite_boots.setAngle(dif_angle > 0 ? player.getLinearVelocity().angle() : player.getLinearVelocity().angle() + 180f);
				
				////int anim_time = sprite_boots.getTime();
				int anim_ratio = sprite_boots.getTime() / sprite_boots.getAnimation().length;
				if(sprite_boots.getAnimation() != sprite_boots.getEntity().getAnimation("walking_side") ||
						sprite_boots.getAnimation() != sprite_boots.getEntity().getAnimation("running_side")) {
					boolean flag_swap = false;
					
					if(sprite_boots.getAnimation() == sprite_boots.getEntity().getAnimation("walking_forward") || 
							sprite_boots.getAnimation() == sprite_boots.getEntity().getAnimation("running_forward"))
						flag_swap = true;
					
					if(player.getLinearVelocity().len() > motion_run)
						sprite_boots.setAnimation("running_side");
					else sprite_boots.setAnimation("walking_side");
					
					if(flag_swap)
						sprite_boots.setTime(sprite_boots.getAnimation().length * anim_ratio);
					
					if(sprite_player.getAnimation() == sprite_player.getEntity().getAnimation("walking_forward_unarmed") ||
							sprite_player.getAnimation() == sprite_player.getEntity().getAnimation("running_forward_unarmed")) {
						float anim_ratio_walking = (float) sprite_player.getTime() / (float) sprite_player.getAnimation().length;
						
						sprite_boots.setTime((int)(sprite_boots.getAnimation().length * anim_ratio_walking));
					}
				}
			} else {
				sprite_boots.setAngle(Math.abs(dif_angle) < 135f ? player.getLinearVelocity().angle() - 90f : player.getLinearVelocity().angle() + 90f);
				
				////int anim_time = sprite_boots.getTime();
				int anim_ratio = sprite_boots.getTime() / sprite_boots.getAnimation().length;
				if(sprite_boots.getAnimation() != sprite_boots.getEntity().getAnimation("walking_forward") ||
						sprite_boots.getAnimation() != sprite_boots.getEntity().getAnimation("running_forward")) {
					////sprite_boots.setTime(anim_time);
					boolean flag_swap = false;
					if(sprite_boots.getAnimation() == sprite_boots.getEntity().getAnimation("walking_side") ||
							sprite_boots.getAnimation() == sprite_boots.getEntity().getAnimation("running_side"))
						flag_swap = true;
					
					if(player.getLinearVelocity().len() > motion_run)
						sprite_boots.setAnimation("running_forward");
					else sprite_boots.setAnimation("walking_forward");
					
					if(flag_swap)
						sprite_boots.setTime(sprite_boots.getAnimation().length * anim_ratio);
					
					if(sprite_player.getAnimation() == sprite_player.getEntity().getAnimation("walking_forward_unarmed") ||
							sprite_player.getAnimation() == sprite_player.getEntity().getAnimation("running_forward_unarmed")) {
						float anim_ratio_walking = (float) sprite_player.getTime() / (float) sprite_player.getAnimation().length;
						
						sprite_boots.setTime((int)(sprite_boots.getAnimation().length * anim_ratio_walking));
					}
				}
			}
		} else {
			float dif_angle = ArithmeticUtils.wrapAngleAroundZero(player.getRotation() * MathUtils.degRad
					- sprite_boots.getAngle() * MathUtils.degRad)  * MathUtils.radDeg;
			
			sprite_boots.setAngle(dif_angle - 90f > 45f ? player.getRotation() - 45f - 90f : dif_angle - 90f < -45f
					? player.getRotation() + 45f - 90f : sprite_boots.getAngle());
			sprite_boots.setAnimation("idle_stay");
		}
		
		//System.out.println("sprite_player.speed:" + sprite_player.speed);
		sprite_player.speed = (int) (15f / (1f/60f / Gdx.graphics.getDeltaTime()));
		sprite_boots.speed = (int) (15f / (1f/60f / Gdx.graphics.getDeltaTime()));
		
		//if(sprite_boots.getTime() != sprite_player.getTime())
		//sprite_boots.setTime(sprite_player.getTime());
		if(Game.CORE.getInventory().getBodyarmor() == null) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_clear");
		} else {
			if(Game.CORE.getInventory().getBodyarmor().getItem() == Item.item_armor_light) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_light");
			} else if(Game.CORE.getInventory().getBodyarmor().getItem() == Item.item_armor_medium) {
				sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_medium");
			}
		}
	}
	
	@Deprecated
	void setAnim() {
		if(anim == null) {
			anim = new Animation(new Mainline(2), 50, "cross", 1000, true, 10);
			
		}
	}
	
	@Deprecated
	private boolean tex_check() {
		if(player.DA_getCurrentItem() == null)
			return true;
		
		if(sprite_map.containsKey(player.DA_getCurrentItem())) {
			return player.getSprite().getTexture() != sprite_map.get(player.DA_getCurrentItem());
		}
		
		return false;
	}
	
	public void renderInfo(SpriteBatch debugBatch) {
		//if(true) //FIXME TO DELETE
		//	return;
		
		lineHp.setText("HP: " + player.getHP());
		//System.out.println("VECTOR LINE x:" + lineHp.getX() +" y: " + lineHp.getY());
		lineHp.setPosition(player.compPhysics.body.getPosistion().x / scale, player.compPhysics.body.getPosistion().y  / scale + 35, Alignment.CENTER.get());
		
		lineHp.draw(debugBatch, 1f);
		super.renderInfo(debugBatch);
	}

	@Override
	public Timeline.Key.Object getFirePoint() {
		for(int i = 0; i < sprite_player.getAnimation().timelines(); i++) {
			if(sprite_player.getAnimation().getTimeline(i).objectInfo.type == ObjectType.Point) {
				//System.out.println("point is: " + sprite_player.getObject(sprite_player.getAnimation().getTimeline(i).name).isBone());
				return sprite_player.getObject(sprite_player.getAnimation().getTimeline(i).name);
			}
		}
		
		return null;
	}
}
