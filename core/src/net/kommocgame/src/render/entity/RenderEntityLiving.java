package net.kommocgame.src.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.Game;
import net.kommocgame.src.entity.EntityLiving;

public class RenderEntityLiving extends RenderEntity {
	
	EntityLiving entityLiving;
	
	private Label label_fieldOfView_radius = new Label("", Game.NEUTRALIZER_UI);
	private Label label_fieldOfView_angle = new Label("", Game.NEUTRALIZER_UI);
	private Label label_task = new Label("", Game.NEUTRALIZER_UI);
	private Label label_stateMachine = new Label("", Game.NEUTRALIZER_UI);
	private Label label_position = new Label("", Game.NEUTRALIZER_UI);
	private Label label_behavior = new Label("", Game.NEUTRALIZER_UI);
	private Label label_rotation = new Label("", Game.NEUTRALIZER_UI);
	private Label label_guild = new Label("", Game.NEUTRALIZER_UI);
	private Label label_speed = new Label("", Game.NEUTRALIZER_UI);
	
	float x, y, hgt = 0.5f;
	
	public RenderEntityLiving(EntityLiving par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		entityLiving = par1;
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		entityLiving.getSprite().draw(renderBatch);
		entityLiving.getSprite().setRotation(entityLiving.getRotation());
	}
	
	@Override
	public void renderInfo(SpriteBatch debugBatch) {
		x = (entityLiving.getPosition().x - entityLiving.getBoundingRadius()) / scale;
		y = entityLiving.getPosition().y + entityLiving.getBoundingRadius();

		if(entityLiving.getFieldOfView() != null) {
			drawDebugText(debugBatch, label_fieldOfView_angle, "FOV: " + entityLiving.getFieldOfView().getAngle(), 1);
			drawDebugText(debugBatch, label_fieldOfView_radius, "FOV: " + entityLiving.getFieldOfView().getRadius(), 2);
		}
		
		drawDebugText(debugBatch, label_position, 
				"X: " + "".format("%.1f", entityLiving.getPosition().x) + "; Y: " + "".format("%.1f", entityLiving.getPosition().y), 3);
		
		if(entityLiving.state_machine != null)
			drawDebugText(debugBatch, label_stateMachine, "State_Machine: " + entityLiving.state_machine.getCurrentState(), 4);
		
		drawDebugText(debugBatch, label_behavior, "Behavior: " + (entityLiving.getBehavior() != null 
				? entityLiving.getBehavior().getClass().getSimpleName() : entityLiving.getBehavior()), 5);
		
		//drawDebugText(debugBatch, label_task,"[" + entityLiving.getAI().getTaskList().size + "] " + 
		//		"Task: " + (entityLiving.getAI().getCurrentTask() != null ? 
		//		entityLiving.getAI().getCurrentTask().getClass().getSimpleName() : entityLiving.getAI().getCurrentTask()), 6);
		
		label_rotation.setColor(Color.BLUE);
		drawDebugText(debugBatch, label_rotation, "Rotation: " + "".format("%.2f", entityLiving.getOrientation()), 8);
		drawDebugText(debugBatch, label_rotation, "			## " + "".format("%.0f", entityLiving.getOrientation() * MathUtils.radDeg + 90f), 8);
		
		label_guild.setColor(Color.GREEN);
		//drawDebugText(debugBatch, label_guild, "Guild: " + entityLiving.getAI().getGuild().getName(), 9);
		
		float speed = (float) Math.sqrt(entityLiving.getLinearVelocity().x * entityLiving.getLinearVelocity().x + 
				entityLiving.getLinearVelocity().y * entityLiving.getLinearVelocity().y);
		
		drawDebugText(debugBatch, label_speed, "Speed: " + "".format("%.1f", speed), 10);
	}
	
	public void drawDebugText(SpriteBatch batch, Label label, String text, int line) {
		label.setText(text);
		label.setPosition(x, (y + hgt * line) / scale);
		label.draw(batch, 1f);
	}
}
