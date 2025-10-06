import java.util.*;

class EvaluateDivision {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // Build the graph as adjacency list
        Map<String, Map<String, Double>> graph = new HashMap<>();
        
        // Add equations to graph
        for (int i = 0; i < equations.size(); i++) {
            String dividend = equations.get(i).get(0);
            String divisor = equations.get(i).get(1);
            double quotient = values[i];
            
            graph.putIfAbsent(dividend, new HashMap<>());
            graph.putIfAbsent(divisor, new HashMap<>());
            
            // Add bidirectional edges with weights
            graph.get(dividend).put(divisor, quotient);
            graph.get(divisor).put(dividend, 1.0 / quotient);
        }
        
        // Process each query
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String dividend = queries.get(i).get(0);
            String divisor = queries.get(i).get(1);
            
            // Check if variables exist in graph
            if (!graph.containsKey(dividend) || !graph.containsKey(divisor)) {
                results[i] = -1.0;
            } else if (dividend.equals(divisor)) {
                results[i] = 1.0;
            } else {
                Set<String> visited = new HashSet<>();
                results[i] = dfs(graph, dividend, divisor, 1.0, visited);
            }
        }
        
        return results;
    }
    
    private double dfs(Map<String, Map<String, Double>> graph, String current, 
                       String target, double product, Set<String> visited) {
        visited.add(current);
        
        // Check if we can reach target directly
        if (graph.get(current).containsKey(target)) {
            return product * graph.get(current).get(target);
        }
        
        // Try all neighbors
        for (Map.Entry<String, Double> neighbor : graph.get(current).entrySet()) {
            String nextNode = neighbor.getKey();
            double edgeWeight = neighbor.getValue();
            
            if (visited.contains(nextNode)) {
                continue;
            }
            
            double result = dfs(graph, nextNode, target, product * edgeWeight, visited);
            if (result != -1.0) {
                return result;
            }
        }
        
        return -1.0;
    }
}
