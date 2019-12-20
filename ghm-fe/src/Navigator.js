import React from 'react';
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Button
} from 'reactstrap';


class Navigator extends React.Component {

    constructor(props) {
        super(props);
        this.toggle = this.toggle.bind(this);
        this.logoutOrLogin = this.logoutOrLogin.bind(this);
        let btn
        if (document.cookie) {
            btn = "Logout";
        } else {
            btn = "Login";
        }
        this.state = {
            isOpen: false,
            user: btn
        };
    }

    toggle() {
        this.setState(state => ({ isOpen: !state.isOpen }));
    }

    logoutOrLogin() {
        if (document.cookie) {
            document.cookie = ""
        }
        window.location.href = "/login";
    }


    render() {

        return (

            <div style={{ boxShadow: "0px 3px 5px gray" }}>
                <Navbar color="dark" dark expand="sm">
                    <NavbarBrand href="/">Greenhouse Management</NavbarBrand>
                    <NavbarToggler onClick={this.toggle} />
                    <Collapse isOpen={this.state.isOpen} navbar>
                        <Nav className="ml-auto" navbar>
                            <NavItem>
                                <NavLink href="/estufa/">Infrastructures</NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink href="/hwlogs">Hardware logs</NavLink>
                            </NavItem>
                            <NavItem nav inNavbar>
                                <Button className="mx-3" onClick={this.logoutOrLogin}>
                                    {this.state.user}
                                </Button>
                            </NavItem>
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        );
    }
}

export default Navigator;