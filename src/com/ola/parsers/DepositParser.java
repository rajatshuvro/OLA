package com.ola.parsers;

import com.ola.dataStructures.Deposit;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DepositParser {
    private InputStream _inputStream;

    private final String UserIdTag = "UserId";
    private final String DateTag = "Date";
    private final String AmountTag = "Amount";
    private final String TypeTag = "Type";

    public DepositParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Deposit> GetDeposits() throws IOException {
        ArrayList<Deposit> deposits = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                UserIdTag, AmountTag, DateTag, TypeTag
        });

        var record =fobParser.GetNextRecord();
        while ( record != null){
            var membership = GetMembership(record);
            if (membership != null)  deposits.add(membership);

            record = fobParser.GetNextRecord();
        }
        fobParser.close();

        return deposits;
    }

    private Deposit GetMembership(HashMap<String, String> record) {
        var userId = ParserUtilities.ParseUInt(record.get(UserIdTag));
        var date  = TimeUtilities.parseDateTime(record.get(DateTag));
        var amount = ParserUtilities.ParseUFloat(record.get(AmountTag));
        var type = record.get(TypeTag);

        if(Deposit.IsValid(userId, amount, date, type))
            return new Deposit(userId, amount, date, type);
        return null;
    }

}
