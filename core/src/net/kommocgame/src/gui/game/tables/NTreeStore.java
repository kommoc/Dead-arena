package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.game.items.TableItemsInfoName;
import net.kommocgame.src.gui.game.items.TableStoreItemInfoTitle;
import net.kommocgame.src.item.Item;

public class NTreeStore extends NavigationTree {
	
	NavigationNode begin;
	
	public NTreeStore() {
		super();
		
		ContainerNode container_node = new ContainerNode(Loader.guiEditor("but/menu/icon_setTo.png"), "Store");
		ContainerItem container_item = new ContainerItem("TUT DOLJEN BIT TEXT", Loader.guiEditor("but/menu/icon_setTo.png"));
		begin = new NavigationNode(container_node, container_item).setNodeName("Magazine");
		
		NavigationNode node_vests = createNode(begin, "armor/icon_bodyArmor.png", "Vests").setNodeName("Vests");
		NavigationNode node_weapons = createNode(begin, "weapons/rifle/icon_weapon_rifle.png", "Weapons").setNodeName("Weapons");
		NavigationNode node_other = createNode(begin, "other/icon_other.png", "Other").setNodeName("Other");
		
		//createNode(node_vests, "armor/icon_bodyArmor_light.png", "Light armor", true, 25000).setNodeName("Medium armor");
		//createNode(node_vests, "armor/icon_bodyArmor_medium.png", "Medium armor", true, 182000).setNodeName("Medium armor");
		
		//createNode(node_other, "other/icon_aid_kit_little.png", "First aid kit", true, 5800).setNodeName("First aid kit");
		//createNode(node_other, "other/icon_aid_kit_medium.png", "Medium aid kit", true, 24500).setNodeName("Medium aid kit");
		//createNode(node_other, "other/icon_aid_kit_large.png", "Large aid kit", true, 55400).setNodeName("Large aid kit");
		
		NavigationNode node_pistols		= createNode(node_weapons, "weapons/pistol/icon_weapon_pistol.png", "Pistols").setNodeName("Pistols");
		NavigationNode node_submachine	= createNode(node_weapons, "weapons/pp/icon_weapon_pp.png", "Submachine guns").setNodeName("Submachine guns");
		NavigationNode node_shotguns	= createNode(node_weapons, "weapons/shotgun/icon_weapon_shotgun.png", "Shotguns").setNodeName("Shotguns");
		NavigationNode node_rifles		= createNode(node_weapons, "weapons/rifle/icon_weapon_rifle.png", "Rifles").setNodeName("Rifles");
		NavigationNode node_machinegun	= createNode(node_weapons, "weapons/machinegun/icon_weapon_machinegun.png", "Machineguns").setNodeName("Machineguns");
		NavigationNode node_grenade		= createNode(node_weapons, "weapons/other/icon_weapon_grenade.png", "Other").setNodeName("Other");
		
		createNode(node_pistols, Item.item_weapon_glock17).setNodeName("Glock-17");
		createNode(node_pistols, Item.item_weapon_desertEagle).setNodeName("Desert Eagle");
		createNode(node_submachine, Item.item_weapon_vector).setNodeName("Kriss Vector");
		createNode(node_shotguns, Item.item_weapon_remington870).setNodeName("Remington 870");
		createNode(node_rifles, Item.item_weapon_ak12).setNodeName("Ak-12");
		createNode(node_rifles, Item.item_weapon_m4a1).setNodeName("M4A1");
		createNode(node_machinegun, Item.item_weapon_m134).setNodeName("M134");
		createNode(node_machinegun, Item.item_weapon_kpv).setNodeName("KPV");
		
		createNode(node_vests, Item.item_armor_light).setNodeName(Item.item_armor_light.getName());
		createNode(node_vests, Item.item_armor_medium).setNodeName(Item.item_armor_medium.getName());
		
		createNode(node_other, Item.item_aidkit_light).setNodeName(Item.item_aidkit_light.getName());
		createNode(node_other, Item.item_aidkit_medium).setNodeName(Item.item_aidkit_medium.getName());
		createNode(node_other, Item.item_aidkit_large).setNodeName(Item.item_aidkit_large.getName());
		
	}
	
	private NavigationNode createNode(NavigationNode node, String str, String name) {
		return createNode(node, str, name, false, 0);
	}
	
	private NavigationNode createNode(NavigationNode node, String str, String name, boolean can_buy, int price) {
		ContainerNode container_node = new ContainerNode(Loader.guiIcon(str), name);
		ContainerItem container_item = new ContainerItem(name, Loader.guiIcon(str), can_buy).setItemPrice(price);
		
		return node.addNode(container_node, container_item);
	}
	
	private NavigationNode createNode(NavigationNode node, Item item) {
		ContainerNode container_node = new ContainerNode(item.getIcon(), item.getName());
		ContainerItem container_item = new ContainerItem(Game.NEUTRALIZER_UI, item);
		
		return node.addNode(container_node, container_item);
	}

	@Override
	public NavigationNode getNodeBegin() {
		return begin;
	}
	
	public class ContainerNode extends Button {
		Image img;
		Label label;
		
		public ContainerNode(Texture icon, String text) {
			this(icon, text, Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		}
		
		public ContainerNode(Texture icon, String text, LabelStyle style) {
			super(Game.NEUTRALIZER_UI);
			this.setColor(Game.COLOR_BUTTON);
			this.img = new Image(icon);
			label = new Label(text, style);
			label.setWrap(true);
			label.setColor(Color.valueOf("#7FFFD4"));
			
			this.add(img).prefSize(Gdx.graphics.getHeight() / 8f).pad(Game.GUI_NUMBER_PANEL_GAP);
			this.add(label).grow().left().padRight(Game.GUI_NUMBER_PANEL_GAP);
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
		}
	}
	
	/** TODO */
	public class ContainerItem extends Table {
		TableTitle table_title;
		
		/**TEST*/
		TableItemsInfoName			table_name;
		TableStoreItemInfoTitle	table_info;
		/**TEST*/
		
		private int price = 0;
		private int count = 0;
		
		public ContainerItem(String name, Texture icon) {
			this(Game.NEUTRALIZER_UI, name, icon, false);
		}
		
		public ContainerItem(String name, Texture icon, boolean buy_flag) {
			this(Game.NEUTRALIZER_UI, name, icon, buy_flag);
		}
		
		public ContainerItem(Skin skin, String name, Texture icon, boolean buy_flag) {
			super(Game.NEUTRALIZER_UI);
			
			if(buy_flag) this.table_title = new TableTitle(Game.NEUTRALIZER_UI, name, icon, true, true, price, count);
				else this.table_title = new TableTitle(name, icon);
			
			this.add(table_title).growX().top().pad(Game.GUI_NUMBER_PANEL_GAP);
			this.row();
		}
		
		public ContainerItem(Skin skin, Item item) {
			super(Game.NEUTRALIZER_UI);
			
			table_name = new TableItemsInfoName(item.getName());
			table_info = new TableStoreItemInfoTitle(item);
			
			this.add(table_name).growX().top().pad(Game.GUI_NUMBER_PANEL_GAP);
			this.row();
			this.add(table_info).growX().top().pad(Game.GUI_NUMBER_PANEL_GAP);
			this.row();
		}
		
		public ContainerItem setItemPrice(int price) {
			this.price = price;
			table_title.setPrice(price);
			return this;
		}
		
		public ContainerItem setCount(int count) {
			this.count = count;
			return this;
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			//this.debugAll();
		}
	}
	
	public class ContainerItemWeapon extends Table {
		
		TableTitle table_title;
		
		public ContainerItemWeapon(String name, Texture icon) {
			this(Game.NEUTRALIZER_UI, name, icon);
		}
		
		public ContainerItemWeapon(Skin skin, String name, Texture icon) {
			super(Game.NEUTRALIZER_UI);
			
			this.table_title = new TableTitle(name, icon);
			
			
			this.add(table_title).growX().top().pad(Game.GUI_NUMBER_PANEL_GAP);
			this.row();
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			//this.debugAll();
		}
	}
	
	public class TableTitle extends Table {
		
		private Label label_name;
		private Label label_price;
		private Label label_count;
		
		private Image icon;
		private Button but_buy;
		
		private	float ICON_SIZE	= Gdx.graphics.getHeight() / 10f * 4f;
		private int count	= 0;
		
		public TableTitle(String name, Texture icon) {
			this(Game.NEUTRALIZER_UI, name, icon, false, false, 0, 0);
		}
		
		public TableTitle(Skin skin, String name, Texture icon, boolean can_buy, boolean counter, int price, int count) {
			super(skin);
			label_name	= new Label(name, Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
			label_price	= new Label("Price: \n" + price + " $", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
			label_count	= new Label("Current in warehouse: " + count, Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
			
			label_name.setWrap(true);
			label_price.setWrap(true);
			
			label_count.setWrap(true);
			
			this.count = count;
			this.icon = new Image(icon);
			but_buy	= new Button(Game.NEON_UI);
			
			Label label_buy	= new Label("BUY", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
			label_buy.setColor(Game.COLOR_TEXT);
			but_buy.add(label_buy).pad(label_buy.getHeight() / 2f);
			but_buy.setColor(Color.valueOf("#CD5C5C"));
			
			if(can_buy) this.add(label_name).growX().center().colspan(2);
				else this.add(label_name).growX().center();
			
			this.row();
			Table table_icon = new Table(skin);
			table_icon.setBackground("panel");
			table_icon.add(this.icon).prefSize(ICON_SIZE).center().pad(Game.GUI_NUMBER_PANEL_GAP);
			this.add(table_icon).grow().pad(Game.GUI_NUMBER_PANEL_GAP);
			table_icon.setColor(Game.COLOR_BACKGROUND);
			
			if(can_buy) {	
				Table table_buy	= new Table(Game.NEUTRALIZER_UI);
				table_buy.add(but_buy).pad(Game.GUI_NUMBER_PANEL_GAP);
				table_buy.row();
				table_buy.add(GuiBase.incapsulateIntoTable(label_price, Game.NEUTRALIZER_UI, Game.GUI_NUMBER_PANEL_GAP / 2f, "panel"))
				.grow().left().pad(Game.GUI_NUMBER_PANEL_GAP);
				
				this.add(table_buy).grow();
				this.row();
			} if(counter) this.add(GuiBase.incapsulateIntoTable(label_count, Game.NEUTRALIZER_UI, Game.GUI_NUMBER_PANEL_GAP / 2f, "panel")).left()
					.colspan(2).growX().pad(Game.GUI_NUMBER_PANEL_GAP);
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			if(but_buy.isChecked()) {
				but_buy.setChecked(false);
				
				this.setCount(++count);
			}
		}
		
		public void setPrice(int price) {
			label_price.setText("Price: \n" + price + " $");
		}
		
		public void setCount(int count) {
			label_count.setText("Current in warehouse: " + count);
		}
	}
}
