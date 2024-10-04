package it.faustobe.santibailor.data.repository;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.faustobe.santibailor.data.local.dao.SearchDao;
import it.faustobe.santibailor.domain.model.SearchResult;

@Singleton
public class SearchRepository {
    private final SearchDao searchDao;

    @Inject
    public SearchRepository(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public List<SearchResult> searchEntities(String query, int limit, int offset) {
        String sqlQuery = "SELECT id, 'Ricorrenza' as type, santo as content FROM santi WHERE santo LIKE ? OR bio LIKE ? " +
                "ORDER BY content ASC LIMIT ? OFFSET ?";

        SupportSQLiteQuery supportQuery = new SimpleSQLiteQuery(sqlQuery,
                new Object[]{"%" + query + "%", "%" + query + "%", limit, offset});

        return searchDao.search(supportQuery);
    }

    public int getTotalSearchResultsCount(String query) {
        return searchDao.getSearchResultsCount("%" + query + "%");
    }
}
