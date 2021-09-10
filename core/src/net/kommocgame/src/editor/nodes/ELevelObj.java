package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.editor.objects.EObject;

public class ELevelObj extends Table {
	
	EObject node;
	GuiEditor gui;
	
	Image img_main;
	Label label_name = new Label("name_", Game.NEUTRALIZER_UI);
	Label label_position = new Label("pos_", Game.NEUTRALIZER_UI);
	Label label_type = new Label("type_", Game.NEUTRALIZER_UI);
	Table img_canvas = new Table(Game.NEUTRALIZER_UI);
	
	float scale_node = 1f;
	
	public ELevelObj(GuiEditor gui, EObject object) {
		super(Game.NEUTRALIZER_UI);
		node = object;
		this.gui = gui;
		this.setSize(192, 64f);
		this.left();
		this.setBackground("button");
		
		label_name.setWrap(true);
		label_position.setWrap(true);
		label_type.setWrap(true);
		
		label_name.setColor(Color.BLUE);
		label_position.setColor(Color.CYAN);
		
		img_main = new Image(node.getImage().getDrawable());
		this.add(img_canvas).width(64f * scale_node).height(64f * scale_node);
		this.add(label_name).width(96f * scale_node).growY();
		this.add(label_position).width(128f * scale_node).growY();
		this.add(label_type).width(64f * scale_node).growY();
		
		label_name.setText(node.getType().getSimpleName());
		
		this.setImage(img_main);
	}
	
	public EObject getNode() {
		return node;
	}
	
	public Image getImage() {
		return img_main;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(!gui.core.getListNodes().contains(node, false)) {
			this.remove();
			return;
		}
		
		if(getNode() != null && getNode().getInstance() != null) {
			label_position.setText("X: " + "".format("%.1f", node.getPosition().x) + "; Y: " + "".format("%.1f", node.getSpawnPosition().y));
		}
		
		if(getStage() == null) {
			System.out.println("rrrrrrr");
		}
	}
	
	private void setImage(Image img) {
		img_main = img;
		float ratio = img.getWidth() / img.getHeight();
		
		img_canvas.clear();
		img_canvas.add(img_main);
		img_canvas.getCell(img_main).size(64f * ratio * scale_node, 64f * scale_node);
	}
}
