import React from 'react';
import axios from 'axios';
import { Card, CardBody, CardTitle, CardSubtitle, CardText, CardLink } from 'reactstrap'


class InfrastructureList extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            estufas: [],
            loop: null
        };
        this.getEstufas = this.getEstufas.bind(this);
    }

    componentDidMount() {
        let loop = setInterval(()=>{
            this.getEstufas();
        }, 5000);
        this.setState({loop: loop});
    }

    componentWillUnmount() {
        clearInterval(this.state.loop);
    }

    getEstufas() {
        axios.get("/estufas")
            .then(res => {
                console.log(res.data)
            });
    }

    render() {

        return (
            <div>
                <Card className="m-2" style={{ width: '18rem' }}>
                    <CardBody>
                        <CardTitle>Estufa1</CardTitle>
                        <CardLink href="/estufa/1">Abrir página de estufa</CardLink>
                    </CardBody>
                </Card>
                <Card className="m-2" style={{ width: '18rem' }}>
                    <CardBody>
                        <CardTitle>Estufa2</CardTitle>
                        <CardLink href="/estufa/2">Abrir página de estufa</CardLink>
                    </CardBody>
                </Card>
            </div>
        );
    }
}

export default InfrastructureList;