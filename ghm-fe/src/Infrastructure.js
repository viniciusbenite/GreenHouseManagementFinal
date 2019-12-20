import React from 'react';
import { Switch, Route } from 'react-router-dom';
import InfrastructureList from './InfrastructureList';
import Estufa from './Estufa';



function Infrastructure() {
    return (
        <Switch>
            <Route exact path='/estufa' component={InfrastructureList} />
            <Route path='/estufa/:number' component={Estufa} />
        </Switch>
    );
}

export default Infrastructure;