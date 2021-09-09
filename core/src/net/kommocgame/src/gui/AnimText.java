package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

@Deprecated
public class AnimText extends Label {
	
	private float x = 0;
	
	private int R1;
	private int R2;
	private float y1;
	
	private int G1;
	private int G2;
	private float y2;
	
	private int B1;
	private int B2;
	private float y3;
	
	private long lastTime = 0;
	private long timer = 2000;
	
	public AnimText(CharSequence text, LabelStyle style) {
		super(text, style);
		this.setTouchable(Touchable.disabled);
		setToDefault();
	}
	
	public void setRangeR(int begin, int end) {
		R1 = begin;
		R2 = end;
	}
	
	public void setRangeG(int begin, int end) {
		G1 = begin;
		G2 = end;
	}
	
	public void setRangeB(int begin, int end) {
		B1 = begin;
		B2 = end;
	}
	
	public void setTimer(long time) {
		timer = time;
	}
	
	public void setToDefault() {
		R1 = 62;
		R2 = 62;
		G1 = 190;
		G2 = 190;
		B1 = 187;
		B2 = 255;
		
		timer = 2000;
	}
	
	@Override
	public void act(float deltaTime) {
		if(lastTime + timer < System.currentTimeMillis()) {
			x = 0;
			lastTime = System.currentTimeMillis();
		} else {
			x = (float)(System.currentTimeMillis() - lastTime) / 1000f;
		}
		
		y1 = MathUtils.sin(x * MathUtils.PI) * (R1 - R2);
		y2 = MathUtils.sin(x * MathUtils.PI) * (G1 - G2);
		y3 = MathUtils.sin(x * MathUtils.PI) * (B1 - B2);
		this.setColor((float)(R1 + y1)/255f, (float)(G1 + y2)/255f, (float)(B1 + y3)/255f, 1f);
		
		
		//y = MathUtils.sin(x * MathUtils.PI) * 68f;
		//this.setColor(62f/255f, 190f/255f, (187f + y)/255f, 1f);
	}
}
