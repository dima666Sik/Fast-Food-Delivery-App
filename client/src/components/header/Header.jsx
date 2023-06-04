import React, { useState } from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import Login from "../../pages/Login";
import Register from "../../pages/Register";
import "./Header.css";
import { basketUIActions } from "../../redux/store/shopping-cart/basketUISlice";
import Basket from "../ui/carts/basket/Basket";
import { axiosLogout } from "../../redux/store/user/userSlice";

export default function Header() {
	const [showLoginModal, setShowLoginModal] = useState(false);
	const [showRegisterModal, setShowRegisterModal] = useState(false);

	const totalQuantity = useSelector((state) => state.cart.totalQuantity);
	const dispatch = useDispatch();
	const visibleBasket = useSelector((state) => state.basketUI.basketIsVisible);

	const toggleVisibleBasket = () => {
		dispatch(basketUIActions.toggleVisible());
	};

	const handleLoginClick = () => {
		setShowLoginModal(true);
	};

	const handleRegisterClick = () => {
		setShowRegisterModal(true);
	};

	const handleHideModal = () => {
		setShowLoginModal(false);
		setShowRegisterModal(false);
	};

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const accessToken = useSelector((state) => state.user.accessToken);

	const userFirstName = useSelector((state) => state.user.firstName);
	const roleUser = useSelector((state) => state.user.role);

	const onLogout = () => {
		dispatch(
			axiosLogout({
				accessToken,
			})
		);
	};

	return (
		<>
			<div className="navbar__pos">
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
									onClick={toggleVisibleBasket}
								>
									<i className="bi bi-cart-plus"></i>
									<span className="cart__badge">{totalQuantity}</span>
								</Nav.Link>
								{isAuthenticated ? (
									<>
										<Nav.Link onClick={onLogout}>
											<i className="bi bi-box-arrow-right"></i>
										</Nav.Link>
										{roleUser === "USER" && (
											<Nav.Link as={Link} to="/profile">
												{userFirstName}
											</Nav.Link>
										)}
									</>
								) : (
									<>
										<Nav.Link onClick={handleLoginClick}>
											<i className="bi bi-person-circle"></i>
										</Nav.Link>
										<Nav.Link>Guest</Nav.Link>
									</>
								)}
							</Nav>
						</Navbar.Collapse>
					</Container>
				</Navbar>
			</div>
			<Login
				show={showLoginModal}
				onHide={handleHideModal}
				onRegisterClick={handleRegisterClick}
			/>
			<Register
				show={showRegisterModal}
				onHide={handleHideModal}
				onLoginClick={handleLoginClick}
			/>
			{visibleBasket && <Basket />}
		</>
	);
}
