import java.util.*;

public class MaxFlow {

    public static HashSet<Integer> visited = new HashSet<Integer>();
    public static Stack<Integer> path = new Stack<Integer>();
    public static ArrayList<Object[]> paths;

	/**
	 * We will be grading this method solely based on the values you return. Yes
	 * it will be strict, but this is extra credit. There is only 1 method so we
	 * obviously won't be testing the intermediate parts of the algorithm. We
	 * are giving you the freedom to code it however you want though.
	 * 
	 * (the same things about changing the method headers and class headers from
	 * all previous homeworks applies to this, I just didn't feel the need to
	 * attach an entirely separate pdf just containing it. reference an old
	 * homework if you forgot what it says!)
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * This method should return a single integer, the maximum flow from s to t.
	 * You can assume that the max flow exists and is greater than 0.
	 * 
	 * There are n vertices in the graph. Numbered 0 to n-1 (inclusive).
	 * 
	 * capacities will be a n by n matrix, that represents an adjacency matrix
	 * of the network. capacities[i][j] is the capacity that can flow from
	 * vertex i to vertex j. A 0 represents there being no edge between i and j
	 * in the network.
	 * 
	 * note that the graph is directed, you may assume that if capacities[i][j]
	 * is greater than 0, then capacities[j][i] is 0, in other words there will
	 * be only one edge between two vertices in either direction
	 * 
	 * s is the source vertex that the flow should start from, s will be in the
	 * range [0, n)
	 * 
	 * t is the sink where the flow should end at, t will be in the range [0, n)
	 * 
	 * @param n
	 *            the number of vertices, this will be less than 500
	 * @param s
	 *            the source vertex
	 * @param t
	 *            the sink vertex
	 * @param capacities
	 *            the capacities of the network
	 * @return the maximum flow of the network
	 */


	public static int flow(int n, int s, int t, int[][] capacities) {
        int flow = 0;
        int min;
        int[] temp_arr = new int[n];
        int count = 0;

        paths = pathList(s, t, capacities);

        for(Object[] a: paths){

            min = capacities[s][(Integer)a[0]];

            for(int i = 0; i < a.length-1; i++){
                min = Math.min(min, capacities[(Integer)a[i]][(Integer)a[i+1]]);
            }

            capacities[s][(Integer)a[0]] -= min;

            for(int i = 0; i < a.length-1; i++){
                capacities[(Integer)a[i]][(Integer)a[i+1]] -= min;
            }

            temp_arr[count++] = min;

        }

        for(int i = 0; i < temp_arr.length; i++)
            flow += temp_arr[i];

        return flow;
	}

    public static int[] neighbors(int s, int[][] capacities){
        if(s >= capacities[s].length)
            return null;

        int[] array = new int[capacities.length];
        int t = 0;

        for(int i = 0; i < capacities[s].length; i++){
            if(i != s && capacities[s][i] != 0){
                array[t++] = i;
            }
        }

        return Arrays.copyOf(array, t);
    }

    public static boolean isStuck(int x, int t, int[][] capacities){
        if(x == t){
            return false;
        }
        for(int i: MaxFlow.neighbors(x, capacities)){
            if(!visited.contains(i)){
                visited.add(i);
                if(!isStuck(i, t, capacities)){
                    return false;
                }
            }
        }
        return true;
    }

    public static void search(int x, int t, int[][] capacities){
        if(x == t){
            Stack<Integer> pathCopy = (Stack<Integer>)path.clone();
            paths.add(pathCopy.toArray());
        }

        visited.clear();
        visited.addAll(path);

        if(isStuck(x,t,capacities)){
            return;
        }

        for(int i: MaxFlow.neighbors(x, capacities)){
            path.push(i);
            search(i, t, capacities);
            path.pop();
        }

    }

    public static ArrayList<Object[]> pathList(int x, int t, int[][] capacities){
    	int temp;
    	path = new Stack<Integer>();
        paths = new ArrayList<Object[]>();
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        ArrayList<Object[]> tempPaths = new ArrayList<Object[]>();
        ArrayList<Integer> tempArray = new ArrayList<Integer>();

        search(x,t,capacities);

        for(int i = 0; i < paths.size(); i++){
            pq.add(paths.get(i).length);
        }

        while(!pq.isEmpty()){
            temp = pq.remove();
            for(int i = 0; i < paths.size(); i++){
                if(paths.get(i).length == temp && !tempArray.contains(i)){
                    tempArray.add(i);
                    tempPaths.add(paths.get(i));
                    break;
                }
            }
        }

        return tempPaths;
    }


}