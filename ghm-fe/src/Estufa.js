import React from 'react';
import { Container, Row, Col, Nav, NavItem, NavLink, TabContent, TabPane } from 'reactstrap';
import classnames from 'classnames';
import Chart from "react-apexcharts";
import axios from 'axios';

class Estufa extends React.Component {

    constructor(props) {
        super(props);
        this.props = props;
        this.toggle = this.toggle.bind(this);
        this.updateGraph = this.updateGraph.bind(this);
        this.state = {
            estufa: "",
            loop: null,
            collapse: false, activeTab: '1',
            categories: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
            data: [5, 2, 3, 4, 6, 6, 7, 3, 9, 13, 11, 12, 13, 7, 15, 4, 5, 14, 9, 4]
        };
    }

    toggle(tab) {
        if (this.state.activeTab !== tab) this.setState({ activeTab: tab });
    }


    componentDidMount() {
        const estufa = parseInt(this.props.match.params.number, 10);
        this.setState({
            estufa: estufa
        });
        let loop = setInterval(() => {
            this.updateGraph();
        }, 5000);
        this.setState({loop: loop});
    }

    componentWillUnmount(){
        clearInterval(this.state.loop);
    }

    updateGraph() {
        axios.get("/data/ph").then((res) => {
            for (let key in res.data) {
                if (key.slice(-1) == this.state.estufa) {
                    this.setState({ data: res.data[key].slice(0,10) });
                }
            }
        });
    }

    render() {
        const graphs = {
            temp: {
                options: {
                    chart: {
                        id: "lines",
                        stacked: false
                    },
                    title: {
                        text: "Temperatura (ºC)"
                    },
                    xaxis: {
                        labels: {
                            categories: this.state.categories
                        },
                        axisBorder: {
                            show: true
                        },
                        axisTicks: {
                            show: true
                        }
                    },

                    stroke: {
                        curve: "smooth",
                        width: 2
                    },
                    dataLabels: {
                        enabled: true
                    },
                },
                series: [{ name: "", data: this.state.data }]
            }
        };



        return (
            <div className='mt-4'>
                <h1 className="mx-5" >Estufa {this.state.estufa}</h1>
                <Container className="mt-5">
                    <Row>
                        <Col sm="12">
                            <Nav tabs color="dark" dark>
                                <NavItem>
                                    <NavLink
                                        className={classnames({ active: this.state.activeTab === '1' })}
                                        onClick={() => { this.toggle('1'); }}>
                                        ph
                                </NavLink>
                                </NavItem>
                                <NavItem>
                                    <NavLink
                                        className={classnames({ active: this.state.activeTab === '2' })}
                                        onClick={() => { this.toggle('2'); }}>
                                        temp
                                </NavLink>
                                </NavItem>
                            </Nav>
                            <TabContent activeTab={this.state.activeTab} className="mt-3">
                                <TabPane tabId="1">
                                    <Row className="align-items-center">

                                        <Col sm="12" >
                                            <Chart
                                                options={graphs.temp.options}
                                                series={graphs.temp.series}
                                                type="line"
                                                className="lines"
                                            />
                                        </Col>
                                    </Row>
                                </TabPane>
                                <TabPane tabId="2">
                                    <Row>
                                        <Col sm="12">
                                            <h4>Humidade</h4>
                                        </Col>
                                    </Row>
                                </TabPane>
                            </TabContent>
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default Estufa;