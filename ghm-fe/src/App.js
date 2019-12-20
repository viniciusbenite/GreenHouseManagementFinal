import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Navigator from './Navigator';
import Infrastructure from './Infrastructure'
import Home from './Home'
import Login from './login'
import HardwareLogs from './hardwareLogs'

function App() {
    return (
        <div>
            <Navigator />
            <Switch>
                <div className="App">
                    <Route path="/" exact component={Home} />
                    <Route path="/estufa/" component={Infrastructure} />
                    <Route path="/login/" component={Login} />
                    <Route path="/hwlogs/" component={HardwareLogs} />
                </div>
            </Switch>
        </div>
    );
}

export default App;