import React, { useEffect, useState } from "react";
import { Container, Row, Col } from "react-bootstrap";
import ProductCard from "../product-card/ProductCard";
import products from "../../../assets/fake-data/products.js";

const HotFood = () => {
	const [hotFood, setHotFood] = useState([]);

	useEffect(() => {
		const filteredFood = products.filter(
			(item) => item.category === "Pizza" || item.category === "Burger"
		);
		const sliceFood = filteredFood.slice(0, 4);
		setHotFood(sliceFood);
	}, []);

	return (
		<Container>
			<Row>
				<Col lg="12" className="text-center mb-5 ">
					<h2>Hot Food</h2>
				</Col>

				{hotFood.map((item) => (
					<Col
						className="col__gallery__product__cards"
						lg="3"
						md="4"
						sm="6"
						xs="6"
						key={item.id}
					>
						<ProductCard item={item} />
					</Col>
				))}
			</Row>
		</Container>
	);
};

export default HotFood;
