import React from 'react'
import axios from 'axios';

class HardwareLogs extends React.Component {
    constructor(props) {
        super(props);
        this.state = { listItems: [] };
        this.updateList = this.updateList.bind(this);

    }

    componentDidMount() {
        this.updateList();
    }

    updateList() {
        axios.get("/data/acoes")
            .then(res => {
                let d = res.data;
                console.log(d);
                const listItems = [];
                for (let key in d) {
                    var list = d[key].map((string) =>
                        <li>{string}</li>
                    );
                    var e = (
                        <div>
                            <h3 className="mt-2">Sensor {key}</h3>
                            <ul>{list}</ul>
                        </div>
                    )
                    listItems.push(e);

                }

                this.setState({ listItems: listItems });
            });
    }

    render() {
        return (
            <div>
                <ul>{this.state.listItems}</ul>
            </div>
        );
    }
}

export default HardwareLogs;