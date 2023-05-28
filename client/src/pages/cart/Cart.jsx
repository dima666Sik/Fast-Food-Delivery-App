import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

import CommonAd from "../../components/ui/common-ad/CommonAd";
import Helmet from "../../components/helmet/Helmet";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";
import "./Cart.css";

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
								<table className="table table-bordered">
									<thead>
										<tr>
											<th>Image</th>
											<th>Product Title</th>
											<th>Price</th>
											<th>Quantity</th>
											<th>Delete</th>
										</tr>
									</thead>
									<tbody>
										{cartItems.map((item) => (
											<Tr item={item} key={item.id} />
										))}
									</tbody>
								</table>
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

const Tr = (props) => {
	const { id, image01, title, price, quantity } = props.item;
	const dispatch = useDispatch();

	const deleteItem = () => {
		dispatch(cartActions.deleteItem(id));
	};
	return (
		<tr>
			<td className="text-center cart__img__box">
				<img src={image01} alt="" />
			</td>
			<td className="text-center">{title}</td>
			<td className="text-center">${price}</td>
			<td className="text-center">{quantity}x</td>
			<td className="text-center cart__item__del">
				<i className="bi bi-trash3-fill" onClick={deleteItem}></i>
			</td>
		</tr>
	);
};

export default Cart;
