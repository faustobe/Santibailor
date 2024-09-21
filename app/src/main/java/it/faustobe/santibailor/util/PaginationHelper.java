package it.faustobe.santibailor.util;

import java.util.ArrayList;
import java.util.List;

public class PaginationHelper<T> {
    private final int pageSize;
    private int currentPage;
    private int totalCount;  // Manteniamo solo questo campo per il conteggio totale
    private List<T> currentPageItems;

    public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = 0;
        this.totalCount = 0;
        this.currentPageItems = new ArrayList<>();
    }

    public interface DataLoader<T> {
        List<T> loadData(int offset, int limit);
        int getTotalCount();
    }

    public void loadNextPage(DataLoader<T> dataLoader) {
        int offset = currentPage * pageSize;
        currentPageItems = dataLoader.loadData(offset, pageSize);
        totalCount = dataLoader.getTotalCount();
        currentPage++;
    }

    public boolean hasNextPage() {
        return currentPage * pageSize < totalCount;
    }

    public List<T> getCurrentPageItems() {
        return currentPageItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public void reset() {
        currentPage = 0;
        totalCount = 0;
        currentPageItems.clear();
    }
}
