package nl.frankkie.spotifystreamer;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by FrankkieNL on 20-6-2015.
 */
public class Util {

    /*
    The Spotify API call, 'top-tracks' requires a ISO 3166-1 alpha-2 country code
    //https://developer.spotify.com/web-api/get-artists-top-tracks/
    //https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
    //EU is not included in this list, as it generates this error:
    {
       "error": {
         "status": 400,
         "message": "Invalid country code"
       }
    }
    Only the officially assigned codes are in the list below.
    */
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
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},

            //TODO: finish this list
            {"NL", "Netherlands"},
            {"US", "United States of America"}
    };

    public static int getIndexByCountryCode(String countryCode){
        for (int i = 0; i < Util.iso3166_1_alpha_2_countryCodes.length; i++){
            if (Util.iso3166_1_alpha_2_countryCodes[i][0].equalsIgnoreCase(countryCode)){
                return i;
            }
        }
        return -1; //not found, yes this will crash the app
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
