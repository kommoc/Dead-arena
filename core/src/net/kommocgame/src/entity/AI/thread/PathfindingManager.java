package net.kommocgame.src.entity.AI.thread;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.PathFinderQueue;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.sched.LoadBalancingScheduler;

import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.HeuristicImp;
import net.kommocgame.src.entity.AI.Node;

public class PathfindingManager {

    private static PathfindingManager instance = null;

    private static int threadCount = 0;
    private static CopyOnWriteArrayList<PathfindingThread> activeThreads;

    private ExecutorService executor;

    private PathfindingManager() {
        activeThreads = new CopyOnWriteArrayList<PathfindingThread>();
        threadCount = Runtime.getRuntime().availableProcessors();

        threadCount = 1;

        executor = Executors.newFixedThreadPool(2);
    }

    public void requestPathfinding(Pather requester, IndexedAStarPathFinder<Node> pathFinder,
                                   Node startNode, Node endNode, GraphPath path) {
        if (activeThreads.size() < threadCount) {
            PathfindingThread pathfindingThread = new PathfindingThread(pathFinder);
            activeThreads.add(pathfindingThread);

            pathfindingThread.addPathfindingRequest(requester, startNode, endNode, path);

            executor.execute(pathfindingThread);
        } else {
            Iterator<PathfindingThread> threads = activeThreads.iterator();
            int requestCount = 2;

            while(threads.hasNext()) {
                PathfindingThread thread = threads.next();

                if (thread.queue.size() < requestCount) {
                    thread.addPathfindingRequest(requester, startNode, endNode, path);

                    break;
                } else {
                    requestCount = thread.queue.size();
                }

                if (!threads.hasNext()) {
                    activeThreads.get(0).addPathfindingRequest(requester, startNode, endNode, path);
                }
            }
        }
    }

    public static void releasePathfindingThread(PathfindingThread pathfindingThread) {
        activeThreads.remove(pathfindingThread);
    }

    public static PathfindingManager getInstance() {
        if (instance == null) {
            instance = new PathfindingManager();
        }

        return instance;
    }

    public class PathfindingThread implements Runnable, Telegraph, Serializable {
        IndexedAStarPathFinder<Node> pathFinder;

        PathFinderQueue<Node> queue;
        LoadBalancingScheduler scheduler;

        private MessageDispatcher dispatcher;
        private ConcurrentHashMap<PathFinderRequest<Node>, Pather> requestMap;
        private CopyOnWriteArrayList<PathFinderRequest> completedRequestQueue;

        public PathfindingThread(IndexedAStarPathFinder<Node> pathFinder) {
            this.pathFinder = pathFinder;

            requestMap = new ConcurrentHashMap<PathFinderRequest<Node>, Pather>();
            completedRequestQueue = new CopyOnWriteArrayList<PathFinderRequest>();

            scheduler = new LoadBalancingScheduler(60);

            dispatcher = new MessageDispatcher();

            queue = new PathFinderQueue<Node>(pathFinder);
            dispatcher.addListener(queue, Messages.REQUEST_PATHFINDING);
        }

        public void addPathfindingRequest(Pather requester, Node startNode, Node endNode, GraphPath path) {
            MessageDispatcher requestDispatcher = new MessageDispatcher();

            requestDispatcher.addListener(this, Messages.PATHFINDING_FINISHED);

            PathFinderRequest<Node> request = new PathFinderRequest<Node>(startNode, endNode,
                    new HeuristicImp(), path, requestDispatcher);
            request.responseMessageCode = Messages.PATHFINDING_FINISHED;

            dispatcher.dispatchMessage(Messages.REQUEST_PATHFINDING, request);

            requestMap.put(request, requester);
        }

        @Override
        public void run() {
            scheduler.addWithAutomaticPhasing(queue, 1);
            
            System.out.println("queue:" + queue);
            
            while (true) {
                scheduler.run(9999999);
            	
                Iterator<PathFinderRequest> iterator = completedRequestQueue.iterator();

                while (iterator.hasNext()) {
                    final PathFinderRequest<Node> completedRequest = iterator.next();

                    final Pather requester = (Pather) requestMap.get(completedRequest);

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            requester.acceptPath(completedRequest);
                        }
                    });

                    requestMap.remove(completedRequest);
                    completedRequestQueue.remove(completedRequest);
                }

                if (requestMap.size() == 0 && queue.size() == 0) break;
            }

            PathfindingManager.releasePathfindingThread(this);
        }

        @Override
        public boolean handleMessage(Telegram msg) {
            if (msg.message == Messages.PATHFINDING_FINISHED) {
                PathFinderRequest<Node> completedRequest = (PathFinderRequest) msg.extraInfo;

                completedRequestQueue.add(completedRequest);

                return true;
            }

            return false;
        }
    }
}
