package day19;

import java.util.*;

public class TreeNode {
    private static final boolean DEBUG = false;

    private final int currentMinute;
    private final TreeNode parent;
    private final List<TreeNode> children;

    private Map<MineralType, Integer> collectingRobotsByType;
    private Map<MineralType, Integer> collectedMineralsByType;

    private MineralType mineralTypeToBuild;

    public int getCurrentMinutes() {
        return this.currentMinute;
    }

    public TreeNode(int currentMinute, TreeNode parent, Map<MineralType, Integer> collectingRobotsByType, Map<MineralType, Integer> collectedMineralsByType, MineralType mineralTypeToBuild) {
        this.currentMinute = currentMinute;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.collectingRobotsByType = collectingRobotsByType;
        this.collectedMineralsByType = collectedMineralsByType;
        this.mineralTypeToBuild = mineralTypeToBuild;
    }

    public TreeNode addChild(MineralType mineralTypeToBuildForChild) {
        Map<MineralType, Integer> childCollectingRobotsByType = new HashMap<>(this.collectingRobotsByType);
        Map<MineralType, Integer> childCollectedMineralsByType = new HashMap<>(this.collectedMineralsByType);

        TreeNode child = new TreeNode(this.currentMinute + 1, this, childCollectingRobotsByType, childCollectedMineralsByType, mineralTypeToBuildForChild);

        this.children.add(child);
        return child;
    }

    public int getCollectedGeodeCount() {
        return this.getCollectedMineralByType(MineralType.GEODE);
    }

    public List<MineralType> getMineralTypeOfCollectingRobotToBuild(Map<MineralType, CollectingRobot> collectingRobotsByMineralTypeBlueprint) {
        List<MineralType> eligibleMineralTypeOfCollectingRobotToBuild = getEligibleMineralTypeOfCollectingRobotToBuild(collectingRobotsByMineralTypeBlueprint);

        // Heuristic 4 : if we can build geode, only build geode
        if (eligibleMineralTypeOfCollectingRobotToBuild.contains(MineralType.GEODE))
            return List.of(MineralType.GEODE);

        // Heuristic 3 : don't build robot above the max cost of its mineral type
        Set<MineralType> typesToDiscard = new HashSet<>();
        for (MineralType type : MineralType.values()) {
            if (! this.needToBuildCollectingRobot(type, collectingRobotsByMineralTypeBlueprint))
                typesToDiscard.add(type);
        }

        // Heuristic 2 : if building X and parent did not but could have => skip
        if (this.parent != null && this.mineralTypeToBuild == null) {
            for (MineralType type : eligibleMineralTypeOfCollectingRobotToBuild) {
                if (type == null)
                    continue;
                List<MineralType> parentEligibleTypes = this.parent.getEligibleMineralTypeOfCollectingRobotToBuild(collectingRobotsByMineralTypeBlueprint);
                if (parentEligibleTypes.contains(type))
                    typesToDiscard.add(type);
            }
        }
        eligibleMineralTypeOfCollectingRobotToBuild.removeAll(typesToDiscard);

        return eligibleMineralTypeOfCollectingRobotToBuild;
    }

    // Heuristic 3 : don't build robot above the max cost of its mineral type
    private boolean needToBuildCollectingRobot(MineralType type, Map<MineralType, CollectingRobot> collectingRobotsByMineralTypeBlueprint) {
        if (type == MineralType.GEODE)
            return true;
        int maxCost = collectingRobotsByMineralTypeBlueprint.values().stream()
                .map(collectingRobot -> collectingRobot.costByMineralType().get(type) != null
                        ? collectingRobot.costByMineralType().get(type)
                        : 0)
                .reduce(0, Integer::max);
        return this.getCollectingRobotByType(type) < maxCost;
    }

    private List<MineralType> getEligibleMineralTypeOfCollectingRobotToBuild(Map<MineralType, CollectingRobot> collectingRobotsByMineralTypeBlueprint) {
        // what minerals do I have in order to build new collecting robots
        List<MineralType> eligibleMineralTypeOfCollectingRobotToBuild = new ArrayList<>();

        // First add Null to allow collect only child
        eligibleMineralTypeOfCollectingRobotToBuild.add(null);

        // - check ore collecting robot
        int collectedOre = this.getCollectedMineralByType(MineralType.ORE);
        if (collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.ORE).costByMineralType().get(MineralType.ORE))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.ORE);

        // - check clay collecting robot
        if (collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.CLAY).costByMineralType().get(MineralType.ORE))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.CLAY);

        // - check obsidian collecting robot
        int collectedClay = this.getCollectedMineralByType(MineralType.CLAY);
        if (collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.OBSIDIAN).costByMineralType().get(MineralType.ORE)
                && collectedClay >= collectingRobotsByMineralTypeBlueprint.get(MineralType.OBSIDIAN).costByMineralType().get(MineralType.CLAY))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.OBSIDIAN);

        // - check geode collecting robot
        int collectedObsidian = this.getCollectedMineralByType(MineralType.OBSIDIAN);
        if (collectedOre >= collectingRobotsByMineralTypeBlueprint.get(MineralType.GEODE).costByMineralType().get(MineralType.ORE)
                && collectedObsidian >= collectingRobotsByMineralTypeBlueprint.get(MineralType.GEODE).costByMineralType().get(MineralType.OBSIDIAN))
            eligibleMineralTypeOfCollectingRobotToBuild.add(MineralType.GEODE);

        return eligibleMineralTypeOfCollectingRobotToBuild;
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

    public void buildRobotAndCollect(Map<MineralType, CollectingRobot> collectingRobotsByMineralType) {
        // First spend minerals to start building robot considering given mineralTypeRobotToBuild (if applicable)
        this.startBuildingRobot(collectingRobotsByMineralType);

        // Then collect with current building robots
        this.collectMinerals();

        // Finally, add the newly built robot (if applicable)
        this.endBuildingRobot();
    }

    private void collectMinerals() {
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

    private void startBuildingRobot(Map<MineralType, CollectingRobot> collectingRobotByMineralType) {
        if (this.mineralTypeToBuild != null) {
            CollectingRobot collectingRobot = collectingRobotByMineralType.get(this.mineralTypeToBuild);
            //if (DEBUG)
            //    System.out.println("Start building collecting-robot for " + collectingRobot.mineralType());
            if (DEBUG)
                System.out.println("Spend " + collectingRobot.costByMineralType() + " to start building a " + collectingRobot.mineralType() + "-collection robot");
            for (MineralType mineralType : collectingRobot.costByMineralType().keySet()) {
                int nbCollectedMineral = this.getCollectedMineralByType(mineralType);
                if (nbCollectedMineral < collectingRobot.costByMineralType().get(mineralType)) {
                    throw new IllegalStateException("Not enough minerals " + nbCollectedMineral + " " + mineralType + " to start building " + collectingRobot.mineralType() + " collection-robot");
                } else {
                    int remainingCollectedMineral = this.addCollectedMineralByType(mineralType, -1 * collectingRobot.costByMineralType().get(mineralType));
                    //if (DEBUG)
                    //    System.out.println("Now remaining only " + remainingCollectedMineral + " " + mineralType);
                }
            }
        }
    }

    private void endBuildingRobot() {
        if (this.mineralTypeToBuild != null) {
            //if (DEBUG)
            //    System.out.println("End building collecting-robot for " + this.mineralTypeToBuild);
            Integer nbCollectingRobots = this.collectingRobotsByType.get(this.mineralTypeToBuild);
            int newCollectingRobotNb = 1;
            if (nbCollectingRobots != null) {
                newCollectingRobotNb += nbCollectingRobots;
            }
            this.collectingRobotsByType.put(this.mineralTypeToBuild, newCollectingRobotNb);
            if (DEBUG)
                System.out.println("The new " + this.mineralTypeToBuild + "-collecting robot is ready; you now have " + newCollectingRobotNb + " of them.");
        }
    }

    public String stats() {
        return "##### Minute " + this.currentMinute +
                " - buildingRobots : " + this.collectingRobotsByType +
                " - collected minerals : " + this.collectedMineralsByType +
                " - mineralType to build : " + this.mineralTypeToBuild;
    }
}
