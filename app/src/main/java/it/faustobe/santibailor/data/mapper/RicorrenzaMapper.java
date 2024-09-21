package it.faustobe.santibailor.data.mapper;

import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.RicorrenzaConTipoEntity;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
//import it.faustobe.santibailor.domain.model.RicorrenzaConTipo;

public class RicorrenzaMapper {

    public static Ricorrenza toDomain(RicorrenzaEntity entity) {
        if (entity == null) {
            return null; // o crea una Ricorrenza con valori di default
        }
        return new Ricorrenza(
                entity.getId(),
                entity.getIdMese(),
                entity.getGiorno(),
                entity.getNome(),
                entity.getBio(),
                entity.getImageUrl(),
                entity.getPrefix(),
                entity.getSuffix(),
                entity.getTipoRicorrenzaId()
        );
    }

    public static RicorrenzaEntity toEntity(Ricorrenza domain) {
        RicorrenzaEntity entity = new RicorrenzaEntity();
        entity.setId(domain.getId());
        entity.setIdMese(domain.getIdMese());
        entity.setGiorno(domain.getGiorno());
        entity.setNome(domain.getNome());
        entity.setBio(domain.getBio());
        entity.setImageUrl(domain.getImageUrl());
        entity.setPrefix(domain.getPrefix());
        entity.setSuffix(domain.getSuffix());
        entity.setTipoRicorrenzaId(domain.getTipoRicorrenzaId());
        return entity;
    }

    public static TipoRicorrenza toDomain(TipoRicorrenzaEntity entity) {
        if (entity == null) {
            return new TipoRicorrenza(0, "Sconosciuto");
        }
        return new TipoRicorrenza(entity.getId(), entity.getNome());
    }

    public static TipoRicorrenzaEntity toEntity(TipoRicorrenza domain) {
        return new TipoRicorrenzaEntity(domain.getId(), domain.getNome());
    }


}