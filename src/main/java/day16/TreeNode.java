package day16;

import java.util.*;

public class TreeNode {
    private final Valve currentValve;
    private final Valve currentElephantValve;
    private final int currentPressureRelease;
    private final int currentMinute;
    private final Map<String, String> openedValveLabelByLabel;

    private final TreeNode parent;
    private final List<TreeNode> children;


    public Valve getCurrentValve() {
        return this.currentValve;
    }
    public Valve getCurrentElephantValve() {
        return this.currentElephantValve;
    }

    private boolean isValveOpened() {
        return this.openedValveLabelByLabel.get(this.currentValve.label()) != null;
    }

    private boolean isElephantValveOpened() {
        return this.currentElephantValve != null && this.openedValveLabelByLabel.get(this.currentElephantValve.label()) != null;
    }

    public TreeNode(Valve currentValve, boolean isValveOpened, Valve currentElephantValve, boolean isElephantValveOpened, int currentPressureRelease, int currentMinute, TreeNode parent) {
        this.currentValve = currentValve;
        this.currentElephantValve = currentElephantValve;
        this.currentPressureRelease = currentPressureRelease;
        this.currentMinute = currentMinute;
        this.openedValveLabelByLabel = new TreeMap<>();
        if (parent != null)
            this.openedValveLabelByLabel.putAll(parent.openedValveLabelByLabel);
        if (isValveOpened)
            this.openedValveLabelByLabel.put(currentValve.label(), currentValve.label());
        if (isElephantValveOpened)
            this.openedValveLabelByLabel.put(currentElephantValve.label(), currentElephantValve.label());

        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public Set<TreeAction> getNextEligibleTreeActions() {
        Set<TreeAction> nextEligibleTreeActions = new HashSet<>();

        boolean isValveOpenable = this.currentValve.flowRate() > 0 && ! this.isValveOpened();
        boolean isElephantValveOpenable = this.currentElephantValve != null && this.currentElephantValve.flowRate() > 0 && ! this.isElephantValveOpened();

        // for part 1
        // for the next minute, either open current valve of this node or move to next potential valves
        if (this.currentElephantValve == null) {
            if (isValveOpenable) {
                nextEligibleTreeActions.add(new TreeAction(ValveActionType.OPEN, this.currentValve.label(), null, null));
            }
            for (String tunnelExitValveLabel : this.currentValve.tunnelExitValveLabels()) {
                nextEligibleTreeActions.add(new TreeAction(ValveActionType.MOVE_TO, tunnelExitValveLabel, null, null));
            }
        }
        // for part 2 with elephant help
        else {
            // if both are openable
            if (isValveOpenable && isElephantValveOpenable) {
                nextEligibleTreeActions.add(new TreeAction(ValveActionType.OPEN, this.currentValve.label(), ValveActionType.OPEN, this.currentElephantValve.label()));
            }
            // only my valve is openable
            if (isValveOpenable) {
                for (String tunnelExitElephantValveLabel : this.currentElephantValve.tunnelExitValveLabels()) {
                    // Don't move both valves to the same next valve (?)
                    if (this.currentValve.label().equals(tunnelExitElephantValveLabel))
                        continue;
                    nextEligibleTreeActions.add(new TreeAction(ValveActionType.OPEN, this.currentValve.label(), ValveActionType.MOVE_TO, tunnelExitElephantValveLabel));
                }
            }
            // only elephant valve is openable
            if (isElephantValveOpenable) {
                for (String tunnelExitValveLabel : this.currentValve.tunnelExitValveLabels()) {
                    // Don't move both valves to the same next valve (?)
                    if (tunnelExitValveLabel.equals(this.currentElephantValve.label()))
                        continue;
                    nextEligibleTreeActions.add(new TreeAction(ValveActionType.MOVE_TO, tunnelExitValveLabel, ValveActionType.OPEN, this.currentElephantValve.label()));
                }
            }
            // move to the exit valves
            for (String tunnelExitValveLabel : this.currentValve.tunnelExitValveLabels()) {
                for (String tunnelExitElephantValveLabel : this.currentElephantValve.tunnelExitValveLabels()) {
                    // Don't move both valves to the same next valve (?)
                    if (tunnelExitValveLabel.equals(tunnelExitElephantValveLabel))
                        continue;

                    nextEligibleTreeActions.add(new TreeAction(ValveActionType.MOVE_TO, tunnelExitValveLabel, ValveActionType.MOVE_TO, tunnelExitElephantValveLabel));
                }
            }
        }
        return nextEligibleTreeActions;
    }

    public TreeNode addChild(Valve nextValveToExplore, boolean openValve, Valve nextElephantValveToExplore, boolean openElephantValve) {

        boolean isValveAlreadyOpened = this.openedValveLabelByLabel.get(nextValveToExplore.label()) != null;
        boolean isElephantValveAlreadyOpened = nextElephantValveToExplore != null && this.openedValveLabelByLabel.get(nextElephantValveToExplore.label()) != null;

        // avoid turning around
        if (this.parent != null
                && this.parent.getCurrentValve().label().equals(nextValveToExplore.label())
                && this.parent.isValveOpened() == isValveAlreadyOpened
                && (nextElephantValveToExplore == null || this.parent.getCurrentElephantValve().label().equals(nextElephantValveToExplore.label())
                && this.parent.isElephantValveOpened() == isElephantValveAlreadyOpened))
            return null;

        int childReleasePressure = this.currentPressureRelease;
        if (openValve)
            childReleasePressure += nextValveToExplore.flowRate();
        if (openElephantValve)
            childReleasePressure += nextElephantValveToExplore.flowRate();

        TreeNode child = new TreeNode(nextValveToExplore, openValve || isValveAlreadyOpened, nextElephantValveToExplore, openElephantValve || isElephantValveAlreadyOpened, childReleasePressure, this.currentMinute + 1, this);
        //System.out.println("Add new child to explore : " + child.stats());

        // avoid repeating the same loop like staying at the same pressureRelease
        // check the ascendant : if we keep the same pressureRelease until the same Valve => skip
        List<TreeNode> ascendants = computeTreePath(new ArrayList<>());
        for (TreeNode ascendant : ascendants) {
            if (ascendant.currentPressureRelease != child.currentPressureRelease)
                break;
            if (ascendant.currentValve.label().equals(child.currentValve.label())
                    && (ascendant.currentElephantValve == null || ascendant.currentElephantValve.label().equals(child.currentElephantValve.label())))
                return null;
        }

        this.children.add(child);

        return child;
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

    public boolean hasExpiredMinutesToExplore(int minutesToExplore) {
        return this.currentMinute >= minutesToExplore;
    }

    private boolean shouldAbandon(long nbMaxValvesToOpen, int minutesToExplore) {
        int nbOpenedValve = this.openedValveLabelByLabel.size();
        return (nbMaxValvesToOpen - nbOpenedValve) > (minutesToExplore - this.currentMinute);
    }

    public String stats() {
        List<String> path = new ArrayList<>(this.computeTreePath(new ArrayList<>()).stream().map(treeNode -> treeNode.currentValve.label()
                + "(" + (treeNode.isValveOpened() ? "O)" : "C)")
                + (treeNode.currentElephantValve != null ? ("/" + treeNode.currentElephantValve.label() + "(" + (treeNode.isElephantValveOpened() ? "O)=" : "C)=")) : "=")
                + treeNode.currentPressureRelease).toList());
        Collections.reverse(path);
        String labelPath = path.stream().reduce("Root", (v1, v2) -> v1 + ">" + v2);
        return "Minute " + this.currentMinute + " :: " + this.openedValveLabelByLabel.values() + " :: " + labelPath + " (total : " + this.computeTotalPressureReleased() + ")";
    }

    public TreeNode addVirtualLeafIfAllValvesAreOpened(long nbMaxValvesToOpen, int minutesToExplore) {
        // check that all openable valves were effectively opened => skip the next explorations by adding a virtual leave with the simulated total pressureRelease remaining
        if (this.openedValveLabelByLabel.size() == nbMaxValvesToOpen/* || shouldAbandon(nbMaxValvesToOpen, minutesToExplore)*/) {
            // add virtual child with to simulate the remaining minutes to elapse
            TreeNode child = new TreeNode(this.currentValve, this.isValveOpened(), this.currentElephantValve, this.isElephantValveOpened(), this.currentPressureRelease * (minutesToExplore - this.currentMinute), minutesToExplore, this);
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
