package day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {
    private static final boolean DEBUG = false;

    private final int currentMinute;
    private final TreeNode parent;
    private final List<TreeNode> children;

    private Map<MineralType, Integer> collectingRobotsByType;
    private Map<MineralType, Integer> collectedMineralsByType;

    public int getCurrentMinutes() {
        return this.currentMinute;
    }

    public TreeNode(int currentMinute, TreeNode parent, Map<MineralType, Integer> collectingRobotsByType, Map<MineralType, Integer> collectedMineralsByType) {
        this.currentMinute = currentMinute;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.collectingRobotsByType = collectingRobotsByType;
        this.collectedMineralsByType = collectedMineralsByType;
    }

    public TreeNode addChild() {
        Map<MineralType, Integer> childCollectingRobotsByType = new HashMap<>(this.collectingRobotsByType);
        Map<MineralType, Integer> childCollectedMineralsByType = new HashMap<>(this.collectedMineralsByType);

        TreeNode child = new TreeNode(this.currentMinute + 1, this, childCollectingRobotsByType, childCollectedMineralsByType);
        this.children.add(child);
        return child;
    }

    public int getCollectedGeodeCount() {
        return this.getCollectedMineralByType(MineralType.GEODE);
    }

    public List<MineralType> getEligibleMineralTypeOfCollectingRobotToBuild(Map<MineralType, CollectingRobot> collectingRobotsByMineralTypeBlueprint) {
        // what minerals do I have in order to build new collecting robots
        List<MineralType> eligibleMineralTypeOfCollectingRobotToBuild = new ArrayList<>();

        // First add Null to allow collect only child
        eligibleMineralTypeOfCollectingRobotToBuild.add(null);

        // TODO : find heuristics to reduce variations considering dynamic data (nb of collecting robots, nb of collected minerals, current minute...)

        // - check ore collecting robot
        int collectedOre = this.getCollectedMineralByType(MineralType.ORE);
        if (this.needToBuildCollectingRobot(MineralType.ORE) && collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.ORE).costByMineralType().get(MineralType.ORE))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.ORE);

        // - check clay collecting robot
        if (this.needToBuildCollectingRobot(MineralType.CLAY) && collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.CLAY).costByMineralType().get(MineralType.ORE))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.CLAY);

        // - check obsidian collecting robot
        int collectedClay = this.getCollectedMineralByType(MineralType.CLAY);
        if (this.needToBuildCollectingRobot(MineralType.OBSIDIAN) && collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.OBSIDIAN).costByMineralType().get(MineralType.ORE)
                && collectedClay >= collectingRobotsByMineralTypeBlueprint.get(MineralType.OBSIDIAN).costByMineralType().get(MineralType.CLAY))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.OBSIDIAN);

        // - check geode collecting robot
        int collectedObsidian = this.getCollectedMineralByType(MineralType.OBSIDIAN);
        if (this.needToBuildCollectingRobot(MineralType.GEODE) && collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.GEODE).costByMineralType().get(MineralType.ORE)
                && collectedObsidian >= collectingRobotsByMineralTypeBlueprint.get(MineralType.GEODE).costByMineralType().get(MineralType.OBSIDIAN))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.GEODE);

        return eligibleMineralTypeOfCollectingRobotToBuild;
    }

    private boolean needToBuildCollectingRobot(MineralType type) {
        return switch(type) {
            case ORE -> this.getCollectedMineralByType(MineralType.CLAY) == 0;
            case CLAY -> this.getCollectedMineralByType(MineralType.OBSIDIAN) == 0;
            case OBSIDIAN -> this.getCollectedMineralByType(MineralType.GEODE) == 0;
            case GEODE -> true;
        };
    }

    public void removeChild(TreeNode child) {
        this.children.remove(child);
    }

    private int getCollectedMineralByType(MineralType type) {
        Integer nb = this.collectedMineralsByType.get(type);
        return nb != null ? nb : 0;
    }

    private int getCollectingRobotByType(MineralType type) {
        Integer nb = this.collectingRobotsByType.get(type);
        return nb != null ? nb : 0;
    }

    private int addCollectedMineralByType(MineralType type, int nbToAdd) {
        if (this.collectedMineralsByType == null) {
            this.collectedMineralsByType = new HashMap<>();
        }
        int newCollectedMineral = nbToAdd + this.getCollectedMineralByType(type);
        this.collectedMineralsByType.put(type, newCollectedMineral);
        return newCollectedMineral;
    }

    public void collectMinerals() {
        //if (DEBUG)
        //    System.out.println("Start collecting minerals with current collecting robots " + this.collectingRobotsByType);
        for (MineralType mineralType : MineralType.values()) {
            Integer nbCollectingRobot = this.collectingRobotsByType.get(mineralType);
            if (nbCollectingRobot != null) {
                int newCollectedMineral = this.addCollectedMineralByType(mineralType, nbCollectingRobot);
                if (DEBUG)
                    System.out.println(nbCollectingRobot + " " + mineralType.name() + "-collecting robot(s) collect(s) " + nbCollectingRobot + " " + mineralType + "; you now have " + newCollectedMineral + " " + mineralType);
            }
        }
    }

    public void startBuildingRobot(CollectingRobot collectingRobot) {
        //if (DEBUG)
        //    System.out.println("Start building collecting-robot for " + collectingRobot.mineralType());
        if (DEBUG)
            System.out.println("Spend " + collectingRobot.costByMineralType() + " to start building a " + collectingRobot.mineralType() + "-collection robot");
        for (MineralType mineralType : collectingRobot.costByMineralType().keySet()) {
            int nbCollectedMineral = this.getCollectedMineralByType(mineralType);
            if (nbCollectedMineral < collectingRobot.costByMineralType().get(mineralType)) {
                throw new IllegalStateException("Not enough minerals " + nbCollectedMineral + " " + mineralType + " to start building " + collectingRobot.mineralType() + " collection-robot");
            }
            else {
                int remainingCollectedMineral = this.addCollectedMineralByType(mineralType, -1*collectingRobot.costByMineralType().get(mineralType));
                //if (DEBUG)
                //    System.out.println("Now remaining only " + remainingCollectedMineral + " " + mineralType);
            }
        }
    }

    public void endBuildingRobot(MineralType mineralTypeRobotToBuild) {
        //if (DEBUG)
        //    System.out.println("End building collecting-robot for " + mineralTypeRobotToBuild);
        Integer nbCollectingRobots = this.collectingRobotsByType.get(mineralTypeRobotToBuild);
        int newCollectingRobotNb = 1;
        if (nbCollectingRobots != null) {
            newCollectingRobotNb += nbCollectingRobots;
        }
        this.collectingRobotsByType.put(mineralTypeRobotToBuild, newCollectingRobotNb);
        if (DEBUG)
            System.out.println("The new " + mineralTypeRobotToBuild + "-collecting robot is ready; you now have " + newCollectingRobotNb + " of them.");
    }

    public String stats() {
        String sb = "##### Minute " + this.currentMinute +
                " - buildingRobots : " + this.collectingRobotsByType +
                " - collected minerals : " + this.collectedMineralsByType;
        return sb;
    }
}
