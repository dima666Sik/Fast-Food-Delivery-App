import React, { useState, useEffect } from "react";
import { Container, Row, Col } from "react-bootstrap";
import products from "../../../assets/fake-data/products.js";
import foodCategoryImg1 from "../../../assets/images/menu-products/product_1_pizza.png";

import foodCategoryImg2 from "../../../assets/images/menu-products/product_2_hamburger.png";

import foodCategoryImg3 from "../../../assets/images/menu-products/product_3_sushi.png";
import GalleryProductCards from "../gallery-product-cards/GalleryProductCards";
import "./MenuProduct.css";

const MenuProducts = () => {
	const [category, setCategory] = useState("ALL");
	const [allProducts, setAllProducts] = useState(products);

	useEffect(() => {
		if (category === "ALL") {
			setAllProducts(products);
		}

		if (category === "BURGER") {
			const filteredProducts = products.filter(
				(item) => item.category === "Burger"
			);

			setAllProducts(filteredProducts);
		}

		if (category === "PIZZA") {
			const filteredProducts = products.filter(
				(item) => item.category === "Pizza"
			);

			setAllProducts(filteredProducts);
		}

		if (category === "BREAD") {
			const filteredProducts = products.filter(
				(item) => item.category === "Bread"
			);

			setAllProducts(filteredProducts);
		}
	}, [category]);

	return (
		<Container>
			<Row>
				<Col lg="12" className="text-center">
					<h2>Popular Foods</h2>
				</Col>

				<Col lg="12" style={{ paddingBottom: "20px" }}>
					<div className="food__category d-flex align-items-center justify-content-center gap-4">
						<button
							className={`all__btn  ${
								category === "ALL" ? "foodBtnActive" : ""
							} `}
							onClick={() => setCategory("ALL")}
						>
							All
						</button>
						<button
							className={`d-flex align-items-center gap-2 ${
								category === "PIZZA" ? "foodBtnActive" : ""
							} `}
							onClick={() => setCategory("PIZZA")}
						>
							<img src={foodCategoryImg1} alt="" />
							Pizza
						</button>

						<button
							className={`d-flex align-items-center gap-2 ${
								category === "BURGER" ? "foodBtnActive" : ""
							} `}
							onClick={() => setCategory("BURGER")}
						>
							<img src={foodCategoryImg2} alt="" />
							Hamburger
						</button>

						<button
							className={`d-flex align-items-center gap-2 ${
								category === "BREAD" ? "foodBtnActive" : ""
							} `}
							onClick={() => setCategory("BREAD")}
						>
							<img src={foodCategoryImg3} alt="" />
							Sushi
						</button>
					</div>
				</Col>
				<GalleryProductCards products={allProducts} />
			</Row>
		</Container>
	);
};

export default MenuProducts;
