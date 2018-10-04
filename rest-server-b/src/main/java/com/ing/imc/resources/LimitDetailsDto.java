package com.ing.imc.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitDetailsDto {

    @NotNull
    private String ownerId;
    @NotNull
    private String contactDetails;
    @NotNull
    private Integer limitAmount;

}
