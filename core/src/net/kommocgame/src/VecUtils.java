package net.kommocgame.src;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VecUtils {
	
	private final static Vector2 vector_1 = new Vector2();
	private final static Vector2 vector_2 = new Vector2();

	public static float vectorToAngle (Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}
	
	public static Vector2 angleToVector (Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}
	
	/** Used only in ItemWeapon. */
	public static Vector2 angleToVector_1(float angle) {
		Vector2 vec2 = new Vector2();
		vec2.x = (float)MathUtils.cosDeg(angle);
		vec2.y = (float)MathUtils.sinDeg(angle);
		return vec2;
	}
	
	public static float getAngleAtPoint(float x, float y) {
		vector_1.set(1, 0);
		vector_2.set(x, y);
		
		return vector_1.angle(vector_2);
	}
	
	public static Vector2 getNormalVec(Vector2 from_vector, Vector2 line_vector, Vector2 point, Vector2 toVector) {
		float lenght = 1f;
		toVector.set(point).sub(from_vector);
		
		if((line_vector.x * line_vector.x + line_vector.y * line_vector.y) != 0)
			lenght = - (toVector.x * line_vector.x + toVector.y * line_vector.y) / 
					(line_vector.x * line_vector.x + line_vector.y * line_vector.y);
		
		return toVector.mulAdd(line_vector, lenght);
	}
}
