package it.faustobe.santibailor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;

@Dao
public interface RicorrenzaDao extends BaseDao<RicorrenzaEntity> {

    @Query("SELECT * FROM santi")
    List<RicorrenzaEntity> getAllRicorrenze();

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    LiveData<List<RicorrenzaEntity>> getRicorrenzePerGiornoMeseLiveData(int giorno, int mese);

    @Query("UPDATE santi SET image_url = :imageUrl WHERE id = :ricorrenzaId")
    void updateImageUrl(int ricorrenzaId, String imageUrl);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese ORDER BY RANDOM()")
    LiveData<List<RicorrenzaEntity>> getRicorrenzeDelGiorno(int giorno, int mese);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese AND tipo_ricorrenza_id = :tipoId")
    LiveData<List<RicorrenzaEntity>> getRicorrenzeDelGiornoPerTipo(int giorno, int mese, int tipoId);

    @Query("SELECT * FROM santi WHERE id = :id")
    LiveData<RicorrenzaEntity> getRicorrenzaById(int id);

    @Query("SELECT * FROM santi WHERE santo LIKE :nome")
    LiveData<List<RicorrenzaEntity>> cercaRicorrenzePerNome(String nome);

    @Query("SELECT COUNT(*) FROM santi")
    int getTotalItemCount();

    @Query("SELECT * FROM santi WHERE " +
            "(:nome IS NULL OR santo LIKE '%' || :nome || '%') " +
            "AND (:tipo IS NULL OR tipo_ricorrenza_id = :tipo) " +
            "AND (:meseInizio IS NULL OR id_mesi >= :meseInizio) " +
            "AND (:meseFine IS NULL OR id_mesi <= :meseFine) " +
            "AND (:giornoInizio IS NULL OR giorno >= :giornoInizio) " +
            "AND (:giornoFine IS NULL OR giorno <= :giornoFine)")
    LiveData<List<RicorrenzaEntity>> ricercaAvanzata(String nome, Integer tipo,
                                                     Integer meseInizio, Integer meseFine,
                                                     Integer giornoInizio, Integer giornoFine);

    @Query("SELECT * FROM santi WHERE " +
            "(:nome IS NULL OR santo LIKE '%' || :nome || '%') " +
            "AND (:tipo IS NULL OR tipo_ricorrenza_id = :tipo) " +
            "AND (:meseInizio IS NULL OR id_mesi > :meseInizio OR (id_mesi = :meseInizio AND giorno >= :giornoInizio)) " +
            "AND (:meseFine IS NULL OR id_mesi < :meseFine OR (id_mesi = :meseFine AND giorno <= :giornoFine)) " +
            "ORDER BY id_mesi, giorno " +
            "LIMIT :limit OFFSET :offset")
    List<RicorrenzaEntity> ricercaAvanzataPaginata(String nome, Integer tipo,
                                                   Integer meseInizio, Integer meseFine,
                                                   Integer giornoInizio, Integer giornoFine,
                                                   int limit, int offset);

    @Query("SELECT COUNT(*) FROM santi WHERE " +
            "(:nome IS NULL OR santo LIKE '%' || :nome || '%') " +
            "AND (:tipo IS NULL OR tipo_ricorrenza_id = :tipo) " +
            "AND (:meseInizio IS NULL OR id_mesi > :meseInizio OR (id_mesi = :meseInizio AND giorno >= :giornoInizio)) " +
            "AND (:meseFine IS NULL OR id_mesi < :meseFine OR (id_mesi = :meseFine AND giorno <= :giornoFine))")
    int contaRisultatiRicercaAvanzata(String nome, Integer tipo,
                                      Integer meseInizio, Integer meseFine,
                                      Integer giornoInizio, Integer giornoFine);

    @Query("SELECT * FROM santi ORDER BY id_mesi, giorno")
    LiveData<List<RicorrenzaEntity>> getAllRicorrenzeOrdered();

    @RawQuery
    List<RicorrenzaEntity> eseguiRicercaAvanzata(SupportSQLiteQuery query);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese ORDER BY RANDOM() LIMIT :limit OFFSET :offset")
    List<RicorrenzaEntity> getRicorrenzeDelGiornoPaginate(int giorno, int mese, int offset, int limit);

    @Query("SELECT COUNT(*) FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    int getCountRicorrenzeDelGiorno(int giorno, int mese);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese AND tipo_ricorrenza_id = :tipoId LIMIT :limit OFFSET :offset")
    List<RicorrenzaEntity> getRicorrenzeDelGiornoPerTipoPaginate(int giorno, int mese, int tipoId, int offset, int limit);

    @Query("SELECT COUNT(*) FROM santi WHERE giorno = :giorno AND id_mesi = :mese AND tipo_ricorrenza_id = :tipoId")
    int getCountRicorrenzeDelGiornoPerTipo(int giorno, int mese, int tipoId);

    // Metodi per debug
    @Query("SELECT COUNT(*) FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    int contaRicorrenzePerGiornoMese(int giorno, int mese);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    List<RicorrenzaEntity> getRicorrenzePerGiornoMese(int giorno, int mese);

    @Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    List<RicorrenzaEntity> debugRicorrenzeDelGiorno(int giorno, int mese);
}