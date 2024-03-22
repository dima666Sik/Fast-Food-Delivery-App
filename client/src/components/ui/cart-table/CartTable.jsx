import React from "react";
import { useDispatch } from "react-redux";

import { cartActions } from "../../../redux/store/shopping-cart/cartSlice";

const CartTable = ({ cartItems, showDelete }) => {
	return (
		<table className="table table-bordered">
			<thead>
				<tr>
					<th>Image</th>
					<th>Product Title</th>
					<th>Price</th>
					<th>Quantity</th>
					{showDelete && <th>Delete</th>}
				</tr>
			</thead>
			<tbody>
				{cartItems.map((item) => (
					<Tr item={item} key={item.id} showDelete={showDelete} />
				))}
			</tbody>
		</table>
	);
};

const Tr = (props) => {
	const { id, image01, title, price, quantity, totalPrice } = props.item;
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
			<td className="text-center">${totalPrice}</td>
			<td className="text-center">{quantity}x</td>
			{props.showDelete && (
				<td className="text-center cart__item__del">
					<i className="bi bi-trash3-fill" onClick={deleteItem}></i>
				</td>
			)}
		</tr>
	);
};

export default CartTable;
