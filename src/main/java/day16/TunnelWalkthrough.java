package day16;

import java.util.*;

public class TunnelWalkthrough {
    private final Map<String, Valve> valvesByLabel;
    private final int minutesToExplore;
    private final long nbMaxValvesToOpen;
    private final boolean withElephantHelp;

    public TunnelWalkthrough(List<Valve> valves, int minutesToExplore, boolean withElephantHelp) {
        this.valvesByLabel = new HashMap<>();
        valves.forEach(valve -> this.valvesByLabel.put(valve.label(), valve));
        this.minutesToExplore = minutesToExplore;
        this.withElephantHelp = withElephantHelp;
        this.nbMaxValvesToOpen = valves.stream().filter(valve -> valve.flowRate() > 0).count();
    }

    private Valve findValveByLabel(String label) {
        return this.valvesByLabel.get(label);
    }

    // With this BFS approach, no way to clean the tree gradually in order to avoid JavaHeapSpace exception, as we reach the leaves (at 30 min) only at the end of the algorithm
    // Better switch to DFS approach (@see exploreDFS)
    public int exploreBFS(String startingValveLabel) {
        Queue<TreeNode> queue = new LinkedList<>();

        Valve rootValve = findValveByLabel(startingValveLabel);
        TreeNode root = new TreeNode(rootValve, false, this.withElephantHelp ? rootValve : null, false,0, 1, null);
        queue.add(root);

        List<TreeNode> leavesHavingExpiredMinutes = new ArrayList<>();
        while (! queue.isEmpty()) {


            // moving to this valve
            TreeNode node = queue.poll();
            if (ProboscideaVolcaniumApp.TEST)
                System.out.println(node.stats());

            if (node.hasExpiredMinutesToExplore(this.minutesToExplore)) {
                leavesHavingExpiredMinutes.add(node);
                continue;
            }

            TreeNode virtualChild = node.addVirtualLeafIfAllValvesAreOpened(this.nbMaxValvesToOpen, this.minutesToExplore);
            if (virtualChild != null) {
                leavesHavingExpiredMinutes.add(virtualChild);
                continue;
            }

            Valve valve = node.getCurrentValve();
            if (ProboscideaVolcaniumApp.TEST)
                System.out.println("Current Valve explored : " + valve);

            for (TreeAction treeAction : node.getNextEligibleTreeActions()) {
                Valve childValve = findValveByLabel(treeAction.valveLabel());
                TreeNode child = node.addChild(childValve, treeAction.type() == ValveActionType.OPEN, null, false);
                if (child != null) {
                    queue.add(child);
                }
            }
        }

        // Retrieve leave in tree with higher totalPressureRelease
        System.out.println("Leaves with expired minutes : " + leavesHavingExpiredMinutes.size());
        TreeNode maxLeave = leavesHavingExpiredMinutes.stream().max(Comparator.comparingInt(TreeNode::computeTotalPressureReleased)).get();
        System.out.println("Optimal path : " + maxLeave.stats());

        return leavesHavingExpiredMinutes.stream().map(TreeNode::computeTotalPressureReleased).reduce(Integer::max).get();
    }

    private record PathOption(int totalPressureRelease, String path) {}

    public int exploreDFS(String startingValveLabel) {
        Valve rootValve = findValveByLabel(startingValveLabel);
        TreeNode root = new TreeNode(rootValve, false, this.withElephantHelp ? rootValve : null, false, 0, 1, null);
        PathOption bestPath = exploreDFS(root);
        System.out.println("Best path : " + bestPath);
        return bestPath.totalPressureRelease;
    }

    public PathOption exploreDFS(TreeNode node) {
        PathOption maxPathOption = new PathOption(0, "");

        // Exploring (moving or opening) this valve
        //if (ProboscideaVolcaniumApp.TEST)
        //    System.out.println(node.stats());

        if (node.hasExpiredMinutesToExplore(this.minutesToExplore)) {
            return new PathOption(node.computeTotalPressureReleased(), node.stats());
        }

        TreeNode virtualChild = node.addVirtualLeafIfAllValvesAreOpened(this.nbMaxValvesToOpen, this.minutesToExplore);
        if (virtualChild != null) {
            //if (ProboscideaVolcaniumApp.TEST)
            //    System.out.println(virtualChild.stats());
            return new PathOption(virtualChild.computeTotalPressureReleased(), virtualChild.stats());
        }

        for (TreeAction treeAction : node.getNextEligibleTreeActions()) {
            Valve childValve = findValveByLabel(treeAction.valveLabel());
            Valve childElephantValve = this.withElephantHelp ? findValveByLabel(treeAction.elephantValveLabel()) : null;
            TreeNode child = node.addChild(childValve, treeAction.type() == ValveActionType.OPEN, childElephantValve, treeAction.elephantType() == ValveActionType.OPEN);
            if (child != null) {
                PathOption childMaxPathOption = exploreDFS(child);
                if (childMaxPathOption.totalPressureRelease > maxPathOption.totalPressureRelease)
                    maxPathOption = childMaxPathOption;
                node.removeChild(child);
            }
        }
        return maxPathOption;
    }
}
