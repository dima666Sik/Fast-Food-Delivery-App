import React, { useState, useEffect } from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import Login from "../../pages/Login";
import Register from "../../pages/Register";
import "./HeaderNavbar.css";
import { cartUIActions } from "../../redux/store/shopping-cart/cartUISlice";
import { cartActionsLiked } from "../../redux/store/shopping-cart/cartsLikedSlice";
import Carts from "../ui/carts/cart/Carts";
import { clearUser } from "../../redux/store/user/userSlice";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";

export default function HeaderNavbar() {
	const [showLoginModal, setShowLoginModal] = useState(false);
	const [showRegisterModal, setShowRegisterModal] = useState(false);

	const totalQuantity = useSelector((state) => state.cart.totalQuantity);
	const dispatch = useDispatch();
	const visibleCart = useSelector((state) => state.cartUI.cartIsVisible);

	const toggleVisibleCart = () => {
		dispatch(cartUIActions.toggleVisible());
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

	const onSubmit = () => {
		dispatch(clearUser());
		dispatch(cartActions.clearCart());
		dispatch(cartActionsLiked.clearCartsLiked());
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
									onClick={toggleVisibleCart}
								>
									<i className="bi bi-cart-plus"></i>
									<span className="cart__badge">{totalQuantity}</span>
								</Nav.Link>
								{isAuthenticated ? (
									<>
										<Nav.Link onClick={onSubmit}>
											<i className="bi bi-box-arrow-right"></i>
										</Nav.Link>
										<Nav.Link>{userFirstName}</Nav.Link>
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
			{visibleCart && <Carts />}
		</>
	);
}
