import React from "react";
import { useDispatch } from "react-redux";
import { Link } from "react-router-dom";

import { cartActions } from "../../../redux/store/shopping-cart/cartSlice";
import imgLike from "../../../assets/images/like.png";
import "./ProductCard.css";

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

	const changeLike = () => {
		// dispatch(
		// 	cartActions.changeLike({
		// 		id,
		// 		likes,
		// 	})
		// );
	};
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
					{likes}{" "}
					<img
						src={imgLike}
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
		</div>
	);
};

export default ProductCard;
