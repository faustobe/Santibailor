package it.faustobe.santibailor.data.mapper;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.data.local.entities.ItemSpesaEntity;
import it.faustobe.santibailor.domain.model.ItemSpesa;

public class ItemSpesaMapper {

    public static ItemSpesa toDomain(ItemSpesaEntity entity) {
        if (entity == null) return null;

        return new ItemSpesa(
            entity.getId(),
            entity.getIdLista(),
            entity.getNome(),
            entity.getQuantita(),
            entity.isCompletato(),
            entity.getCategoria(),
            entity.getNote(),
            entity.getOrdine()
        );
    }

    public static ItemSpesaEntity toEntity(ItemSpesa domain) {
        if (domain == null) return null;

        ItemSpesaEntity entity = new ItemSpesaEntity();
        entity.setId(domain.getId());
        entity.setIdLista(domain.getIdLista());
        entity.setNome(domain.getNome());
        entity.setQuantita(domain.getQuantita());
        entity.setCompletato(domain.isCompletato());
        entity.setCategoria(domain.getCategoria());
        entity.setNote(domain.getNote());
        entity.setOrdine(domain.getOrdine());
        return entity;
    }

    public static List<ItemSpesa> toDomainList(List<ItemSpesaEntity> entities) {
        if (entities == null) return new ArrayList<>();

        List<ItemSpesa> result = new ArrayList<>();
        for (ItemSpesaEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }

    public static List<ItemSpesaEntity> toEntityList(List<ItemSpesa> domains) {
        if (domains == null) return new ArrayList<>();

        List<ItemSpesaEntity> result = new ArrayList<>();
        for (ItemSpesa domain : domains) {
            result.add(toEntity(domain));
        }
        return result;
    }
}
