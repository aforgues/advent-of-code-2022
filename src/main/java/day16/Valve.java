package day16;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public record Valve(String label, int flowRate, List<String> tunnelExitValveLabels) {
    private static final String TUNNEL_LEAD_TO_VALVE = "tunnel leads to valve ";
    private static final String TUNNELS_LEAD_TO_VALVES = "tunnels lead to valves ";

    public static Valve fromContent(String content) {
        Scanner scanner = new Scanner(content);
        scanner.useDelimiter(" ");
        scanner.next(); // skip Valve
        String label = content.split(" ")[1];
        int flowRate = Integer.parseInt(content.split(";")[0].split("=")[1]);
        String tunnelExitValveAsString = content.substring(content.indexOf(TUNNEL_LEAD_TO_VALVE) + TUNNEL_LEAD_TO_VALVE.length());
        String tunnelExitValvesAsString = content.substring(content.indexOf(TUNNELS_LEAD_TO_VALVES) + TUNNELS_LEAD_TO_VALVES.length());
        List<String> tunnelExitValveLabels = content.contains(TUNNELS_LEAD_TO_VALVES) ? Arrays.stream(tunnelExitValvesAsString.split(", ")).toList()
                : List.of(tunnelExitValveAsString);
        return new Valve(label, flowRate, tunnelExitValveLabels);
    }
}
