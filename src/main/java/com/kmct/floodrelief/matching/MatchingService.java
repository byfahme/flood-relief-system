package com.kmct.floodrelief.matching;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MatchingService {

    /**
     * Plans transfers from camps with surplus to camps with shortage.
     * Items are matched one at a time. Camps are served in list order.
     * Any shortage that no surplus can cover is left unmatched.
     */
    public List<Transfer> match(List<CampStock> camps) {
        List<Transfer> transfers = new ArrayList<>();

        TreeSet<String> items = new TreeSet<>();
        for (CampStock camp : camps) {
            items.addAll(camp.surplus().keySet());
            items.addAll(camp.shortage().keySet());
        }

        for (String item : items) {
            Map<String, Integer> available = new LinkedHashMap<>();
            Map<String, Integer> missing = new LinkedHashMap<>();

            for (CampStock camp : camps) {
                int s = camp.surplus().getOrDefault(item, 0);
                int m = camp.shortage().getOrDefault(item, 0);
                if (s > 0) available.put(camp.campName(), s);
                if (m > 0) missing.put(camp.campName(), m);
            }

            for (Map.Entry<String, Integer> need : missing.entrySet()) {
                String toCamp = need.getKey();
                int remaining = need.getValue();

                for (Map.Entry<String, Integer> supply : available.entrySet()) {
                    if (remaining == 0) break;
                    int give = Math.min(remaining, supply.getValue());
                    if (give == 0) continue;

                    transfers.add(new Transfer(supply.getKey(), toCamp, item, give));
                    supply.setValue(supply.getValue() - give);
                    remaining -= give;
                }
            }
        }
        return transfers;
    }
}