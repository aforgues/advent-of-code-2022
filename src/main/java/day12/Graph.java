package day12;

import day09.Position;

import java.util.*;

class Graph
{
    //private int V;                              //number of nodes in the graph
    private Map<Position, LinkedList<Position>> adjMap;              //adjacency list
    private Queue<Position> queue;                   //maintaining a queue

    Graph() {
        //V = v;
        adjMap = new HashMap<>();
        queue = new LinkedList<>();
    }

    LinkedList<Position> getAdjacencyList(Position position) {
        return this.adjMap.get(position);
    }

    List<Position> getParentPositions(Position position) {
        List<Position> parents = new ArrayList<>();
        for (Position p : adjMap.keySet()) {
            if (adjMap.get(p).contains(position))
                parents.add(p);
        }
        return parents;
    }

    void addEdge(Position v,Position w) {
        //adding an edge to the adjacency list (edges are bidirectional in this example)
        adjMap.computeIfAbsent(v, k -> new LinkedList<>()).add(w);
    }

    List<Position> BFS(Position n, Position target)
    {

        Map<Position, Boolean> exploredPosition = new HashMap<>();
        Position exploringPosition = n;
        exploredPosition.put(exploringPosition, true);
        //Position nodes[] = new Position[V];       //initialize boolean array for holding the data
        //int a = 0;
        Position a;

        //nodes[n]=true;
        queue.add(n);                   //root node is added to the top of the queue
        List<Position> path = new ArrayList<>();

        while (queue.size() != 0)
        {
            n = queue.poll();             //remove the top element of the queue
            System.out.println(n+" " + getAdjacencyList(n));           //print the top element of the queue
            path.add(n);

            if (n.equals(target))
                return path;

            for (int i = 0; i < adjMap.get(n).size(); i++)  //iterate through the linked list and push all neighbors into queue
            {
                a = adjMap.get(n).get(i);

                //only insert nodes into queue if they have not been explored already
                if (exploredPosition.get(a) == null) {
                    exploredPosition.put(a, true);
                    //nodes[a] = true;
                    queue.add(a);
                }
            }
        }
        return Collections.emptyList();
    }
}
