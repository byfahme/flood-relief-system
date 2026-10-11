package com.kmct.floodrelief.matching;

/**
 * A volunteer assigned to one camp. A volunteer keeps that camp's
 * resource and need lists up to date and decides on transfers that
 * involve the camp. Volunteers do not move the goods themselves.
 */
public class Volunteer {

    private final String name;
    private final String assignedCamp;

    public Volunteer(String name, String assignedCamp) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Volunteer name is required");
        }
        if (assignedCamp == null || assignedCamp.isBlank()) {
            throw new IllegalArgumentException("Assigned camp is required");
        }
        this.name = name;
        this.assignedCamp = assignedCamp;
    }

    public String getName() {
        return name;
    }

    public String getAssignedCamp() {
        return assignedCamp;
    }

    /** A volunteer can only update the lists of their own camp. */
    public boolean canUpdate(String camp) {
        return assignedCamp.equals(camp);
    }

    /** A volunteer decides on any transfer that leaves or arrives at their camp. */
    public boolean canDecide(Transfer transfer) {
        return assignedCamp.equals(transfer.fromCamp()) || assignedCamp.equals(transfer.toCamp());
    }
}