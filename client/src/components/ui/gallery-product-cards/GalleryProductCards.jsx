import React from "react";
import { Col } from "react-bootstrap";

import ProductCard from "../product-card/ProductCard.jsx";
import "./GalleryProductCards.css";

const GalleryProductCards = ({ products }) => {
	return (
		<>
			{products.map((item) => (
				<Col
					className="col__gallery__product__cards"
					lg="3"
					md="4"
					key={item.id}
				>
					<ProductCard item={item} />
				</Col>
			))}
		</>
	);
};

export default GalleryProductCards;
