package day16;

import java.util.*;

public class TunnelWalkthrough {
    private final List<Valve> valves;
    private final int minutesToExplore;
    private final long nbMaxValvesToOpen;

    public TunnelWalkthrough(List<Valve> valves, int minutesToExplore) {
        this.valves = valves;
        this.minutesToExplore = minutesToExplore;
        this.nbMaxValvesToOpen = this.valves.stream().filter(valve -> valve.flowRate() > 0).count();
    }

    private Valve findValveByLabel(String label) {
        return this.valves.stream().filter(valve -> valve.label().equals(label)).findFirst().get();
    }

    // With this BFS approach, no way to clean the tree gradually in order to avoid JavaHeapSpace exception, as we reach the leaves (at 30 min) only at the end of the algorithm
    // Better switch to DFS approach (@see exploreDFS)
    public int exploreBFS(String startingValveLabel) {
        Queue<TreeNode> queue = new LinkedList<>();

        TreeNode root = new TreeNode(findValveByLabel(startingValveLabel), false, 0, 1, 0, null);
        queue.add(root);

        List<TreeNode> leavesHavingExpiredMinutes = new ArrayList<>();
        while (! queue.isEmpty()) {


            // moving to this valve
            TreeNode node = queue.poll();
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
            //System.out.println("Current Valve explored : " + valve);

            // for the next minute, either open current valve of this node or move to next potential valves
            // Open current valve
            TreeNode openedChild = node.explore();
            if (openedChild != null) {
                queue.add(openedChild);
            }

            // Move
            List<String> tunnelExitValveLabels = valve.tunnelExitValveLabels();
            for (String tunnelExitValveLabel : tunnelExitValveLabels) {

                Valve childValve = findValveByLabel(tunnelExitValveLabel);
                TreeNode child = node.addChild(childValve);
                if (child != null) {
                    queue.add(child);
                }
            }
        }

        // Retrieve leave in tree with higher totalPressureRelease
        System.out.println("Leaves with expired minutes : " + leavesHavingExpiredMinutes.size());
        System.out.println("########## Leaves stats #############");
        //leavesHavingExpiredMinutes.stream().forEach(treeNode -> System.out.println(treeNode.stats()));
        TreeNode maxLeave = leavesHavingExpiredMinutes.stream().max(Comparator.comparingInt(TreeNode::computeTotalPressureReleased)).get();
        System.out.println("Optimal path : " + maxLeave.stats());

        return leavesHavingExpiredMinutes.stream().map(TreeNode::computeTotalPressureReleased).reduce(Integer::max).get();
    }

    private record PathOption(int totalPressureRelease, String path) {}

    public int exploreDFS(String startingValveLabel) {
        TreeNode root = new TreeNode(findValveByLabel(startingValveLabel), false, 0, 1, 0, null);
        PathOption bestPath = exploreDFS(root);
        System.out.println("Best path : " + bestPath);
        return bestPath.totalPressureRelease;
    }

    public PathOption exploreDFS(TreeNode node) {
        PathOption maxPathOption = new PathOption(0, "");

        // moving to this valve
        //System.out.println(node.stats());

        if (node.hasExpiredMinutesToExplore(this.minutesToExplore)) {
            return new PathOption(node.computeTotalPressureReleased(), node.stats());
        }

        TreeNode virtualChild = node.addVirtualLeafIfAllValvesAreOpened(this.nbMaxValvesToOpen, this.minutesToExplore);
        if (virtualChild != null) {
            return new PathOption(virtualChild.computeTotalPressureReleased(), virtualChild.stats());
        }

        Valve valve = node.getCurrentValve();
        //System.out.println("Current Valve explored : " + valve);

        // for the next minute, either open current valve of this node or move to next potential valves
        // Open current valve
        TreeNode openedChild = node.explore();
        if (openedChild != null) {
            maxPathOption = exploreDFS(openedChild);
            node.removeChild(openedChild);
        }

        // Move
        List<String> tunnelExitValveLabels = valve.tunnelExitValveLabels();
        for (String tunnelExitValveLabel : tunnelExitValveLabels) {

            Valve childValve = findValveByLabel(tunnelExitValveLabel);
            TreeNode child = node.addChild(childValve);
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
