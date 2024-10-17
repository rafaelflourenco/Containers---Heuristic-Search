import org.junit.jupiter.engine.config.CachingJupiterConfiguration;

import java.util.*;

class ContainerBoard implements Ilayout,Cloneable {
    private final List<Stack<Container>> stacks;
    private double cost;
    
    public ContainerBoard(String str, boolean withCost) {
        stacks = new ArrayList<>();
        cost = 0;  
        String[] stackStrs = str.split(" ");

        for (String stackStr : stackStrs) {
            Stack<Container> stack = new Stack<>();

            if (withCost) {
                // Parse with cost (initial configuration)
                for (int i = 0; i < stackStr.length(); i += 2) {
                    if (i + 1 < stackStr.length()) {
                        char id = stackStr.charAt(i);  // Container's identifier
                        int cost = Character.getNumericValue(stackStr.charAt(i + 1));
                        // Container's movement cost
                        stack.push(new Container(id, cost));  // Push container onto the stack
                    } else {
                        throw new IllegalArgumentException("Invalid input format for container: " + stackStr);
                    }
                }
            } else {
                // Parse without cost (goal configuration)
                for (int i = 0; i < stackStr.length(); i++) {
                    char id = stackStr.charAt(i);  // Only container's identifier
                    stack.push(new Container(id, 0));  // Push container with default cost (e.g., 0)
                }
            }
            stacks.add(stack);
            sortStackByBottomElement();
        }
    }


    public void removeEmptyStacks() {
        stacks.removeIf(Stack::isEmpty);
    }

    public void sortStackByBottomElement() {
        stacks.sort((stack1, stack2) -> {
            if (stack1.isEmpty() && stack2.isEmpty()) return 0;
            if (stack1.isEmpty()) return 1;
            if (stack2.isEmpty()) return -1;

            return Character.compare(stack1.get(0).getId(), stack2.get(0).getId());
        });
    }

    @Override
    public List<Ilayout> children() {
        List<Ilayout> children = new ArrayList<>();

        // Generate children by moving containers between stacks
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                Container topContainer = stacks.get(i).peek();

                for (int j = 0; j <= stacks.size(); j++) {
                    if (j != i) {
                        ContainerBoard newBoard = clone();

                        if (j == newBoard.getStacks().size()) {
                            newBoard.addEmptyStack();
                        }

                        if (newBoard.getStacks().size() >= j) {
                            newBoard.move(i, j);
                            newBoard.cost = topContainer.getCost();
                            newBoard.removeEmptyStacks();
                            newBoard.sortStackByBottomElement();
                            children.add(newBoard);
                        }
                    }
                }
            }
        }

        return children;
    }



    @Override
    public boolean isGoal(Ilayout layout) {
        if (!(layout instanceof ContainerBoard other)) {
            return false;
        }
        return haveSameContainers(this,(ContainerBoard) layout);
    }

    @Override
    public double getCost() {
        return this.cost;
    }

    public void addEmptyStack() {
        stacks.add(new Stack<>());  // Add a new empty stack
    }

    // Move a container from one stack to another
    private void move(int fromStack, int toStack) {
        Container container = stacks.get(fromStack).pop();
        stacks.get(toStack).push(container);
    }



    private boolean haveSameContainers(ContainerBoard board1, ContainerBoard board2) {
        List<Stack<Container>> stacks1 = board1.getStacks();
        List<Stack<Container>> stacks2 = board2.getStacks();

        if (stacks1.size() != stacks2.size()) {
            return false;
        }

        for (int i = 0; i < stacks1.size(); i++) {
            Stack<Container> stack1 = stacks1.get(i);
            Stack<Container> stack2 = stacks2.get(i);

            if (stack1.size() != stack2.size()) {
                return false;
            }

            for (int j = 0; j < stack1.size(); j++) {
                if (!stack1.get(j).equals(stack2.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ContainerBoard other = (ContainerBoard) obj;
        return stacks.equals(other.stacks);
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;  // A prime number to reduce collisions

        for (Stack<Container> stack : stacks) {
            if (stack.isEmpty()) continue;  // Skip empty stacks

            // Create a stack hash using the containers within
            int stackHash = 1;
            for (Container container : stack) {
                // Weigh each container based on its position
                stackHash = prime * stackHash + Character.hashCode(container.getId());
                stackHash = prime * stackHash + Integer.hashCode(container.getCost());
            }

            // Combine the stack's hash with the overall result
            result = prime * result + stackHash;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Stack<Container> stack : stacks) {
            sb.append("[");
            for (int i = 0; i < stack.size(); i++) {
                sb.append(stack.get(i).getId());
                if (i < stack.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }


    public List<Stack<Container>> getStacks() {
        return stacks;
    }


    @Override
    public ContainerBoard clone() {
        ContainerBoard clone = new ContainerBoard("",true);
        clone.cost = this.cost;
        clone.stacks.clear();
        for (Stack<Container> stack : this.stacks) {
            Stack<Container> newStack = new Stack<>();
            newStack.addAll(stack);
            clone.stacks.add(newStack);
        }
        return clone;
    }
}
