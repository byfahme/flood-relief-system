package com.kmct.floodrelief.matching;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VolunteerTest {

    @Test
    void canUpdate_onlyOwnCamp() {
        Volunteer v = new Volunteer("Nimlin", "A");

        assertTrue(v.canUpdate("A"));
        assertFalse(v.canUpdate("B"));
    }

    @Test
    void canDecide_transferInvolvingOwnCamp() {
        Volunteer v = new Volunteer("Nimlin", "A");

        assertTrue(v.canDecide(new Transfer("A", "B", "rice", 5)));
        assertTrue(v.canDecide(new Transfer("C", "A", "rice", 5)));
        assertFalse(v.canDecide(new Transfer("B", "C", "rice", 5)));
    }

    @Test
    void blankName_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Volunteer(" ", "A"));
    }
}