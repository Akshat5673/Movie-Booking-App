package com.demo.MovieBookingApp.adapter;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class GenericEntityDtoAdapter {
    private ModelMapper mapper = new ModelMapper();

    public static <T, U> T toEntityObject(U dto, Class<T> entityClass) {
        return mapper.map(dto, entityClass);
    }

    public static <T, U> U toDtoObject(T entity, Class<U> dtoClass) {
        return mapper.map(entity, dtoClass);
    }

    public static <T, U> List<U> toDtoList(List<T> entityList, Class<U> dtoClass) {
        return entityList.stream().map(entity -> toDtoObject(entity, dtoClass)).toList();
    }
}
