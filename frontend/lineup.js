//This is the page users are redirected to after authorizing a Spotify login
import React, {useEffect, useState} from "react";
import downloadjs from 'downloadjs';
import html2canvas from 'html2canvas';

export default function Lineup(){
    const [userTopArtists, setUserTopArtists] = useState();

    //Communicates with backend to fetch and render information
    //In this example a list of the user's top artists
    useEffect(() => {
        async function getData() {
            const data = await fetch("http://localhost:8080/api/user-top-artists")
            .then(response => response.json());
            console.log(data)
            setUserTopArtists(data);
        }
        getData()
    }, [])

    //Downloads the flyer by selecting specific div classname
    const handleCaptureClick = async () => {
        const flyerImage = document.querySelector(".flyer");
        if (!flyerImage) return;

        const canvas = await html2canvas(flyerImage);
        const dataURL = canvas.toDataURL('image/png');
        downloadjs(dataURL, 'lineup.png', 'image/png');
    };

    return(
        <>
            <button onClick={handleCaptureClick}>Download Image</button>
            <div className="flyer">
                <h1>SPOTIFESTIVAL</h1>
                <h1>2022</h1>
                <div className="flyer-artists">
                    {userTopArtists && (
                        <div>
                            <p style={{paddingTop:80, fontSize:28}}>{userTopArtists[0].name}</p>
                            <p style={{fontSize:22}}>{userTopArtists[1].name} {userTopArtists[2].name}</p>
                            <p style={{fontSize:18}}>{userTopArtists[3].name} {userTopArtists[4].name} {userTopArtists[5].name}</p>
                            <p style={{fontSize:16}}>
                                {userTopArtists[6].name} {userTopArtists[7].name} {userTopArtists[8].name} {userTopArtists[9].name} {userTopArtists[10].name} {userTopArtists[11].name} {userTopArtists[12].name} {userTopArtists[13].name} {userTopArtists[14].name} & MORE
                            </p>
                        </div>
                    )
                    }
                </div>
            </div>
        </>
    );
}