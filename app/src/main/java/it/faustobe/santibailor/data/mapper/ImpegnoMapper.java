package it.faustobe.santibailor.data.mapper;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.data.local.entities.ImpegnoEntity;
import it.faustobe.santibailor.domain.model.Impegno;

/**
 * Mapper per convertire tra ImpegnoEntity (data layer) e Impegno (domain layer)
 */
public class ImpegnoMapper {

    /**
     * Converte da Entity a Domain model
     */
    public static Impegno toDomain(ImpegnoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Impegno(
                entity.getId(),
                entity.getTitolo(),
                entity.getDescrizione(),
                entity.getDataOra(),
                entity.getCategoria(),
                entity.isReminderEnabled(),
                entity.getReminderMinutesBefore(),
                entity.isCompletato(),
                entity.getNote(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPriorita(),
                entity.getImageUrl()
        );
    }

    /**
     * Converte da Domain model a Entity
     */
    public static ImpegnoEntity toEntity(Impegno impegno) {
        if (impegno == null) {
            return null;
        }

        ImpegnoEntity entity = new ImpegnoEntity(
                impegno.getTitolo(),
                impegno.getDescrizione(),
                impegno.getDataOra(),
                impegno.getCategoria(),
                impegno.isReminderEnabled(),
                impegno.getReminderMinutesBefore(),
                impegno.isCompletato(),
                impegno.getNote(),
                impegno.getCreatedAt(),
                impegno.getUpdatedAt(),
                impegno.getPriorita(),
                impegno.getImageUrl()
        );

        entity.setId(impegno.getId());
        return entity;
    }

    /**
     * Converte una lista di Entity in una lista di Domain models
     */
    public static List<Impegno> toDomainList(List<ImpegnoEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        List<Impegno> impegni = new ArrayList<>();
        for (ImpegnoEntity entity : entities) {
            impegni.add(toDomain(entity));
        }
        return impegni;
    }

    /**
     * Converte una lista di Domain models in una lista di Entity
     */
    public static List<ImpegnoEntity> toEntityList(List<Impegno> impegni) {
        if (impegni == null) {
            return new ArrayList<>();
        }

        List<ImpegnoEntity> entities = new ArrayList<>();
        for (Impegno impegno : impegni) {
            entities.add(toEntity(impegno));
        }
        return entities;
    }
}
