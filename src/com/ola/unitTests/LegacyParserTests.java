package com.ola.unitTests;
import com.ola.parsers.BookParser;
import com.ola.parsers.LegacyBookParser;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyParserTests {
    public static InputStream GetBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("ISBN\tTitle\tAnnotation\tAR_Level\tAR_Points\tAuthor\tBinding\tGrade_Level\tIllustrator\tInterest_Level_Minimum\tInterest_Level_Maximum\tLexile_0-1200\tList_Price\tFiction_Nonfiction_F_NF\tPage_Count\tPublication_Date\tPublisher\tReading_Level\tReading_Recovery_Level\tSpanish_Y_N\tTeachers_College\tWord_Count\tGuided_Reading_Level\tLocation\tGenre\tCondition\tUnique_ID\n");
        writer.write("9848494226\tGattu Baajaar Korte Jaay\t\t\t\tLorraine Li\t          \t\t\t\t\t\t5.0000\tF\t16\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \tGeneral\tGood\t318028\n");
        writer.write("9848494336\tChhotto Murgi Chhaanaa\t\t\t\tSaamsi Haasaan\t          \t\t\t\t\t\t5.0000\t\t15\t\tWorld of Children's Books Ltd \t4\t\t\t\t\t\t - None - \tGeneral\tNew\t428013\n");
        writer.write("9848494944\tDaainir Jaadumontro\t\t\t\tVon Oi Chin\t          \t\t\t\t\t\t5.0000\tF\t12\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \t - None - \tGood\t318055\n");
        writer.write("9848494944\tDaainir Jaadumontro\t\t\t\tVon Oi Chin\t          \t\t\t\t\t\t5.0000\tF\t12\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \t - None - \t - None - \t3204\n");
        writer.write("\tTusir Aamgaachh\t\t\t\tLimiyaa Dewaan\t          \t\t\t\t\t\t10.0000\tF\t16\t\tBrac\t3\t\tN\t\t\t\t - None - \t - None - \tNew\t308018\n");
        writer.write("9789849125167\tAamraa khelaa kori\t\t\t\tNirapod Sorkaar\t          \t\t\t\t\t\t5.0000\t\t12\t\tBoi Porhi\t2\t\t\t\t\t\t - None - \t - None - \tNew\t228033\n");
        writer.write("9789849125167\tAamraa khelaa kori\t\t\t\tNirapod Sorkaar\t          \t\t\t\t\t\t5.0000\t\t12\t\tBoi Porhi\t2\t\t\t\t\t\t - None - \t - None - \tNew\t228034\n");
        writer.write("9789849113768\tAamaar Prothom Bornomaalaa\t\t\t\tJugol Sorkaar\t          \t\t\t\t\t\t\tNF\t\t\t\t\t\tY\t\t\tNone\t - None - \t - None - \tGood\t118030\n");
        writer.write("9841900149\tBaagher Paalki Chorhaa\t\t\t\tUpendrokishor Ray Chowdhury\t          \t\t\t\t\t\t10.0000\tF\t32\t\tBrac\t5\t\tN\t\t\t\t - None - \t - None - \tNew\t508341\n");
        writer.write("9841900149\tBaagher Paalki Chorhaa\t\t\t\tUpendrokishor Ray Chowdhury\t          \t\t\t\t\t\t10.0000\tF\t32\t\tBrac\t5\t\tN\t\t\t\t - None - \t - None - \tNew\t508340\n");
        writer.write("9841900173\tGraamer Naam Chougaachhee\t\t\t\tAabu Saaleh\t          \t\t\t\t\t\t5.0000\tF\t16\t\tBrac\t2\t\tN\t\t\t\t - None - \t - None - \tNew\t204304\n");
        writer.write("9841900173\tGraamer Naam Chougaachhee\t\t\t\tAabu Saaleh\t          \t\t\t\t\t\t5.0000\tF\t16\t\tBrac\t2\t\tN\t\t\t\t - None - \t - None - \tNew\t204302\n");
        writer.write("\tGolper Asor-4\t\t\t\t\t          \t\t\t\t\t\t10.0000\tF\t20\t\tBrac\t4\t\tN\t\t\t\t - None - \t - None - \t - None - \t408014\n");
        writer.write("8943000005498\tShebaar Raani O Hudhud Paakhi\t\t\t\tSaniyasnain Khan    \t          \t\t\t\t\t\t5.0000\tNF\t24\t\tBangla Prokaash\t5\t\tN\t\t\t\t - None - \tReligious\tGood\t511024\n");
        writer.write("\tMitur Shopno\t\t\t\tTranslated\t          \t\t\t\t\t\t10.0000\tF\t14\t\tBrac\t3\t\tN\t\t\t\t - None - \t - None - \tNew\t308402\n");
        writer.write("9848494872\tBaabaa Roj Deri Kore\t\t\t\tVon Oi Chin\t          \t\t\t\t\t\t5.0000\tF\t12\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \tGeneral\tGood\t318047\n");
        writer.write("\tLaau Gorh Gorh Gaarhi\t\t\t\t\t          \t\t\t\t\t\t5.0000\tF\t10\t\tBrac\t2\t\tN\t\t\t\t - None - \t - None - \tNew\t208349\n");
        writer.write("\tAadibaaseeder Peshaa\t\t\t\t\t          \t\t\t\t\t\t5.0000\tF\t10\t\tBrac\t2\t\tN\t\t\t\t - None - \t - None - \tNew\t204504\n");
        writer.write("9848494108\tAami Aar Baabaa Cake Baanaai Level-3 Book-1\t\t\t\tLorraine Li\t          \t\t\t\t\t\t5.0000\tF\t16\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \tGeneral\tGood\t318026\n");
        writer.write("9848494336\tChhotto Murgi Chhaanaa\t\t\t\tSaamsi Haasaan\t          \t\t\t\t\t\t5.0000\t\t15\t\tWorld of Children's Books Ltd \t4\t\t\t\t\t\t - None - \tGeneral\tNew\t428016\n");
        writer.write("9843000005344\tPiprher Aatongko\t\t\t\tSaniyasnain Khan\t          \t\t\t\t\t\t5.0000\tNF\t24\t\tBangla Prokaash\t5\t\tN\t\t\t\t - None - \tReligious\tGood\t511033\n");
        writer.write("9841900963\tIkrir Ful\t\t\t\tM. Aaslaam Liton\t          \t\t\t\t\t\t10.0000\tF\t16\t\tBrac\t4\t\tN\t\t\t\t - None - \t - None - \tNew\t408401\n");
        writer.write("9789383202379\tKrishna\t\t\t\t\t          \t\t\t\t\t\t5.0000\tNF\t\t\t\t1\t\tN\t\t\t\t - None - \t - None - \tGood\t111001\n");
        writer.write("\tMaamaa O Aami Haate Jaai\t\t\t\t\t          \t\t\t\t\t\t5.0000\tF\t8\t\tBrac\t2\t\tN\t\t\t\t - None - \t - None - \tNew\t208423\n");
        writer.write("9848494901\tAamaar Icchaa Kore...\t\t\t\tVon Oi Chin\t          \t\t\t\t\t\t5.0000\tF\t12\t\tWorld's of Children's Books Limited\t3\t\tN\t\t\t\t - None - \t - None - \tGood\t318051\n");
        writer.write("9789843412300\tEso PoshuPaakhi Chini\t\t\t\tSaabrinaa Islaam\t          \t\t\t\t\t\t5.0000\tF\t\t\tIgnite Publications Limited\t1\t\tN\t\t\t\t - None - \t - None - \tGood\t118019\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void ParseBook() throws IOException{
        var bookParser = new LegacyBookParser(GetBooksStream());
        var books = bookParser.GetBooks();
        var count = books.size();
        bookParser.Close();
        assertEquals(19, count);
        assertNull(books.get(1).ExpiryDate);
    }
}
