import React, { useState } from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import Login from "../../pages/Login";
import Register from "../../pages/Register";
import "./HeaderNavbar.css";

export default function HeaderNavbar() {
	const [showModal, setShowModal] = useState(false);
	const [showRegisterModal, setShowRegisterModal] = useState(false);

	const handleLoginClick = () => {
		setShowModal(true);
	};

	const handleRegisterClick = () => {
		setShowRegisterModal(true);
	};

	const handleHideModal = () => {
		setShowModal(false);
		setShowRegisterModal(false);
	};

	return (
		<>
			<div className="navbar-pos">
				<Navbar bg="dark" variant="dark" expand="md" collapseOnSelect>
					<Container>
						<Navbar.Brand>
							<Nav.Link as={Link} to="/">
								Fast Food Dev
							</Nav.Link>
						</Navbar.Brand>
						<Navbar.Toggle
							aria-controls="basic-navbar-nav"
							className="ml-auto"
						/>
						<Navbar.Collapse id="basic-navbar-nav">
							<Nav className="m-auto">
								<Nav.Link as={Link} to="/home">
									<i className="bi bi-house-fill"></i> Home
								</Nav.Link>
								<Nav.Link as={Link} to="/foods">
									<i className="bi bi-file-text"></i> Foods
								</Nav.Link>
								<Nav.Link as={Link} to="/cart">
									<i className="bi bi-cart2"></i> Cart
								</Nav.Link>
								<Nav.Link as={Link} to="/contact">
									<i className="bi bi-person-vcard-fill"></i> Contact
								</Nav.Link>
							</Nav>
							<Nav>
								<Nav.Link
									className="cart__plus__container"
									as={Link}
									to="/checkout"
								>
									<i className="bi bi-cart-plus"></i>
									<span className="cart__badge">2</span>
								</Nav.Link>
								<Nav.Link onClick={handleLoginClick}>
									<i className="bi bi-person-circle"></i>
								</Nav.Link>
							</Nav>
						</Navbar.Collapse>
					</Container>
				</Navbar>
			</div>
			<Login
				show={showModal}
				onHide={handleHideModal}
				onRegisterClick={handleRegisterClick}
			/>
			<Register
				show={showRegisterModal}
				onHide={handleHideModal}
				onLoginClick={handleLoginClick}
			/>
		</>
	);
}
