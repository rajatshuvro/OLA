package com.ola.unitTests.NativeSearchTests;

import com.ola.NativeSearch.InvertedIndex;
import com.ola.NativeSearch.JaroWinkler;
import com.ola.NativeSearch.SmithWaterman;
import com.ola.NativeSearch.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class PhraseRankingTests {
    private String[] GetPhrases(){
        return new String[]{
                "Mitu ekdin sisimpure",
                "Halum jabe onek dur",
                "Alur khojey",
                "Ikrir Ful",
                "Dada vai Aar Ami Tuktuki",
                "Dekhbo Ghure jogotake",
                "Goyenda shikur rohossho ovijan ",
                "Ekdin sobai meley",
                "Halumer gramey jaoa",
                "Porir deshe tuktuki",
                "Bhulomona jadukor",
                "Tuktuki jokhon boro hobe",
                "Banger dim banger chana",
                "Boishakhi melai hoi hollor",
                "Our Boat Ride Through Bangladesh",
                "Our Bus ride through Bangladesh",
                "Dhaka Nodi",
                "Thik Dukkur bela",
                "Konkaboti",
                "Tina Ar Mina",
                "Kalo Kak Ar shada Megh ",
                "Vut ki Odvut",
                "Kajla didi",
                "Pahare amar bari",
                "Ghulghuli naki choruier basa",
                "Chora chori",
                "Chobir khata",
                "Nodi nebe",
                "Shuapoka O projapoti",
                "Dhek Ar Ak",
                "Chorai chobi",
                "Tales from Bangladesh ",
                "Mitu ekdin sisimpure",
                "Sisimpure holud vut",
                "Bijh  theke thalai ",
                "Ghuri oranor mela",
                "Shotimoti",
                "Amra pari",
                "Ikri O projapoti",
                "Eigol paki kothai gelo ",
                "Panite jopath jopath",
                "Mach dhorar moja ",
                "Nanan kajer nanan nam",
                "Saladiar soshomo khabar",
                "Dhuki katbirali",
                "Pani",
                "Gach O khai",
                "Lal Murgi",
                "Dadima O ami",
                "Chotto chele Belal",
                "Phritibir sobcheye Boro pitha",
                "Dui Bon",
                "Golper Asor (1)",
                "Golper Asor",
                "Golper Asor",
                "Golper Asor (4)",
                "Golper Asor (2)",
                "Golper Asor (3)",
                "The Daughter Of Nature",
                "Dui Bon",
                "Dekchir Baccha",
                "My Picture Book of Flower",
                "My Picture Book of Animal",
                "Swapnobilasi Pakhi",
                "Chhutir Dine Dupure",
                "Hercules",
                "Ami Bhoot Hote Chai",
                "Nokol Raja",
                "Onno rokom Din",
                "Nokol Raja",
                "Rabindranath Tagore for Children - The Man from Kabul",
                "Rabindranath Tagore for Children - The Hidden Treasure",
                "Rabindranath Tagore for Children - The Real Artist",
                "Rabindranath Tagore for Children - Khoka Babu's Return",
                "Rabindranath Tagore for Children - The Tutor",
                "Rabindranath Tagore for Children - The Sudden Trouble",
                "Maayer Notun Fuldani",
                "Aamaar Aachhe",
                "Ekhon Khelar Somoy",
                "Daainir Jaadumontro",
                "Graam Aamaar Khub Bhaalo Laage",
                "Aamaar Graamer Baarhi",
                "Aamaar Graamer Baarhi",
                "Baabaa Roj Deri Kore",
                "Khorgosh Chaanaa Berhate Jaabe",
                "Khorgosh Chaanaa Berhate Jaabe",
                "Aamaar Notun Camera",
                "Aamaar Notun Camera",
                "Rabindranath Tagore for Children - Losing Jewels",
                "Rabindranath Tagore for Children - The Boy and His Tree",
                "Rabindranath Tagore for Children - The Postmaster",
                "Rabindranath Tagore for Children - The Holidays",
                "Rabindranath Tagore for Children - Wish Fulfilment",
                "Rabindranath Tagore for Children - The Exercise Book",
                "Rabindranath Tagore for Children - Keeping The Vow",
                "Rabindranath Tagore for Children - Clouds and Sunshine",
                "Rabindranath Tagore for Children - The Guardian of Property",
                "Wool-er Ball",
                "Shobai Mile Khela Kori",
                "Chhotto Murgi Chhana",
                "Hojjar Chhagol",
                "Hojja O Chor",
                "Danober Golpo",
                "Dottidanor Golpo",
                "Lal Tupi Pora Chhotto Meyeti",
                "Amar Shathe Poro 1 2 3",
                "Murgigulo Gune Raakho",
                "Jeebon Baachaano",
                "Jeebon Baachaano",
                "Meenaa Ki School Chherhe Dibe",
                "Meena Ki School Chherhe Dibe",
                "Meena O Dushtu Chhelera",
                "Chhelei Hote Hobe",
                "Jekhane sobai sheke",
                "Pani",
                "Golper Asor (1)",
                "Petuk Robot Level-4 Book-4",
                "Aami Rong Korte Paari. Level-1 Book-4",
                "Aami Rong Korte Paari. Level-1 Book-4",
                "Petuk Robot Level-4 Book-4",
                "Boloto Aaami Ke? Level-1 Book-1",
                "Bolte Paaro Ki!",
                "Boi Porhi",
                "Aamra Dujon Bondhhu",
                "Aamaar Icchaa Kore...",
                "Kaar Kaar Fotaa Achhe?",
                "Raaino Gosol Kore",
                "Milir Party",
                "Milir Party",
                "Aamio Jaabo",
                "Aamio Jaabo",
                "Hese Khele Porhi",
                "Hese Khele Porhi",
                "Rong Beronger Praani",
                "Aamaar Facebok"
        };
    }

    @Test
    public void Compare_JW_SW(){
        var jwCount = JWSearch_success_count();
        var swCount = SWSearch_success_count();
        Assertions.assertTrue(jwCount < swCount);
    }

    private int JWSearch_success_count(){
        var searchIndex = new InvertedIndex(new JaroWinkler());
        var phrases = GetPhrases();

        for (var phrase: phrases)
            searchIndex.Add(phrase);

        var count=0;
        for(var i=0; i < phrases.length; i++){
            var query = GetQuery(phrases[i]);
            var rankings = searchIndex.Search(query);

            if(rankings.length > 0 && i == rankings[0]) count++;

        }
        return count;
    }

    private int SWSearch_success_count(){
        var searchIndex = new InvertedIndex(new SmithWaterman());
        var phrases = GetPhrases();

        for (var phrase: phrases)
            searchIndex.Add(phrase);

        var count=0;
        for(var i=0; i < phrases.length; i++){
            var query = GetQuery(phrases[i]);
            var rankings = searchIndex.Search(query);

            if(rankings.length > 0 && i == rankings[0]) count++;

        }
        return count;
    }

    // generates a 2 word query with randomly inserted modifications
    private String GetQuery(String phrase) {
        var rand = new Random();
        var tokens = Utilities.GetTokens(phrase);

        if(tokens.size() ==1)
            return GetEditedString(tokens.get(0), 2);
        var i1 = rand.nextInt(tokens.size());
        var i2 = i1;
        do{
            i2 = rand.nextInt(tokens.size());
        }while (i1==i2);

        var word1 = tokens.get(i1);
        var word2 = tokens.get(i2);

        word1  = GetEditedString(word1, 1);
        word2  = GetEditedString(word2, 1);

        return word1+" "+word2;

    }

    private String GetEditedString(String s, int distance){

        for(var i=0; i < distance; i++){

            if(s.contains("h")) {
                s = s.replaceFirst("h", "");
                continue;
            }
            if(s.contains("aa")) {
                s = s.replaceFirst("aa", "a");
                continue;
            }
            if(s.contains("o")) {
                s = s.replaceFirst("o", "u");
                continue;
            }
            if(s.contains("u")) {
                s = s.replaceFirst("u", "o");
                continue;
            }
            //random deletion
            var rand = new Random();
            var index = rand.nextInt(s.length());
            s = s.replace(s.charAt(index), 'x');

        }

        return s;
    }
}
