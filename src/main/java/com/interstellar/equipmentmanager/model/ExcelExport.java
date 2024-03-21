package com.interstellar.equipmentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExcelExport {
    private String fileName;
    private byte[] export;
}
