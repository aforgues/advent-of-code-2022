package day24;

import utils.Position;

import java.util.*;

public class Expedition {
    private final Valley valley;
    private final Map<Integer, List<Blizzard>> blizzardsByMinute;

    private long nbNodeExplored = 0;

    public Expedition(Valley valley) {
        this.valley = valley;
        this.blizzardsByMinute = new HashMap<>();
    }

    public int exploreBFS(List<Blizzard> blizzards, Position startPosition, Position exitPosition) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<NodeKey> alreadySeenNodes = new ArrayList<>();

        TreeNode root = new TreeNode(null, 0, startPosition);
        this.blizzardsByMinute.clear();
        this.blizzardsByMinute.put(0, blizzards);
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

            if (node.hasReachedValleyExit(exitPosition)) {
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

    public List<Blizzard> getBlizzardsAt(int minute) {
        return this.blizzardsByMinute.get(minute);
    }
}
