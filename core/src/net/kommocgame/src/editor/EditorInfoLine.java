package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;

public class EditorInfoLine extends Table {
	
	GuiEditor gui;
	
	Label label_object_name = new Label("label_obj", Game.NEUTRALIZER_UI);
	Label label_action = new Label("label_action", Game.NEUTRALIZER_UI);
	EditorButton but_tp = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_setTo.png"));
	TextTooltip tooltip_tpCam;
	
	public EditorInfoLine(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EditorInfoLine(GuiEditor gui, Skin skin) {
		super(skin);
		this.gui = gui;
		Table table_info = new Table(skin);
		
		this.setSize(328f, 64f);
		this.bottom().right();
		
		label_object_name.setColor(Color.RED);
		label_action.setColor(Color.CYAN);
		
		label_action.setWrap(true);
		label_object_name.setWrap(true);
		
		this.add(table_info).expand().bottom().left();
		table_info.left();
		table_info.add(label_object_name).growX().left();
		table_info.row();
		table_info.add(label_action).growX().left();
		this.add(but_tp).expandY().bottom();
		
		tooltip_tpCam = new TextTooltip("Teleport camera to object." ,skin);
		tooltip_tpCam.getManager().initialTime = 0f;
		tooltip_tpCam.getManager().edgeDistance = 0;
		tooltip_tpCam.getManager().offsetY = -64f;
		tooltip_tpCam.getManager().offsetX = -96f;
		tooltip_tpCam.getManager().resetTime = 0f;
		tooltip_tpCam.getManager().subsequentTime = 0f;
		
		
		but_tp.addListener(tooltip_tpCam);
	}
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(but_tp.isChecked() && gui.core.getEAHandler().getChoosedObject() != null) {
			but_tp.setChecked(false);
			gui.core.getCamera().position.x = gui.core.getEAHandler().getChoosedObject().getPosition().x
					+ (Gdx.graphics.getWidth() - gui.panel_levelObjects.getWidth()) / 2f * Game.SCALE_GUI_VALUE
					- gui.panel_levelObjects.getWidth() * Game.SCALE_GUI_VALUE;
			gui.core.getCamera().position.y = gui.core.getEAHandler().getChoosedObject().getPosition().y
					+ (Gdx.graphics.getHeight() - gui.panel_objects.getHeight()) / 2f * Game.SCALE_GUI_VALUE
					- gui.panel_objects.getHeight() * Game.SCALE_GUI_VALUE;
		} else but_tp.setChecked(false);
		
		if(gui.core.getEAHandler().getChoosedObject() != null) {
			label_object_name.setVisible(true);
			label_object_name.setText("Object: " + gui.core.getEAHandler().getChoosedObject().getType().getSimpleName());
		} else label_object_name.setVisible(false);
		if(gui.core.getEAHandler().getCurrentAction() != null) {
			label_action.setVisible(true);
			label_action.setText("Action: " + gui.core.getEAHandler().getCurrentAction());
		} else label_action.setVisible(false);
	}
}
