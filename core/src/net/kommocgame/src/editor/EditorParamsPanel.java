package net.kommocgame.src.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.nodes.params.EPBounds;
import net.kommocgame.src.editor.nodes.params.EPData;
import net.kommocgame.src.editor.nodes.params.EPName;
import net.kommocgame.src.editor.nodes.params.EPRotation;
import net.kommocgame.src.editor.nodes.params.EPSpawnPosition;
import net.kommocgame.src.editor.objects.EObject;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.gui.GuiManager;

public class EditorParamsPanel extends Window {
	@Deprecated
	private Object choosed_obj;
	
	GuiEditor gui;
	EObject object;
	EObject prew_object;
	
	private TextFieldFilter filter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			if((c >= '0' && c <= '9') || c == '.') {
				if(textField.getText().contains(".") && c == '.')
					return false;
				
				return true;
			}
			
			return false;
		}
	};
	
	private Table scroll_table = new Table(Game.NEUTRALIZER_UI);
	private ScrollPane scroll_pane = new ScrollPane(scroll_table);
	
	public EditorParamsPanel(GuiEditor gui, String title, Skin skin) {
		super(title, skin);
		this.setMovable(false);
		this.gui = gui;
		
		this.add(scroll_pane).expand().top().left();
		scroll_pane.setScrollingDisabled(true, false);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		object = gui.core.getEAHandler().getChoosedObject();
		
		if(object != null) {
			if(prew_object != object) {
				this.setObject();
			}
		} else {
			scroll_table.reset();
		}
		
		prew_object = object;
		//this.debugAll();
		
		//13.06.19 - fix the unfocus scroll.
				if(getStage() != null && getStage().getRoot() != null &&
						this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) != null) {
					
					if(GuiManager.getInstanceOf(getStage().getRoot(), ScrollPane.class, 
								this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true)) == scroll_pane) {
						getStage().setScrollFocus(scroll_pane);
					} else {
						getStage().unfocus(scroll_pane);
					}
				} else {
					getStage().unfocus(scroll_pane);
				}
	}
	
	@Deprecated
	public void setEntityLiving(EntityLiving entityLiving) {
		if(choosed_obj == null || choosed_obj != entityLiving)
			choosed_obj = entityLiving;
		else return;
		
		this.reset();
		this.top();
		this.left();
		
		this.add(new ParamPosition(Game.NEUTRALIZER_UI));
		this.row();
		this.add(new ParamRotation(Game.NEUTRALIZER_UI));
		
	}
	
	public void setObject() {
		scroll_table.reset();
		scroll_table.top();
		scroll_table.left();
		
		scroll_table.add(new EPSpawnPosition(gui)).left().width(this.getWidth());
		scroll_table.row();
		scroll_table.add(new EPBounds(gui)).left().width(this.getWidth());
		scroll_table.row();
		scroll_table.add(new EPRotation(gui)).left().width(this.getWidth());
		scroll_table.row();
		scroll_table.add(new EPName(gui)).left().width(this.getWidth());
		scroll_table.row();
		
		for(int i = 0; i < object.getAdditionalData().getParameters().size; i++) {
			scroll_table.add(new EPData(gui, object.getAdditionalData().getParameters().get(i))).left().width(this.getWidth());
			scroll_table.row();
		}
	}
	
	@Deprecated
	private class ParamPosition extends Table {
		
		private TextField text_x = new TextField("", Game.NEUTRALIZER_UI);
		private TextField text_y = new TextField("", Game.NEUTRALIZER_UI);
		private Label label = new Label("Position", Game.NEUTRALIZER_UI);
		private EditorButton but_apply = new EditorButton(Game.COMMODORE_64_UI, Loader.guiEditor("but/menu/icon_apply.png"));
		
		public ParamPosition(Skin skin) {
			super(skin);
			Label label_x = new Label("X: ", Game.NEUTRALIZER_UI);
			Label label_y = new Label("Y: ", Game.NEUTRALIZER_UI);
			
			text_x.setTextFieldFilter(filter);
			text_y.setTextFieldFilter(filter);
			
			text_x.setText(String.valueOf(((EntityLiving)choosed_obj).compPhysics.body.definition.getPosition().x));
			text_y.setText(String.valueOf(((EntityLiving)choosed_obj).compPhysics.body.definition.getPosition().y));
			
			this.add(label).expandX();
			this.row();
			this.add(label_x);
			this.add(text_x);
			this.row();
			this.add(label_y);
			this.add(text_y);
			this.row();
			this.add(but_apply).expandX();
			this.pack();
		}
		
		@Override
		public void act(float deltaTime) {
			if(but_apply.isChecked() && !text_x.getText().isEmpty() && !text_y.getText().isEmpty()) {
				this.setPos(this.getPositionX(), this.getPositionY());
				but_apply.setChecked(false);
			}
		}
		
		public void setPos(float x, float y) {
			((EntityLiving)choosed_obj).compPhysics.body.setPosition(x, y);
		}
		
		public float getPositionX() {
			return Float.parseFloat(text_x.getText());
		}
		
		public float getPositionY() {
			return Float.parseFloat(text_y.getText());
		}
	}
	
	@Deprecated
	private class ParamRotation extends Table {
		
		private TextField text_rot = new TextField("", Game.NEUTRALIZER_UI);
		private Label label = new Label("Rotation", Game.NEUTRALIZER_UI);
		private EditorButton but_apply = new EditorButton(Game.COMMODORE_64_UI, Loader.guiEditor("but/menu/icon_apply.png"));
		
		public ParamRotation(Skin skin) {
			super(skin);
			Label label_angle = new Label("Angle: ", Game.NEUTRALIZER_UI);
			
			text_rot.setTextFieldFilter(filter);
			text_rot.setText(String.valueOf(((EntityLiving)choosed_obj).compPhysics.body.definition.getAngle()));
			
			this.add(label).expandX();
			this.row();
			this.add(label_angle);
			this.add(text_rot);
			this.row();
			this.add(but_apply).expandX();
			this.pack();
		}
		
		@Override
		public void act(float deltaTime) {
			if(but_apply.isChecked() && !text_rot.getText().isEmpty()) {
				System.out.println("OLOLOSHKA");
				this.setRot(getRot());
				but_apply.setChecked(false);
			}
			
			//if(choosed_obj != null)
				//System.out.println("ROTATION: " + ((EntityLiving)choosed_obj).compPhysics.body.definition.getAngle());
		}
		
		public void setRot(float angle) {
			//((EntityLiving)choosed_obj).compPhysics.body.setRotation(angle);
			//((EntityLiving)choosed_obj).compPhysics.body.definition.setTransform(0, 0, angle); FIXME
		}
		
		public float getRot() {
			return Float.parseFloat(text_rot.getText());
		}
	}
}
