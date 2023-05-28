import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";

import { cartActions } from "../../../redux/store/shopping-cart/cartSlice.js";

import imgUpLike from "../../../assets/images/up_like.png";
import imgLowLike from "../../../assets/images/low_like.png";
import "./ProductCard.css";
import ModalAlert from "../../alerts/ModalAlert";
import { axiosSetLikeAndStatus } from "../../../redux/store/shopping-cart/cartsLikedSlice.js";

const ProductCard = (props) => {
	const { id, title, image01, likes, price } = props.item;
	const dispatch = useDispatch();
	const addToCart = () => {
		dispatch(
			cartActions.addItem({
				id,
				title,
				image01,
				price,
			})
		);
	};
	const [showModal, setShowModal] = useState(false);
	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);

	const listCartsLiked = useSelector(
		(state) => state.cartsLiked.listCartsLiked
	);

	const accessToken = useSelector((state) => state.user.accessToken);

	const changeLike = () => {
		if (isAuthenticated) {
			dispatch(
				axiosSetLikeAndStatus({
					id,
					likes,
					accessToken,
				})
			);
		} else {
			setShowModal(true);
		}
	};

	const existItem = listCartsLiked.find((item) => item.id === id);
	const status = existItem ? existItem.status : false;
	let updatedLikes = existItem ? existItem.likes : likes;

	return (
		<div className="product__item">
			<Link to={`/foods/${id}`}>
				<div className="product__img">
					<img src={image01} alt="product-img" className="w-50" />
				</div>
			</Link>

			<div className="product__content">
				<h5>
					<Link to={`/foods/${id}`}>{title}</Link>
				</h5>
				<div className="likes pb-4 d-flex justify-content-center align-items-center">
					{updatedLikes}
					<img
						src={status ? imgUpLike : imgLowLike}
						alt="product-img"
						className="like"
						onClick={changeLike}
					/>
				</div>
				<div className=" d-flex align-items-center justify-content-between ">
					<span className="product__price">${price}</span>
					<button className="addToCart__btn" onClick={addToCart}>
						Add to Cart
					</button>
				</div>
			</div>
			{showModal && (
				<ModalAlert
					paramTitle={"Error Authenticated"}
					paramBody={
						"You don't have rights to like. Please Authorization/Registration in this Application..."
					}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</div>
	);
};

export default ProductCard;
