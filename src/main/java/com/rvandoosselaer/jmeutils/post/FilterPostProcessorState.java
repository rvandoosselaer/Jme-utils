package com.rvandoosselaer.jmeutils.post;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An appstate that handles the lifecycle of a FilterPostProcessor. When the FilterPostProcessor is initialized, the
 * num samples are set based on the settings of the context.
 * The state manages the filters with a backing arraylist since the order of Filters is important. Filters can be added
 * or removed in a specific order. When the backing list is changed, the filters will be detached and reattached in order.
 *
 * @author: rvandoosselaer
 */
@Slf4j
public class FilterPostProcessorState extends BaseAppState {

    @Getter
    private FilterPostProcessor fpp;
    private boolean invalid = false;
    private final List<Filter> filters = new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        fpp = new FilterPostProcessor(app.getAssetManager());

        int samples = getSamples();
        if (samples > 0) {
            log.trace("Setting samples to {} on {}", samples, fpp);
            fpp.setNumSamples(samples);
        }

        app.getViewPort().addProcessor(fpp);
    }

    @Override
    protected void cleanup(Application app) {
        app.getViewPort().removeProcessor(fpp);
        filters.clear();
    }

    @Override
    protected void onEnable() {
        attachFilters();
    }

    @Override
    protected void onDisable() {
        detachFilters();
    }

    @Override
    public void update(float tpf) {
        if (isInvalid()) {
            detachFilters();
            attachFilters();
            validate();
        }
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
        invalidate();
    }

    public void addFilter(int index, Filter filter) {
        filters.add(index, filter);
        invalidate();
    }

    public void removeFilter(Filter filter) {
        filters.remove(filter);
        invalidate();
    }

    public int getIndexOf(Filter filter) {
        return filters.indexOf(filter);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getFilter(Class<T> filterType) {
        return (Optional<T>) filters.stream()
                .filter(filter -> filter.getClass().isAssignableFrom(filterType))
                .findFirst();
    }

    private void attachFilters() {
        if (filters.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder("Attaching filters:");
        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            fpp.addFilter(filter);
            sb.append("\n").append(i).append(". ").append(StringUtils.isEmpty(filter.getName()) ? filter : filter.getName());
        }
        log.debug(sb.toString());
    }

    private void detachFilters() {
        if (filters.isEmpty()) {
            return;
        }

        boolean shouldLog = false;
        StringBuilder sb = new StringBuilder("Removing filters:");
        for (int i = filters.size() - 1; i >= 0; i--) {
            Filter filter = filters.get(i);
            if (fpp.getFilterList().contains(filter)) {
                fpp.removeFilter(filter);
                sb.append("\n").append(i).append(". ").append(StringUtils.isEmpty(filter.getName()) ? filter : filter.getName());
                shouldLog = true;
            }
        }
        if (shouldLog) {
            log.debug(sb.toString());
        }
    }

    private int getSamples() {
        return getApplication().getContext().getSettings().getSamples();
    }

    private void invalidate() {
        invalid = true;
    }

    private void validate() {
        invalid = false;
    }

    private boolean isInvalid() {
        return invalid;
    }

}
