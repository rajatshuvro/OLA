package com.ola.parsers;

import com.ola.dataStructures.Membership;
import com.ola.dataStructures.User;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MembershipParser {
    private InputStream _inputStream;

    private final String UserIdTag = "UserId";
    private final String StartDateTag = "Start";
    private final String EndDateTag = "End";

    public MembershipParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Membership> GetMemberships() throws IOException {
        ArrayList<Membership> memberships = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                UserIdTag, StartDateTag, EndDateTag
        });

        var record =fobParser.GetNextRecord();
        while ( record != null){
            var membership = GetMembership(record);
            if (membership != null)  memberships.add(membership);

            record = fobParser.GetNextRecord();
        }
        fobParser.close();

        return memberships;
    }

    private Membership GetMembership(HashMap<String, String> record) {
        var userId = ParserUtilities.ParseUInt(record.get(UserIdTag));
        var start  = TimeUtilities.parseDate(record.get(StartDateTag));
        var end    = TimeUtilities.parseDate(record.get(EndDateTag));

        return new Membership(userId, start, end);
    }

}
