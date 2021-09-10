package net.kommocgame.src.debug;

import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.IControllable;
import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.HeuristicImp;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.Requerements.ZombieAI;
import net.kommocgame.src.entity.AI.task.EnumTaskPriority;
import net.kommocgame.src.entity.AI.task.TaskMoveTo;
import net.kommocgame.src.entity.AI.task.TaskTest;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.gui.GuiConsole;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.render.GameManager;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.TerrainObject;

/** Class is helper for entering command. This class have many instances of game logic. */
public class CommandLine {
	
	public static Array<Label> command_history = new Array<Label>(512);
	
	public Array<String> prefix = new Array<String>();
	
	public static CommandLine instance;
	public Game GAME;
	public ShapeRenderer SR;
	public OrthographicCamera CAM;
	public RenderEngine RE;
	public IControllable PLAYER;
	public GuiManager GUI;
	public World WORLD;
	
	public String exec_line;
	public String args_line;
	
	/** TEST_AStar_PathFinding. **/
	public static TEST_AStar_PathFinding test_astar_pthf = new TEST_AStar_PathFinding();
	
	/************** DEBUG_ENTITY_PTF **************/
	public EntityLiving test_entity_ptf;
	
	/************** DEBUG_ENTITY_FOV **************/
	public EntityLiving test_entity_fov;
	
	public CommandLine(Game game) {
		instance = this;
		this.GAME = game;
		SR = game.shapeRenderer;
		CAM = game.mainCamera;
		RE = game.renderEngine;
		PLAYER = game.player;
		GUI = game.guiManager;
		WORLD = game.world;
		
		prefix.add("GAME");
		prefix.add("SR");
		prefix.add("CAM");
		prefix.add("RE");
		prefix.add("PLAYER");
		prefix.add("GUI");
		prefix.add("WORLD");
	}
	
	public void exec(String line) throws ReflectionException {
		int index = line.indexOf(" ");
		
		if(index != -1)
			exec_line = "_" + line.substring(0, index);
		else 
			exec_line = "_" + line.substring(0, line.length());
		
		args_line = line.substring(index + 1, line.length());
		
		for(Method method : ClassReflection.getMethods(this.getClass())) {
			if(method.getName().equals(exec_line)) {
				method.invoke(this, args_line);
			}
		}
		
		System.out.println("exec_line: " + exec_line);
	}
	
	private boolean isFloat(String str) {
		boolean hasPoint = false;
		boolean isFalse = false;
		boolean check = false;
		
		for(char c : str.toCharArray()) {
			if(Character.isDigit(c) || c == '.' || c == '-') {
				if(!check && c == '-')
					if(!isFalse)
						isFalse = true;
					else
						return false;
				
				if(c == '.') {
					if(hasPoint)
						return false;
					
					hasPoint = true;
				}
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	private Array<String> getTokens(String str) {
		Array<String> tokens = new Array<String>();
		str.trim();
		
		while(str.contains(" ")) {
			if(!str.startsWith(" "))
				tokens.add(str.substring(0, str.indexOf(" ")));
			str = str.substring(str.indexOf(" ") + 1);
		}
		
		if(str.length() > 0) {
			tokens.add(str);
		}
			
		return tokens;
	}
	
	public boolean getDebugNode() {
		return RE.debug_AStar.getState();
	}
	
	/******************************************** COMMANDS ********************************************/
	
	/** Delete all entity around. */
	public void _DEA(String line) {
		EntityPlayer player = (EntityPlayer)Game.getPlayer();
		
		if(player != null) {
			System.out.println("	delete entity's around of player...");
			for(int i = 0; i < player.getActionSensor().getContactList().size; i++) {
				if(!(player.getActionSensor().getContactList().get(i) instanceof EntityPlayer) && (player.getActionSensor().getContactList().get(i) instanceof EntityBase)) {
					player.getActionSensor().getContactList().get(i).setDead();
				}
			}
		}
	}
	
	/** Spawn entity into world. */
	public void _spawn(String line) {
		Array<String> tokens = getTokens(line);
		EntityBase entity_class;
		float entity_x;
		float entity_y;
		
		if(isFloat(tokens.get(1)) || isFloat(tokens.get(2))) {
			entity_x = Float.valueOf(tokens.get(1));
			entity_y = Float.valueOf(tokens.get(2));
		} else {
			System.out.println("		COMMAND IS NOT EXECUTEBLE.");
			return;
		}
		
		
		for(Class instant : GameManager.instance.entityMap.keys()) {
			if(instant.getSimpleName().equals(tokens.get(0))) {
				try {
					WORLD.addEntityIntoWorld((EntityBase) ClassReflection.getConstructor(instant, World.class, SpriteBatch.class, float.class, float.class).newInstance(this.WORLD,
							this.GAME.spriteBatch, entity_x, entity_y));
				} catch (ReflectionException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**************XXX DEBUG_A* **************/
	
	public void _setDebugNode(String line) {
		Array<String> tokens = getTokens(line);
		System.out.println("Tokens: " + tokens);
		
		if(Integer.valueOf(tokens.get(0)) == 0) {
			RE.debug_AStar.setDebug(false);
			System.out.println("DEBUG_ASTAR_GRID 	OFF");
		} else if(Integer.valueOf(tokens.get(0)) == 1) {
			RE.debug_AStar.setDebug(true);
			System.out.println("DEBUG_ASTAR_GRID 	ON");
		}
	}
	
	public void _node_getInfo(String line) {
		System.out.println(WORLD.level.getGridNodes().getNodeByPos(InputHandler.getNodeX(), InputHandler.getNodeY()).toString());
	}
	
	public void _node_setEnable(String line) throws Exception {
		Array<String> tokens = getTokens(line);
		
		if(Integer.valueOf(tokens.get(0)) == 0) {
			WORLD.getLevel().getGridNodes().setStateNode(InputHandler.getNodeX(), InputHandler.getNodeY(), false);
			System.out.println("DEBUG_ASTAR_GRID 	Node [" + InputHandler.getNodeX() + "; " + InputHandler.getNodeY() + "] OFF");
		} else if(Integer.valueOf(tokens.get(0)) == 1) {
			WORLD.getLevel().getGridNodes().setStateNode(InputHandler.getNodeX(), InputHandler.getNodeY(), true);
			System.out.println("DEBUG_ASTAR_GRID 	Node [" + InputHandler.getNodeX() + "; " + InputHandler.getNodeY() + "] ON");
		} else {
			throw new Exception("ERROR: CAN ALLOW ONLY INT [0, 1]");
		}
	}
	
	public void _node_d_setA(String line) {
		test_astar_pthf.setFromNode(WORLD.getLevel().getGridNodes().getNodeByPos(InputHandler.getNodeX(), InputHandler.getNodeY()));
	}
	
	public void _node_d_setB(String line) {
		test_astar_pthf.setToNode(WORLD.getLevel().getGridNodes().getNodeByPos(InputHandler.getNodeX(), InputHandler.getNodeY()));
	}
	
	public void _node_d_createPath(String line) {
		RenderEngine.debug_AStar.setGraphPath(null);
		PathFinderRequest<Node> request = new PathFinderRequest<Node>(test_astar_pthf.getFromNode(), test_astar_pthf.getToNode(),
				new HeuristicImp(), new GraphPath());
		request.client = test_astar_pthf;
		
		//WORLD.message_handler.dispatchMessage(0, request);
		//WORLD.message_handler.dispatchMessage(test_astar_pthf, WORLD.pathFinder_queue, 0, request, false);
	}
	
	public void _node_d_out(String line) {
		System.out.println("////////////////////");
		System.out.println("FromNode:	" + test_astar_pthf.getFromNode());
		System.out.println("toNode:		" + test_astar_pthf.getToNode());
		System.out.println("PATH: " + test_astar_pthf.pathTo.nodes.size);
		System.out.println("\\\\\\\\\\\\\\\\\\\\");
		
		RenderEngine.debug_AStar.setGraphPath(test_astar_pthf.pathTo);
	}
	
	public void _node_d_info(String line) {
		System.out.println("////////////////////");
		System.out.println("PATH: " + test_astar_pthf.pathTo.nodes.size);
		System.out.println("\\\\\\\\\\\\\\\\\\\\");
	}
	
	public void _node_traceEntity(String line) {
		
	}
	
	public void _node_d_reset(String line) {
		test_astar_pthf.reset();
		RenderEngine.debug_AStar.setGraphPath(null);
	}
	
	/**************XXX DEBUG_ENTITY_PTF **************/
	
	public void _entity_d_ptf_setEntity(String line) {
		Object entity = WORLD.getEntityByCursor();
		System.out.println("	object: " + entity);
		
		if(entity instanceof EntityLiving) {
			test_entity_ptf = (EntityLiving) entity;
		}
	}
	
	public void _entity_d_ptf_setTarget(String line) {
		if(test_entity_ptf != null) {
			test_entity_ptf.getPathFinder().createPath(InputHandler.getNodeX(), InputHandler.getNodeY());
		} else
			System.err.println("	test_entity_ptf is not defined!");
	}
	
	public void _entity_d_ptf_out(String line) {
		System.out.println("////////////////////");
		System.out.println("FromNode:	" + test_entity_ptf.getPathFinder().getFromNode());
		System.out.println("toNode:		" + test_entity_ptf.getPathFinder().getToNode());
		System.out.println("PATH: " + test_entity_ptf.getPathFinder().getGraphPath().nodes.size);
		System.out.println("\\\\\\\\\\\\\\\\\\\\");
		
		RenderEngine.debug_AStar.setGraphPath(test_entity_ptf.getPathFinder().getGraphPath());
	}
	
	public void _entity_d_ptf_reset(String line) {
		RenderEngine.debug_AStar.setGraphPath(null);
		System.out.println("	Entity: " + test_entity_ptf);
		System.out.println("	compAStar: " + test_entity_ptf.getPathFinder());
		
		test_entity_ptf.getPathFinder().clear();
		test_entity_ptf = null;
	}
	
	/**************XXX DEBUG_ENTITY_FOV **************/
	
	public void _entity_d_fov_setEntity(String line) {
		Array<String> tokens = getTokens(line);
		Object entity = WORLD.getEntityByCursor();
		System.out.println("	DEBUG_ENTITY_FOV : " + entity);
		
		if(entity instanceof EntityLiving) {
			this.test_entity_fov = (EntityLiving) entity;
		} else
			System.out.println("	DEBUG_ENTITY_FOV is null.");
	}
	
	public void _balance_task1(String line) {
		Array<String> tokens = getTokens(line);
		Object entity = WORLD.getEntityByCursor();
		System.out.println("	DEBUG_ENTITY_FOV : " + entity);
		
		if(entity instanceof EntityLiving) {
			this.test_entity_fov = (EntityLiving) entity;
		} else
			System.out.println("	DEBUG_ENTITY_FOV is null.");
	}
	
	public void _entity_d_fov_check(String line) {
		if(test_entity_fov != null) {
			if(test_entity_fov.getFieldOfView() != null) {
				System.out.println("	DEBUG_ENTITY_FOV can see point: " + 
						test_entity_fov.getFieldOfView().checkPoint(InputHandler.BOX2D_mouse_vec));
			} else System.out.println("	DEBUG_ENTITY_FOV - that entity hav'nt fieldOfView instance.");
		} else System.out.println("	DEBUG_ENTITY_FOV is null.");
	}
	
	/**************XXX DEBUG_ENTITY **************/
	
	public void _entity_debug(String line) {
		Array<String> tokens = getTokens(line);
		Object entity = WORLD.getEntityByCursor();
		System.out.println(" DEBUG_ENTITY 	:	" + entity);
		
		if(Integer.valueOf(tokens.get(0)) == 0) {
			if(entity != null && entity instanceof EntityLiving)
				RE.debug_ai.removeEntity((EntityLiving)entity);
			
			System.out.println(" DEBUG_ENTITY: " + entity + "	OFF");
		} else if(Integer.valueOf(tokens.get(0)) == 1) {
			if(entity != null && entity instanceof EntityLiving)
				RE.debug_ai.addEntity((EntityLiving)entity);
			
			System.out.println(" DEBUG_ENTITY 	ON");
		}
	}
	
	/**************XXX AI **************/
	
	
	/**************XXX OTHER **************/
	
	public void _mouse_getInfo(String line) {
		System.out.println("	World mouse.\n" + "   X: " + InputHandler.BOX2D_mouse_x + "\n   Y: " + InputHandler.BOX2D_mouse_y);
		System.out.println("	Node mouse.\n" + "   X: " + InputHandler.getNodeX() + "\n   Y: " + InputHandler.getNodeY());
	}
	
	public void _world_d_spawnTerrObj(String line) {
		if(GAME.world != null) {
			TerrainObject terrain = new TerrainObject(GAME.world.getLevel(), Loader.objectsMobs("img_def_0.png"), InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
			
			GAME.world.getLevel().spawnTerrainObj(terrain, terrain.getPosition());
		}
	}
	
	public void _pause(String line) {
		GAME.pause();
	}
	
	public void _resume(String line) {
		GAME.resume();
	}
	
	public void _game_setPlayer(String line) {
		Array<String> tokens = getTokens(line);
		Object entity = WORLD.getEntityByCursor();
		
		System.out.println("	GAME_SET_PLAYER: " + entity);
		
		if(entity instanceof EntityLiving) {
			EntityLiving entityLiving = (EntityLiving)entity;
			
			GAME.setPlayer(entityLiving);
			System.out.println("	GAME_SET_PLAYER: player was swapped.");	
		}
	}
	
	/** getEntityByCursor */
	public void _world_getEBC(String line) {
		System.out.println("	ObjectInCursor: " + WORLD.getEntityByCursor());
	}
	
	/** -1: ignore and contitnue;
		0: terminate ray return fraction;
		1: don't clip and continue. */
	public void _world_raycast(String line) {
		System.out.println("////////////////////////////////");
		System.out.println("	TEST_RAYCAST_CALLBACK BEGIN:");
		Vector2 vec_debug = new Vector2(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
		System.out.println("		mouse: " + vec_debug);
		
		if(vec_debug.dst(GAME.player.getPosition()) > 0f)
			WORLD.physics.getBox2d().rayCast(WORLD, GAME.player.getPosition(), vec_debug);
		System.out.println("	TEST_RAYCAST_CALLBACK END");
	}
	
	public void _world_calculateBounds(String line) {
		System.out.println("	Calculate entity bounds.");
		
		WORLD.calcAStarGrid();
	}
	
	public void _clear(String line) {
		command_history.clear();
		
		if(GAME.guiManager.guiList.peek() instanceof GuiConsole) {
			((GuiConsole)GAME.guiManager.guiList.peek()).getHistory().clear();
		}
	}
}
