package it.faustobe.santibailor.data.local.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {
    @Insert
    long insert(T entity);

    @Update
    int update(T entity);

    @Delete
    int delete(T entity);
}
