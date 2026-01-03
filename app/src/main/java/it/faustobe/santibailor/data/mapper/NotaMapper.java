package it.faustobe.santibailor.data.mapper;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.data.local.entities.NoteEntity;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * Mapper per convertire tra NoteEntity (data layer) e Nota (domain layer)
 * Fornisce metodi static per conversioni bidirezionali
 */
public class NotaMapper {

    /**
     * Converte da Entity (data layer) a Domain model
     * @param entity NoteEntity dal database
     * @return Nota domain model, o null se entity è null
     */
    public static Nota toDomain(NoteEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Nota(
                entity.getId(),
                entity.getTitolo(),
                entity.getContenuto(),
                entity.getDataCreazione(),
                entity.getDataModifica()
        );
    }

    /**
     * Converte da Domain model a Entity (data layer)
     * @param nota Nota domain model
     * @return NoteEntity per il database, o null se nota è null
     */
    public static NoteEntity toEntity(Nota nota) {
        if (nota == null) {
            return null;
        }

        NoteEntity entity = new NoteEntity(
                nota.getTitolo(),
                nota.getContenuto(),
                nota.getDataCreazione(),
                nota.getDataModifica()
        );

        entity.setId(nota.getId());
        return entity;
    }

    /**
     * Converte una lista di Entity in una lista di Domain models
     * @param entities Lista di NoteEntity
     * @return Lista di Nota, lista vuota se entities è null
     */
    public static List<Nota> toDomainList(List<NoteEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        List<Nota> note = new ArrayList<>();
        for (NoteEntity entity : entities) {
            Nota nota = toDomain(entity);
            if (nota != null) {
                note.add(nota);
            }
        }
        return note;
    }

    /**
     * Converte una lista di Domain models in una lista di Entity
     * @param note Lista di Nota
     * @return Lista di NoteEntity, lista vuota se note è null
     */
    public static List<NoteEntity> toEntityList(List<Nota> note) {
        if (note == null) {
            return new ArrayList<>();
        }

        List<NoteEntity> entities = new ArrayList<>();
        for (Nota nota : note) {
            NoteEntity entity = toEntity(nota);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
