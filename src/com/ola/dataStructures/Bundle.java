package com.ola.dataStructures;

import com.ola.utilities.TimeUtilities;

import java.util.Date;

public class Bundle {
    public final String Id;
    public final String Description;
    public final String[] BookIds;
    public final Date EntryDate;

    public Bundle(String id, String description, String[] bookIds, Date entryDate){
        Id = id;
        Description = description;
        BookIds = bookIds;
        EntryDate = entryDate;
    }

    @Override
    public String toString(){
        return
                        "Bundle Id:        "+ Id+'\n'+
                        "Description:      "+ Description+'\n'+
                        "Book ids:         "+ String.join(", ",BookIds)+'\n'+
                        "Date:             "+ TimeUtilities.ToString(EntryDate);

    }
}
