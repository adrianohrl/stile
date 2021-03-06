package tech.adrianohrl.stile.control.production.reports;

import tech.adrianohrl.stile.control.production.reports.filters.EmployeeRelatedEventsList;
import tech.adrianohrl.stile.exceptions.ReportException;
import tech.adrianohrl.stile.model.events.AbstractEmployeeRelatedEvent;
import tech.adrianohrl.stile.model.personnel.Manager;
import tech.adrianohrl.stile.model.personnel.Sector;
import tech.adrianohrl.stile.model.personnel.Subordinate;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 */
public class SectorEfficiencyReport extends AbstractEfficiencyReport {
    
    private final Sector sector;
    private final MultipleSubordinateEfficiencyReport multipleSubordinateEfficiencyReport;

    public SectorEfficiencyReport(Sector sector, List<Subordinate> subordinates, EmployeeRelatedEventsList<AbstractEmployeeRelatedEvent> events, Manager manager, Calendar startDate, Calendar endDate) throws ReportException {
        super(events, manager, startDate, endDate);
        if (sector == null) {
            throw new ReportException("The report sector must not be null!!!");
        }
        this.sector = sector;
        multipleSubordinateEfficiencyReport = new MultipleSubordinateEfficiencyReport(subordinates, events, manager, startDate, endDate);
    }

    public SectorEfficiencyReport(Sector sector, EmployeeRelatedEventsList<AbstractEmployeeRelatedEvent> events, Manager manager, Calendar startDate, Calendar endDate) throws ReportException {
        this(sector, sector.getSupervisor().getSubordinates(), events, manager, startDate, endDate);
    }

    @Override
    protected TreeMap<SeriesType, ReportNumericSeries> getSeriesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
