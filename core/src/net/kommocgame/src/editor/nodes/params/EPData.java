package net.kommocgame.src.editor.nodes.params;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ArrayMap;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorButton;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.editor.GuiEditorSpawnerOptrions;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.props.EBodyType;
import net.kommocgame.src.world.DataObject;

public class EPData extends EParamObj {
	
	DataObject data;
	
	TextField text_name = new TextField("", Game.NEUTRALIZER_UI);
	SelectBox selector_boolean = new SelectBox(Game.NEON_UI);
	SelectBox selector_color = new SelectBox(Game.NEON_UI);
	SelectBox selector_param = new SelectBox(Game.NEON_UI);
	
	EditorButton but_apply = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_apply.png"));
	
	Label label_title = new Label("param_name", Game.NEUTRALIZER_UI);
	
	public EPData(GuiEditor gui, DataObject data) {
		this(gui, Game.NEUTRALIZER_UI, data);
	}

	public EPData(GuiEditor gui, Skin skin, DataObject data) {
		super(gui, skin);
		this.setBackground("button");
		this.left();
		this.data = data;
		
		Table table_param = new Table(skin);
		Table table_button = new Table(skin);
		
		label_title.setText(data.getName());
		this.add(label_title);
		this.row();
		this.add(table_param).expandX();
		this.add(table_button);
		
		table_param.add(new Label("Value: ", skin));
		
		selector_boolean.setItems(true, false);
		selector_color.setItems("BLACK", "BLUE", "BROWN", "CHARTREUSE", "CLEAR", "CORAL", "CYAN",
				"DARK_GRAY", "FIREBRICK", "FOREST", "GOLD", "GOLDENROD", "GRAY", "GREEN",
				"LIGHT_GRAY", "LIME", "MAGENTA", "MAROON", "NAVY", "OLIVE", "ORANGE", "PINK",
				"PURPLE", "RED", "ROYAL", "SALMON", "SCARLET", "SKY", "SLATE",
				"TAN", "TEAL", "VIOLET", "WHITE", "YELLOW");
		
		if(data.getParameter() instanceof Integer || data.getParameter() instanceof Double ||data.getParameter() instanceof Float) {
			table_param.add(text_name);
			text_name.setText("" + data.getParameter());
			
			text_name.setTextFieldFilter(filter);
		} if(data.getParameter() instanceof Boolean) {
			table_param.add(selector_boolean);
			
			selector_boolean.setSelected((Boolean) data.getParameter());
		} if(data.getParameter() instanceof Color) {
			table_param.add(selector_color);
			selector_color.setSelected(EColor.getEColor((Color)data.getParameter()).toString());
		} if(data.getParameter() instanceof Enum) {
			Enum param = (Enum) data.getParameter();
			
			selector_param.setItems(param.getDeclaringClass().getEnumConstants());
			selector_param.setSelected(param);
			table_param.add(selector_param);
		}
		
		table_button.add(but_apply);
		System.out.println("		EPData(new instance) ### created succesfully.");
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		try {
			//System.out.println("String.valueOf(data.getParameter()): "+ String.valueOf(data.getParameter()));
			//System.out.println("text_name.getText(): "+ text_name.getText());
			//System.out.println("text_name.getText(): "+ (text_name.getText() != String.valueOf(data.getParameter())));
			
			if(text_name.getText() != String.valueOf(data.getParameter())) {
				text_name.setColor(Color.RED);
			} else {
				text_name.setColor(Color.WHITE);
			}
			
			if(data.getParameter() instanceof Boolean)
				if(selector_boolean.getSelection() != data.getParameter()) {
					selector_boolean.setColor(Color.RED);
				} else {
					selector_boolean.setColor(Color.WHITE);
				}
			
			if(data.getParameter() instanceof Color)
				if((EColor.valueOf((String)selector_color.getSelection().getLastSelected())).getColor().toFloatBits() != 
						((Color)data.getParameter()).toFloatBits()) {
					selector_color.setColor(Color.RED);
				} else {
					selector_color.setColor(Color.WHITE);
				}
			
			//System.out.println("		EPData.act() ### stg_1");
			if(but_apply.isChecked()) {
				but_apply.setChecked(false);
				System.out.println("		EPData.act() ### data.getName(): " + data.getName());
				System.out.println("		EPData.act() ### data.getParameter(): " + data.getParameter());
				System.out.println("		EPData.act() ### data is Enum: " + (data.getParameter() instanceof Enum));
				
				if(data.getName().equals("Pool list")) {
					System.out.println("		EPData.act() ### data.getName() equal : " + (data.getName().equals("Pool list")));
					gui.getGuiManager().addGui(new GuiEditorSpawnerOptrions(gui.game, gui, object, data));
					return;
				}
				
				if(data.getParameter() instanceof Integer) {
					data.setParameter(Integer.valueOf(text_name.getText()));
				} if(data.getParameter() instanceof Float) {
					data.setParameter(Float.valueOf(text_name.getText()));
				} if(data.getParameter() instanceof Double) {
					data.setParameter(Double.valueOf(text_name.getText()));
				} if(data.getParameter() instanceof Boolean) {
					System.out.println("EPData.act() ### Selector_boolean: " + selector_boolean.getSelected());
					
					data.setParameter(selector_boolean.getSelected());
				} if(data.getParameter() instanceof Color) {
					data.setParameter((EColor.valueOf((String)selector_color.getSelection().getLastSelected())).getColor());
				} if(data.getParameter() instanceof Enum) {
					System.out.println("EPData.act() ### Selector_enum: " + selector_param.getSelected());
					
					data.setParameter((Enum)selector_param.getSelection().getLastSelected());
				}
				
				//if(data.getParameter() instanceof String)
				//	data.setParameter(text_name.getText());
				
				
			}
		} catch(Exception e) {}
	}
}
