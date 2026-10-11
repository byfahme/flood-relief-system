package com.kmct.floodrelief.matching;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MatchingServiceTest {

    private final MatchingService service = new MatchingService();

    @Test
    void threeCampExample_surplusCoversTwoShortages() {
        List<CampStock> camps = List.of(
            new CampStock("A", Map.of("rice", 10), Map.of()),
            new CampStock("B", Map.of(), Map.of("rice", 6)),
            new CampStock("C", Map.of(), Map.of("rice", 8))
        );

        List<Transfer> result = service.match(camps);

        assertEquals(2, result.size());
        assertEquals(new Transfer("A", "B", "rice", 6), result.get(0));
        assertEquals(new Transfer("A", "C", "rice", 4), result.get(1));
    }

    @Test
    void shortageLargerThanSurplus_isPartlyMatched() {
        List<CampStock> camps = List.of(
            new CampStock("A", Map.of("water", 3), Map.of()),
            new CampStock("B", Map.of(), Map.of("water", 6))
        );

        List<Transfer> result = service.match(camps);

        assertEquals(1, result.size());
        assertEquals(new Transfer("A", "B", "water", 3), result.get(0));
    }

    @Test
    void noSurplus_producesNoTransfers() {
        List<CampStock> camps = List.of(
            new CampStock("A", Map.of(), Map.of("medicine", 5)),
            new CampStock("B", Map.of(), Map.of("medicine", 2))
        );

        assertTrue(service.match(camps).isEmpty());
    }

    @Test
    void differentItems_areMatchedSeparately() {
        List<CampStock> camps = List.of(
            new CampStock("A", Map.of("rice", 4), Map.of()),
            new CampStock("B", Map.of("water", 5), Map.of("rice", 2))
        );

        List<Transfer> result = service.match(camps);

        assertEquals(1, result.size());
        assertEquals(new Transfer("A", "B", "rice", 2), result.get(0));
    }
}