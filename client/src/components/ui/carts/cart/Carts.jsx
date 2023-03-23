import React from "react";
import CartItem from "../cart-item/CartItem";
import { ListGroup } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./Carts.css";
import { useDispatch, useSelector } from "react-redux";
import { cartUIActions } from "../../../../redux/store/shopping-cart/cartUISlice";
const Carts = () => {
	const dispatch = useDispatch();
	const cartProducts = useSelector((state) => state.cart.cartItems);
	const toggleVisibleCart = () => {
		dispatch(cartUIActions.toggleVisible());
	};
	const totalAmount = useSelector((state) => state.cart.totalAmount);
	return (
		<div className="cart__container">
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

export default Carts;
