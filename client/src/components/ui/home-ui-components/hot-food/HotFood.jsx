import React, { useEffect, useState } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";

import ProductCard from "../../product-card/ProductCard";
import { useGetAllProducts } from "../../../../hooks/useGetAllProducts";

const HotFood = () => {
	const { products, isLoading } = useGetAllProducts();

	const [hotFood, setHotFood] = useState([]);
	console.log(products);
	useEffect(() => {
		if (!isLoading) {
			const filteredFood = products.filter((item) => item.category === "Pizza");
			const sortedFood = filteredFood.sort((a, b) => b.likes - a.likes);
			const slicedFood = sortedFood.slice(0, 4);
			setHotFood(slicedFood);
		}
	}, [isLoading]);

	return (
		<Container>
			<Row>
				<Col lg="12" className="text-center mb-5 ">
					<h2>Hot Pizza</h2>
				</Col>

				{isLoading || !hotFood ? (
					<div className="text-center">
						<Spinner />
					</div>
				) : hotFood.length === 0 ? (
					<div className="review pt-5 mb-5 pb-5">
						<p className="user__name text-center my-0">Hot Food is absent</p>
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
