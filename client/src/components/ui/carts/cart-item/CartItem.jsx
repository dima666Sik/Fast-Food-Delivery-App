import React from "react";
import { ListGroupItem } from "react-bootstrap";
import { useDispatch } from "react-redux";

import { cartActions } from "../../../../redux/store/shopping-cart/cartSlice";

import "./CartItem.css";

const CartItem = ({ item }) => {
	const { id, title, price, image01, quantity, totalPrice } = item;
	const dispatch = useDispatch();

	const incrementItem = () => {
		dispatch(
			cartActions.addItem({
				id,
				title,
				price,
				image01,
			})
		);
	};

	const decreaseItem = () => {
		dispatch(cartActions.removeItem(id));
	};

	const deleteItem = () => {
		dispatch(cartActions.deleteItem(id));
	};

	return (
		<ListGroupItem className="cart__item">
			<div className="cart__item__info d-flex gap-2">
				<img src={image01} alt="" />

				<div className="cart__product__info w-100 d-flex align-items-center gap-4 justify-content-between">
					<div>
						<h6 className="cart__product__title">{title}</h6>
						<p className=" d-flex align-items-center gap-5 cart__product__price">
							{quantity}x <span>${totalPrice}</span>
						</p>
						<div className=" d-flex align-items-center justify-content-between increase__decrease__btn">
							<span className="increase__btn" onClick={incrementItem}>
								<i className="bi bi-plus-circle"></i>
							</span>
							<span className="quantity">{quantity}</span>
							<span className="decrease__btn" onClick={decreaseItem}>
								<i className="bi bi-dash-circle"></i>
							</span>
						</div>
					</div>

					<span className="delete__btn" onClick={deleteItem}>
						<i className="bi bi-x-circle"></i>
					</span>
				</div>
			</div>
		</ListGroupItem>
	);
};

export default CartItem;
