package com.example.Project.mapper;
import com.example.Project.dto.PlayerDTO;
import com.example.Project.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    //@Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "wins", source = "wins")
    @Mapping(target = "age", source = "age")
    PlayerDTO mapWithoutId(com.example.Project.entity.Player player);

    //@Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "wins", target = "wins")
    @Mapping(source = "age", target = "age")
    Player mapGenerated(generated.Player generated);
}
