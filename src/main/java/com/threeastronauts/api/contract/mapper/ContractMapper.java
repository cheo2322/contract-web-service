package com.threeastronauts.api.contract.mapper;

import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.model.Contract;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractMapper {

  Contract contractDtoToContract(ContractDto contractDto);
}
