package tech.adrianohrl.stile.control.production.reports;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 */
public enum EfficiencySeriesTypes implements SeriesType<EfficiencySeriesTypes> {
    
    EFFECTIVE_DURATION("Effective Duration"),
    EXPECTED_DURATION("Expected Duration"),
    FREE_DURATION("Free Duration"),
    TOTAL_DURATION("Total Duration"),
    PRODUCED_QUANTITY("Produced Quantity"),
    RETURNED_QUANTITY("Returned Quantity"),
    EFFECTIVE_EFFICIENCY("Effective Efficiency"),
    TOTAL_EFFICIENCY("Total Efficiency");

    private final String name;    

    private EfficiencySeriesTypes(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(String name) {
        return this.name.equalsIgnoreCase(name);
    }
    
}
