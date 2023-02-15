import React from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./HeaderNavbar.css";

export default function HeaderNavbar() {
	return (
		<>
			<Navbar bg="dark" variant="dark" expand="md" collapseOnSelect>
				<Container>
					<Navbar.Brand>
						<Nav.Link as={Link} to="/">
							Fast Food Dev
						</Nav.Link>
					</Navbar.Brand>
					<Navbar.Toggle aria-controls="basic-navbar-nav" className="ml-auto" />
					<Navbar.Collapse id="basic-navbar-nav">
						<Nav className="m-auto">
							<Nav.Link as={Link} to="/home">
								Home
							</Nav.Link>
							<Nav.Link as={Link} to="/foods">
								Foods
							</Nav.Link>
							<Nav.Link as={Link} to="/cart">
								Cart
							</Nav.Link>
							<Nav.Link as={Link} to="/contact">
								Contact
							</Nav.Link>
						</Nav>

						<Nav>
							<Nav.Link as={Link} to="/checkout">
								<i className="bi bi-cart2"></i>
							</Nav.Link>
							<Nav.Link as={Link} to="/login">
								<i className="bi bi-person-circle"></i>
							</Nav.Link>
						</Nav>
					</Navbar.Collapse>
				</Container>
			</Navbar>
		</>
	);
}
