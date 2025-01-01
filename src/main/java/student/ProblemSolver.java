package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

import graph.Edge;
import graph.Graph;
import graph.WeightedGraph;

public class ProblemSolver implements IProblem {

	@Override
	public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) { // O(m log m)
		ArrayList<Edge<V>> confirmedVerticies = new ArrayList<>(); // O(1)
		HashSet<V> confirmedNodes = new HashSet<>(); // O(1)
		PriorityQueue<Edge<V>> toSearch = new PriorityQueue<>(g); // O(1)
		V node = g.getFirstNode(); // O(1)
		confirmedNodes.add(node); // O(1)

		for (Edge<V> edge : g.adjacentEdges(node)) { // deg(node) times = O(n-1) -> O(n log n) = O(n log n)
			toSearch.add(edge); // O(n) * O(log(n-1)) = O(n log n)
		}

		while (!toSearch.isEmpty()) { // 2m times
			Edge<V> minEdge = toSearch.remove(); // 2m times O (log 2m) = m times O(logm)
			node = confirmedNodes.contains(minEdge.a) ? minEdge.b : minEdge.a; // 2m times O(1)
			if (confirmedNodes.add(node)) { // n times O(1)
				for (Edge<V> edge : g.adjacentEdges(node)) { // 2m times (sum av gradtall til hver node er 2m)
					toSearch.add(edge); // 2m times O(log 2m) = O(m log m)
				}
				confirmedVerticies.add(minEdge); // n times O(1)
			}
		}
		return confirmedVerticies; // O(1)
	}

	@Override
	public <V> V lca(Graph<V> g, V root, V u, V v) { // O(n)
		HashMap<V, V> path = new HashMap<>(); // O(1)
		ArrayList<V> toSearch = new ArrayList<>(); // O(1)
		toSearch.add(root); // O(1)

		int i = 0; // O(1)
		while (i < toSearch.size()) { // n times -> O(n)
			V ansestor = toSearch.get(i++); // n times O(1)
			for (V child : g.neighbours(ansestor)) { // O(2m) = O(2(n-1)) = O(2n) = O(n) times (tot gradtall til alle node )
				if (!path.containsKey(child)) { // 2n times O(1)
					path.put(child, ansestor); // n times O(1)
					toSearch.add(child); // n times O(1)
				}
			}
		}
		HashSet<V> ansestorsU = new HashSet<>(); // O(1)
		while (!ansestorsU.contains(root)) { // n times - > O(n)
			ansestorsU.add(u); // O(1)
			u = path.get(u); // O(1)
		}
		while (!ansestorsU.contains(v)) { // n times -> O(n)
			v = path.get(v); // O(1)
		}
		return v; // O(1)
	}

	@Override
	public <V> Edge<V> addRedundant(Graph<V> g, V root) { // O(n)
		HashMap<V, Integer> subtreeSizes = getSubtreeSizes(g, root); // O(n)
		SubtreeSizeCmp<V> cmp = new SubtreeSizeCmp<>(subtreeSizes); // O(1)

		if (g.degree(root) == 1) { // O(1)
			return new Edge<V>(root, findLowestLeaf(g, root, subtreeSizes, cmp)); // O(n)
		}

		V largest = null, secondLargest = null;
		for (V node : g.neighbours(root)) { // O(n-1) = O(n)
			if (largest == null || cmp.compare(node, largest) == -1) { // O(1)
				secondLargest = largest; // O(1)
				largest = node; // O(1)
			} else if (secondLargest == null || cmp.compare(node, secondLargest) == -1) { // O(1)
				secondLargest = node; // O(1)
			}
		}

		V largestSubtreeLeaf = findLowestLeaf(g, largest, subtreeSizes, cmp); // O(n)
		V secondLargestSubtreeLeaf = findLowestLeaf(g, secondLargest, subtreeSizes, cmp); // O(n)

		return new Edge<V>(largestSubtreeLeaf, secondLargestSubtreeLeaf); // O(1)
	}

	///////////////////
	// Hjelpemetoder //
	///////////////////

	private <V> HashMap<V, Integer> getSubtreeSizes(Graph<V> g, V root) { // O(n)
		HashMap<V, Integer> subtreeSizes = new HashMap<>(); // O(1)
		HashSet<V> visited = new HashSet<>(); // O(1)
		Stack<V> stackToSearch = new Stack<>(); // O(1)
		stackToSearch.push(root); // O(1)

		while (!stackToSearch.isEmpty()) { // 2n times -> O(n)
			V node = stackToSearch.peek(); // 2n times O(1)

			if (visited.add(node)) { // n times -> O(n)
				for (V child : g.neighbours(node)) { // 2m times = O(2(n-1)) = O(n)
					if (!visited.contains(child)) {
						stackToSearch.push(child);
					} // 2m times (O(1) + n times O(1)) = O(n)
				}
			} else { // n times -> O(n)
				int size = 0; // n times O(1)
				for (V child : g.neighbours(node)) { // 2m times = O(n-1) = O(n)
					size += subtreeSizes.getOrDefault(child, 0); // 2m times O(1)
				}
				subtreeSizes.put(node, ++size); // n times O(1)
				stackToSearch.pop(); // n times O(1)
			}
		}
		return subtreeSizes; // O(1)
	}

	private <V> V findLowestLeaf(Graph<V> g, V node, HashMap<V, Integer> subtreeSizes, SubtreeSizeCmp<V> cmp) { // O(n)
		while (subtreeSizes.get(node) > 1) { // n times
			V largestChild = null; // O(1)
			for (V child : g.neighbours(node)) { // 2m times = 2(n-1) = O(n)
				if (cmp.compare(child, node) == 1) { // O(1)
					if (largestChild == null || cmp.compare(child, largestChild) == -1) { // O(1)
						largestChild = child; // O(1)
					}
				}
			} node = largestChild; // n times O(1)
		}
		return node; // O(1)
	}
}

class SubtreeSizeCmp<V> implements Comparator<V> {

	private final HashMap<V, Integer> subtreeSizes;

	/**
	 * key : root node of sub tree
	 * value : number of nodes in sub tree
	 * @param subtreeSizes hashMap of subtreesizes
	 */
	SubtreeSizeCmp(HashMap<V, Integer> subtreeSizes) { // O(1)
		this.subtreeSizes = subtreeSizes; // O(1)
	}

	@Override
	public int compare(V u, V v) {
		return Integer.compare(subtreeSizes.get(v), subtreeSizes.get(u)); // O(1)
	}
}

// Fikk beskjed av Martin at rekursive funksjoner
// ikke er så bra for minebruk og kjøretidsanalyse,
// men vil ikke slette... hehe XD

//////////////////////////
// Rekursiv løsning //////
//////////////////////////

// private <V> V findLowestLeaf(Graph<V> g, V node, HashMap<V, Integer>
// subtreeSizes) {

// int currentSize = subtreeSizes.get(node);

// if (currentSize == 1) {
// return node;
// }

// V biggesChild = null;
// int biggestChildSize = 0;
// for (V child : g.neighbours(node)){
// int childSize = subtreeSizes.get(child);
// if (biggestChildSize < childSize && childSize < currentSize) {
// biggesChild = child;
// biggestChildSize = childSize;
// }
// }

// return findLowestLeaf(g, biggesChild, subtreeSizes);

///////////////

// private <V> HashMap<V, Integer> getSubTreeSizes(Graph<V> g, V node,
// HashMap<V, Integer> subtreeSizes, HashSet<V> nodes ){
// nodes.add(node);
// subtreeSizes.put(node, 1);
// for (V child : g.neighbours(node)) {
// if (!nodes.contains(child)) {
// getSubTreeSizes(g, child, subtreeSizes, nodes);
// subtreeSizes.put(node, subtreeSizes.get(node) + subtreeSizes.get(child));
// }
// }
// return subtreeSizes;
// }
