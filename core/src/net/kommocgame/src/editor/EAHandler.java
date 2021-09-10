package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.editor.actions.EActionDelete;
import net.kommocgame.src.editor.actions.EActionRotation;
import net.kommocgame.src.editor.actions.EActionScale;
import net.kommocgame.src.editor.actions.EActionTranslate;
import net.kommocgame.src.editor.actions.EditorAction;
import net.kommocgame.src.editor.objects.EOEntity;
import net.kommocgame.src.editor.objects.EOTerrain;
import net.kommocgame.src.editor.objects.EOTrigger;
import net.kommocgame.src.editor.objects.EObject;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.gui.GuiConsole;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.level.TerrainObject;

public class EAHandler {
	
	/** Choosed object. */
	private IEditorActions object;
	
	/** Current action. */
	private EditorAction action;
	
	/** Copy of choosed object. */
	private EObject copy_buffer;
	
	private GuiEditor gui;
	
	public EAHandler(GuiEditor gui) {
		this.gui = gui;
		RenderEngine.debug_editor.setHandler(this);
	}
	
	public void update() {
		if(object == null) {
			if(KeyBinding.but_e_chooseObj && !gui.clickListener.isOver()) {
				setObject();
				System.out.println("		object: " + object);
				
				//System.out.println("		Entity: " + gui.core.getWorld().getObjectByCursor(EntityBase.class));
				//System.out.println("		Trigger: " + gui.core.getWorld().getObjectByCursor(TriggerBase.class));
				//System.out.println("		TerrainObj: " + gui.core.getWorld().getLevel().terrain.getTerrainObject((Image) gui.core.getWorld().getLevel().terrain.objects
				//		.hit(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y, true)));
				return;
			}
		}
		
		if(KeyBinding.but_e_copy && KeyBinding.getKey(Keys.CONTROL_LEFT)) {
			if(getChoosedObject() != null) {
				copy_buffer = getChoosedObject().getCopy();
				
				System.out.println("		EAHandler.update() ### copy: " + copy_buffer);
			}
		}
		
		if(KeyBinding.but_e_co_translate) {
			if(object != null && getChoosedObject().getInstance() != null && !(action instanceof EActionTranslate)) {
				if(action != null)
					action.cancel();
				action = new EActionTranslate(object);
			}
			return;
		}
		
		if(KeyBinding.but_e_co_delete) {
			if(object != null && getChoosedObject().getInstance() != null && !(action instanceof EActionDelete)) {
				if(action != null)
					action.cancel();
				action = new EActionDelete(object, gui.core);
			}
			return;
		}
		
		if(KeyBinding.but_e_co_rotate) {
			if(object != null && getChoosedObject().getInstance() != null && !(action instanceof EActionRotation)) {
				if(action != null)
					action.cancel();
				action = new EActionRotation(object);
			}
			return;
		}
		
		if(KeyBinding.but_e_co_scale) {
			if(object != null && getChoosedObject().getInstance() != null && !(action instanceof EActionScale)) {
				if(action != null)
					action.cancel();
				action = new EActionScale(object);
			}
			return;
		}
		
		if(action != null) {
			action.update();
			
			if(KeyBinding.but_e_event_apply) {
				System.out.println("apply");
				action.apply();
			} if(KeyBinding.but_e_event_cancel) {
				System.out.println("cancel");
				action.cancel();
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)) {
				action.setCtrl(!action.isCtrl());
				System.out.println("		EAHandler.update() ### CONTROL_LEFT mode: " + action.isCtrl());
			} if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
				action.setShift(!action.isShift());
				System.out.println("		EAHandler.update() ### SHIFT_LEFT mode: " + action.isShift());
			} if(Gdx.input.isKeyJustPressed(Keys.ALT_LEFT)) {
				action.setAlt(!action.isAlt());
				System.out.println("		EAHandler.update() ### ALT_LEFT mode: " + action.isAlt());
			}
			
			if(action.isDeactive())
				action = null;
		} else if(KeyBinding.but_e_paste && KeyBinding.getKey(Keys.CONTROL_LEFT)) {
			pasteObject();
		}
		
		if(KeyBinding.but_e_event_cancel && object != null && action == null || !gui.core.getListNodes().contains((EObject)object, false)) {
			object = null;
		}
	}
	
	/** Entity - Trigger - TerrainObject */
	public void setObject() {
		if(gui.core.getWorld() != null && gui.core.getWorld().getLevel() != null) {
			Object obj = gui.core.getWorld().getObjectByCursor(EntityBase.class);
			
			if(obj == null)
				obj = gui.core.getWorld().getObjectByCursor(TriggerBase.class);
			else object = (EOEntity) gui.core.getNodeByObject(obj);
			
			if(obj == null) {
				obj = gui.core.getWorld().getLevel().terrain.getTerrainObject((Image) gui.core.getWorld().getLevel().terrain.objects
						.hit(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y, true));
				if(obj != null)
					object = (EOTerrain) gui.core.getNodeByObject(obj);
			} else if(object == null) object = (EOTrigger) gui.core.getNodeByObject(obj);
		}
		
		gui.core.setNodeObject(getChoosedObject());
	}
	
	/** Need for remote call from panel. */
	public void setObject(Object obj) {
		object = (EObject) obj;
	}
	
	/** Return the current action. */
	public EditorAction getCurrentAction() {
		return action;
	}
	
	/** Return the EObject instance. */
	public EObject getChoosedObject() {
		return (EObject) object;
	}
	
	/** Return the copy of EObject instance. */
	public EObject getCopyObject() {
		return copy_buffer;
	}
	
	/** Need for DebugEditor. */
	public boolean readyToDraw() {
		return gui != null ? gui.core.getWorld().getLevel() != null : false;
	}
	
	/** Spawn EObject into world. */
	private void pasteObject() {
		System.out.println("		EAHandler.update() ### paste");
		
		copy_buffer.setWorld(gui.core.getWorld());
		copy_buffer.createInstance();
		
		for(int index = 0; index < copy_buffer.getInstance().getAdditionalData().getParameters().size; index++) {
			DataObject data = copy_buffer.getInstance().getAdditionalData().getParameters().get(index);
			
			System.out.println("	EditorCore.loadLevel() ### value_name: " + data.getName() + " is contains: "
					+ copy_buffer.getAdditionalData().getMap().containsKey(data.getName()));
			
			if(copy_buffer.getAdditionalData().getMap().containsKey(data.getName())) {
				data.setParameter(copy_buffer.getAdditionalData().getMap().get(data.getName()));
			}
		}
		
		copy_buffer.setAdditionalData(copy_buffer.getInstance().getAdditionalData());
		
		gui.core.addObject(copy_buffer);
		if(copy_buffer.getInstance() instanceof TerrainObject) {
			TerrainObject tobject = (TerrainObject) copy_buffer.getInstance();
			tobject.setLayerIndex(-1);
			tobject.setLayerPositionID(-1);
		}
		
		action = new EActionTranslate(copy_buffer);
		setObject(copy_buffer);
		copy_buffer = copy_buffer.getCopy();
	}
}
