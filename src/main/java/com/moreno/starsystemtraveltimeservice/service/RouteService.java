package com.moreno.starsystemtraveltimeservice.service;


import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.model.entity.TravelToRelationShip;
import com.moreno.starsystemtraveltimeservice.repository.StarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RouteService {
    private final StarSystemRepository starSystemRepository;

    @Autowired
    public RouteService(StarSystemRepository starSystemRepository) {
        this.starSystemRepository = starSystemRepository;
    }


    public int getDistance(List<String> starsystems) {
        if(starsystems.size()<=1){
            return 0;
        }
        int sum=0;
        for (int i = 0; i < starsystems.size()-1; i++) {
            StarSystemEntity starSystemEntity1=starSystemRepository.findById(starsystems.get(i)).orElse(null);
            StarSystemEntity starSystemEntity2=starSystemRepository.findById(starsystems.get(i+1)).orElse(null);
            if(starSystemEntity1==null || starSystemEntity2==null){
                return -1;
            }
            TravelToRelationShip travelToRelationShip=starSystemEntity1.getTravelTo().stream().filter(t->t.getDestinationSystem().equals(starSystemEntity2)).findAny().orElse(null);
            if(travelToRelationShip==null){
                return -1;
            }
            sum+=travelToRelationShip.getTravelTimeInHours();

        }

        return sum;
    }

    public List<List<StarSystemEntity>> getAllRoutesWithMaximumStops(String startSystemId, String destSystemId, int maxStops) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
          return new ArrayList<>();
        }
        return calculateRoutesWithMaxStops(start,end,maxStops).stream().filter(route->route.size()>1).toList();
    }




    public List<List<StarSystemEntity>> getAllRoutesWithExactStops(String startSystemId, String destSystemId, int exactStops) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
            return new ArrayList<>();
        }

        return calculateRoutesWithMaxLengthDFS(start,end,exactStops+2).stream().filter(route->route.size()==exactStops+2).toList();
    }

    public int getShortestRouteDuration(String startSystemId, String destSystemId) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
            return -1;
        }
        return djikstraShortestPath(start,end);
    }

    public  List<List<StarSystemEntity>> getAllRoutesWithMaximumTravelTime(String startSystemId, String destSystemId, int maxTravelTime) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
          return new ArrayList<>();
        }
        return calculateRoutesWithMaxTraveltimeDFS(start,end,maxTravelTime).stream().filter(route->route.size()>1).distinct().toList();

    }

    private List<List<StarSystemEntity>> calculateRoutesWithMaxTraveltimeDFS(StarSystemEntity start, StarSystemEntity end, int maxTraveltime){
        List<List<StarSystemEntity>> routes = new ArrayList<>();
        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);
        adaptedDfs( start, end, firstPath, 0, maxTraveltime, routes);
        return routes;
    }


    /*
    Depth First Search Algorithm
    adapted from https://www.programiz.com/dsa/graph-dfs
    adjust to collect all routes that fulfill the given criteria.
    Basically this code tries to expand from the start node along one path as long as possible until either there is no way to go forward or the path is too long.
        in the parameter length is current length of the path stored.
    On this way it collects all paths that may fulfill the criteria that a path starts with start and ends with end.
    If it cannot expand further -> the backtracking happens and the path loses its tail and the node before that tries to go another way.
    Until there are no further paths to be explored

     */
    private  void dfs( StarSystemEntity current, StarSystemEntity end, List<StarSystemEntity> path, int length, int maxLength, List<List<StarSystemEntity>> routes ) {
        if (length > maxLength) {
            return;
        }
        if (current.equals(end)) {
            routes.add(new ArrayList<>(path));
        }

        StarSystemEntity neighbor;
        for (TravelToRelationShip relationShip : current.getTravelTo()) {
            neighbor=relationShip.getDestinationSystem();
            if (!current.equals(neighbor) || (neighbor.equals(end) && !path.get(0).equals(end))) {
                //revisit this node in path if it couldn't be expanded enough
                path.add(neighbor);
                dfs( neighbor, end, path, length + 1, maxLength, routes);
                path.remove(path.size() - 1);
            }
        }

    }
    /*
    Similar to DFS just that the condition that stops the path from being expanded is the maxTraveltime
     */
    private  void adaptedDfs( StarSystemEntity current, StarSystemEntity end, List<StarSystemEntity> path, int traveltime, int maxTravelTime, List<List<StarSystemEntity>> routes) {
        if (traveltime >= maxTravelTime) {
            return;
        }
        if (current.equals(end)) {
            routes.add(new ArrayList<>(path));
        }

        StarSystemEntity neighbor;
        for (TravelToRelationShip relationShip : current.getTravelTo()) {
            neighbor=relationShip.getDestinationSystem();
            if (!current.equals(neighbor) || (neighbor.equals(end) && !path.get(0).equals(end))) {
                path.add(neighbor);
                adaptedDfs( neighbor, end, path, traveltime+relationShip.getTravelTimeInHours(), maxTravelTime, routes);
                path.remove(path.size() - 1);
            }
        }

    }
    private List<List<StarSystemEntity>> calculateRoutesWithMaxLengthDFS(StarSystemEntity start, StarSystemEntity end, int maxStops){
        List<List<StarSystemEntity>> routes = new ArrayList<>();

        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);


        dfs( start, end, firstPath, 1, maxStops+1, routes);
        return routes;
    }
    private List<List<StarSystemEntity>> calculateRoutesWithMaxStops(StarSystemEntity start, StarSystemEntity end, int maxStops){
        List<List<StarSystemEntity>> routes = new ArrayList<>();
        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);
        dfs( start, end, firstPath, 1, maxStops+1, routes);
        return routes;
    }

    /*
        Djikstra Shortest Path
        adapted from https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-using-priority_queue-stl/
        adjusted for our requirements.
        namely that if start and end are equal the intuitive solution would be length of 0 since the starship is already at its destination.
        but this is not allowed here.

     */
    private class iPair implements Comparable<iPair> {
        int  weight;
        StarSystemEntity vertex;

        iPair(StarSystemEntity v, int w)
        {
            vertex = v;
            weight = w;
        }

        // Comparison method for priority queue
        public int compareTo(iPair other)
        {
            return Integer.compare(this.weight,
                    other.weight);
        }
    }
    private int djikstraShortestPath(StarSystemEntity start,StarSystemEntity end){
        PriorityQueue<iPair> pq = new PriorityQueue<>();
        pq.add(new iPair(start, 0));
        List<StarSystemEntity> allStarSystems=starSystemRepository.findAll();
        Map<String,Integer> dist= new HashMap<>();

        allStarSystems.forEach(starSystemEntity -> dist.put(starSystemEntity.getName(),Integer.MAX_VALUE));
        dist.put(start.getName(),0);
        int distToEnd;
        while (!pq.isEmpty()) {
            StarSystemEntity u = pq.poll().vertex;
             distToEnd=dist.get(end.getName());
            if (u.equals(end) && distToEnd>0) { //if the required path was found, we can return without computing the rest
                return distToEnd;
            }
            for (TravelToRelationShip travelToRelationShip : u.getTravelTo()) {
                StarSystemEntity v=travelToRelationShip.getDestinationSystem();
                int weight = travelToRelationShip.getTravelTimeInHours();
                if (dist.get(v.getName()) > dist.get(u.getName()) + weight) {
                    dist.put(v.getName(),dist.get(u.getName()) + weight);
                    pq.add(new iPair(v, dist.get(v.getName())));
                }
            }
        }
        //for the case were start and end are equal

        if(start.getName().equals(end.getName())){
            Map<String, Integer> lastChanceMap=new HashMap<>();
            dist.forEach((name, value) -> {
                StarSystemEntity entity = starSystemRepository.findById(name).orElse(null);
                if (entity != null) {
                    List<TravelToRelationShip> travelToRelationShips = entity.getTravelTo();
                    TravelToRelationShip travelTo = travelToRelationShips.stream().filter(travelToRelationShip -> travelToRelationShip.getDestinationSystem().getName().equals(end.getName())).findAny().orElse(null);
                    if (travelTo != null) {
                        lastChanceMap.put(name, value + travelTo.getTravelTimeInHours());
                    }
                }

            });
            Integer distance=lastChanceMap.values().stream().filter(integer -> integer>0).sorted().findFirst().orElse(null);
            if(distance!=null){
                return distance;
            }
        }

        return -1;
    }


}
