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



    public String testConnection(){

        return "hi";
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
    //DFS Algorithm
    //ACHTUNG LOOPS DÜRFEN IM PATH SEIN
    public List<List<StarSystemEntity>> getAllRoutesWithMaximumStops(String startSystemId, String destSystemId, int maxStops) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
          return   new ArrayList<List<StarSystemEntity>>();
        }
        return calculateRoutesWithMaxStops(start,end,maxStops).stream().filter(route->route.size()>1).toList();
    }
    private List<List<StarSystemEntity>> calculateRoutesWithMaxLengthDFS(StarSystemEntity start, StarSystemEntity end, int maxStops){
        List<List<StarSystemEntity>> routes = new ArrayList<>();
        Set<StarSystemEntity> visited = new HashSet<>();
        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);
        visited.add(start);

        dfs( start, end, firstPath, 1, maxStops+1, routes, visited);
        return routes;
    }
    private List<List<StarSystemEntity>> calculateRoutesWithMaxStops(StarSystemEntity start, StarSystemEntity end, int maxStops){
        List<List<StarSystemEntity>> routes = new ArrayList<>();
        Set<StarSystemEntity> visited = new HashSet<>();
        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);
        visited.add(start);

        dfs( start, end, firstPath, 1, maxStops+1, routes, visited);
        return routes;
    }
    private  void dfs( StarSystemEntity current, StarSystemEntity end, List<StarSystemEntity> path, int length, int maxLength, List<List<StarSystemEntity>> routes, Set<StarSystemEntity> visited) {
        if (length > maxLength) {
            return;
        }
        if (current.equals(end)) {
            routes.add(new ArrayList<>(path));
        }
        visited.add(current);
        StarSystemEntity neighbor;
        for (TravelToRelationShip relationShip : current.getTravelTo()) {
            neighbor=relationShip.getDestinationSystem();
            if (!current.equals(neighbor) || (neighbor.equals(end) && !path.get(0).equals(end))) {
                path.add(neighbor);
                dfs( neighbor, end, path, length + 1, maxLength, routes, visited);
                path.remove(path.size() - 1);
            }
        }
        visited.remove(current);
    }


    public List<List<StarSystemEntity>> getAllRoutesWithExactStops(String startSystemId, String destSystemId, int exactStops) {
        StarSystemEntity start=starSystemRepository.findById(startSystemId).orElse(null);
        StarSystemEntity end=starSystemRepository.findById(destSystemId).orElse(null);
        if(start==null || end==null){
            return new ArrayList<List<StarSystemEntity>>();
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
          return new ArrayList<List<StarSystemEntity>>();
        }
        return calculateRoutesWithMaxTraveltimeDFS(start,end,maxTravelTime).stream().filter(route->route.size()>1).distinct().toList();

    }

    private List<List<StarSystemEntity>> calculateRoutesWithMaxTraveltimeDFS(StarSystemEntity start, StarSystemEntity end, int maxTraveltime){
        List<List<StarSystemEntity>> routes = new ArrayList<>();
        Set<StarSystemEntity> visited = new HashSet<>();
        List<StarSystemEntity> firstPath=new ArrayList<>();
        firstPath.add(start);
        visited.add(start);

        adaptedDfs( start, end, firstPath, 0, maxTraveltime, routes, visited);
        return routes;
    }

    private  void adaptedDfs( StarSystemEntity current, StarSystemEntity end, List<StarSystemEntity> path, int traveltime, int maxTravelTime, List<List<StarSystemEntity>> routes, Set<StarSystemEntity> visited) {
        if (traveltime >= maxTravelTime) {
            return;
        }
        if (current.equals(end)) {
            routes.add(new ArrayList<>(path));
        }
        visited.add(current);
        StarSystemEntity neighbor;
        for (TravelToRelationShip relationShip : current.getTravelTo()) {
            neighbor=relationShip.getDestinationSystem();
            if (!current.equals(neighbor) || (neighbor.equals(end) && !path.get(0).equals(end))) {
                path.add(neighbor);
                adaptedDfs( neighbor, end, path, traveltime+relationShip.getTravelTimeInHours(), maxTravelTime, routes, visited);
                path.remove(path.size() - 1);
            }
        }
        visited.remove(current);
    }



    //adapted from https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-using-priority_queue-stl/
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
        Map<StarSystemEntity,Integer> dist= new HashMap<>();

        allStarSystems.forEach(starSystemEntity -> {
            dist.put(starSystemEntity,Integer.MAX_VALUE);
        });

        dist.put(start,0);
        int distToEnd=0;

        while (!pq.isEmpty()) {
            StarSystemEntity u = pq.poll().vertex;
             distToEnd=dist.get(end);
            if (u.equals(end) && distToEnd>0) {
                return distToEnd;
            }
            for (TravelToRelationShip travelToRelationShip : u.getTravelTo()) {
                StarSystemEntity v=travelToRelationShip.getDestinationSystem();

                int weight = travelToRelationShip.getTravelTimeInHours();

                // Relaxation step
                if (dist.get(v) > dist.get(u) + weight) {
                    dist.put(v,dist.get(u) + weight);
                    pq.add(new iPair(v, dist.get(v)));
                }
            }
        }

        return -1;
    }


}
