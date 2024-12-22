import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { ListGroup } from "react-bootstrap";
import { Link } from "react-router-dom";

import "./Basket.css";
import CartItem from "../cart-item/CartItem";

import { basketUIActions } from "../../../../redux/store/shopping-cart/basketUISlice";

const Basket = () => {
	const dispatch = useDispatch();
	const cartProducts = useSelector((state) => state.cart.cartItems);

	const handleToggleVisibleCart = () => {
		dispatch(basketUIActions.toggleVisible());
	};

	const toggleVisibleCart = handleToggleVisibleCart;

	const totalAmount = useSelector((state) => state.cart.totalAmount);

	const handleClickOutside = (event) => {
		if (event.target.className === "cart__container") {
			handleToggleVisibleCart();
		}
	};
	console.log("mmmm");
	return (
		<div className="cart__container" onClick={handleClickOutside}>
			<ListGroup className="cart">
				<div className="cart__close" onClick={toggleVisibleCart}>
					<span>
						<i className="bi bi-x"></i>
					</span>
				</div>

				<div className="cart__item__list">
					{cartProducts.length ? (
						cartProducts.map((item, index) => (
							<CartItem item={item} key={index} />
						))
					) : (
						<h6 className="text-center mt-5">No items added</h6>
					)}
				</div>

				<div className="cart__bottom">
					<h6>
						Subtotal amount: <span>${totalAmount}</span>
					</h6>
					<button>
						<Link to="/checkout">Checkout</Link>
					</button>
				</div>
			</ListGroup>
		</div>
	);
};

export default Basket;
