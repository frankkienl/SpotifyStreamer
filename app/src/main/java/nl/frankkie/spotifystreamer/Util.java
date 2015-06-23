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
