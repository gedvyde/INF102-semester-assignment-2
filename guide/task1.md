# Task 1
The power company has gathered information on all the houses which needs to be connected to the grid. In addition, they have mapped for each pair of connected houses how expensive the cable between them will be. This has been modelled as a weighted graph where each node is a house and a cable between houses is an edge with a weight for cost of that cable. The company wants to construct the power grid with the smallest cost possible while supplying all houses with electricity. Which cables should be used in the network?

This problem in literature is known as *Minimum Spanning Tree*, see chapter 4.3 in *Algorithms*.

**Implement ``IProblem::mst``.** By passing ``ProblemSolverMSTTest`` you will get full points for functionality. To get full points for the task your solution also has to be efficient.
