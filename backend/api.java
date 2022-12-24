import se.michaelthelin.spotify.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;

//URI callback must match what's on your spotify dashboard for this example it is: "http://localhost:8080/api/get-user-code"
@RestController
@RequestMapping("/api")
public class Authorization {
    //Create a seperate and secure file for your client id and secret
    //They are exposed here for example purposes only
    private static final String client_Id = "get-from-spotify-dev-dashboard";
    private static final String client_Secret = "get-from-spotify-dev-dashboard";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    private static String code = "";

    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(client_Id)
        .setClientSecret(client_Secret)
        .setRedirectUri(redirectUri)
        .build();

    @GetMapping("/login")
    @ResponseBody
    public String loginWithSpotify(){
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .scope("user-read-private, user-read-email, user-top-read")
            .show_dialog(true)
            .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    //This section gets user's token and must match the Spotify Dev Dashboard Redirect URL
    //The token is needed to access user information through the spotify api
    //In our case we are accessing the user's top artists, example on line 55
    @GetMapping(value = "/get-user-code")
    public void getSpotifyToken(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
            .build();
        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        }catch(IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            System.out.println("ERROR MSG:" + e.getMessage());
        }
        response.sendRedirect("http://localhost:3000/lineup");
    }

    //This method gets user's top artists and returns the information as a list for you to manipulate
    //Lineup page on our frontend renders this data upon its loading
    @GetMapping(value = "user-top-artists")
    public Artist[] getUserTopArtist(){
        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
            .limit(15)
            .offset(0)
            .time_range("long_term")
            .build();
        try{
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            return artistPaging.getItems();
        }catch (Exception e) {
            System.out.println("ERROR MSG:" + e.getMessage());
        }
        return new Artist[0];
    }
}
