import React, { useEffect, useState } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import axios from "axios";

import ProductCard from "../product-card/ProductCard";
import { useGetAllProducts } from "../../../hooks/useGetAllProducts";
// import products from "../../../assets/fake-data/products.js";

const HotFood = () => {
	const { products, isLoading } = useGetAllProducts();

	const [hotFood, setHotFood] = useState([]);
	console.log(products);
	useEffect(() => {
		if (!isLoading) {
			const filteredFood = products.filter(
				(item) => item.category === "Pizza" || item.category === "Burger"
			);
			const sliceFood = filteredFood.slice(0, 4);
			setHotFood(sliceFood);
		}
	}, [isLoading]);

	return (
		<Container>
			<Row>
				<Col lg="12" className="text-center mb-5 ">
					<h2>Hot Food</h2>
				</Col>

				{isLoading || !hotFood ? (
					<div className="text-center">
						<Spinner />
					</div>
				) : (
					hotFood.map((item) => (
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
					))
				)}
			</Row>
		</Container>
	);
};

export default HotFood;
