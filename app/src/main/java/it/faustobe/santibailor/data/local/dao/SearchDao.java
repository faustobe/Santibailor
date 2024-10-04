package it.faustobe.santibailor.data.local.dao;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.room.Query;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import it.faustobe.santibailor.domain.model.SearchResult;

@Dao
public interface SearchDao {
    @RawQuery
    List<SearchResult> search(SupportSQLiteQuery query);

    @Query("SELECT COUNT(*) FROM santi WHERE santo LIKE :query OR bio LIKE :query")
    int getSearchResultsCount(String query);
}