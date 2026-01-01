package it.faustobe.santibailor.data.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.faustobe.santibailor.data.local.entities.ListaSpesaEntity;
import it.faustobe.santibailor.domain.model.ListaSpesa;

public class ListaSpesaMapper {

    public static ListaSpesa toDomain(ListaSpesaEntity entity) {
        if (entity == null) return null;

        return new ListaSpesa(
            entity.getId(),
            entity.getNome(),
            new Date(entity.getDataCreazione()),
            entity.isCompletata(),
            entity.getColore()
        );
    }

    public static ListaSpesaEntity toEntity(ListaSpesa domain) {
        if (domain == null) return null;

        ListaSpesaEntity entity = new ListaSpesaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDataCreazione(domain.getDataCreazione() != null ? domain.getDataCreazione().getTime() : System.currentTimeMillis());
        entity.setCompletata(domain.isCompletata());
        entity.setColore(domain.getColore());
        return entity;
    }

    public static List<ListaSpesa> toDomainList(List<ListaSpesaEntity> entities) {
        if (entities == null) return new ArrayList<>();

        List<ListaSpesa> result = new ArrayList<>();
        for (ListaSpesaEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }

    public static List<ListaSpesaEntity> toEntityList(List<ListaSpesa> domains) {
        if (domains == null) return new ArrayList<>();

        List<ListaSpesaEntity> result = new ArrayList<>();
        for (ListaSpesa domain : domains) {
            result.add(toEntity(domain));
        }
        return result;
    }
}
