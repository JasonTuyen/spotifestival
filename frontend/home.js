//This is the root or homepage
import React from "react";


export default function Home(){
    const getSpotifyLogin = () => {
        //Button will link to your backend login api via fetch
        fetch("http://localhost:8080/api/login")
        .then((response) => response.text())
        .then(response => {
          window.location.replace(response);
        })
    }

    return(
        <div>
            <div>
                <h1>Spotifestival</h1>
                <h2>Your Top Artists in Festival Poster Format</h2>
            </div>
            <div>
                {/*Button will link to your backend login via fetch*/}
                <button onClick = {getSpotifyLogin}>Log in with Spotify</button>
            </div>
      </div>
    );
}