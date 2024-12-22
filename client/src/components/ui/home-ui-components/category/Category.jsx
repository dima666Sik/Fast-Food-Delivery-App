import React from "react";
import { Container, Row, Col } from "react-bootstrap";

import categoryImg1 from "../../../../assets/images/category/category-01.png";
import categoryImg2 from "../../../../assets/images/category/category-02.png";
import categoryImg3 from "../../../../assets/images/category/category-03.png";
import "./Category.css";

const categoryData = [
	{
		display: "Pizza",
		imgUrl: categoryImg2,
	},
	{
		display: "Sushi",
		imgUrl: categoryImg3,
	},
	{
		display: "Hamburger",
		imgUrl: categoryImg1,
	},
];

const Category = () => {
	return (
		<Container>
			<Row className="category__container justify-content-around">
				{categoryData.map((item, index) => (
					<Col
						lg="3"
						md="4"
						sm="6"
						xs="6"
						className="col__category__item mb-4"
						key={index}
					>
						<div className="category__item d-flex align-items-center gap-3">
							<div className="category__img">
								<img src={item.imgUrl} alt="category__item" />
							</div>
							<h6>{item.display}</h6>
						</div>
					</Col>
				))}
			</Row>
		</Container>
	);
};

export default Category;
