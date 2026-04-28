
package graph_template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
 

public class ListBasedDiGraph implements DiGraph {
	private List<GraphNode> nodeList = new ArrayList<>();

	@Override
	public Boolean addNode(GraphNode node) {
		if (node == null || node.getValue() == null || getNode(node.getValue()) != null) {
			return false;
		}
		nodeList.add(node);
		return true;
	}

	@Override
	public Boolean removeNode(GraphNode node) {
		if (node == null) {
			return false;
		}
		GraphNode targetNode = getNode(node.getValue());
		if (targetNode == null) {
			return false;
		}
		for (GraphNode cur : nodeList) {
			cur.removeNeighbor(targetNode);
		}
		
		nodeList.remove(targetNode);
		
		return true;
		
	}

	@Override
	public Boolean setNodeValue(GraphNode node, String newNodeValue) {
		if (node == null || newNodeValue == null) {
			return false;
		}
		for (GraphNode n : nodeList) {
			if (n.getValue().equals(newNodeValue) && !n.getValue().equals(node.getValue())) {
				return false;
			}
		}
		for (GraphNode n : nodeList) {
			if (n.getValue().equals(node.getValue())) {
				n.setValue(newNodeValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getNodeValue(GraphNode node) {
		if (node == null) {
			return null;
		}
		GraphNode target = getNode(node.getValue());
		
		if (target == null) {
			return null;
		}
		
		return target.getValue();
	}

	@Override
	public Boolean addEdge(GraphNode fromNode, GraphNode toNode, Integer weight) {
		if (fromNode == null || toNode == null || weight == null) {
			return false;
		}
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		
		if (targetFromNode == null || targetToNode == null) {
			return false;
		}
	 	 
		return targetFromNode.addNeighbor(targetToNode, weight);
	}

	@Override
	public Boolean removeEdge(GraphNode fromNode, GraphNode toNode) {
		if (fromNode == null || toNode == null) {
			return false;
		}
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		
		if (targetFromNode == null || targetToNode == null) {
			return false;
		}
		
		return targetFromNode.removeNeighbor(targetToNode);
		
	}

	@Override
	public Boolean setEdgeValue(GraphNode fromNode, GraphNode toNode, Integer newWeight) {
		if (fromNode == null || toNode == null || newWeight == null) {
			return false;
		}
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		
		if (targetFromNode == null || targetToNode == null ||
				targetFromNode.getDistanceToNeighbor(targetToNode) == null) {
			return false;
		}
		
		targetFromNode.removeNeighbor(targetToNode);
		targetFromNode.addNeighbor(targetToNode, newWeight);
		return true;
	}

	@Override
	public Integer getEdgeValue(GraphNode fromNode, GraphNode toNode) {
		if (fromNode == null || toNode == null) {
			return null;
		}
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		
		if (targetFromNode == null || targetToNode == null) {
			return null;
		}
		
		return targetFromNode.getDistanceToNeighbor(targetToNode);
	}

	@Override
	public List<GraphNode> getAdjacentNodes(GraphNode node) {
		if (node == null) {
			return new ArrayList<>();
		}
		GraphNode targetNode = getNode(node.getValue());
		
		if (targetNode == null) {
			return new ArrayList<>();
		}
		
		return targetNode.getNeighbors();
	}

	@Override
	public Boolean nodesAreAdjacent(GraphNode fromNode, GraphNode toNode) {
		return getEdgeValue(fromNode, toNode) != null;
	}

	@Override
	public Boolean nodeIsReachable(GraphNode fromNode, GraphNode toNode) {
		if (fromNode == null || toNode == null) {
			return false;
		}
		GraphNode startNode = getNode(fromNode.getValue());
		GraphNode targetNode = getNode(toNode.getValue());
		
		if (startNode == null || targetNode == null) {
			return false;
		}
		
		Set<GraphNode> visited = new HashSet<>();
		Queue<GraphNode> nodesToCheck = new LinkedList<>();
		nodesToCheck.add(startNode);
		visited.add(startNode);
		
		while (!nodesToCheck.isEmpty()) {
			GraphNode currentNode = nodesToCheck.remove();
			if (currentNode.getValue().equals(targetNode.getValue())) {
				return true;
			}
			for (GraphNode neighbor : currentNode.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					nodesToCheck.add(neighbor);
				}
			}
		}
		
		return false;
	}

	@Override
	public Boolean hasCycles() {
		Set<GraphNode> completelyChecked = new HashSet<>();
		Set<GraphNode> currentlyChecking = new HashSet<>();
		
		for (GraphNode node : nodeList) {
			if (hasCyclesFromNode(node, completelyChecked, currentlyChecking)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<GraphNode> getNodes() {
		return nodeList;
	}

	@Override
	 
	public GraphNode getNode(String nodeValue) {
		if (nodeValue == null) {
			return null;
		}
		for (GraphNode thisNode : nodeList) {
			if (thisNode.getValue().equals(nodeValue))
				return thisNode;
		}
		return null;
	}

	@Override
	public int fewestHops(GraphNode fromNode, GraphNode toNode) {
		if (fromNode == null || toNode == null) {
			return -1;
		}
		GraphNode startNode = getNode(fromNode.getValue());
		GraphNode targetNode = getNode(toNode.getValue());
		
		if (startNode == null || targetNode == null) {
			return -1;
		}
		
		Set<GraphNode> visited = new HashSet<>();
		Queue<GraphNode> nodesToCheck = new LinkedList<>();
		Map<GraphNode, Integer> hopCounts = new HashMap<>();
		
		nodesToCheck.add(startNode);
		visited.add(startNode);
		hopCounts.put(startNode, 0);
		
		while (!nodesToCheck.isEmpty()) {
			GraphNode currentNode = nodesToCheck.remove();
			int currentHops = hopCounts.get(currentNode);
			
			if (currentNode.getValue().equals(targetNode.getValue())) {
				return currentHops;
			}
			
			for (GraphNode neighbor : currentNode.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					hopCounts.put(neighbor, currentHops + 1);
					nodesToCheck.add(neighbor);
				}
			}
		}
		
		return -1;
	}

	@Override
	public int shortestPath(GraphNode fromNode, GraphNode toNode) {
		if (fromNode == null || toNode == null) {
			return -1;
		}
		GraphNode startNode = getNode(fromNode.getValue());
		GraphNode targetNode = getNode(toNode.getValue());
		
		if (startNode == null || targetNode == null) {
			return -1;
		}
		
		Map<GraphNode, Integer> distances = new HashMap<>();
		Set<GraphNode> visited = new HashSet<>();
		
		for (GraphNode node : nodeList) {
			distances.put(node, Integer.MAX_VALUE);
		}
		distances.put(startNode, 0);
		
		while (visited.size() < nodeList.size()) {
			GraphNode currentNode = getClosestUnvisitedNode(distances, visited);
			
			if (currentNode == null) {
				break;
			}
			if (currentNode.getValue().equals(targetNode.getValue())) {
				return distances.get(currentNode);
			}
			
			visited.add(currentNode);
			
			for (GraphNode neighbor : currentNode.getNeighbors()) {
				Integer edgeWeight = currentNode.getDistanceToNeighbor(neighbor);
				Integer currentDistance = distances.get(currentNode);
				
				if (!visited.contains(neighbor) && edgeWeight != null &&
						currentDistance != Integer.MAX_VALUE &&
						currentDistance + edgeWeight < distances.get(neighbor)) {
					distances.put(neighbor, currentDistance + edgeWeight);
				}
			}
		}
		
		return -1;
	}

	private Boolean hasCyclesFromNode(GraphNode node, Set<GraphNode> completelyChecked,
			Set<GraphNode> currentlyChecking) {
		if (currentlyChecking.contains(node)) {
			return true;
		}
		if (completelyChecked.contains(node)) {
			return false;
		}
		
		currentlyChecking.add(node);
		
		for (GraphNode neighbor : node.getNeighbors()) {
			if (hasCyclesFromNode(neighbor, completelyChecked, currentlyChecking)) {
				return true;
			}
		}
		
		currentlyChecking.remove(node);
		completelyChecked.add(node);
		return false;
	}
	
	private GraphNode getClosestUnvisitedNode(Map<GraphNode, Integer> distances, Set<GraphNode> visited) {
		GraphNode closestNode = null;
		int closestDistance = Integer.MAX_VALUE;
		
		for (GraphNode node : distances.keySet()) {
			int distance = distances.get(node);
			if (!visited.contains(node) && distance < closestDistance) {
				closestNode = node;
				closestDistance = distance;
			}
		}
		
		return closestNode;
	}

 
	 
	
}
