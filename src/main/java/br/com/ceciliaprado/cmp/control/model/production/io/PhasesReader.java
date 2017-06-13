/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.model.production.io;

import br.com.ceciliaprado.cmp.exceptions.IOException;
import br.com.ceciliaprado.cmp.model.personnel.Sector;
import br.com.ceciliaprado.cmp.model.production.Phase;
import br.com.ceciliaprado.cmp.util.AbstractReader;
import br.com.ceciliaprado.cmp.util.Field;
import br.com.ceciliaprado.cmp.util.StringField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrianohrl
 */
public class PhasesReader extends AbstractReader<Phase> {
    
    /** Column Titles **/
    private final static String NAME_COLUMN_TITLE = "Name";
    private final static String SECTOR_COLUMN_TITLE = "Sector";

    @Override
    protected List<Field> getDefaultFields() {
        List<Field> defaultFields = new ArrayList<>();
        defaultFields.add(new StringField(NAME_COLUMN_TITLE, true));
        defaultFields.add(new StringField(SECTOR_COLUMN_TITLE, true));
        return defaultFields;
    }

    @Override
    protected Phase build(List<Field> fields) throws IOException {
        String sectorName = Field.getFieldValue(fields, SECTOR_COLUMN_TITLE);
        Sector sector = getSector(sectorName);
        if (sector == null) {
            throw  new IOException(sectorName + " sector is not registered yet!!!");
        }
        return new Phase(Field.getFieldValue(fields, NAME_COLUMN_TITLE), sector);
    }
    
    protected Sector getSector(String sectorName) {
        return new Sector(sectorName, null);
    }
    
}