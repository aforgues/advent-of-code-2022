package day19;

import java.util.Map;

public class BlueprintExplorer {
    private final Blueprint blueprint;
    private static final int MAX_MINUTES = 24;

    public BlueprintExplorer(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public int explore() {
        TreeNode root = new TreeNode(1, null, Map.of(MineralType.ORE, 1), null);

        return collectMinerals(root, null);
    }

    private int collectMinerals(TreeNode node, MineralType mineralTypeRobotToBuild) {
        int maxGeodesCollected = 0;

        //System.out.println(node.stats());

        // First spend minerals to start building robot considering given mineralTypeRobotToBuild
        if (mineralTypeRobotToBuild != null) {
            node.startBuildingRobot(this.blueprint.collectingRobotsByMineralType().get(mineralTypeRobotToBuild));
        }

        // Then collect with current building robots
        node.collectMinerals();

        // Finally, add the newly built robot
        if (mineralTypeRobotToBuild != null) {
            node.endBuildingRobot(mineralTypeRobotToBuild);
        }

        if (node.getCurrentMinutes() == MAX_MINUTES) {
            return node.getCollectedGeodeCount();
        }

        for (MineralType mineralTypeToBuild : node.getEligibleMineralTypeOfCollectingRobotToBuild(this.blueprint.collectingRobotsByMineralType())) {
            TreeNode child = node.addChild();
            maxGeodesCollected = Math.max(maxGeodesCollected, collectMinerals(child, mineralTypeToBuild));
            node.removeChild(child);
        }

        return maxGeodesCollected;
    }
}
