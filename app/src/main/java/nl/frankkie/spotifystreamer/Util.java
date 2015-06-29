package nl.frankkie.spotifystreamer;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by FrankkieNL on 20-6-2015.
 */
public class Util {

    /*
    The Spotify API call, 'top-tracks' requires a ISO 3166-1 alpha-2 country code
    https://developer.spotify.com/web-api/get-artists-top-tracks/
    List of Codes
    https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
    List of counties Spotify Operates in:
    https://www.spotify.com/nl/legal/end-user-agreement/plain/#s19
    https://www.spotify.com/nl/about-us/contact/
    EU is not included in this list, as it generates this error:
    {
       "error": {
         "status": 400,
         "message": "Invalid country code"
       }
    }
    Only the officially assigned codes are in the list below.
    And only the countries that Spotify operates in.
    */
    public static String[][] iso3166_1_alpha_2_countryCodes_listedLegal = new String[][]{
            {"AD", "Andorra"},
            {"AT", "Austria"},
            {"AU", "Australia"},
            {"BE", "Belgium"},
            {"CH","Switzerland"},
            {"DE","Germany"},
            {"DK","Denmark"},
            {"ES","Spain"},
            {"FI","Finland"},
            {"FR","France"},
            {"GB","United Kingdom of Great Britain and Northern Ireland"},
            {"HK","Hong Kong"},
            {"IE","Ireland"},
            {"IT","Italy"},
            {"LI","Liechtenstein"},
            {"LU","Luxembourg"},
            {"MC","Monaco"},
            {"MX","Mexico"},
            {"NL","Netherlands"},
            {"NO","Norway"},
            {"NZ","New Zealand"},
            {"PL","Poland"},
            {"SE","Sweden"},
            {"SG","Singapore"},
            {"US","United States of America"},
    };

    public static String[][] iso3166_1_alpha_2_countryCodes = new String[][]{
            //A
            {"AD", "Andorra"},
            {"AE", "United Arab Emirates"},
            {"AF", "Afghanistan"},
            {"AG", "Antigua and Barbuda"},
            {"AI", "Anguilla"},
            {"AL", "Albania"},
            {"AM", "Armenia"},
            {"AO", "Angola"},
            {"AQ", "Antarctica"},
            {"AR", "Argentina"},
            {"AS", "American Samoa"},
            {"AT", "Austria"},
            {"AU", "Australia"},
            {"AW", "Aruba"},
            {"AX", "Åland Islands"},
            {"AZ", "Azerbaijan"},
            //B
            {"BA", "Bosnia and Herzegovina"},
            {"BB", "Barbados"},
            {"BD", "Bangladesh"},
            {"BE", "Belgium"},
            {"BF", "Burkina Faso"},
            {"BG", "Bulgaria"},
            {"BH", "Bahrain"},
            {"BI", "Burundi"},
            {"BJ", "Benin"},
            {"BL", "Saint Barthélemy"},
            {"BM", "Bermuda"},
            {"BN", "Brunei Darussalam"},
            {"BO", "Bolivia, Plurinational State of"},
            {"BQ", "Bonaire, Sint Eustatius and Saba"},
            {"BR", "Brazil"},
            {"BS", "Bahamas"},
            {"BT", "Bhutan"},
            {"BV", "Bouvet Island"},
            {"BW", "Botswana"},
            {"BY", "Belarus"},
            {"BZ", "Belize"},
            //C
            {"CA","Canada"},
            {"CC","Cocos (Keeling) Islands"},
            {"CD","Congo, the Democratic Republic of"},
            {"CF","Central African Republic"},
            {"CG","Congo"},
            {"CH","Switzerland"},
            {"CI","Côte d'Ivoire"},
            {"CK","Cook Islands"},
            {"CL","Chile"},
            {"CM","Cameroon"},
            {"CN","China"},
            {"CO","Colombia"},
            {"CR","Costa Rica"},
            {"CU","Cuba"},
            {"CV","Cabo Verde"},
            {"CW","Curaçao"},
            {"CX","Christmas Island"},
            {"CY","Cyprus"},
            {"CZ","Czech Republic"},
            //D
            {"DE","Germany"},
            {"DJ","Djibouti"},
            {"DK","Denmark"},
            {"DM","Dominica"},
            {"DO","Dominican Republic"},
            {"DZ","Algeria"},
            //E
            {"EC","Ecuador"},
            {"EE","Estonia"},
            {"EG","Egypt"},
            {"EH","Western Sahara"},
            {"ER","Eritrea"},
            {"ES","Spain"},
            {"ET","Ethiopia"},
            //F
            {"FI","Finland"},
            {"FJ","Fiji"},
            {"FK","Falkland Islands (Malvinas)"},
            {"FM","Micronesia, Federated States of"},
            {"FO","Faroe Islands"},
            {"FR","France"},
            //G
            {"GA","Gabon"},
            {"GB","United Kingdom of Great Britain and Northern Ireland"},
            {"GD","Grenada"},
            {"GE","Georgia"},
            {"GF","French Guiana"},
            {"GG","Guernsey"},
            {"GH","Ghana"},
            {"GI","Gibraltar"},
            {"GL","Greenland"},
            {"GM","Gambia"},
            {"GN","Guinea"},
            {"GP","Guadeloupe"},
            {"GQ","Equatorial Guinea"},
            {"GR","Greece"},
            {"GS","South Georgia and the South Sandwich Islands"},
            {"GT","Guatemala"},
            {"GU","Guam"},
            {"GW","Guinea-Bissau"},
            {"GY","Guyana"},
            //H
            {"HK","Hong Kong"},
            {"HM","Heard Island and McDonalds Islands"},
            {"HN","Honduras"},
            {"HR","Croatia"},
            {"HT","Haiti"},
            {"HU","Hungary"},
            //I
            {"ID","Indonesia"},
            {"IE","Ireland"},
            {"IL","Israel"},
            {"IM","Isle of Man"},
            {"IN","India"},
            {"IO","British Indian Ocean Territory"},
            {"IQ","Iraq"},
            {"IR","Iran, Islamic Republic of"},
            {"IS","Iceland"},
            {"IT","Italy"},
            //J
            {"JE","Jersey"},
            {"JM","Jamaica"},
            {"JO","Jordan"},
            {"JP","Japan"},
            //K
            {"KE","Kenya"},
            {"KG","Kyrgyzstan"},
            {"KH","Cambodia"},
            {"KI","Kiribati"},
            {"KM","Comoros"},
            {"KN","Saint Kitts and Nevis"},
            {"KP","Korea, Democratic People's Republic of"},
            {"KR","Korea, Republic of"},
            {"KW","Kuwait"},
            {"KY","Cayman Islands"},
            {"KZ","Kazakhstan"},
            //L
            {"LA","Lao People's Democratic Republic"},
            {"LB","Lebanon"},
            {"LC","Saint Lucia"},
            {"LI","Liechtenstein"},
            {"LK","Sri Lanka"},
            {"LR","Liberia"},
            {"LS","Lesotho"},
            {"LT","Lithuania"},
            {"LU","Luxembourg"},
            {"LV","Latvia"},
            //M
            {"MA","Morocco"},
            {"MC","Monaco"},
            {"MD","Moldova, Republic of"},
            {"ME","Montenegro"},
            {"MF","Saint Martin (French part)"},
            {"MG","Madagascar"},
            {"MH","Marshall Islands"},
            {"MK","Macedonia, the former Yugoslav Republic of"},
            {"ML","Mali"},
            {"MM","Myanmar"},
            {"MN","Mongolia"},
            {"MO","Macao"},
            {"MP","Northern Mariana Islands"},
            {"MQ","Martinique"},
            {"MR","Mauritania"},
            {"MS","Montserrat"},
            {"MT","Malta"},
            {"MU","Mauritius"},
            {"MV","Maldives"},
            {"MW","Malawi"},
            {"MX","Mexico"},
            {"MY","Malaysia"},
            {"MZ","Mozambique"},
            //N
            {"NA","Namibia"},
            {"NC","New Caledonia"},
            {"NE","Niger"},
            {"NF","Norfolk Island"},
            {"NG","Nigeria"},
            {"NI","Nicaragua"},
            {"NL","Netherlands"},
            {"NO","Norway"},
            {"NP","Nepal"},
            {"NR","Nauru"},
            {"NU","Niue"},
            {"NZ","New Zealand"},
            //O
            {"OM","Oman"},
            //P
            {"PA","Panama"},
            {"PE","Peru"},
            {"PF","French Polynesia"},
            {"PG","Papua New Guinea"},
            {"PH","Philippines"},
            {"PK","Pakistan"},
            {"PL","Poland"},
            {"PM","Saint Pierre and Miquelon"},
            {"PN","Pitcairn"},
            {"PR","Puerto Rico"},
            {"PS","Palestine, State of"},
            {"PT","Portugal"},
            {"PW","Palau"},
            {"PY","Paraguay"},
            //Q
            {"QA","Qatar"},
            //R
            {"RE","Réunion"},
            {"RO","Romania"},
            {"RS","Serbia"},
            {"RU","Russian Federation"},
            {"RW","Rwanda"},
            //S
            {"SA","Saudi Arabia"},
            {"SB","Solomon Islands"},
            {"SC","Seychelles"},
            {"SD","Sudan"},
            {"SE","Sweden"},
            {"SG","Singapore"},
            {"SH","Saint Helena, Ascension and Tristan da Cunha"},
            {"SI","Slovenia"},
            {"SJ","Svalbard and Jan Mayen"},
            {"SK","Slovakia"},
            {"SL","Sierra Leone"},
            {"SM","San Marino"},
            {"SN","Senegal"},
            {"SO","Somalia"},
            {"SR","Suriname"},
            {"SS","South Sudan"},
            {"ST","Sao Tome and Principe"},
            {"SV","El Salvador"},
            {"SX","Sint Maarten (Dutch part)"},
            {"SY","Syrian Arab Republic"},
            {"SZ","Swaziland"},
            //T
            {"TC","Turks and Caicos Islands"},
            {"TD","Chad"},
            {"TF","French Southern Territories"},
            {"TG","Togo"},
            {"TH","Thailand"},
            {"TJ","Tajikistan"},
            {"TK","Tokelau"},
            {"TL","Timor-Leste"},
            {"TM","Turkmenistan"},
            {"TN","Tunisia"},
            {"TO","Tonga"},
            {"TR","Turkey"},
            {"TT","Tuvalu"},
            {"TW","Taiwan, Province of China"},
            {"TZ","Tanzania, United Republic of"},
            //U
            {"UA","Ukraine"},
            {"UG","Uganda"},
            {"UM","United States Minor Outlying Islands"},
            {"US","United States of America"},
            {"UY","Uruguay"},
            {"UZ","Uzbekistan"},
            //V
            {"VA","Holy See"},
            {"VC","Saint Vincent and the Grenadines"},
            {"VE","Venezuela, Bolivarian Republic of"},
            {"VG","Virgin Islands, British"},
            {"VI","Virgin Islands, U.S."},
            {"VN","Viet Nam"},
            {"VU","Vanuatu"},
            //W
            {"WF","Wallis and Futuna"},
            {"WS","Samoa"},
            //Y
            {"YE","Yemen"},
            {"YT","Mayotte"},
            //Z
            {"ZA","South Africa"},
            {"ZM","Zambia"},
            {"ZW","Zimbabwe"}
    };

    /*
    https://developer.spotify.com/web-api/get-artists-top-tracks/
    The sample lists the following coutries:
    "available_markets": [
    "AD", "AR", "AT", "AU", "BE", "BG", "BO", "BR", "CA", "CH", "CL", "CO", "CR",
    "CY", "CZ", "DE", "DK", "DO", "EC", "EE", "ES", "FI", "FR", "GB", "GR", "GT",
    "HK", "HN", "HU", "IE", "IS", "IT", "LI", "LT", "LU", "LV", "MC", "MT", "MX",
    "MY", "NI", "NL", "NO", "NZ", "PA", "PE", "PH", "PL", "PT", "PY", "RO", "SE",
    "SG", "SI", "SK", "SV", "TR", "TW", "US", "UY"
    ]
     */
    public static String[][] iso3166_1_alpha_2_countryCodes_APISample = new String[][]{
            {"AD", "Andorra"},
            {"AR", "Argentina"},
            {"AT", "Austria"},
            {"AU", "Australia"},
            {"BE", "Belgium"},
            {"BG", "Bulgaria"},
            {"BO", "Bolivia, Plurinational State of"},
            {"BR", "Brazil"},
            {"CA","Canada"},
            {"CH","Switzerland"},
            {"CL","Chile"},
            {"CO","Colombia"},
            {"CR","Costa Rica"},
            {"CY","Cyprus"},
            {"CZ","Czech Republic"},
            {"DE","Germany"},
            {"DK","Denmark"},
            {"DO","Dominican Republic"},
            {"EC","Ecuador"},
            {"EE","Estonia"},
            {"ES","Spain"},
            {"FI","Finland"},
            {"FR","France"},
            {"GB","United Kingdom of Great Britain and Northern Ireland"},
            {"GR","Greece"},
            {"GT","Guatemala"},
            {"HK","Hong Kong"},
            {"HU","Hungary"},
            {"IE","Ireland"},
            {"IS","Iceland"},
            {"IT","Italy"},
            {"LI","Liechtenstein"},
            {"LT","Lithuania"},
            {"LU","Luxembourg"},
            {"LV","Latvia"},
            {"MC","Monaco"},
            {"MT","Malta"},
            {"MX","Mexico"},
            {"MY","Malaysia"},
            {"NI","Nicaragua"},
            {"NL","Netherlands"},
            {"NO","Norway"},
            {"NZ","New Zealand"},
            {"PA","Panama"},
            {"PE","Peru"},
            {"PH","Philippines"},
            {"PL","Poland"},
            {"PT","Portugal"},
            {"PY","Paraguay"},
            {"RO","Romania"},
            {"SE","Sweden"},
            {"SG","Singapore"},
            {"SI","Slovenia"},
            {"SK","Slovakia"},
            {"SV","El Salvador"},
            {"TR","Turkey"},
            {"TW","Taiwan, Province of China"},
            {"US","United States of America"},
            {"UY","Uruguay"},
    };

    /*
    List of countries that are listed in the FAQ
    (Not sorted by code, see Brazil and Bulgaria)
    Countries with nothing but '//' behind it are definitely in the list
    https://support.spotify.com/au/learn-more/faq/#!/article/Availability-in-overseas-territories
    */
    public static String[][] iso3166_1_alpha_2_countryCodes_listedFAQ = new String[][]{
            {"AD", "Andorra"},//
            {"AR", "Argentina"},//
            {"AT", "Austria"},//
            {"AU", "Australia"},//
            {"AX", "Åland Islands"},//Finland
            {"BE", "Belgium"},//
            {"BM", "Bermuda"},//United Kingdom
            {"BO", "Bolivia, Plurinational State of"},//Bolivia
            {"BR", "Brazil"},//
            {"BG", "Bulgaria"},//
            {"CA","Canada"},//
            {"CL","Chile"},//
            {"CO","Colombia"},//
            {"CR","Costa Rica"},//
            {"CY","Cyprus"},//
            {"CZ","Czech Republic"},//
            {"DK","Denmark"},//
            {"FO","Faroe Islands"},//Denmark
            {"GL","Greenland"},//Denmark
            {"DO","Dominican Republic"},
            {"EC","Ecuador"},//
            {"SV","El Salvador"},//
            {"EE","Estonia"},//
            {"FI","Finland"},//
            {"AX", "Åland Islands"},//Finland
            {"FR","France"},//
            {"YT","Mayotte"},//France
            {"RE","Réunion"},//France
            {"PF","French Polynesia"},//France
            {"PM","Saint Pierre and Miquelon"},//France
            {"WF","Wallis and Futuna"},//France
            {"NC","New Caledonia"},//France
            {"DE","Germany"},//
            {"GR","Greece"},//
            {"GT","Guatemala"},//
            {"HN","Honduras"},//
            {"HK","Hong Kong"},//
            {"HU","Hungary"},//
            {"IS","Iceland"},//
            {"IT","Italy"},//
            {"LV","Latvia"},//
            {"LI","Liechtenstein"},//
            {"LT","Lithuania"},//
            {"LU","Luxembourg"},//
            {"MY","Malaysia"},//
            {"MT","Malta"},//
            {"MX","Mexico"},//
            {"MC","Monaco"},//
            {"NL","Netherlands"},//
            {"SX","Sint Maarten (Dutch part)"},//Netherlands Antilles
            {"NZ","New Zealand"},//
            {"CK","Cook Islands"},//New Zealand
            {"NU","Niue"},//New Zealand
            {"TK","Tokelau"},//Tokelau
            {"NI","Nicaragua"},//
            {"NO","Norway"},//
            {"SJ","Svalbard and Jan Mayen"},//Norway
            {"PA","Panama"},//
            {"PY","Paraguay"},//
            {"PE","Peru"},//
            {"PH","Philippines"},//
            {"PL","Poland"},//
            {"PT","Portugal"},//
            {"SG","Singapore"},//
            {"SK","Slovakia"},//
            {"ES","Spain"},//
            //Balearic Islands is NOT in ISO 3166-1 alpha-2
            //https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
            //It is in ISO 3166-2, as IB, according to
            //https://en.wikipedia.org/?title=Balearic_Islands
            {"IB", "Balearic Islands"},//Spain
            //Canary Islands is NOT in ISO 3166-1 alpha-2
            //It is in ISO 3166, as IC (reserved), according to
            //https://en.wikipedia.org/wiki/Canary_Islands
            {"IC","Canary Islands"},//Spain
            //Skipped Ceuta and Melilla
            {"SE","Sweden"},//
            {"CH","Switzerland"},//
            {"TW","Taiwan"},//
            {"UK","United Kingdom"},//
            {"BM", "Bermuda"},//United Kingdom
            {"FK","Falkland Islands"},//United Kingdom
            {"GI","Gibraltar"},//United Kingdom
            {"PN","Pitcairn Islands"},//United Kingdom
            {"SH","Saint Helena, Ascension and Tristan da Cunha"},//United Kingdom
            //Skipped Sovereign Base Areas of Akrotiri and Dhekelia (Cyprus)
            {"GG","Guernsey"},//United Kingdom
            {"IM","Isle of Man"},//United Kingdom
            {"JE","Jersey"},//United Kingdom
            {"UY","Uruguay"},//
            {"US","United States of America"},//
            {"AS", "American Samoa"},//United States of America
            {"GU","Guam"},//US
            {"MP","Northern Mariana Islands"},//US
            {"PR","Puerto Rico"},//US
            {"UM","United States Minor Outlying Islands"},//US
            {"VI","Virgin Islands, U.S."},//US
    };

    public static int getIndexByCountryCode(String countryCode, String[][] list){
        for (int i = 0; i < list.length; i++){
            if (list[i][0].equalsIgnoreCase(countryCode)){
                return i;
            }
        }
        return -1; //not found
    }

    /**
     * This will find the image that has the height that closest matched the preference.
     * The image can be bigger or smaller though.
     *
     * @param images          list of images to check
     * @param preferredHeight the height you would like the best
     * @return url of an image that best matches the preferred height.
     */
    public static String getImageWithBestSize(List<Image> images, int preferredHeight) {
        if (images == null || images.size() == 0) {
            //no images
            return null;
        }
        int index = -1;
        int lowestDiff = Integer.MAX_VALUE;
        for (int i = 0; i < images.size(); i++) {
            int imageHeight = images.get(i).height;
            int diffWithPreferred = Math.abs(preferredHeight - imageHeight);
            if (diffWithPreferred < lowestDiff) {
                lowestDiff = diffWithPreferred;
                index = i;
            }
        }
        return images.get(index).url;
    }
}
