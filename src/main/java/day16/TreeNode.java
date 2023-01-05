package day16;

import java.util.*;

public class TreeNode {
    private final Valve currentValve;
    private final boolean isValveOpened;
    private final int currentPressureRelease;
    private final int currentMinute;

    private final int depth;
    private final TreeNode parent;
    private final List<TreeNode> children;


    public Valve getCurrentValve() {
        return this.currentValve;
    }

    public TreeNode(Valve currentValve, boolean isValveOpened, int currentPressureRelease, int currentMinute, int depth, TreeNode parent) {
        this.currentValve = currentValve;
        this.isValveOpened = isValveOpened;
        this.currentPressureRelease = currentPressureRelease;
        this.currentMinute = currentMinute;

        this.depth = depth;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public TreeNode addChild(Valve nextValveToExplore) {
        boolean isValveAlreadyOpened = this.computeOpenedValves(new HashSet<>()).contains(nextValveToExplore);

        // avoid turning around
        if (this.parent != null && this.parent.getCurrentValve().equals(nextValveToExplore)
        && this.parent.isValveOpened == isValveAlreadyOpened)
            return null;


        TreeNode child = new TreeNode(nextValveToExplore, isValveAlreadyOpened, this.currentPressureRelease, this.currentMinute + 1, this.depth + 1, this);
        //System.out.println("Add new child to explore : " + child.stats());

        // avoid repeating the same loop like staying at the same pressureRelease
        // check the ascendant : if we keep the same pressureRelease until the same Valve => skip
        List<TreeNode> ascendants = computeTreePath(new ArrayList<>());
        for(TreeNode ascendant : ascendants) {
            if (ascendant.currentPressureRelease != child.currentPressureRelease)
                break;
            if (ascendant.currentValve.equals(child.currentValve))
                return null;
        }

        this.children.add(child);

        return child;
    }

    public TreeNode explore() {
        // worth to open it
        if (this.currentValve.flowRate() > 0 && ! this.isValveOpened) {
            TreeNode child = new TreeNode(this.currentValve, true, this.currentPressureRelease + this.currentValve.flowRate(), this.currentMinute + 1, this.depth + 1, this);
            //System.out.println("Add new child node while opening current valve : " + child.stats());
            this.children.add(child);
            return child;
        }
        return null;
    }

    private List<TreeNode> computeTreePath(List<TreeNode> path) {
        path.add(this);
        if (this.parent != null) {
            path = this.parent.computeTreePath(path);
        }
        return path;
    }

    public int computeTotalPressureReleased() {
        return this.computeTotalPressureReleased(new ArrayList<>()).stream().reduce(0, Integer::sum);
    }

    private List<Integer> computeTotalPressureReleased(List<Integer> path) {
        path.add(this.currentPressureRelease);

        if (this.parent != null) {
            path = this.parent.computeTotalPressureReleased(path);
        }
        return path;
    }

    private Set<Valve> computeOpenedValves(Set<Valve> openedValves) {
        if (this.isValveOpened) {
            openedValves.add(this.currentValve);
        }

        if (this.parent != null) {
            openedValves = this.parent.computeOpenedValves(openedValves);
        }
        return openedValves;
    }

    public boolean hasExpiredMinutesToExplore(int minutesToExplore) {
        return this.currentMinute >= minutesToExplore;
    }

    public String stats() {
        List<String> path = new ArrayList<>(this.computeTreePath(new ArrayList<>()).stream().map(treeNode -> treeNode.currentValve.label() + "(" + (treeNode.isValveOpened ? "O-" : "C-") + treeNode.currentPressureRelease + ")").toList());
        Collections.reverse(path);
        String labelPath = path.stream().reduce("Root", (v1, v2) -> v1 + ">" + v2);
        return "Minute " + this.currentMinute + " :: " + labelPath + " (total : " + this.computeTotalPressureReleased() + ")";
    }

    public TreeNode addVirtualLeafIfAllValvesAreOpened(long nbMaxValvesToOpen, int minutesToExplore) {
        // check that all openable valves were effectively opened => skip the next explorations by adding a virtual leave with the simulated total pressureRelease remaining
        if (this.computeOpenedValves(new HashSet<>()).size() == nbMaxValvesToOpen) {
            // add virtual child with to simulate the remaining minutes to elapse
            TreeNode child = new TreeNode(this.currentValve, this.isValveOpened, this.currentPressureRelease * (minutesToExplore - this.currentMinute), minutesToExplore, this.depth + 1, this);
            //System.out.println("Add virtual child node as all valves are already opened : " + child.stats());
            this.children.add(child);
            return child;
        }
        return null;
    }

    public void removeChild(TreeNode child) {
        this.children.remove(child);
    }
}
