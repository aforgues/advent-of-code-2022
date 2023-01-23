package day19;

import java.util.HashMap;
import java.util.Map;

public class BlueprintExplorer {
    private final Blueprint blueprint;
    private final int nbMaxMinutes;

    private long nbNodeExplored = 0;

    public BlueprintExplorer(Blueprint blueprint, int nbMaxMinutes) {
        this.blueprint = blueprint;
        this.nbMaxMinutes = nbMaxMinutes;
    }

    public int explore() {
        TreeNode root = new TreeNode(1, null, Map.of(MineralType.ORE, 1), new HashMap<>(), null);

        return collectMinerals(root);
    }

    private int collectMinerals(TreeNode node) {
        int maxGeodesCollected = node.getCollectedGeodeCount();

        nbNodeExplored++;

        //System.out.println(node.stats());
        node.buildRobotAndCollect(this.blueprint.collectingRobotsByMineralType());

        if (node.getCurrentMinutes() == this.nbMaxMinutes) {
            return node.getCollectedGeodeCount();
        }

        for (MineralType mineralTypeToBuild : node.getMineralTypeOfCollectingRobotToBuild(this.blueprint.collectingRobotsByMineralType())) {
            TreeNode child = node.addChild(mineralTypeToBuild);
            maxGeodesCollected = Math.max(maxGeodesCollected, collectMinerals(child));
            node.removeChild(child);
        }
        if (nbNodeExplored%10000000 == 0)
            System.out.println("B:" + this.blueprint.id() + ",M:"+ node.getCurrentMinutes() + ",N:" + nbNodeExplored);

        return maxGeodesCollected;
    }
}
