package net.kommocgame.src.entity.AI.asynch;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.asynch.actions.Action;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.world.World;

public class AIManager {
	
	private ThreadAI thread;
	private static SteerableImp player;
	
	public AIManager() {
		thread = new ThreadAI();
		thread.start();
	}
	
	protected static SteerableImp getPlayer() {
		return player;
	}
	
	public ConcurrentHashMap<SteerableImp, EntityLiving> getListenersMap() {
		return thread.tracking_map;
	}
	
	public CopyOnWriteArrayList<SteerableImp> getListenersList() {
		return thread.tracking_list;
	}
	
	public void addListener(EntityLiving entity) {
		entity.setTracking(true);
		thread.trackEntity(entity);
	}
	
	public void removeListener(EntityLiving entity) {
		entity.setTracking(false);
	}
	
	public void resetAll(World world) {
		System.out.println("AIManager.resetAll() ### Remove all trackers: " + thread.tracking_list.size());
		
		thread.tracking_list.clear();
		thread.tracking_map.clear();
	}
	
	public boolean trackerIsEmpty() {
		//System.out.println("AIManager.tackerIsEmpty() ### TRACKER_LIST_SIZE: " + thread.tracking_list.size());
		return thread.tracking_list.isEmpty();
	}
	
	public void updateEntityInfo(EntityLiving entity) {
		
	}
	
	public static class ThreadAI extends Thread {
		private CopyOnWriteArrayList<SteerableImp> tracking_list;
		private ConcurrentHashMap<SteerableImp, EntityLiving> tracking_map;
		
		public ThreadAI() {
			tracking_list	= new CopyOnWriteArrayList<SteerableImp>();
			tracking_map	= new ConcurrentHashMap<SteerableImp, EntityLiving>();
		}
		
		public void trackEntity(EntityLiving entity) {
			SteerableImp steerable = new SteerableImp(entity);
			if(entity instanceof EntityPlayer)
				player = steerable;
			
			tracking_list.add(steerable);
			tracking_map.put(steerable, entity);
		}
		
		private void removeTrackEntity(SteerableImp steerable) {
			tracking_list.remove(steerable);
			tracking_map.remove(steerable);
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					if(tracking_list.isEmpty())
						this.sleep(100l);
					else {
						Iterator<SteerableImp> iterator = tracking_list.iterator();

		                while (iterator.hasNext()) {
		                    final SteerableImp steerable = iterator.next();
		                    final EntityLiving ientityRequest = tracking_map.get(steerable);
		                    
		                    steerable.getAI().update(Gdx.graphics.getDeltaTime());
							
							if(steerable.getBehavior() != null) {
								steerable.getBehavior().calculateSteering(steerable.getSteering());
							}
							
							steerable.setDelete(ientityRequest.isDeleted());
							
							if(steerable.isDelete()) {
								removeTrackEntity(steerable);
							} else {
								steerable.getInfoRequest().updateInfo();
								
								for(Action action : steerable.getActionList()) {
									if(action.isChecked()) {
										System.out.println("AIManager.run() THREAD ### DELETE_Actions");
										
										steerable.getActionList().removeValue(action, false);
									}
								}
							}
							
		                    Gdx.app.postRunnable(new Runnable() {
		                        @Override
		                        public void run() {
		                        	ientityRequest.applySteering(steerable.getSteering());
									ientityRequest.setTracking(steerable.isDelete());
									
									if(!ientityRequest.isDeleted()) {
										steerable.getInfoRequest().applyRequest(ientityRequest.getPosition(), ientityRequest.getLinearVelocity(),
											ientityRequest.getRotation(), ientityRequest.getOrientation());
										
										for(Action action : steerable.getActionList()) {
											if(!action.isChecked())
												ientityRequest.executeAction(action);
										}
									}
									
									for(RaycastRequest request : steerable.getRaycast_list()) {
										request.applyRaycast(ientityRequest.worldObj);
									}
									//System.out.println("AIManager.run() RUNNABLE ### Actions: " + steerable.getActionList().size);
		                        }
		                    });
		                }
		                
		                this.sleep(100l);
					}
					
					//System.out.println("AIManager_LIST_size: " + tracking_list.size());
					//System.out.println("AIManager_MAP_size: " + tracking_map.size());
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
