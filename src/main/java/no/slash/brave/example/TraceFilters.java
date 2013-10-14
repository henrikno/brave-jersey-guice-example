package no.slash.brave.example;

import com.github.kristofa.brave.TraceFilter;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public class TraceFilters {
    private final List<TraceFilter> filters;

    public TraceFilters(final List<TraceFilter> traceFilters) {
        Validate.notNull(traceFilters);
        filters = new ArrayList<TraceFilter>(traceFilters.size());
        filters.addAll(traceFilters);
    }

    public List<TraceFilter> getTraceFilters() {
        return filters;
    }
}
