package day19;

import java.util.*;

public record Blueprint(int id, Map<MineralType, CollectingRobot> collectingRobotsByMineralType) {
    public static Blueprint fromPuzzleInput(String content) {
        int id = Integer.parseInt(content.split(" ")[1].replace(":", ""));

        Map<MineralType, CollectingRobot> collectingRobotsByMineralType = new HashMap<>();
        String robotConfigs = content.split(": ")[1];
        Scanner scanner = new Scanner(robotConfigs);
        scanner.useDelimiter("\\. ");

        // ore
        String oreRobotConfig = scanner.next();
        int oreCost = Integer.parseInt(oreRobotConfig.split(" ")[4]);
        collectingRobotsByMineralType.put(MineralType.ORE, new CollectingRobot(MineralType.ORE, Map.of(MineralType.ORE, oreCost)));

        // clay
        String clayRobotConfig = scanner.next();
        int oreCostForClayCollectingRobot = Integer.parseInt(clayRobotConfig.split(" ")[4]);
        collectingRobotsByMineralType.put(MineralType.CLAY, new CollectingRobot(MineralType.CLAY, Map.of(MineralType.ORE, oreCostForClayCollectingRobot)));

        // obsidian
        String obsidianRobotConfig = scanner.next();
        int oreCostForObsidianCollectingRobot = Integer.parseInt(obsidianRobotConfig.split(" ")[4]);
        int clayCostForObsidianCollectingRobot = Integer.parseInt(obsidianRobotConfig.split(" ")[7]);
        collectingRobotsByMineralType.put(MineralType.OBSIDIAN, new CollectingRobot(MineralType.OBSIDIAN, Map.of(MineralType.ORE, oreCostForObsidianCollectingRobot, MineralType.CLAY, clayCostForObsidianCollectingRobot)));

        // geode
        String geodeRobotConfig = scanner.next();
        int oreCostForGeodeCollectingRobot = Integer.parseInt(geodeRobotConfig.split(" ")[4]);
        int obsidianCostForGeodeCollectingRobot = Integer.parseInt(geodeRobotConfig.split(" ")[7]);
        collectingRobotsByMineralType.put(MineralType.GEODE, new CollectingRobot(MineralType.GEODE, Map.of(MineralType.ORE, oreCostForGeodeCollectingRobot, MineralType.OBSIDIAN, obsidianCostForGeodeCollectingRobot)));

        return new Blueprint(id, collectingRobotsByMineralType);
    }
}
