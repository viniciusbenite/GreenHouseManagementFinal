import React from 'react';
import { useHistory } from 'react-router-dom';


function Home() {
    let history = useHistory();
    history.push('/login/');
    return (<div>HOME</div>);
}

export default Home;
