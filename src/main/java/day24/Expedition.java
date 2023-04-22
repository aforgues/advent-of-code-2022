package day24;

import utils.Position;

import java.util.*;

public class Expedition {
    private final Valley valley;
    private final List<Blizzard> blizzards;
    private final Map<Integer, List<Blizzard>> blizzardsByMinute;

    private long nbNodeExplored = 0;

    public Expedition(Valley valley, List<Blizzard> blizzards) {
        this.valley = valley;
        this.blizzards = blizzards;
        this.blizzardsByMinute = new HashMap<>();
    }

    public int exploreBFS() {
        Queue<TreeNode> queue = new LinkedList<>();
        List<NodeKey> alreadySeenNodes = new ArrayList<>();

        TreeNode root = new TreeNode(null, 0, new Position(2,1));
        this.blizzardsByMinute.put(0, this.blizzards);
        queue.add(root);

        while (! queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (alreadySeenNodes.contains(node.getNodeKey())) {
                continue;
            }
            alreadySeenNodes.add(node.getNodeKey());
            nbNodeExplored++;

            if (nbNodeExplored%1000 == 0) {
                System.out.println(node.getNodeKey() + " - explored #" + nbNodeExplored);
                //node.displayValleyInConsole(this.valley, this.blizzardsByMinute);
            }

            if (node.hasReachedValleyExit(this.valley)) {
                System.out.println("Expedition path is " + node.getExpeditionPath());
                node.displayFullExpeditionInConsole(this.valley, this.blizzardsByMinute);
                return node.currentMinute();
            }

            for (Position nextPosition : node.getPotentialNextExpeditionPositions(this.nextBlizzards(node), this.valley)) {
                TreeNode child = node.addChild(nextPosition);
                queue.add(child);
            }
        }

        throw new IllegalStateException("No way to find exit of the valley !!!");
    }

    private List<Blizzard> nextBlizzards(TreeNode node) {
        List<Blizzard> alreadyComputedNextBlizzards = this.blizzardsByMinute.get(node.currentMinute() + 1);
        if (alreadyComputedNextBlizzards != null) {
            return alreadyComputedNextBlizzards;
        }
        else {
            List<Blizzard> updatedBlizzards = new ArrayList<>();
            this.blizzardsByMinute.get(node.currentMinute()).forEach(blizzard -> updatedBlizzards.add(new Blizzard(blizzard.nextPosition(this.valley), blizzard.moveType())));
            this.blizzardsByMinute.put(node.currentMinute() + 1, updatedBlizzards);
            return updatedBlizzards;
        }
    }
}
