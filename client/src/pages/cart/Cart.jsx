import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

import CommonAd from "../../components/ui/common-ad/CommonAd";
import Helmet from "../../components/helmet/Helmet";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";
import "./Cart.css";
import ModalAlert from "../../components/alerts/ModalAlert";
import CartTable from "../../components/ui/cart-table/CartTable";

const Cart = () => {
	const cartItems = useSelector((state) => state.cart.cartItems);
	const totalAmount = useSelector((state) => state.cart.totalAmount);

	useEffect(() => {
		window.scrollTo(0, 0);
	}, []);

	return (
		<Helmet title="Cart">
			<CommonAd title="Your Cart" />
			<section>
				<Container>
					<Row>
						<Col lg="12">
							{cartItems.length === 0 ? (
								<h5 className="text-center">Your cart is empty</h5>
							) : (
								<CartTable cartItems={cartItems} showDelete={true} />
							)}

							<div className="mt-4">
								<h6>
									Subtotal: $
									<span className="cart__subtotal">{totalAmount}</span>
								</h6>
								<p>Taxes and shipping will calculate at checkout</p>
								<div className="cart__page__btn">
									<button className="addToCart__btn me-4">
										<Link to="/foods">Continue Shopping</Link>
									</button>
									<button className="addToCart__btn">
										<Link to="/checkout">Proceed to checkout</Link>
									</button>
								</div>
							</div>
						</Col>
					</Row>
				</Container>
			</section>
		</Helmet>
	);
};

export default Cart;
