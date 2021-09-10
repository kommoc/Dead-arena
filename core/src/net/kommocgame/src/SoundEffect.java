package net.kommocgame.src;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import net.dermetfan.gdx.math.GeometryUtils;
import net.kommocgame.src.SoundManager.SoundSource;

public class SoundEffect implements Poolable {
	
	public float x;
	public float y;
	
	private float pan = 0.5f;
	private float pitch = 1f;
	// world delete sound 
	private long last_time;
	private boolean pre_done = false;
	
	private Sound sound;
	private long id;
	private float value_off = 1f;
	private float volume = 1f;
	private float current_volume = volume;
	private boolean isRepeatable = false;
	
	private float distance = 1f;
	private float max_distance = distance + distance * value_off;
	private float distanceToListener;
	
	private boolean isFinished = false;
	
	/** Pool sound. Need to be defined after getting from array. */
	public SoundEffect() {
		x = 0;
		y = 0;
		sound = null;
		
		System.out.println("SoundManager(new instance) ### created pool constructor.");
	}
	
	/** Simply sound. After add this sound will play now. */
	public SoundEffect(Sound sound, float x, float y) {
		this.sound = sound;
		this.x = x;
		this.y = y;
	}
	
	/** Simply sound. After add this sound will play now. */
	public SoundEffect(SoundSource soundSource, float x, float y) {
		this(soundSource.getSound(), x, y);
	}
	
	/** Initialize method called after receive from pool. */
	public SoundEffect setInitialize(SoundSource soundSource, float x, float y) {
		return setInitialize(soundSource.getSound(), x, y);
	}
	
	/** Initialize method called after receive from pool. */
	public SoundEffect setInitialize(Sound sound, float x, float y) {
		this.sound = sound;
		this.x = x;
		this.y = y;
		return this;
	}
	
	/** Set the field of clear sound. */
	public SoundEffect setDistance(float distance) {
		this.distance = distance;
		max_distance = distance + distance * value_off;
		return this;
	}
	
	/** Could be repeat. */
	public SoundEffect setRepeateble(boolean par1) {
		this.isRepeatable = par1;
		return this;
	}
	
	/** Set the value that set max distance. */
	public SoundEffect setLinearFade(float par1) {
		this.value_off = par1;
		max_distance = distance + distance * value_off;
		return this;
	}
	
	/** Set the volume of sound. */
	public SoundEffect setVolume(float volume) {
		this.volume = volume;
		return this;
	}
	
	/** Set the point of sound. */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/** Delete sound effect from world. */
	public void setFinished() {
		isFinished = true;
		sound.stop(id);
	}
	
	public float getVolume() {
		return volume;
	}
	
	public float getLinearFade() {
		return value_off;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getMaxDistance() {
		return max_distance;
	}
	
	/** Return volume of sound by listener has on current position. */
	public float getHearingEffect(Vector2 pos) {
		float distanceToTarget = GeometryUtils.distance(this.x, this.y, pos.x, pos.y);
		
		if(distanceToTarget > max_distance)
			return 0f;
		else if(distanceToTarget > distance && distanceToTarget <= max_distance)
			return volume * (max_distance - distanceToTarget) / (max_distance - distance);
		else
			return volume;
	}
	
	public boolean isFinished() {
		if(isFinished)
			return true;
		
		if(last_time + SoundManager.time.get(sound) < System.currentTimeMillis() && !pre_done && !isRepeatable)
			return true;
		
		return false;
	}
	
	/** Stop playng current sound. */
	public void stop() {
		pre_done = true;
		sound.stop(id);
	}
	
	/** Update sound data. Params x, y - position Listener. */
	public void update(float x, float y) {
		distanceToListener = GeometryUtils.distance(this.x, this.y, x, y);
		
		if(distanceToListener > max_distance) {
			sound.setVolume(id, 0);
			current_volume = 0;
			return;
		} else if(distanceToListener > distance && distanceToListener <= max_distance) {
			current_volume = volume * (max_distance - distanceToListener) / (max_distance - distance);
			
			if(current_volume < 0)
				current_volume = 0;
			
			float x_diff = x - this.x;
			
			if(Math.abs(x_diff) > 5)
				pan = -(x_diff - Math.abs(x_diff) / x_diff * distance) / max_distance * 1.4f;
		} else {
			pan = 0f;
			current_volume = volume;
		}
		
		if(current_volume > volume)
			try {
				throw new Exception("current_volume > volume");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(pan > 1 || pan < -1)
			try {
				throw new Exception(" pan > 1 || pan < -1 ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		sound.setPan(id, pan, current_volume * Game.profile.settings_volume_sound());
	}
	
	/** Params x, y - position Listener. */
	public long play(float x, float y) {
		//System.out.println("\npreUpdate: " + current_volume);
		//System.out.println("\npreUpdate: " + (current_volume * Game.profile.settings_volume_sound()));
		this.update(x, y);
		//System.out.println("distanceToListener" + distanceToListener);
		////System.out.println("max_distance" + max_distance);
		//System.out.println("postUpdate: " + current_volume);
		
		if(isRepeatable)
			return id = this.sound.loop(current_volume * Game.profile.settings_volume_sound(), pitch, pan);
		else {
			last_time = System.currentTimeMillis();
			return id = this.sound.play(current_volume * Game.profile.settings_volume_sound(), pitch, pan);
		}
	}

	@Override
	public void reset() {
		pan = 0.5f;
		pitch = 1f;
		// world delete sound 
		pre_done = false;
		
		value_off = 1f;
		volume = 1f;
		current_volume = volume;
		isRepeatable = false;
		
		distance = 1f;
		max_distance = distance + distance * value_off;
		isFinished = false;
	}
}
