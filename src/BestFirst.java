import java.util.*;

class BestFirst {
    protected Queue<State> abertos;

    static class State {
        private final Ilayout layout;
        private final State father;
        private double cost;

        // Constructor initializes layout and father, cost is calculated separately
        public State(Ilayout l, State parent) {
            layout = l;
            father = parent;
            if (father != null)
                cost = father.cost + l.getCost();
            else
                this.cost = 0.0;
        }

        // Method to get cost
        public double getCost() {
            return cost;
        }

        // Override equals and hashCode to allow state comparison in collections
        public int hashCode() {
            return layout.hashCode();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.layout.equals(n.layout);
        }

        @Override
        public String toString() {
            return layout.toString();
        }
    }

    // Generate the successors (children) of a given state
    private List<State> sucessores(State n) {
        List<State> successors = new ArrayList<>();
        List<Ilayout> children = n.layout.children();
        for (Ilayout childLayout : children) {
            // Avoid returning to the parent layout (loop prevention)
            if (n.father == null || !childLayout.equals(n.father.layout)) {
                State newState = new State(childLayout, n);
                successors.add(newState);
            }
        }
        return successors;
    }

    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        abertos = new PriorityQueue<>(10,
                (s1, s2) -> (int) Math.signum(s1.getCost() - s2.getCost()));;  // Priority based on cost (min-heap)
        Set<Ilayout> openedSet = new HashSet<>();  // Tracks layouts in the priority queue
        Map<Ilayout, State> fechados = new HashMap<>();   // Tracks layouts that have been fully processed (closed list)

        State startState = new State(s, null);
        abertos.add(startState);
        openedSet.add(s);  // Add to both priority queue and the set

        while (!abertos.isEmpty()) {
            State actual = abertos.poll();   // Remove from the priority queue
            openedSet.remove(actual.layout);  // Also remove from the set

            if (actual.layout.isGoal(goal)) {
                return reconstructPath(actual);
            }
            fechados.put(actual.layout, actual);  // Move to closed list
            // Generate and process successors
            List<State> successors = sucessores(actual);
            for (State succ : successors) {
                // If this successor is in the closed list and has a higher cost, skip it
                if (fechados.containsKey(succ.layout) && succ.getCost() >= fechados.get(succ.layout).getCost()) {
                    continue;
                }
                // If the successor is not in the open set (abertos), add it to both the priority queue and the set
                if (!openedSet.contains(succ.layout)) {
                    abertos.add(succ);
                    openedSet.add(succ.layout);  // Track it in the set
                }
            }
        }
        return null;  // Return null if no solution is found
    }

    // Reconstructs the solution path by following parent states back to the start
    private Iterator<State> reconstructPath(State goalState) {
        List<State> path = new ArrayList<>();
        State state = goalState;

        while (state != null) {
            path.add(state);
            state = state.father;
        }
        Collections.reverse(path);  // Reverse the path to show the correct order from start to goal
        return path.iterator();
    }
}
