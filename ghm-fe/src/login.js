import React from 'react';
import axios from 'axios';
import { Button, Form, FormGroup, Label, Input, Col, Container } from 'reactstrap';

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: null,
            password: null
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.requestLogin = this.requestLogin.bind(this);
    }


    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }

    requestLogin(e) {
        e.preventDefault();
        axios.get("/login/" + this.state.email + "/" + this.state.password)
            .then(res => {
                document.cookie = "owner";
                window.location.href = "estufa";
            });
        //efeitos de teste
        document.cookie = "owner";
        window.location.href = "estufa";
    }

    render() {

        return (
            <Container className="App" >
                <div className="mt-4">
                    <h2>Sign In</h2>
                </div>
                <Form className="form mt-4" onSubmit={this.requestLogin}>
                    <Col sm="5">
                        <FormGroup>
                            <Label>Email</Label>
                            <Input
                                type="email"
                                name="email"
                                id="exampleEmail"
                                placeholder="example@email.com"
                                onChange={this.handleInputChange}
                            />
                        </FormGroup>
                    </Col>
                    <Col sm="5">
                        <FormGroup>
                            <Label for="examplePassword">Password</Label>
                            <Input
                                type="password"
                                name="password"
                                id="examplePassword"
                                placeholder="password"
                                onChange={this.handleInputChange}
                            />
                        </FormGroup>
                    </Col>
                    <Button>Submit</Button>
                </Form>
            </Container>
        );
    }
}


export default Login;