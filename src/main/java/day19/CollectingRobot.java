package day19;

import java.util.Map;

public record CollectingRobot(MineralType mineralType, Map<MineralType, Integer> costByMineralType) {
}
