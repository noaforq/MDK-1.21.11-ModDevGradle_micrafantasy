package cloud.laboratory.n.micrafantasy.job;

public enum JobType {
    NONE("none", 0),
    PALADIN("paladin", 1),
    WHITE_MAGE("white_mage", 2);

    private final String id;
    private final int numericId;

    JobType(String id, int numericId) {
        this.id = id;
        this.numericId = numericId;
    }

    public String getId() {
        return id;
    }

    public int getNumericId() {
        return numericId;
    }

    public static JobType fromId(String id) {
        for (JobType type : values()) {
            if (type.id.equals(id)) return type;
        }
        return NONE;
    }

    public static JobType fromNumericId(int id) {
        for (JobType type : values()) {
            if (type.numericId == id) return type;
        }
        return NONE;
    }
}

